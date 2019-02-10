package protocol;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.Date;

public class MainClassExample {
    public static void main(String[] args) {
        try {
            //create xml from object (StringWriter)
            Message sendMessage = new Message("Hello", new Date(), "user");
            StringWriter strb = sendMessage.convertToXML();

            //create object from xml (StringWriter)
            Message message = (Message) new Message().convertToObject(strb);
            System.out.println(message.getMessage());

            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
