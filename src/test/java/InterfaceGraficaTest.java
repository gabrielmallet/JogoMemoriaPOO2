import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InterfaceGraficaTest {
    private InterfaceGrafica interfaceGrafica;
    private List<Cartas> cartas;


    @BeforeEach
    void setUp() throws Exception{
        SwingUtilities.invokeAndWait(() ->{
            interfaceGrafica = new InterfaceGrafica();
            interfaceGrafica.setVisible(true);
            cartas = obterCartasDoFrame();
        });
    }

    @Test
    void testCartaComecaDesvirada() throws Exception{
        Cartas carta = cartas.get(0);
        assertFalse(carta.isVirada(),"Carta deve começar desvirada");
    }

    @Test
    void testCliqueEmCartaDeveVirar() throws Exception {
        Cartas carta = cartas.get(0);
        SwingUtilities.invokeAndWait(carta::doClick); // Simula clique na carta
        assertTrue(carta.isVirada(), "A carta deve estar virada após o clique");
    }

    @Test
    void testCliqueEmCartaNaoDeveVirar() throws Exception{
        Cartas carta = cartas.get(0);
        SwingUtilities.invokeAndWait(carta::doClick);

        SwingUtilities.invokeAndWait(carta::doClick);
        assertTrue(carta.isVirada(),"A carta deve se manter virada após o clique");
    }

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
}