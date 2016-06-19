package zap.bussiness.metrics;

import web.network.Network;
import zap.bussiness.Connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rafa on 19/06/2016.
 */
public class MetricsCalculator {
    public void calculateMetrics(Network network, List<Connection> connections){
        HashMap<String, Integer> edges = new HashMap<>();
        HashMap<String, Integer> top = new HashMap<>();
        HashMap<String, Integer> topSender = new HashMap<>();
        HashMap<String, Integer> topTarget = new HashMap<>();

        for(Connection c:connections){
            //Armazenar sempre na mesma ordem B->A e A->B mapeiam sempre para A - B
            String edge;
            if(c.getSender().toLowerCase().compareTo(c.getReceiver().toLowerCase()) > 0){
                edge = c.getSender() + " - " + c.getReceiver();
            }
            else {
                edge = c.getReceiver() + " - " + c.getSender();
            }

            incrementCount(edges, edge);
            incrementCount(topSender, c.getSender());
            incrementCount(topTarget, c.getReceiver());
        }

        network.setMainEdges(findMax(edges));
        network.setTopSender(findMax(topSender));
        network.setTopTarget(findMax(topTarget));


    }

    public String findMax(HashMap<String, Integer> map) {
        int maxCount = 0;
        String max ="";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());

            if((Integer)pair.getValue() >= maxCount) {
                maxCount = (Integer)pair.getValue();
                if ((Integer) pair.getValue() == maxCount) {
                    max += "[" + (String)pair.getKey() + "]("+pair.getValue()+")   ";
                }
                else {
                    max = (String)pair.getKey();
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return max;
    }

    public void incrementCount(HashMap<String, Integer> map, String countble) {
        int t = map.get(countble) != null ? map.get(countble) : 0;
        map.put(countble,++t);
    }


}
