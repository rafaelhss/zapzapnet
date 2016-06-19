package zap;

import web.network.Network;
import zap.bussiness.*;
import zap.bussiness.generators.EdgesListGenerator;
import zap.bussiness.generators.SigmaJsonGenerator;
import zap.bussiness.metrics.MetricsCalculator;

import java.io.File;
import java.util.List;

/**
 * Created by rafa on 15/06/2016.
 */
public class ZapMail {

    public Network processZipFile(File file){
        try{

            List<Connection> connections = new ConnectionFactory().getConnections(file);

            connections = new Obfuscator().obfuscateLabels(connections, Obfuscator.ObfuscationType.LEAVE_NAME_OR_4_DIGITS);

            Network result = new Network();
            result.setEdges(new EdgesListGenerator().generate(connections).toString());

            new MetricsCalculator().calculateMetrics(result, connections);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
