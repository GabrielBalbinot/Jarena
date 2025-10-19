/**
 * O agente busca uma posição inicial (centralizada) e a partir daí começa seus movimentos normais.
 * A movimentação normal se dá de mandeira diagonal, onde cada unidade de tempo o agente alterna entre
 * BAIXO/CIMA e DIREITA/ESQUERDA (essa alternância varia de acordo com o sentido que o agente está indo).
 * Assim que recebe energia, o agente para e envia as coordenadas para aliados próximos. Sempre que receberem mensagens,
 * os agentes ajustam o sentido de sua movimentação a fim de capturar o mesmo cogumelo cujas coordenadas foram recebidas.
 * Caso engajem em combate, eles altera o sentido de sua movimentação a fim de fugir do combate. Caso a energia fique baixa, 
 * o agente para e não andará mais pelo restante da partida, salvo uma única exceção: vencer um combate. Caso o agente vença
 * um combate e tenha energia suficiente, ele voltará a andar e continuar a caçar mais cogumelos.
 * 
 * 
 * Davi Henrique Pezenatto <davinatto@gmail.com> <20240019891>
 * Gabriel Francisco Dall Rosa Balbinot <gabriel.balbinot@estudante.uffs.edu.br> <20240019882>
 */

package br.uffs.cc.jarena;

import java.util.Random;

public class AgenteDG extends Agente
{
	private int quadrante;
	private int[] limitesHorizontas = new int[2];
	private int[] limitesVerticais = new int[2];
	private boolean recebendoEnergia = false;
	private boolean nuncaMaisAndar = false;
	private boolean movimentoInicialFeito = false;
	private int passosIniciaisRestantes = 0;
	private boolean faseHorizontalInicial = true;
	private int direcaoHorizontalInicial = DIREITA;

	/*
	 * Os atributos direcaoVertical e direcaoHorizontal são usados como flag para determinar como a movimentação deve ocorrer.
	 * Se direcaoVertical é falso, então o boneco deverá subir no quadrante, e descer caso o atributo seja verdadeiro.
	 * O mesmo ocorre com direcaoHorizontal, porém ao ser falso, o boneco nada para esquerda, sendo verdadeiro ele anda para direita.
	 */
	private boolean direcaoVertical; // true se estiver descendo
	private boolean direcaoHorizontal; // true se estiver indo para direita

	public AgenteDG(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		this.direcaoVertical = setDirecaoVertical();
		this.direcaoHorizontal = setDirecaoHorizontal();
		this.quadrante = setQuadrante(x, y);
		setLimitesDoQuadrante(quadrante);
		mudarMovimentacao();
		setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {

		 /*
		 * Se a energia do agente chegar a 100 ou menor que isso, então ele permanecerá parado até morrer,
		 * a fim de conservar energia até o seu imutável destino
		 */

		if (getEnergia() <= 100) {
			nuncaMaisAndar = true;
			super.para();
			return;
		}

		if (nuncaMaisAndar) {
			return;
		}

		if (!movimentoInicialFeito) {
    		int x = getX();
    		int y = getY();
    		int meioX = Constants.LARGURA_MAPA / 2;
    		int meioY = Constants.ALTURA_MAPA / 2;
    		int idAgente = getId() * 3;

    		if (passosIniciaisRestantes == 0 && faseHorizontalInicial) {//Faz eles andarem na horizontal no inicio
    		    if (x < meioX) {
    		        direcaoHorizontalInicial = DIREITA;//se estiverem antes do meio do mapa, andam para direita
    		    } else {
    		        direcaoHorizontalInicial = ESQUERDA;//sn, para esquerda
    		    }
    		    passosIniciaisRestantes = idAgente;       //vão andar a distancia do Id
    		    setDirecao(direcaoHorizontalInicial); //qual direção vão iniciar
    		    return;
    		}

    		if (faseHorizontalInicial && passosIniciaisRestantes > 0) {//se ainda tem oq andar
    		    setDirecao(direcaoHorizontalInicial);//define a direção inicial
    		    passosIniciaisRestantes--; //vai diminuindo os passos restantes
    		    if (passosIniciaisRestantes == 0) {//se atinge a distancia começa a andar vertical
    		        faseHorizontalInicial = false;
    		        if (getY() < meioY) {//se for antes do meio do mapa, anda para cima
    		            setDirecao(CIMA);
    		        } else {
    		            setDirecao(BAIXO);//sn para baixo
    		        }
    		        movimentoInicialFeito = true;
    		    }
    		    return;
    		}
		}


		mudarMovimentacao();
			
		if(podeDividir() && getEnergia() >= 2000) {
			divide();
		}

		recebendoEnergia = false;

	}
	
	public void recebeuEnergia() {
		recebendoEnergia = true;
		int a = getX();
		int b = getY();
		enviaMensagem(a + " " + b);
		super.para();
	}

	
	public void tomouDano(int energiaRestanteInimigo) {

		/*
		 * O agente tenta mudar de direção caso engaje em combate
		 */
		if (energiaRestanteInimigo >= getEnergia()) {
			this.direcaoHorizontal = !this.direcaoHorizontal;
			this.direcaoVertical = !this.direcaoVertical;
		}

	}
	
	public void ganhouCombate() {

		/*
		 * Caso o agente estivesse parado devido ao baixo nível de energia (menor que 100)...
		 * o agente ganha energia ao vencer algum combate, então verifica se ele tem energia suficiente para voltar a andar
		 */
		if (getEnergia() > 250) {
			nuncaMaisAndar = false;
		}

	}
	
	public void recebeuMensagem(String msg) {
		String[] partes = msg.split(" ");
		int x = Integer.parseInt(partes[0]);
		int y = Integer.parseInt(partes[1]);
		if(x>getX()){
			direcaoHorizontal = true;
		}else{
			direcaoHorizontal = false;
		}
		if(y>getY()){
			direcaoVertical = true;
		}else{
			direcaoVertical = false;
		}
	}
	
	public String getEquipe() {
		// Definimos que o nome da equipe do agente é "Fernando".
		return "AgenteDG";
	}

	private int setQuadrante(int x, int y) {

		final int centroX = Constants.LARGURA_MAPA/2;
		final int centroY = Constants.ALTURA_MAPA/2;
		
		if (x >= centroX) {

			if (y <= centroY) {
				return 1;
			} else {
				return 4;
			}

		} else {
			if (y <= centroY) {
				return 2;
			} else {
				return 3;
			}
		}

	}

	private void setLimitesDoQuadrante(int quadrante) {

		if (quadrante == 1) {
			limitesHorizontas[0] = Constants.LARGURA_MAPA/2;
			limitesHorizontas[1] = Constants.LARGURA_MAPA;
			limitesVerticais[0] = 0;
			limitesVerticais[1] = Constants.ALTURA_MAPA/2; 
		} else if (quadrante == 2) {
			limitesHorizontas[0] = 0;
			limitesHorizontas[1] = Constants.LARGURA_MAPA/2;
			limitesVerticais[0] = 0;
			limitesVerticais[1] = Constants.ALTURA_MAPA/2; 
		} else if (quadrante == 3) {
			limitesHorizontas[0] = 0;
			limitesHorizontas[1] = Constants.LARGURA_MAPA/2;
			limitesVerticais[0] = Constants.ALTURA_MAPA/2;
			limitesVerticais[1] = Constants.ALTURA_MAPA; 
		} else {
			limitesHorizontas[0] = Constants.LARGURA_MAPA/2;
			limitesHorizontas[1] = Constants.LARGURA_MAPA;
			limitesVerticais[0] = Constants.ALTURA_MAPA/2;
			limitesVerticais[1] = Constants.ALTURA_MAPA; 
		}
		
	}

	private void bateuNosLimites(int x, int y) {
		if (x >= limitesHorizontas[1]) {			
			this.direcaoHorizontal = false;

		} else if (x <= limitesHorizontas[0]) {
			
			this.direcaoHorizontal = true;
		}

		if (y >= limitesVerticais[1]) {
			
			this.direcaoVertical = false;
		} else if (y <= limitesVerticais[0]) {
			
			this.direcaoVertical = true;
		}
	}

	private void mudarMovimentacao() {

		if (recebendoEnergia)
			return;

		bateuNosLimites(getX(), getY());

		if (this.direcaoVertical && this.direcaoHorizontal) {

			if (getDirecao() == DIREITA) {
				setDirecao(BAIXO);
			} else {
				setDirecao(DIREITA);
			}

		} else if (!this.direcaoVertical && this.direcaoHorizontal) {

			if (getDirecao() == DIREITA) {
				setDirecao(CIMA);
			} else {
				setDirecao(DIREITA);
			}

		} else if (this.direcaoVertical && !this.direcaoHorizontal) {

			if (getDirecao() == ESQUERDA) {
				setDirecao(BAIXO);
			} else {
				setDirecao(ESQUERDA);
			}

		} else if (!this.direcaoVertical && !this.direcaoHorizontal) {

			if (getDirecao() == ESQUERDA) {
				setDirecao(CIMA);
			} else {
				setDirecao(ESQUERDA);
			}

		}
		
	}

	private boolean setDirecaoVertical() {

		if (getId() % 2 == 0) {
			return true;
		}

		return false;
	}

	private boolean setDirecaoHorizontal() {
		
		if (getId() % 2 == 0) {
			return false;
		}

		return true;
	}

}