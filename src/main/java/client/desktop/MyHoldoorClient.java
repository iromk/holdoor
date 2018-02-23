package client.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MyHoldoorClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        String sceneFile = "myholdoor.fxml";
        // it didn't want to load fxml until I added <resources> into pom.xml

        Parent root = null;
        URL url  = null;

        try {
            url = getClass().getResource(sceneFile);
            root = FXMLLoader.load(url);
        } catch (Exception e) {
            System.out.println("Exception on FXMLLoader.load()");
            System.out.println("url: " + url);
            System.out.println(e);
            throw e;
        }

        primaryStage.setTitle("My Holdoor");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    private void connectToServer() {
        userSession = UserSession.get();
        userSession.establish();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
