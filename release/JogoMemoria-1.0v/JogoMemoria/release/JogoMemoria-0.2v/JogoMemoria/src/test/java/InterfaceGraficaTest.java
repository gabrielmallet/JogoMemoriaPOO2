import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            cartas = obterCartasDoFrame();
            botoes = obterBotoesDoFrame();
        });
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
        JButton reset = botoes.get(1);

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

    /**
     * Verifica se o programa esta detectando se o jogo terminou, e tambem verifica se ele retorna se o jogo voltou para o estado inicial apos o fim.
     * @throws Exception
     */
    @Test
    void testCondicaoFinaldeJogoFuncional() throws Exception{
        Cartas carta1 = cartas.get(0);
        Cartas carta2 = cartas.get(2);
        Cartas carta3 = cartas.get(3);
        Cartas carta4 = cartas.get(4);
        SwingUtilities.invokeAndWait(() ->{
            carta1.doClick();
            carta2.doClick();
        });
        Thread.sleep(3000);
        SwingUtilities.invokeAndWait(() ->{
            carta3.doClick();
            carta4.doClick();
        });
        assertEquals(0,interfaceGrafica.getTentativas());
        assertEquals(0,interfaceGrafica.getPontuacao());
        assertEquals(1,interfaceGrafica.getVitorias());
        assertFalse(carta1.isVirada());
        assertFalse(carta2.isVirada());
        assertFalse(carta3.isVirada());
        assertFalse(carta4.isVirada());
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
}