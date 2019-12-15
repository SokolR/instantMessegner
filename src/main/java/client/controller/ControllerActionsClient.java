package client.controller;

import client.view.ViewFactory;
import server.model.XmlSet;

import java.util.List;


public interface ControllerActionsClient {

    boolean connectToServer();

    void setUserXml(XmlSet userXml);

    XmlSet getUserXml();

    void getMessage();

    void sendAllMessage(String msg);

    void sendPrivateMessage(List<String> users, String msg, int keyDialog);

    void closeChat();

    void registration(String login, String password);

    void authentication(String login, String password);

    void createView(ViewFactory factory);

    void editUser(String newPassword);

    void remove();

    void ban(String banUser);

    void unBan(String unBanUser);

    void remove(String removeUser);

    String getMyUser();

    void createPrChat(List<String> users,String title);
}
