import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Classe que lida com a lógica de tudo relacionado ao arquivo JSON que salva a pontuação de jogadores
 * @author Gabriel
 * @since 1.0v
 * @see Comparator,List,ArrayList,File,ObjectMapper
 */
public class PontuacaoJogador {
    private String nomeJogador;
    private int tentativas;
    private static final String CAMINHO_ARQUIVO = "placares/pontuacoes.json";

    PontuacaoJogador(){
    }

    /**
     * Retorna nome do jogador armazenado na variável nomeJogador.
     * @return Retorna o nome do jogador.
     */
    public String getNomeJogador(){
        return nomeJogador;
    }

    /**
     * Retorna número de tentativas armazenados na variável tentativas.
     * @return Retorna número de tentativas.
     */
    public int getTentativasJogador(){
        return tentativas;
    }

    /**
     * Define qual é o nome do jogador.
     * @param nomeJogador
     */
    public void setNomeJogador(String nomeJogador){
        this.nomeJogador = nomeJogador;
    }

    /**
     * Define qual foi o número de tentativas.
     * @param tentativas
     */
    public void setTentativasJogador(int tentativas){
        this.tentativas = tentativas;
    }

    /**
     * Verifica se um arquivo JSON já existe, lê o arquivo e retorna uma Lista PontuacaoJogador
     * @since 1.0v
     * @see ObjectMapper,File,ArrayList
     * @return Retorna uma lista com os players e suas tentativas
     */
    public static List<PontuacaoJogador> lerJogadoresArquivo(){
        ObjectMapper mapper = new ObjectMapper();
        File arquivo = new File(CAMINHO_ARQUIVO);

        if(!arquivo.exists()){
            return new ArrayList<>();//retorna uma lista vazia caso o arquivo JSON não exista
        }

        try{
            return mapper.readValue(arquivo, new TypeReference<List<PontuacaoJogador>>() {//le o arquivo JSON e retorna uma Array com os valores escritos nele
            });
        }catch (IOException e){
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Cria um arquivo em JSON ou atualiza um existente. Cria o diretório caso não exista também.
     * @see ArrayList,ObjectMapper,File
     * @since 1.0v
     */
    public void gerarJSON(){
        List<PontuacaoJogador> jogadores = lerJogadoresArquivo();
        jogadores.add(this);//adiciona o jogador atual na lista

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

    /**
     * Cria uma sublista com os jogadores melhor pontuados, baseando-se no número de tentativas.
     * @see Collections,ArrayList,Integer
     * @return Retorna uma lista com os 10 jogadores que concluiram o jogo com o menor número de tentativas.
     */
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
