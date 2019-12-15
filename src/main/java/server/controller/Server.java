package server.controller;

import org.xml.sax.SAXException;
import java.io.IOException;

public interface Server {

    void displayInfoLog(String message);

    void run()throws IOException, SAXException;

    void stop()throws IOException;
}
