import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PontuacaoJogador {
    private String nomeJogador;
    private int tentativas;
    private static final String CAMINHO_ARQUIVO = "placares/pontuacoes.json";

    PontuacaoJogador(){

    }

    public PontuacaoJogador(int tentativas, String nomeJogador){
        this.tentativas = tentativas;
        this.nomeJogador = nomeJogador;
    }

    public String getNomeJogador(){
        return nomeJogador;
    }

    public int getTentativasJogador(){
        return tentativas;
    }

    public void setNomeJogador(String nomeJogador){
        this.nomeJogador = nomeJogador;

        System.out.println(nomeJogador);
    }
    public void setTentativasJogador(int tentativas){
        this.tentativas = tentativas;

        System.out.print(tentativas);
    }

    /**
     * Verifica se um arquivo JSON já existe, lê o arquivo e retorna uma Lista PontuacaoJogador
     * @return Retorna uma lista com os players e suas tentativas
     */
    public static List<PontuacaoJogador> lerJogadoresArquivo(){
        ObjectMapper mapper = new ObjectMapper();
        File arquivo = new File(CAMINHO_ARQUIVO);

        if(!arquivo.exists()){
            return new ArrayList<>();
        }

        try{
            return mapper.readValue(arquivo, new TypeReference<List<PontuacaoJogador>>() {
            });
        }catch (IOException e){
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void gerarJSON(){
        List<PontuacaoJogador> jogadores = lerJogadoresArquivo();

        jogadores.add(this);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try{
            File arquivo = new File(CAMINHO_ARQUIVO);
            File diretorio = arquivo.getParentFile();

            if (!diretorio.exists()){
                diretorio.mkdirs();
            }

            mapper.writeValue(arquivo,jogadores);
            System.out.println("arquivo salvo em: " + arquivo.getAbsolutePath());

        }catch(IOException e){
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static List<PontuacaoJogador> getTop10Jogadores(){
        List<PontuacaoJogador> jogadores = lerJogadoresArquivo();

        Collections.sort(jogadores, new Comparator<PontuacaoJogador>() {
            @Override
            public int compare(PontuacaoJogador j1, PontuacaoJogador j2) {
                return Integer.compare(j1.getTentativasJogador(),j2.getTentativasJogador());
            }
        });

        int tamanho = Math.min(jogadores.size(), 10);
        return  jogadores.subList(0, tamanho);
    }
}
