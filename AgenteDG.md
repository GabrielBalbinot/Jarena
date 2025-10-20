# Estratégia do `AgenteDG`

## Autores
- **Davi Henrique Pezenatto**: `davinatto@gmail.com`
- **Gabriel Francisco Dall Rosa Balbinot**: `gabriel.balbinot@estudante.uffs.edu.br`

## 1. Visão Geral

O `AgenteDG` é projetado para operar de forma eficiente em um ambiente competitivo, equilibrando a coleta de recursos (energia), combate e cooperação em equipe. A estrategia se baseia em quatro pilares:

1.  **Posicionamento Inicial Estratégico**: Garante que os agentes se espalhem pelo mapa para uma cobertura mais ampla.
2.  **Movimentação Diagonal Constante**: Um padrão de patrulha eficiente para cobrir a área de um quadrante.
3.  **Comunicação e Cooperação**: Agentes se ajudam para capturar cogumelos de forma mais rápida.
4.  **Gerenciamento de Energia e Sobrevivência**: Regras claras para quando lutar, fugir, parar de se mover ou se reproduzir.

---

## 2. Fases de Comportamento

A estratégia do agente pode ser dividida em várias fases e comportamentos específicos.

### 2.1. Inicialização (Construtor)

Ao ser criado, cada agente define seu estado inicial para garantir diversidade no comportamento da equipe:

-   **Definição de Quadrante**: O mapa é dividido em quatro quadrantes. O agente identifica em qual quadrante ele nasceu e define suas fronteiras de atuação (`setQuadrante` e `setLimitesDoQuadrante`). Isso evita que todos os agentes se concentrem em uma única área.
-   **Direção Inicial**: A direção inicial de movimento (vertical e horizontal) é determinada com base no posicionamento inicial do agente em relação ao centro do mapa, possibilitando que o agende sempre se mova e venha a cobrir a maior direção do mapa.

### 2.2. Movimento Inicial de Dispersão

Antes de iniciar sua patrulha normal, o agente executa uma rotina de posicionamento única:

1.  **Movimento Horizontal**: O agente se move horizontalmente em direção ao centro do mapa. A distância percorrida é proporcional ao seu `ID` (`idAgente = getId() * 3`), o que ajuda a criar um espaçamento entre eles.
2.  **Movimento Vertical**: Após completar a fase horizontal, ele dá um passo na vertical, também em direção ao centro do mapa.

Este comportamento (`!movimentoInicialFeito`) ocorre apenas uma vez e serve para tirar os agentes de suas posições de nascimento e distribuí-los de forma mais centralizada em seus respectivos quadrantes.

### 2.3. Movimentação Padrão (Patrulha Diagonal)

Uma vez posicionado, o agente inicia seu ciclo de movimento principal (`mudarMovimentacao`):

-   **Padrão Diagonal**: O movimento é feito alternando um passo na vertical e um passo na horizontal (ex: CIMA, DIREITA, CIMA, DIREITA...). Isso cria uma varredura diagonal eficiente da área.
-   **Respeito aos Limites**: Ao atingir a borda de seu quadrante (`bateuNosLimites`), o agente inverte a direção de movimento naquele eixo (por exemplo, se estava indo para a direita, passa a ir para a esquerda), mantendo-se sempre dentro de sua zona de patrulha.

### 2.4. Coleta de Energia e Cooperação

-   **Encontrou Energia**: Ao encontrar um cogumelo (`recebeuEnergia`), o agente imediatamente **para** de se mover para absorver a energia.
-   **Chamado aos Aliados**: Simultaneamente, ele transmite uma mensagem contendo suas coordenadas (`enviaMensagem(a + " " + b)`).
-   **Resposta ao Chamado**: Agentes da mesma equipe que recebem essa mensagem (`recebeuMensagem`) ajustam sua direção de movimento para se deslocarem em direção às coordenadas recebidas, convergindo para o local do cogumelo.

### 2.5. Estratégia de Combate

-   **Tomou Dano (`tomouDano`)**: Se um agente entra em combate e o inimigo possui mais energia que ele, o agente considera a luta desvantajosa. Como mecanismo de fuga, ele **inverte sua direção de movimento** (tanto horizontal quanto vertical) para tentar escapar.
-   **Ganhou Combate (`ganhouCombate`)**: Vencer um combate concede energia. Se o agente estava no estado de "parado por baixa energia", uma vitória pode lhe dar energia suficiente (`> 250`) para **retomar sua movimentação normal**.

### 2.6. Gerenciamento de Energia e Ciclo de Vida

-   **Energia Baixa (Modo de Sobrevivência)**: Se a energia do agente ficar igual ou inferior a 100, ele entra em um estado de hibernação (`nuncaMaisAndar = true`). Ele para de se mover completamente para conservar a pouca energia que lhe resta, a menos que vença um combate.
-   **Reprodução**: Se o agente acumular bastante energia (`>= 2000`) e a arena permitir (`podeDividir`), ele se divide, criando um novo agente aliado para fortalecer a equipe.