package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class FormController extends Controller {

    @FXML
    Button buttonForSignIn;

    @FXML
    Button buttonForSignUp;

    private Controller signInController, signUpController;

    FormController (Controller signInController, Controller signUpController) {
        this.signInController = signInController;
        this.signUpController = signUpController;
    }

    public void showWindow() {
        window.show();
    }

    @FXML
    void onClickSignIn(ActionEvent event) {
        //TODO
    }

    @FXML
    void onClickSignUp(ActionEvent event) throws Exception {
        SignUpController signUpController = new SignUpController();
        signUpController.showWindow();
        //TODO
    }
}
