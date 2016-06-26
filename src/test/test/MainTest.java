package test;

import web.persistence.Network;
import web.zap.NetworkFactory;
import web.zap.bussiness.Unzipper;

import java.io.File;

/**
 * Created by rafa on 19/06/2016.
 */
public class MainTest {
    public static void main(String[] args){

        File zippedFile = new File("C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\zapzapnet\\chats\\download\\Tetsbyjnxksajcnsakjcnaskjcn asc sacnsaj sacsaci.zip");

        File unzippedFile = new File(new Unzipper().unZipIt(zippedFile.getAbsolutePath(), zippedFile.getAbsolutePath().replace(".zip", "").trim()));

        Network network = new NetworkFactory().fromZipFile(unzippedFile, zippedFile.getName().replace(".zip","").trim());


        System.out.println(network.getConnectionMetrics().getMainEdges());
        System.out.println(network.getConnectionMetrics().getTopSender());
        System.out.println(network.getConnectionMetrics().getTopTarget());
        System.out.println(network.getGroupname());



    }
}
