package server.model;

import java.util.List;

public interface ModelActions {

    long authorizationUser(String login, String password);

    List<String> getBanList();

    boolean setBan(String login, boolean ban);

    User getUser(long id);

    boolean addUser(User user);

    boolean removeUser(User user);

    boolean editUser(User user);

    void start();

    void stop();
}
