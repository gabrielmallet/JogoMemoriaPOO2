import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//ALGUNS CÓDIGOS FORAM PRODUZIDOS COM AJUDO DO DEEPSEEK, JÁ QUE NÃO RETINHA OS CONHECIMENTOS NECESSÁRIOS DE CERTOS MÉTODOS.
//OS MESMOS FORAM CRIADOS COM O INTUITO DE APRENDER E FOI REALIZADA UMA ANÁLISE PARA ENTENDER A LÓGICA DO CÓDIGO APLICADO.

//NÃO FORAM REALIZADOS TESTES UNITÁRIOS ESPECÍFICOS PARA A CLASSE PONTUAÇÃOJOGADOR POIS SEUS MÉTODOS SÃO TODOS UTILIZADOS EM TESTES JÁ REALIZADOS NESTA MESMA CLASSE.

/**
 * Classe de teste que utiliza JUnit.
 * @see SwingUtilities
 * @see InterfaceGrafica
 * @see Cartas
 * @see JButton
 * @see Thread
 */
public class InterfaceGraficaTest {
    private InterfaceGrafica interfaceGrafica;
    private List<Cartas> cartas;
    private List<JButton> botoes;

    /**
     * Prepara o cenario para testes.
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception{
        SwingUtilities.invokeAndWait(() ->{
            interfaceGrafica = new InterfaceGrafica();
            interfaceGrafica.setVisible(true);
            deletarArquivoPontuacoes();
            cartas = obterCartasDoFrame();
            botoes = obterBotoesDoFrame();
        });
    }

    /**
     * Testa se a tabela de placar contém dados.
     * @throws Exception
     */
    @Test
    void testConteudoTabelaPlacar() throws Exception {
        // Adiciona alguns jogadores de teste
        PontuacaoJogador jogador1 = new PontuacaoJogador();
        jogador1.setNomeJogador("Jogador A");
        jogador1.setTentativasJogador(5);
        jogador1.gerarJSON();



        PontuacaoJogador jogador2 = new PontuacaoJogador();
        jogador2.setNomeJogador("Jogador B");
        jogador2.setTentativasJogador(8);
        jogador2.gerarJSON();

        // Abre a janela de placar
        SwingUtilities.invokeAndWait(() -> interfaceGrafica.janelaPlacarTop10Vencedores());
        Thread.sleep(500);

        SwingUtilities.invokeAndWait(() -> {
            JFrame janela = interfaceGrafica.janelaTop10Vencedores;
            JScrollPane scrollPane = (JScrollPane) janela.getContentPane().getComponent(0);
            JTable tabela = (JTable) scrollPane.getViewport().getView();
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();

            assertTrue(model.getRowCount() > 0, "Tabela deve conter dados");
            assertEquals("Jogador A", model.getValueAt(0, 1), "Melhor jogador deve estar no topo");
            assertEquals(5, model.getValueAt(0, 2), "Tentativas devem corresponder");
        });
    }

    /**
     * Testa se a janela de placar é aberta ao clicar no botão.
     * @throws Exception
     */
    @Test
    void testJanelaVencedorAberta() throws Exception {
        // Simula a condição de vitória
        SwingUtilities.invokeAndWait(() -> {
            interfaceGrafica.pontuacao = 2; // Força condição de vitória
            interfaceGrafica.janelaVencedor(); // Chama diretamente o método privado
        });

        // Verifica se a janela foi criada
        SwingUtilities.invokeAndWait(() -> {
            assertNotNull(interfaceGrafica.janelaVencedor, "Janela de vencedor deve ser criada");
            assertTrue(interfaceGrafica.janelaVencedor.isVisible(), "Janela deve estar visível");
            assertEquals("VOCE VENCEU!!!", interfaceGrafica.janelaVencedor.getTitle(), "Título incorreto");
        });
    }

    /**
     * Testa o salvamento do jogador ao confirmar na janela de vitória.
     * @throws Exception
     */
    @Test
    void testSalvarJogadorNaJanelaVencedor() throws Exception {
        // Simula vitória
        SwingUtilities.invokeAndWait(() -> {
            interfaceGrafica.pontuacao = 2;
            interfaceGrafica.janelaVencedor();
        });

        // Obtém componentes da janela de vencedor
        JFrame janelaVencedor = interfaceGrafica.janelaVencedor;
        JTextField campoNome = null;
        JButton botaoConfirmar = null;

        for (Component c : janelaVencedor.getContentPane().getComponents()) {
            if (c instanceof JTextField) campoNome = (JTextField) c;
            if (c instanceof JButton && ((JButton) c).getText().equals("Confirmar")) {
                botaoConfirmar = (JButton) c;
            }
        }

        assertNotNull(campoNome, "Campo de nome não encontrado");
        assertNotNull(botaoConfirmar, "Botão confirmar não encontrado");

        // Preenche nome e clica em confirmar
        JTextField finalCampoNome = campoNome;
        JButton finalBotaoConfirmar = botaoConfirmar;
        SwingUtilities.invokeAndWait(() -> {
            finalCampoNome.setText("Jogador Teste");
            finalBotaoConfirmar.doClick();
        });

        // Verifica se o estado do jogo foi resetado
        assertEquals(0, interfaceGrafica.getPontuacao(), "Pontuação deve ser resetada para 0");
        assertEquals(0, interfaceGrafica.getTentativas(), "Tentativas devem ser resetadas para 0");
        assertEquals(1, interfaceGrafica.getVitorias(), "Vitórias devem ser incrementadas para 1");
    }

    /**
     * Verifica se as cartas começam sem mostrar o conteudo.
     * @throws Exception
     */
    @Test
    void testCartaComecaDesvirada() throws Exception{
        Cartas carta = cartas.get(0);
        assertFalse(carta.isVirada(),"Carta deve começar desvirada");
    }

    /**
     * Verifica se o clique em uma carta vira ela.
     * @throws Exception
     */
    @Test
    void testCliqueEmCartaDeveVirar() throws Exception {
        Cartas carta = cartas.get(0);
        SwingUtilities.invokeAndWait(carta::doClick); // Simula clique na carta
        assertTrue(carta.isVirada(), "A carta deve estar virada após o clique");
    }

    /**
     * Verifica se ao clicar na mesma carta ela permanece virada.
     * @throws Exception
     */
    @Test
    void testCliqueNaMesmaCartaNaoDeveVirar() throws Exception{
        Cartas carta = cartas.get(0);
        SwingUtilities.invokeAndWait(carta::doClick);

        SwingUtilities.invokeAndWait(carta::doClick);
        assertTrue(carta.isVirada(),"A carta deve se manter virada após o clique");
    }

    /**
     * Verifica se a carta desvirar apos um timer de 1000 ms.
     * @throws Exception
     */
    @Test
    void testCartaDesviraAposTimer() throws Exception{
        Cartas carta1 = cartas.get(0);
        Cartas carta2 = cartas.get(1);

        SwingUtilities.invokeAndWait(() ->{
            carta1.doClick();
            carta2.doClick();
        });
        assertTrue(carta1.isVirada());
        assertTrue(carta2.isVirada());

        Thread.sleep(1100);
        assertFalse(carta1.isVirada());
        assertFalse(carta2.isVirada());
    }

    /**
     * Verifica se clicar em duas cartas aumenta  o numero de tentativas.
     * @throws Exception
     */
    @Test
    void testCliqueEmDuasCartasDeveAumentarTentativas() throws Exception {
        Cartas carta1 = cartas.get(0);
        Cartas carta2 = cartas.get(1);

        SwingUtilities.invokeAndWait(() -> {
            carta1.doClick();
            carta2.doClick();
        });

        // Verifica o contador de tentativas (via reflexão ou expondo um getter)
        assertEquals(1, interfaceGrafica.getTentativas(),"Tentativa deve dar 1");
    }

    /**
     * Confere se compara as cartas e mantem elas viradas apos valor igual.
     * @throws Exception
     */
    @Test
    void testCartaPermaneceViradaAposPontuada() throws Exception{
        Cartas carta1 = cartas.get(3);
        Cartas carta2 = cartas.get(4);

        SwingUtilities.invokeAndWait(() ->{
            carta1.doClick();
            carta2.doClick();
        });
        Thread.sleep(5000);
        assertTrue(carta1.isVirada(),"Deve se manter virada");
        assertTrue(carta2.isVirada(),"Deve se manter virada");
    }

    /**
     * Verifica se a pontuacao conta apos duas cartas iguais forem viradas.
     * @throws Exception
     */
    @Test
    void testPontuacaodeveContar() throws Exception{
        Cartas carta1 = cartas.get(3);
        Cartas carta2 = cartas.get(4);

        SwingUtilities.invokeAndWait(() ->{
            carta1.doClick();
            carta2.doClick();
        });
        assertEquals(1, interfaceGrafica.getPontuacao(),"Pontuacao deve marcar 1");
    }

    /**
     * Verifica se o botão resetar retorna o jogo ao estado inicial.
     * @throws Exception
     */
    @Test
    void testBotaoResetarDeveZerarTudo() throws Exception{
        Cartas carta3 = cartas.get(2);
        Cartas carta4 = cartas.get(3);
        Cartas carta5 = cartas.get(4);
        JButton reset = botoes.get(0);

        SwingUtilities.invokeAndWait(() ->{
            carta4.doClick();
            carta5.doClick();
        });
        SwingUtilities.invokeAndWait(() ->{
            carta3.doClick();
            reset.doClick();
        });
        assertEquals(0,interfaceGrafica.getTentativas());
        assertEquals(0,interfaceGrafica.getPontuacao());
        assertFalse(carta3.isVirada());
        assertFalse(carta4.isVirada());
        assertFalse(carta5.isVirada());

    }

    //Daqui para baixo são métodos que registra botões em ArraysLists, facilitando os testes.
    /**
     * Usado nos testes, esse metodo cria uma lista com as cartas presentes na interface grafica do jogo.
     * @return Retorna uma lista com as cartas presentes na interface grafica do jogo.
     */
    private java.util.List<Cartas> obterCartasDoFrame() {
        Component[] components = interfaceGrafica.getContentPane().getComponents();
        java.util.List<Cartas> cartasList = new ArrayList<>();
        for (Component c : components) {
            if (c instanceof Cartas) {
                cartasList.add((Cartas) c);
            }

        }
        return cartasList;
    }

    /**
     * Usado nos testes, este metodo cria uma lista com os demais botões presentes na interface gráfica do jogo.
     * @return Retorna em uma lista os outros botões presentes na interface grafica do jogo.
     */
    private java.util.List<JButton> obterBotoesDoFrame() {
        Component[] components = interfaceGrafica.getContentPane().getComponents();
        java.util.List<JButton> botoesList = new ArrayList<>();
        for (Component c : components) {
            if (c instanceof JButton) {
                botoesList.add((JButton) c);
            }

        }
        return botoesList;
    }

    /**
     * Verifica se o arquivo ja existe e deleta ele caso já tenha sido criado
     * @see File
     */
    public void deletarArquivoPontuacoes() {
        File arquivo = new File("placares/pontuacoes.json");

        if (arquivo.exists()) {
            if (arquivo.delete()) {
                System.out.println("Arquivo pontuacoes.json deletado com sucesso.");
            } else {
                System.out.println("Falha ao deletar o arquivo.");
            }
        } else {
            System.out.println("Arquivo não existe.");
        }
    }
}