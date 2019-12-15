package client.view;

import java.util.List;

public interface AdminView extends UserView {

    void setBanUsers(List<String> banUsers);
}
