/**
 * Um exemplo de agente que anda aleatoriamente na arena. Esse agente pode ser usado como base
 * para a criação de um agente mais esperto. Para mais informações sobre métodos que podem
 * ser utilizados, veja a classe Agente.java.
 * 
 * Gabriel Francisco Dall Rosa Balbinot <gabriel.balbinot@estudante.uffs.edu.br>
 */

package br.uffs.cc.jarena;

public class AgenteDummy extends Agente
{
	private static int qtdAgentes = 0;

	private int quadrante;
	private int[] limitesHorizontas = new int[2];
	private int[] limitesVerticais = new int[2];

	private boolean descendo;
	private boolean indoParaDireita;

	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		qtdAgentes++;

		this.descendo = setDescida();
		this.indoParaDireita = setDireita();

		this.quadrante = setQuadrante(x, y);
		setLimitesDoQuadrante(quadrante);

		//setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {

		mudarMovimentacao();
			
		if(podeDividir() && getEnergia() >= 2000) {
			divide();
		}
	}
	
	public void recebeuEnergia() {
		// Invocado sempre que o agente recebe energia.
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
			this.indoParaDireita = false;

		} else if (x <= limitesHorizontas[0]) {
			
			this.indoParaDireita = true;
		}

		if (y >= limitesVerticais[1]) {
			
			this.descendo = false;
		} else if (y <= limitesVerticais[0]) {
			
			this.descendo = true;
		}
	}

	private void mudarMovimentacao() {
		bateuNosLimites(getX(), getY());

		if (this.descendo && this.indoParaDireita) {

			if (getDirecao() == DIREITA) {
				setDirecao(BAIXO);
			} else {
				setDirecao(DIREITA);
			}

		} else if (!this.descendo && this.indoParaDireita) {

			if (getDirecao() == DIREITA) {
				setDirecao(CIMA);
			} else {
				setDirecao(DIREITA);
			}

		} else if (this.descendo && !this.indoParaDireita) {

			if (getDirecao() == ESQUERDA) {
				setDirecao(BAIXO);
			} else {
				setDirecao(ESQUERDA);
			}

		} else if (!this.descendo && !this.indoParaDireita) {

			if (getDirecao() == ESQUERDA) {
				setDirecao(CIMA);
			} else {
				setDirecao(ESQUERDA);
			}

		}
		
	}

	private boolean setDescida() {
		if (qtdAgentes % 2 == 0) {
			return true;
		}

		return false;
	}

	private boolean setDireita() {
		if (qtdAgentes % 2 == 0) {
			return false;
		}

		return true;
	}

}


