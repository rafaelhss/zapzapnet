package web.zap.bussiness.generators;

import org.apache.commons.collections.list.SetUniqueList;
import web.zap.bussiness.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 *
 // Generate a random graph:
 for (i = 0; i < N; i++)
 g.nodes.push({
 id: 'n' + i,
 label: 'Node ' + i,
 x: Math.random(),
 y: Math.random(),
 size: Math.random(),
 color: '#666'
 });
 for (i = 0; i < E; i++)
 g.edges.push({
 id: 'e' + i,
 source: 'n' + (Math.random() * N | 0),
 target: 'n' + (Math.random() * N | 0),
 size: Math.random(),
 color: '#ccc'
 });
 *
 */
public class SigmaJsonGenerator implements Generator {

    String gNodesPushTemplate = "g.nodes.push({\n" +
                                    " id: '@@ID@@',\n" +
                                    " label: '@@LABEL@@',\n" +
                                    " x: Math.random(),\n" +
                                    " y: Math.random(),\n" +
                                    " size: 10,\n" +
                                    " color: '#666'\n" +
                                " });";

    String gEdgesPushTemplate = "g.edges.push({\n" +
                                    " id: 'e@@ID@@',\n" +
                                    " source: '@@SOURCE@@',\n" +
                                    " target: '@@TARGET@@',\n" +
                                    " size: @@SIZE@@,\n" +
                                    " color: '#ccc'\n" +
                                " });";

    public StringBuilder generate(List<Connection> connections){

        StringBuilder resultado = new StringBuilder();
        List<String> nodes = SetUniqueList.decorate(new ArrayList<String>());

        for(int i=0; i < connections.size(); i++){
            Connection connection = connections.get(i);

            nodes.add(connection.getSender());
            nodes.add(connection.getReceiver());

            String gEdgesPush = gEdgesPushTemplate
                    .replace("@@ID@@", String.valueOf(i))
                    .replace("@@SOURCE@@", connection.getSender())
                    .replace("@@TARGET@@", connection.getReceiver())
                    .replace("@@SIZE@@", String.valueOf(1));

            resultado.append(gEdgesPush);
        }

        for(String node : nodes){
            String gNodesPush = gNodesPushTemplate
                    .replace("@@ID@@", node)
                    .replace("@@LABEL@@", node);

            resultado.append(gNodesPush);
        }



        return resultado;
    }
}
