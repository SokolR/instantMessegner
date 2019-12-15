package client.view;

import java.util.List;
import java.util.Map;

public interface UserView {
    void createPrivateChat(List<String> privateUser,int keyDialog,String title);

    void setPrivateMessage(String msg,int keyDialog);

    void setAllMessage(String msg);

    void setActiveUsers(List<String> activeUsers);

    void editLogin(String login);

    Map<Integer,PrivateChat> getMap();

}
