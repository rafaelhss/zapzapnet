package web.zap.bussiness.metrics;

import lombok.Getter;
import lombok.Setter;
import web.zap.bussiness.Message;

import javax.persistence.*;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rafa on 25/06/2016.
 */
@Getter
@Setter
@Entity
public class MessageMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<WordCloudElement> wordCloud;

    private Integer msgs;
    private Integer timeBetweenMsg;


    private static List<String> blackList = Arrays.asList("pra","vou","tá", "omitted", "image", "aí", "vai", "aqui", "vc", "tb", "eh", "sim", "não", "bom", "pq",  "q", "", "a", "á", "agora", "ainda", "alguém", "algum", "alguma", "algumas", "alguns", "ampla", "amplas", "amplo", "amplos", "ante", "antes", "ao", "aos", "após", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "até", "através", "cada", "coisa", "coisas", "com", "como", "contra", "contudo", "da", "daquele", "daqueles", "das", "de", "dela", "delas", "dele", "deles", "depois", "dessa", "dessas", "desse", "desses", "desta", "destas", "deste", "deste", "destes", "deve", "devem", "devendo", "dever", "deverá", "deverão", "deveria", "deveriam", "devia", "deviam", "disse", "disso", "disto", "dito", "diz", "dizem", "do", "dos", "e", "é", "e'", "ela", "elas", "ele", "eles", "em", "enquanto", "entre", "era", "essa", "essas", "esse", "esses", "esta", "está", "estamos", "estão", "estas", "estava", "estavam", "estávamos", "este", "estes", "estou", "eu", "fazendo", "fazer", "feita", "feitas", "feito", "feitos", "foi", "for", "foram", "fosse", "fossem", "grande", "grandes", "há", "isso", "isto", "já", "la", "lá", "lhe", "lhes", "lo", "mas", "me", "mesma", "mesmas", "mesmo", "mesmos", "meu", "meus", "minha", "minhas", "muita", "muitas", "muito", "muitos", "na", "não", "nas", "nem", "nenhum", "nessa", "nessas", "nesta", "nestas", "ninguém", "no", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "num", "numa", "nunca", "o", "os", "ou", "outra", "outras", "outro", "outros", "para", "pela", "pelas", "pelo", "pelos", "pequena", "pequenas", "pequeno", "pequenos", "per", "perante", "pode", "pôde", "podendo", "poder", "poderia", "poderiam", "podia", "podiam", "pois", "por", "porém", "porque", "posso", "pouca", "poucas", "pouco", "poucos", "primeiro", "primeiros", "própria", "próprias", "próprio", "próprios", "quais", "qual", "quando", "quanto", "quantos", "que", "quem", "são", "se", "seja", "sejam", "sem", "sempre", "sendo", "será", "serão", "seu", "seus", "	si", "sido", "só", "sob", "sobre", "sua", "suas", "talvez", "também", "tampouco", "te", "tem", "tendo", "tenha", "ter", "teu", "teus", "ti", "tido", "tinha", "tinham", "toda", "todas", "todavia", "todo", "todos", "tu", "tua", "tuas", "tudo", "última", "últimas", "último", "últimos", "um", "uma", "umas", "uns", "vendo", "ver", "vez", "vindo", "vir", "vos", "vós", "a", "about", "above", "according", "across", "actually", "after", "again", "against", "all", "almost", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anything", "are", "aren", "as", "at", "away", "back", "back", "be", "because", "been", "before", "behind", "being", "below", "besides", "better", "between", "beyond", "both", "but", "by", "can", "certain", "could", "do", "does", "down", "during", "each", "else", "enough", "even", "ever", "few", "for", "from", "further", "get", "going", "got", "great", "has", "have", "he", "her", "here", "high", "his", "how", "however", "i", "if", "in", "instead", "into", "is", "it", "its", "itself", "just", "later", "least", "less", "less", "let", "little", "many", "may", "maybe", "me", "might", "more", "most", "much", "must", "neither", "never", "new", "no", "non", "nor", "not", "nothing", "of", "off", "often", "old", "on", "once", "one", "only", "or", "other", "our", "out", "over", "perhaps", "put", "rather", "really", "right", "set", "several", "she", "should", "since", "snot", "snt", "so", "some", "something", "sometimes", "soon", "still", "such", "t", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "this", "those", "though", "three", "through", "till", "to", "together", "too", "toward", "towards", "two", "under", "up", "upon", "us", "very", "very", "was", "were", "what", "when", "where", "whether", "which", "while", "whole", "whose", "will", "with", "within", "without", "would", "yet", "you", "your", "about", "after", "all", "and", "any", "an", "are", "as", "at", "a", "been", "before", "be", "but", "by", "can", "could", "did", "down", "do", "first", "for", "from", "good", "great", "had", "has", "have", "her", "he", "him", "his", "if", "into", "in", "is", "its", "it", "I", "know", "like", "little", "made", "man", "may", "men", "me", "more", "mr", "much", "must", "my", "not", "now", "no", "of", "one", "only", "on", "or", "other", "our", "out", "over", "said", "see", "she", "should", "some", "so", "such", "than", "that", "their", "them", "then", "there", "these", "they", "the", "this", "time", "to", "two", "upon", "up", "us", "very", "was", "were", "we", "what", "when", "which", "who", "will", "with", "would", "your", "you", "about", "all", "and", "are", "as", "at", "a", "back", "because", "been", "be", "but", "can't", "can", "come", "could", "didn't", "did", "don't", "do", "for", "from", "get", "going", "good", "got", "go", "had", "have", "he's", "here", "her", "hey", "he", "him", "his", "how", "I'll", "I'm", "if", "in", "is", "it's", "it", "I", "just", "know", "like", "look", "mean", "me", "my", "not", "now", "no", "of", "oh", "okay", "ok", "one", "on", "or", "out", "really", "right", "say", "see", "she", "something", "some", "so", "tell", "that's", "that", "then", "there", "they", "the", "think", "this", "time", "to", "up", "want", "was", "well", "were", "we", "what", "when", "who", "why", "will", "with", "would", "yeah", "yes", "you're", "your", "you");
    private static final Pattern UNDESIRABLES = Pattern.compile("[,.;!?<>%^]");

    private static final int MIN_LENGHT = 5 ;


    public MessageMetrics(){

    }
    public MessageMetrics(List<Message> msgs){



        Map<String, Long> wordCloudMap = msgs.stream()
                .flatMap(msg -> Pattern.compile(" ").splitAsStream(msg.getText()))
                .filter(msg -> msg.length() >= MIN_LENGHT)
                .map(msg -> UNDESIRABLES.matcher(msg.trim().toLowerCase()).replaceAll(""))
                .map(msg -> Normalizer
                        .normalize(msg, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", ""))
                .filter(msg -> blackList.stream().noneMatch(word -> word.equals(msg.toLowerCase())))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        this.wordCloud = wordCloudMap.entrySet().stream()
                .map(msg -> new WordCloudElement(null, msg.getKey(),msg.getValue()))
                .collect(Collectors.toList());


/*
        List<String> types = wordCloud.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(e -> e.getKey() + "-" + e.getValue())
                .collect(Collectors.toList());

        types.forEach(System.out::println);
*/
    }

}
