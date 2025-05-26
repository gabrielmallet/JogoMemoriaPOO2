import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
}
