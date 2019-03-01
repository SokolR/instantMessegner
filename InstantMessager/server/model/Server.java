package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private ConnectionListener connectionListener;

    private List<Client> clientList = new ArrayList<>();

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionListener = new ConnectionListener(this);
    }

    public void start() throws IOException {
        connectionListener.start();

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (((input = stdIn.readLine()) != null) && connectionListener.isAlive()) {
            if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Admin: " + input);
                for (Client c : clientList) {
                    c.send("Admin: " + input);
                }
            }
        }
        stop();
    }

    public void stop() {
        connectionListener.stop();
        for (Client c : clientList) {
            c.closeSession();
        }
        System.out.println("Server down!");
    }

    public synchronized void addConnection(Connection connection) {
        Client client = new Client(connection, clientList);
        clientList.add(client);
        client.startSession();
        System.out.println("Client connected");
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
