package zap;

import zap.bussiness.Connection;
import zap.bussiness.ConnectionFactory;
import zap.bussiness.PajekNetGenerator;
import zap.bussiness.Unzipper;

import java.util.List;

/**
 * Created by rafa on 15/06/2016.
 */
public class ZapMail {

    public void processFolder(String folder){
        try{
            String UnzippedFilesPath = new Unzipper().unzipFilesInFolder(folder);
            List<Connection> connections = new ConnectionFactory().getConnections(UnzippedFilesPath);
            StringBuilder net = new PajekNetGenerator().generate(connections);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
