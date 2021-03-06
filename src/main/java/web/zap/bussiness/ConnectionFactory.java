package web.zap.bussiness;

import java.io.File;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafa on 15/06/2016.
 */
public class ConnectionFactory {

    public List<Connection> getConnections(Path UNZIPPEDFILES) throws ParseException {
        int fileCount = UNZIPPEDFILES.toFile().listFiles().length;

        int fileIndex = 0;
        List<Connection> connections = new ArrayList<>();
        for (File arq : UNZIPPEDFILES.toFile().listFiles()) {
            List<Message> msgs = new MessageFactory().extractMsgs(arq);
            connections.addAll(getConnections(msgs));
        }
        return connections;
    }

    public List<Connection> getConnections(List<Message> msgs) throws ParseException {
        List<Connection> connections = new ArrayList<>() ;

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