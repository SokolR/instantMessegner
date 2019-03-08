package sample;

import javafx.stage.Stage;

public abstract class Controller {
    protected Stage window;

    public void setWindow(Stage window) {
        this.window = window;
    }

    public void showWindow() {
        if (window != null) {
            window.show();
        }
    }
}
