package protocol;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Message {
    private String message;
    private Date date;
    private String userName;

    public Message(String message, Date date, String userName) {
        this.message = message;
        this.setDate(date);
        this.userName = userName;
    }

    public Message() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        /*DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(date);*/
        return date;
    }

    public void setDate(Date date) {
        //new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
        this.date = date;
    }
}
