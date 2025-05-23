import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que roda a interface gráfica e lógica do jogo.
 * @author Gabriel
 * @version 0.1
 * @see javax.swing.JFrame
 * @see java.util.List
 * @see Cartas
 * @see java.util.ArrayList
 * @see java.util.Timer
 */
public class InterfaceGrafica extends JFrame {
    private int tentativas = 0;
    private final List<Cartas> cartasViradas = new ArrayList<>();

    /**
     * Desenha a Interface gráfica do jogo.
     * @see Cartas
     */
    InterfaceGrafica(){
        setSize(1000,1000);
        setTitle("Jogo da Memória");
        setLocationRelativeTo(null);
        setLayout(null);

        //CRIAÇÂO DE CARTAS
        Cartas carta1 = new Cartas("carta1",200,10);
        Cartas carta2 = new Cartas("carta2",10,10);
        Cartas carta3 = new Cartas("carta1",500,10);
        Cartas carta4 = new Cartas("carta3",200,100);

        carta1.addActionListener(e -> processarClique(carta1));
        carta2.addActionListener(e -> processarClique(carta2));
        carta3.addActionListener(e -> processarClique(carta3));
        carta4.addActionListener(e -> processarClique(carta4));

        add(carta1);
        add(carta2);
        add(carta3);
        add(carta4);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Processa toda a lógica de cliques, rotação de cartas e número de tentativas.
     * @param carta Carta clicada pelo jogador.
     * @see java.util.Timer
     */
    private void processarClique(Cartas carta){
        if(carta.isVirada() || cartasViradas.size() == 2) return;

        carta.virar();
        cartasViradas.add(carta);

        if(cartasViradas.size() == 2){
            tentativas++;
            System.out.println("Tentativa: " + tentativas);

            Timer timer = new Timer(1000, e -> {
                cartasViradas.forEach(Cartas::virar);
                cartasViradas.clear();
            });
            timer.setRepeats(false);
            timer.start();
        }

    }

    public int getTentativas(){
        return tentativas;
    }

}