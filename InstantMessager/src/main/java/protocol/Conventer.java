package protocol;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class Conventer {

    public static StringWriter convertToXML(Object ob) throws JAXBException {
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(ob.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(ob, writer);
        return writer;
    }

    public static Object convertToObject(Object ob, StringWriter str) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(new StringReader(str.toString()));
    }
}
