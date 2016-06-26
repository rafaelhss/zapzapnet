package web.zap;

import web.persistence.Network;
import web.zap.bussiness.*;
import web.zap.bussiness.generators.EdgesListGenerator;
import web.zap.bussiness.metrics.ConnectionMetrics;
import web.zap.bussiness.metrics.MessageMetrics;

import java.io.File;
import java.util.List;

/**
 * Created by rafa on 15/06/2016.
 */
public class NetworkFactory {

    public Network fromZipFile(File file, String name){
        try{

            List<Message> msgs = new MessageFactory().extractMsgs(file);

            List<Connection> connections = new ConnectionFactory().getConnections(msgs);

            connections = new Obfuscator().obfuscateLabels(connections, Obfuscator.ObfuscationType.LEAVE_NAME_OR_4_DIGITS);


            Network result = new Network();

            ConnectionMetrics connectionMetrics = new ConnectionMetrics(connections);
            result.setConnectionMetrics(connectionMetrics);

            MessageMetrics messageMetrics = new MessageMetrics(msgs);
            result.setMessageMetrics(messageMetrics);

            result.setEdges(new EdgesListGenerator().generate(connections));

            result.setGroupname(name);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
