package web.attatchment;

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
    public static boolean Download(String url, String fileDestination)
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
