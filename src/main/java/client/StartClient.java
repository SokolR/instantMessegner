package client;

import client.controller.ClientController;
import client.view.ChatViewSwing;
import org.xml.sax.SAXException;

import java.io.IOException;

public class StartClient {
    public static void main(String[] args) throws IOException, SAXException {
        ClientController client = new ClientController();
        client.createView(ChatViewSwing.getFactory());
        client.run();
    }
}
