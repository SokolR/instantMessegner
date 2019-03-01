package server;

import server.model.Server;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server s = new Server(4444);
        s.start();
    }
}
