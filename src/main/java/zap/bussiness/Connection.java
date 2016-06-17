package zap.bussiness;

import java.util.Date;

/**
 * Created by rafa on 20/05/2016.
 */
public class Connection {
    private String sender;
    private String receiver;
    private Date data;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
