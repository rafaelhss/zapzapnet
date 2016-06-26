package web.zap.bussiness;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafa on 25/06/2016.
 */
public class MessageFactory {
    public ArrayList<Message> extractMsgs(File arq) throws ParseException {
        List<String> lines = readStringListFromFile(arq);
        Pattern pattern = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,2},\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}[\\s]{0,1}[A-Za-z0-9]{0,2}:");

        ArrayList<Message> msgs = new ArrayList<Message>();

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()) {

                String aux = line.substring(matcher.end());
                String sender = "Error";

                String text = "Error";
                if(aux.indexOf(":") > 0) {
                    sender = aux.substring(0,aux.indexOf(":"));
                    text = aux.substring(aux.indexOf(":")+1);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy, HH:mm:ss");
                    String dataLimpa = matcher.group().substring(0, StringUtils.ordinalIndexOf(matcher.group(), ":",3));
                    System.out.println("dataLimpa:" + dataLimpa);
                    Date date = sdf.parse(dataLimpa);

                    Message msg = new Message();
                    msg.setDate(date);
                    msg.setSender(sender.trim().replaceAll("[^A-Za-z0-9 ]", ""));
                    msg.setText(text.trim());

                    msgs.add(msg);

                }
                else {
                    System.out.println("erro. mensagem invalida: "+ line );
                }
            }
        }


        //System.out.println(msgs.size());


        return msgs;

    }
    private List<String> readStringListFromFile(File file) {
        BufferedReader reader = null;
        List<String> lines = new ArrayList<>();
        try {//FileSystems.getDefault().getPath(".", filename ),
            reader  = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                lines.add(line);
            }

            if(reader != null)
                reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
}
