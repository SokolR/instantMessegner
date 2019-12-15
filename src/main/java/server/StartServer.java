package server;

import org.xml.sax.SAXException;
import server.controller.ControllerServer;
import server.model.Model;

import java.io.IOException;
import java.text.ParseException;

public class StartServer {
    public static void main(String[] args)throws IOException, ParseException, SAXException {
        ControllerServer controllerServer = new ControllerServer(new Model());
        controllerServer.run();
    }

}
