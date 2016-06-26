package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rafa on 25/06/2016.
 */
public class HashMapUtil {
    public static String findMax(HashMap<String, Integer> map) {
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

    public static void incrementCount(HashMap<String, Integer> map, String countble) {
        int t = map.get(countble) != null ? map.get(countble) : 0;
        map.put(countble,++t);
    }

}
