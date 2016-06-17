package web;



        import java.util.concurrent.atomic.AtomicLong;

        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;
        import web.attatchment.Attachment;
        import web.attatchment.AttachmentDeserializer;
        import web.attatchment.AttatchmentDownloader;

@RestController
public class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/teste")
    public void processMail(@RequestParam(value="attachments") String attachments) {
        if(attachments != null && !attachments.isEmpty()) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Attachment.class, new AttachmentDeserializer())
                    .create();

            System.out.println("Attatchment");
            for(Attachment attachment : gson.fromJson(attachments, Attachment[].class)) {
                System.out.println(attachment.getUrl());
                System.out.println(attachment.getName());
                String httpPrefix = "https://";
                String key = "api:key-474ddb5b00478f3adec0e422ebf5050a";
                String url = attachment.getUrl().replace(httpPrefix, httpPrefix+key+"@");
                AttatchmentDownloader.Download(url,"C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\zapzapnet\\chats\\download\\"+attachment.getName());
            }




        }
        System.out.println("teste");
       System.out.println(attachments);
    }
}