package web.attatchment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.ConfigProvider;

import java.io.FileOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by rafa on 17/06/2016.
 */
public class AttatchmentDownloader {

    public static String downloadAll(String attachments, String destinationFolder){
        String lastNameDownloaded = "Error";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Attachment.class, new AttachmentDeserializer())
                .create();

        System.out.println("Attatchment");
        for (Attachment attachment : gson.fromJson(attachments, Attachment[].class)) {
            System.out.println(attachment.getUrl());
            System.out.println(attachment.getName());
            String httpPrefix = "https://";
            String key = "api:"+ ConfigProvider.getKey();
            String url = attachment.getUrl().replace(httpPrefix, httpPrefix + key + "@");
            String finalFilePath = destinationFolder + attachment.getName().trim();
            AttatchmentDownloader.Download(url, finalFilePath);
            lastNameDownloaded = finalFilePath;
            System.out.println("finalFilePath: " + finalFilePath);
        }
        return lastNameDownloaded;
    }

    private static boolean Download(String url, String fileDestination)
    {
        URL website;
        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("tentando...");
                website = new URL(url);
                Authenticator.setDefault(new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("api", "key-474ddb5b00478f3adec0e422ebf5050a".toCharArray());
                        }
                });
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileDestination);
                Long bytes = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Tentativa: " + i + "Erro au buscar url:" + url );
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true; // desisti
    }
}
