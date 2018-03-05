package client.desktop;

import client.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientLoginController implements Initializable {


    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Label labelNotice;

    @FXML
    private void onLoginButtonAction(ActionEvent event) {

        try {

            UserSession.get().authenticate(inputUsername.getText(), inputPassword.getText());

        } catch (IOException e) {

            updateNotification(e.getMessage());
        }
        showMainWindow(getStageOfEvent(event));
    }

    private Stage getStageOfEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void showMainWindow(Stage stage) {
        final String sceneFile = "ClientMainWindow.fxml";
        Parent root = null;
        URL url = null;

        try {
            url = getClass().getResource(sceneFile);
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            System.out.println("Exception on FXMLLoader.load()");
            System.out.println("url: " + url);
            System.out.println(e);
            throw new RuntimeException("Fatal error while creating ClientMainWindow");
        }
        stage.setTitle("My Holdoor");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void updateNotification(String notice) {
        labelNotice.setText(notice);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
