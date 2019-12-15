package server.model;

import org.apache.log4j.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

final class UserIO {
    private static final Logger LOG = Logger.getLogger(UserIO.class);

    protected static class Singleton {
        public static final UserIO _INSTANCE = new UserIO();
    }

    protected static UserIO getInstance() {
        return Singleton._INSTANCE;
    }

    private UserIO() {}

    protected synchronized void writeList(HashMap<Long, User> listUser) {
        ListUser list = new ListUser();
        list.setList(listUser);

        try {
            JAXBContext jaxbContext   = JAXBContext.newInstance(ListUser.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, new File("listUser.txt"));

            LOG.info("write in file success");
        } catch (JAXBException e) {
            LOG.error("JAXB marshal writes " + e);
        }
    }

    protected ListUser readList() throws FileNotFoundException {
        ListUser list = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ListUser.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            list = (ListUser) jaxbUnmarshaller.unmarshal(new FileInputStream("listUser.txt"));

            LOG.info("read from file success");
        } catch (JAXBException e) {
            LOG.error("JAXB un-marshal reads " + e);
        }
        return list;
    }
}
