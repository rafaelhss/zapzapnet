package web.zap.bussiness;

import java.util.List;

/**
 * Created by deinf.rsoares on 17/06/2016.
 */
public class Obfuscator {

    public enum ObfuscationType {
        FULL, LEAVE_LAST_4_CHARS, LEAVE_NAME_OR_4_DIGITS;
    }

    private String getCharForNumber(int i) {
//        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;

        if( i<0 ) {
            return getCharForNumber(-i-1);
        }

        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } else {
            return getCharForNumber(quot-1) + letter;
        }
    }

    public List<Connection> obfuscateLabels(List<Connection> connections, ObfuscationType obfuscationType) {

        if(obfuscationType.equals(ObfuscationType.FULL)) {
            for (Connection connection : connections) {
                System.out.println(getCharForNumber(connection.getReceiver().hashCode()));
                connection.setReceiver(getCharForNumber(connection.getReceiver().hashCode()));
                connection.setSender(getCharForNumber(connection.getSender().hashCode()));
            }
        }
        if(obfuscationType.equals(ObfuscationType.LEAVE_LAST_4_CHARS)) {
            for (Connection connection : connections) {

                String last4 = connection.getReceiver().substring(connection.getReceiver().length()-4);
                connection.setReceiver("xxxxxxxxx" + last4);

                last4 = connection.getSender().substring(connection.getSender().length()-4);
                connection.setSender("xxxxxxxxx" +last4);
            }
        }
        if(obfuscationType.equals(ObfuscationType.LEAVE_NAME_OR_4_DIGITS)) {
            for (Connection connection : connections) {

                try {
                    String last4 = connection.getReceiver().substring(connection.getReceiver().length()-4).trim();
                    Integer.parseInt((last4));
                    connection.setReceiver("(NN) NNNN-" + last4);
                } catch (NumberFormatException e){
                    //Se nao converteu para numero eh pq eh nome. deixa como ta
                    //e.printStackTrace();
                } catch (StringIndexOutOfBoundsException e){
                    // se nao deu substring 4 eh pq eh nome(tipo "eli")
                    //e.printStackTrace();
                }

                try {
                    String last4 = connection.getSender().substring(connection.getSender().length()-4);
                    Integer.parseInt(last4);
                    connection.setSender("(NN) NNNN-" + last4);
                } catch (NumberFormatException e){
                    //Se nao converteu para numero eh pq eh nome. deixa como ta
                } catch (StringIndexOutOfBoundsException e){
                    // se nao deu substring 4 eh pq eh nome(tipo "eli")
                }
            }
        }


        return connections;
    }

    public static String displayCharValues(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append((int) c);
            sb.append("-");
        }
        return sb.toString();
    }
}
