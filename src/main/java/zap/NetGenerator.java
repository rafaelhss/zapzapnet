package zap;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.list.SetUniqueList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;
import java.util.stream.StreamSupport;
public class NetGenerator {

    List<String> vertices = SetUniqueList.decorate(new ArrayList<String>());
    List<Date> datas = SetUniqueList.decorate(new ArrayList<Date>());
    ArrayList<Conn> edges = new ArrayList<>();


    public void generate(List<Connection> connections){


        //IF LONGITONNAL ...
        connections.forEach(this::geraConexao);

        System.out.println("taamnho:" + vertices.size());
        StringBuilder net = new StringBuilder();
        net.append("*Vertices " + vertices.size());
        vertices.stream().forEach(v -> net.append(System.lineSeparator() + (vertices.indexOf(v)+1) + " " + v));

        net.append(System.lineSeparator());
        net.append("*Edges");


        ArrayList<Date> datasSort = new ArrayList<Date>(datas);
       // datasSort.stream().forEach(System.out::println);
        //Sorting
        Collections.sort(datasSort, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                if (lhs.getTime() < rhs.getTime())
                    return -1;
                else if (lhs.getTime() == rhs.getTime())
                    return 0;
                else
                    return 1;
            }
        });


        //IF Longitiginal
        geraEdges(net);

        //System.out.println(net);

        //System.out.println(net.toString());
        try {
            Path p = Paths.get("C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\Network.net");
            Files.write(p, net.toString().getBytes());
            System.out.println("Rede gerada com sucesso: " + p.toString() );
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void geraEdges(StringBuilder net) {

        Map<String, Long> counted = edges.stream()
                .collect(Collectors.groupingBy(c -> String.valueOf(c.getA()) + " " + String.valueOf(c.getB())
                        , Collectors.counting()));

        counted.forEach((k,v)->{
            net.append(System.lineSeparator() + k+ " " + v);
        });



    }


    private void geraEdgesLongitidinal(StringBuilder net, ArrayList<Date> datasSort) {
        edges.stream().forEach(v -> net.append(System.lineSeparator() + v.getA() + " " + v.getB() + " 1 ["
                + (datasSort.indexOf(v.getDateTime()) + 1) + "]") );
    }

    private void geraConexao(Connection connection) {

        System.out.print(".");
        try {
            String elementA = connection.getReceiver();

            String elementB = connection.getSender();


            elementA = "\"" +elementA.trim().toLowerCase() + "\"";
            elementB = "\"" +elementB.trim().toLowerCase() + "\"";

            vertices.add(elementA);
            vertices.add(elementB);


            Date data = connection.getData();
            //edges.add((vertices.indexOf(elementA) + 1) + " " + (vertices.indexOf(elementB) + 1));
            edges.add(new Conn(vertices.indexOf(elementA) + 1, vertices.indexOf(elementB) + 1, data));
            datas.add(null);

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Registro invalido:" + connection.getSender() + " " + connection.getReceiver() + " " + connection.getData());

        }

    }

    private void geraConexaoLongituginal(Connection connection) {

        System.out.print(".");
        try {
            String elementA = connection.getReceiver();

            String elementB = connection.getSender();


            elementA = "\"" +elementA.trim().toLowerCase() + "\"";
            elementB = "\"" +elementB.trim().toLowerCase() + "\"";

            vertices.add(elementA);
            vertices.add(elementB);


            Date data = connection.getData();
            //edges.add((vertices.indexOf(elementA) + 1) + " " + (vertices.indexOf(elementB) + 1));
            edges.add(new Conn(vertices.indexOf(elementA) + 1, vertices.indexOf(elementB) + 1, data));
            datas.add(data);

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Registro invalido:" + connection.getSender() + " " + connection.getReceiver() + " " + connection.getData());

        }

    }

    @Getter
    @Setter
    public static class Conn {

        private Date dateTime;

        private int a;
        private int b;

        public Conn(int a, int b, Date dateTime) {
            this.a = a;
            this.b = b;
            this.dateTime = dateTime;
            //System.out.println("a:" + a + " b:" + b + " d:" + dateTime);
        }
    }


}
