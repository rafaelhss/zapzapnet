package zap;

import web.network.Network;
import zap.bussiness.*;
import zap.bussiness.generators.EdgesListGenerator;
import zap.bussiness.generators.SigmaJsonGenerator;

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

            //EmailDispatcher.SendSimpleMessage("rafaelhss@gmail.com", "234567");

            Network result = new Network();
            result.setEdges(new EdgesListGenerator().generate(connections).toString());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
