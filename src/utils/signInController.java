package utils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class signInController {

    @FXML private ImageView bgImage;
    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {

        Image img = new Image(getClass().getResourceAsStream("../icons/background.jpg"));
        bgImage.setImage(img);
    }

    @FXML
    private void handleLogin() {
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Remplir les champs !");
        } else {
            lblMessage.setText("Bienvenue, " + username + " !");
            // TODO: Switch scene to main dashboard
        }
    }
}
