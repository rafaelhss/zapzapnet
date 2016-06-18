package zap.bussiness;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafa on 15/06/2016.
 */
public class ConnectionFactory {

    public List<Connection> getConnections(Path UNZIPPEDFILES) throws ParseException {
        int fileCount = UNZIPPEDFILES.toFile().listFiles().length;

        int fileIndex = 0;
        List<Connection> connections = new ArrayList<>();
        for (File arq : UNZIPPEDFILES.toFile().listFiles()) {
            connections.addAll(getConnections(arq));
        }
        return connections;
    }

    public List<Connection> getConnections(File arq) throws ParseException {
        List<Connection> connections = new ArrayList<>() ;
        List<Message> msgs = extractMsgs(arq);
        if(msgs.size() <= 0){
            throw new ParseException("Não foram encontradas mensagens", 0);
        }
        double avgTimeBetweenMsgs = getAvgTimeBetweenMsgs(msgs);
        System.out.println("avgTimeBetweenMsgs:" + avgTimeBetweenMsgs);

        System.out.println("size:" + msgs.size());
        for (int i=0; i < msgs.size()-1; i++){

            Message current = msgs.get(i);
            Message next = msgs.get(i + 1);

            long diff = (next.getDate().getTime()/1000) -(current.getDate().getTime()/1000);


            if((!next.getSender().equals(current.getSender())) && ((diff) < (avgTimeBetweenMsgs))) {
                connections.add(connectionFactory(current, next));
            }
        }
        return connections;
    }

    private Connection connectionFactory(Message current, Message next) {
        Connection c = new Connection();
        c.setSender(next.getSender());
        c.setReceiver(current.getSender());
        c.setData(next.getDate());
        logConnection(c);
        return c;
    }

    private void logConnection(Connection c) {
        //System.out.println("sender:" + c.getSender() + " receiver:" + c.getReceiver() + " date:" + c.getData());
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
    private ArrayList<Message> extractMsgs(File arq) throws ParseException {
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
                    String dataLimpa = matcher.group().substring(0,StringUtils.ordinalIndexOf(matcher.group(), ":",3));
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

    private double getAvgTimeBetweenMsgs(List<Message> msgs) {

        double MAX_DIFF_IGNORE =  60*60*2;
        double lastTimestamp = msgs.get(0).getDate().getTime();

        List<Double> diffs = new ArrayList<Double>();
        for (Message message : msgs) {
            double now = message.getDate().getTime();
            double diff = (now - lastTimestamp)/1000;
            //	System.out.println(now + " " + lastTimestamp + " " + diff/1000);
            //System.out.println(new Date((long) now) + " " + new Date((long) lastTimestamp) + " " + diff/1000 );
            if(diff < MAX_DIFF_IGNORE) {
                diffs.add(diff);
            }
            lastTimestamp = now;
        }

        double[] m = new double[diffs.size()];
        for (int i = 0; i < m.length; i++) {
            m[i] = diffs.get(i).doubleValue();  // java 1.4 style
            // or:
            m[i] = diffs.get(i);                // java 1.5+ style (outboxing)
        }
        int middle = m.length/2;
        if (m.length%2 == 1) {
            return m[middle];
        } else {
            return (m[middle-1] + m[middle]) / 2.0;
        }

    }
}