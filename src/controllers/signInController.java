package controllers;
import utils.UserDAO;
import database.OracleDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.FloatingField;


public class signInController {

    @FXML private ImageView bgImage;
    @FXML private FloatingField Username;
    @FXML private FloatingField Password;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleLogin() {
        String username = Username.getText().trim();
        String password = Password.getText();

        boolean isValid = UserDAO.checkLogin(username, password);
        
        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Remplir les champs !");
        } else if (isValid) {
            lblMessage.setText("Bienvenue, " + username + " !");
        }else {
            lblMessage.setText("username oU password est incorrect !!");
        }
    }
}
