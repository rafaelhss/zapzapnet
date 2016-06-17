package web.attatchment;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by rafa on 17/06/2016.
 */
public class AttachmentDeserializer implements JsonDeserializer<Attachment> {
    @Override
    public Attachment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Attachment attachment = new Attachment();
        attachment.setUrl(jsonElement.getAsJsonObject().get("url").getAsString());
        attachment.setName(jsonElement.getAsJsonObject().get("name").getAsString());

        return attachment;
    }
}
