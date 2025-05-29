import javax.swing.*;
/**
 * Classe que gera a logica das cartas.
 * @author Gabriel
 * @version 0.1
 * @see javax.swing.JButton
 * @see java.awt.Component
 * @see javax.swing.AbstractButton
 */
public class Cartas extends JButton {
    private String conteudo;
    private String esconder = "";
    private int height = 100;
    private int width = 100;
    private boolean virada = false;
    /**
     * Metodo principal usado para gerar as cartas.
     * @param nome Nome da carta.
     * @param x Posição X da carta na janela.
     * @param y Posição Y da carta na janela.
     */
    Cartas(String nome, int x, int y){
        this.conteudo = nome;
        setBounds(x,y,width,height);
    }
    /**
     * Processamento lógico para virar a carta.
     */
    public void virar(){
        virada = !virada;
        setText(virada ? conteudo : esconder);
    }
    /**
     * Retorna se a carta esta virada ou não.
     * @return Estado atual da carta.
     */
    public boolean isVirada(){
        return virada;
    }
    /**
     * Retorna o que esta escrito na carta.
     * @return Retorna o conteudo da carta.
     */
    public String getConteudo(){
        return conteudo;
    }
}