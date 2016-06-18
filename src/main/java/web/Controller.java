package web;



import java.io.File;
        import java.util.concurrent.atomic.AtomicLong;

        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import mail.EmailDispatcher;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;
        import util.ConfigProvider;
        import web.attatchment.Attachment;
        import web.attatchment.AttachmentDeserializer;
        import web.attatchment.AttatchmentDownloader;
        import web.network.Network;
        import web.network.NetworkRepository;
        import zap.ZapMail;
import zap.bussiness.Unzipper;

@RestController
public class Controller {

    private static final String attatchmentsFolder = "C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\zapzapnet\\chats\\download\\";

    @Autowired
    private NetworkRepository networkRepository;

    @RequestMapping("/email")
    public void processMail(@RequestParam(value = "attachments") String attachments) {
        System.out.println("processMail");
        if (attachments != null && !attachments.isEmpty()) {

            File zippedFile = new File(AttatchmentDownloader.downloadAll(attachments, attatchmentsFolder));

            File unzippedFile = new File(new Unzipper().unZipIt(zippedFile.getAbsolutePath(), zippedFile.getAbsolutePath().replace(".zip", "\\")));


            Network network = new ZapMail().processZipFile(unzippedFile);
            networkRepository.save(network);

            EmailDispatcher.SendSimpleMessage("rafaelhss@gmail.com", "234567");

            System.out.println("salvo:" + network.getId());

        }
        System.out.println("teste");
        System.out.println(attachments);
    }

    @RequestMapping("/network/{id}")
    public ResponseEntity<Network> getNetwork(@PathVariable(value = "id") Integer id) {

        Network n2 = networkRepository.findOne(id);

        if (n2 == null){
            return new ResponseEntity<>(n2,HttpStatus.NOT_FOUND);
        }
        System.out.println(n2);
        System.out.println("oi");
        return new ResponseEntity<>(n2,HttpStatus.OK);
    }
}