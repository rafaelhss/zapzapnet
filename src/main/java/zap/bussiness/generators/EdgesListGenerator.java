package zap.bussiness.generators;

import org.apache.commons.collections.list.SetUniqueList;
import util.ConfigProvider;
import zap.bussiness.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafa on 18/06/2016.
 */
public class EdgesListGenerator {
    public StringBuilder generate(List<Connection> connections){

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
