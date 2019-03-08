package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller signInController = getController("registration.fxml", "Authorization", new SignInController()); // fixme: anoter fxml form for log in
        Controller signUpController = getController("registration.fxml", "Registration", new SignUpController());
        Controller formController = new FormController(signInController, signUpController);

        FXMLLoader loader = new FXMLLoader();
        loader.setController(formController);
        loader.setLocation(getClass().getResource("startform.fxml"));
        Parent content = loader.load();

        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(content));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private <C extends Controller> C getController(String filename, String title, C controller) throws IOException {
        Stage window = new Stage();
        controller.setWindow(window);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
        loader.setController(controller);
        Parent parent = loader.load();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setScene(new Scene(parent));
        return controller;
    }
}
