package test;

import mail.EmailDispatcher;
import web.network.Network;
import zap.ZapMail;
import zap.bussiness.Unzipper;

import java.io.File;

/**
 * Created by rafa on 19/06/2016.
 */
public class MainTest {
    public static void main(String[] args){

        File zippedFile = new File("C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\zapzapnet\\chats\\download\\WhatsApp.zip");

        File unzippedFile = new File(new Unzipper().unZipIt(zippedFile.getAbsolutePath(), zippedFile.getAbsolutePath().replace(".zip", "").trim()));

        Network network = new ZapMail().processZipFile(unzippedFile);
        network.setGroupname(zippedFile.getName().replace(".zip","").trim());

        System.out.println(network.getMainEdges());
        System.out.println(network.getTopSender());
        System.out.println(network.getTopTarget());



    }
}
