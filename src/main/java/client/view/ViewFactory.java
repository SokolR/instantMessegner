package client.view;

import client.controller.ControllerActionsClient;

public interface ViewFactory {
    ChatView createView(ControllerActionsClient controller);
}
