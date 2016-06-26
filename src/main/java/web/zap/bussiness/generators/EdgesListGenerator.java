package web.zap.bussiness.generators;

import lombok.Getter;
import lombok.Setter;
import util.ConfigProvider;
import web.network.Edge;
import web.zap.bussiness.Connection;
import web.zap.bussiness.metrics.WordCloudElement;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rafa on 18/06/2016.
 */
public class EdgesListGenerator {


    public List<Edge> generate(List<Connection> connections){
        return connections.stream()
                .collect(Collectors.groupingBy(con -> {
                    List<String> result = new ArrayList<String>();
                    if (con.getSender().compareTo(con.getReceiver()) > 0) {
                        result.add(con.getSender());
                        result.add(con.getReceiver());
                        return result;
                    }

                    result.add(con.getReceiver());
                    result.add(con.getSender());
                    return result;
                }, Collectors.counting()))
                .entrySet().stream().map(edge -> new Edge(null, edge.getKey().get(0),edge.getKey().get(1),edge.getValue().intValue()))
                .collect(Collectors.toList());
    }


    private StringBuilder generate_old(List<Connection> connections){

        StringBuilder resultado = new StringBuilder();
        String edgeTemplate = ConfigProvider.getEdgesSeparator() + "@@SOURCE@@" + ConfigProvider.getNodesSeparator() + "@@TARGET@@";

        for(int i=0; i < connections.size(); i++){
            Connection connection = connections.get(i);


            String edge = edgeTemplate
                    //.replace("@@ID@@", String.valueOf(i))
                    .replace("@@SOURCE@@", connection.getSender())
                    .replace("@@TARGET@@", connection.getReceiver());
                    //.replace("@@SIZE@@", String.valueOf(1));

            resultado.append(edge);
        }

        return resultado;
    }


}
