import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que roda a interface gráfica e lógica do jogo.
 * @author Gabriel
 * @version 0.2
 * @see javax.swing.JFrame
 * @see java.util.List
 * @see Cartas
 * @see java.util.ArrayList
 * @see java.util.Timer
 */
public class InterfaceGrafica extends JFrame {
    private int tentativas = 0;
    private int pontuacao = 0;
    private int vitorias = 0;
    private Cartas cartavirada1;
    private Cartas cartavirada2;
    private JLabel pontuacaotexto;
    private JLabel tentativastexto;
    private final List<Cartas> cartasViradas = new ArrayList<>();
    private final List<Cartas> cartasPontuadas = new ArrayList<>();
    private JFrame janelaVencedor;
    private JFrame janelaTop10Vencedores;

    /**
     * Desenha a Interface gráfica do jogo.
     * @since 0.1v
     * @see Cartas
     */
    InterfaceGrafica(){
        setSize(1000,1000);
        setTitle("Jogo da Memória");
        setLocationRelativeTo(null);
        setLayout(null);

        pontuacaotexto = new JLabel("PONTUAÇÃO: " + pontuacao);
        pontuacaotexto.setBounds(100,800,100,100);
        add(pontuacaotexto);

        tentativastexto = new JLabel("TENTATIVAS: " + tentativas);
        tentativastexto.setBounds(800,800,100,100);
        add(tentativastexto);

        JButton botaoreiniciarjogo = new JButton("Reiniciar");
        botaoreiniciarjogo.setBounds(500,800,100,50);
        add(botaoreiniciarjogo);
        botaoreiniciarjogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Jogo Reiniciado");

                cartasPontuadas.forEach(Cartas::virar);
                cartasPontuadas.clear();
                cartasViradas.forEach(Cartas::virar);
                cartasViradas.clear();
                tentativas = 0;
                pontuacao = 0;
                atualizarTextoPontuacao();
                atualizarTextoTentativas();
            }
        });

        JButton botaoPlacarJogadoresTop10 = new JButton("Placar");
        botaoPlacarJogadoresTop10.setBounds(500,900,80,50);
        add(botaoPlacarJogadoresTop10);
        botaoPlacarJogadoresTop10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janelaPlacarTop10Vencedores();
            }
        });

        //CRIAÇÂO DE CARTAS
        Cartas carta1 = new Cartas("carta1",200,10);
        Cartas carta2 = new Cartas("carta2",10,10);
        Cartas carta3 = new Cartas("carta1",500,10);
        Cartas carta4 = new Cartas("carta3",200,100);
        Cartas carta5 = new Cartas("carta3",500,100);

        carta1.addActionListener(e -> processarClique(carta1));
        carta2.addActionListener(e -> processarClique(carta2));
        carta3.addActionListener(e -> processarClique(carta3));
        carta4.addActionListener(e -> processarClique(carta4));
        carta5.addActionListener(ActionEvent -> processarClique(carta5));

        add(carta1);
        add(carta2);
        add(carta3);
        add(carta4);
        add(carta5);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Processa toda a lógica de cliques, rotação de cartas e número de tentativas.
     * @param carta Carta clicada pelo jogador.
     * @since 0.1v
     * @see java.util.Timer
     */
    private void processarClique(Cartas carta){
        if(carta.isVirada() || cartasViradas.size() == 2) return; //Impede o jogador de virar a mesma carta ou mais de duas

        carta.virar();
        cartasViradas.add(carta);//Adiciona a carta em uma Lista de cartas viradas

        if(cartasViradas.size() == 2){//apos selecionar 2 cartas, compara elas e atualiza as tentativas
            tentativas++;
            atualizarTextoTentativas();
            compararCartas();

            if (compararCartas()){
                cartasPontuadas.add(cartavirada1);//salva as cartas pontuadas em uma lista para virá-las no final do jogo ou quando o botao reset for pressionado
                cartasPontuadas.add(cartavirada2);
                pontuacao++;
                pontuacaotexto.setText("PONTUAÇÃO: " + pontuacao);
                cartasViradas.clear();
            }else{
                Timer timer = new Timer(1000, e -> {
                    cartasViradas.forEach(Cartas::virar);
                    cartasViradas.clear();
                });
                timer.setRepeats(false);
                timer.start();
            }

        }
        verificarcondicaoFinalJogoEFinalizar();
    }

    /**
     * Usado para verificar se as cartas são iguais.
     * @since 0.2v
     * @return Retorna se as cartas viradas são iguais umas as outras.
     */
    private boolean compararCartas(){
        cartavirada1 = cartasViradas.get(0);
        cartavirada2 = cartasViradas.get(1);

        String conteudocarta1 = cartavirada1.getConteudo();
        String conteudocarta2 = cartavirada2.getConteudo();

        return conteudocarta1.equals(conteudocarta2);
    }

    /**
     * Executado na para verificar se o jogo atingiu a confição final, caso tenha atingido executa o metodo janelaVencedor.
     * @since 0.2v
     * @see JOptionPane
     */
    private void verificarcondicaoFinalJogoEFinalizar(){
        if(pontuacao == 2){
            janelaVencedor();
        }
    }

    private void janelaVencedor(){
        janelaVencedor = new JFrame("VOCE VENCEU!!!");
        janelaVencedor.setSize(500,500);
        janelaVencedor.setLocationRelativeTo(null);
        janelaVencedor.setLayout(null);

        JLabel textoDigiteSeuNome = new JLabel("Digite seu nome:");
        textoDigiteSeuNome.setBounds(10,0,100,30);
        janelaVencedor.add(textoDigiteSeuNome);

        JTextField textoJanelaVencedorNomeJogador = new JTextField();
        textoJanelaVencedorNomeJogador.setBounds(130,0,200,30);
        janelaVencedor.add(textoJanelaVencedorNomeJogador);

        JLabel textoJanelaVencedorPontuacaoJogador = new JLabel("Sua pontuação foi: " + pontuacao);
        textoJanelaVencedorPontuacaoJogador.setBounds(150,50,150,50);
        janelaVencedor.add(textoJanelaVencedorPontuacaoJogador);

        JLabel textoJanelaVencedorTentativasJogador = new JLabel("Suas tentativas foram: " + tentativas);
        textoJanelaVencedorTentativasJogador.setBounds(150,100,150,50);
        janelaVencedor.add(textoJanelaVencedorTentativasJogador);

        JButton botaoJanelaVencedorConfirmar = new JButton("Confirmar");
        botaoJanelaVencedorConfirmar.setBounds(175,320,100,50);
        janelaVencedor.add(botaoJanelaVencedorConfirmar);
        botaoJanelaVencedorConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PontuacaoJogador pontuacaojogador = new PontuacaoJogador();
                String nomeJogador = textoJanelaVencedorNomeJogador.getText();

                pontuacaojogador.setNomeJogador(nomeJogador);
                pontuacaojogador.setTentativasJogador(tentativas);

                pontuacaojogador.gerarJSON();


                vitorias++;
                pontuacao = 0;
                tentativas = 0;
                atualizarTextoPontuacao();
                atualizarTextoTentativas();
                cartasPontuadas.forEach(Cartas::virar);
                cartasPontuadas.clear();

                janelaVencedor.dispose();
            }
        });


        janelaVencedor.setVisible(true);
        janelaVencedor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void janelaPlacarTop10Vencedores(){
        janelaTop10Vencedores = new JFrame("PLACAR TOP 10 JOGADORES");
        janelaTop10Vencedores.setSize(400,300);
        janelaTop10Vencedores.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        janelaTop10Vencedores.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Posição");
        model.addColumn("Jogador");
        model.addColumn("Tentativas");

        List<PontuacaoJogador> top10jogadores = PontuacaoJogador.getTop10Jogadores();

        for(int i = 0; i < top10jogadores.size(); i++){
            PontuacaoJogador jogador = top10jogadores.get(i);
            model.addRow(new Object[]{
                    i + 1,
                    jogador.getNomeJogador(),
                    jogador.getTentativasJogador()
            });
        }

        JTable tabela = new JTable(model);
        JScrollPane scrollPaneJanelaTop10 = new JScrollPane(tabela);
        janelaTop10Vencedores.add(scrollPaneJanelaTop10);

        janelaTop10Vencedores.setVisible(true);
    }

    /**
     * Atualiza o texto de Pontuação na tela.
     * @since 0.2v
     */
    private void atualizarTextoPontuacao(){
        pontuacaotexto.setText("PONTUAÇÃO: " + pontuacao);
    }

    /**
     * Atualiza o texto de Tentativa na tela.
     * @since 0.2v
     */
    private void atualizarTextoTentativas(){
        tentativastexto.setText("TENTATIVAS: " + tentativas);
    }

    /**
     * Usado para testes envolvendo numero de tentativas.
     * @since 0.1v
     * @return Número de tentativas realizadas.
     */
    public int getTentativas(){
        return tentativas;
    }

    /**
     * Usado para testes envolvendo numero da pontuação.
     * @since 0.2v
     * @return Retorna a pontuação atual do jogo.
     */
    public int getPontuacao() { return pontuacao; }

    /**
     * Retorna o numero de vitorias para testes
     * @since 0.2v
     * @return Numero de vitorias
     */
    public int getVitorias() { return vitorias; }

}