package util;

/**
 * Created by deinf.rsoares on 17/06/2016.
 */
public class ConfigProvider {
    public static String getKey(){
        return "key-474ddb5b00478f3adec0e422ebf5050a";
    }

    public static String getDomain(){
        return "sandbox43250dec73c44618984f3f7ee96f5b84.mailgun.org";
    }

    public static String getEdgesSeparator() {return "#########";}
    public static String getNodesSeparator() {return "---------";}

    public static String getRootPath(){
        //return "https://whatsappnet.herokuapp.com";
        return "https://6270e912.ngrok.io";
    }

}
