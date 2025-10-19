/**
 * Um exemplo de agente que anda aleatoriamente na arena. Esse agente pode ser usado como base
 * para a criação de um agente mais esperto. Para mais informações sobre métodos que podem
 * ser utilizados, veja a classe Agente.java.
 * 
 * Gabriel Francisco Dall Rosa Balbinot <gabriel.balbinot@estudante.uffs.edu.br>
 */

package br.uffs.cc.jarena;

import java.util.Random;

public class AgenteDummy extends Agente
{
	private int quadrante;
	private int[] limitesHorizontas = new int[2];
	private int[] limitesVerticais = new int[2];
	private boolean recebendoEnergia = false;
	private boolean nuncaMaisAndar = false;
	private boolean movimentoInicialFeito = false;
	private int passosRestantes = 0;
	private boolean faseHorizontalInicial = true;
	private int direcaoHorizontalInicial = DIREITA;

	/*
	 * Os atributos direcaoVertical e direcaoHorizontal são usados como flag para determinar como a movimentação deve ocorrer.
	 * Se direcaoVertical é falso, então o boneco deverá subir no quadrante, e descer caso o atributo seja verdadeiro.
	 * O mesmo ocorre com direcaoHorizontal, porém ao ser falso, o boneco nada para esquerda, sendo verdadeiro ele anda para direita.
	 */
	private boolean direcaoVertical; // true se estiver descendo
	private boolean direcaoHorizontal; // true se estiver indo para direita

	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		this.direcaoVertical = setDirecaoVertical();
		this.direcaoHorizontal = setDirecaoHorizontal();
		this.quadrante = setQuadrante(x, y);
		setLimitesDoQuadrante(quadrante);
		mudarMovimentacao();
		setDirecao(geraDirecaoAleatoria());

		//setDirecao(geraDirecaoAleatoria());
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

    		if (passosRestantes == 0 && faseHorizontalInicial) {//Faz eles andarem na horizontal no inicio
    		    if (x < meioX) {
    		        direcaoHorizontalInicial = DIREITA;//se estiverem antes do meio do mapa, andam para direita
    		    } else {
    		        direcaoHorizontalInicial = ESQUERDA;//sn, para esquerda
    		    }
    		    passosRestantes = idAgente;       //vão andar a distancia do Id
    		    setDirecao(direcaoHorizontalInicial); //qual direção cão iniciar
    		    return;
    		}

    		if (faseHorizontalInicial && passosRestantes > 0) {//se ainda tem oq andar
    		    setDirecao(direcaoHorizontalInicial);//define a direção inicial
    		    passosRestantes--; //vai diminuindo os passos restantes
    		    if (passosRestantes == 0) {//se atinge a distancia começa a andar vertical
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
		// Invocado quando o agente está na mesma posição que um agente inimigo
		// e eles estão batalhando (ambos tomam dano).
	}
	
	public void ganhouCombate() {
		// Invocado se estamos batalhando e nosso inimigo morreu.
	}
	
	public void recebeuMensagem(String msg) {
		String[] partes = msg.split(" ");
		int x = Integer.parseInt(partes[0]);
		int y = Integer.parseInt(partes[1]);
		if(x>getX()){
			setDirecao(1);
		}else{
			setDirecao(2);
		}
		if(y>getY()){
			setDirecao(3);
		}else{
			setDirecao(4);
		}
		// Invocado sempre que um agente aliado próximo envia uma mensagem.
	}
	
	public String getEquipe() {
		// Definimos que o nome da equipe do agente é "Fernando".
		return "Gabriel Balbinot";
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

		Random r = new Random();
		/*

		geraNumeroAleatorio é utilizada para definir se o boneco vai começar indo para baixo ou para cima,
		fazendo mod 2
		
		*/

		int geraNumeroAleatorio = r.nextInt(0, 1000);

		if (geraNumeroAleatorio % 2 == 0) {
			return true;
		}

		return false;
	}

	private boolean setDirecaoHorizontal() {
		Random r = new Random();
		/*

		geraNumeroAleatorio é utilizada para definir se o boneco vai começar indo para direita ou para esquerda,
		fazendo mod 2, porém aqui é o inverso da 
		
		*/
		int geraNumeroAleatorio = r.nextInt(0, 1000);
		if (geraNumeroAleatorio % 2 == 0) {
			return false;
		}

		return true;
	}

}


