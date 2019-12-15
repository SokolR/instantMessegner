package client.view;

public interface ChatView {

    void createUserView();

    void createAdminView();

    void createEnterToChat();

    void closeEnterToChat();

    UserView getUserFrame();

    AdminView getAdminFrame();
}
