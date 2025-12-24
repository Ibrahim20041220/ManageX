package controllers;
import utils.UserDAO;
import database.tables.UserTable;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import utils.FloatingField;
import utils.RememberMeUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import models.UserSession;
import javafx.util.Duration;

import java.io.IOException;



public class signInController {

    @FXML private ImageView bgImage;
    @FXML private FloatingField Username;
    @FXML private FloatingField Password;
    @FXML private Label lblMessage;

    @FXML private CheckBox rememberMe;

    @FXML
    private ImageView loadingIcon;

    private RotateTransition loadingAnimation;



    @FXML
    public void initialize() {
        loadingAnimation = new RotateTransition(Duration.seconds(1), loadingIcon);
        loadingAnimation.setByAngle(360);
        loadingAnimation.setCycleCount(Animation.INDEFINITE);
        loadingAnimation.setInterpolator(Interpolator.LINEAR);
            
        

    }

    @FXML
    private void handleLogin() {

        startLoading();

        String username = Username.getText().trim();
        String password = Password.getText();

        boolean isValid = UserDAO.checkLogin(username, password);
        
        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Remplir les champs !");
        }else if (isValid) {
        lblMessage.setText("Bienvenue, " + username + " !");

        User user = UserTable.login(username, password) ;

        if(user != null){


            UserSession.startSession(user);

            boolean remember = rememberMe.isSelected();


            if (remember) {
                String rememberToken = UserTable.haveRememberToken();

                System.out.println("tooken"+rememberToken);

                if (rememberToken != null) {
                    
                    RememberMeUtil.saveToken(rememberToken);
                }
            }


            try {
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) Username.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
    } else {
            lblMessage.setText("username oU password est incorrect !!");
        }
    }

    public void fillInputs(){
        Username.getField().setText(UserSession.getInstance().getUser().getEmail());

        Password.getField().setText("**********");
    }

    public void startLoading() {
        loadingIcon.setVisible(true);
        loadingIcon.setManaged(true);
        loadingAnimation.play();
    }

    private void stopLoading() {
        loadingAnimation.stop();
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);
    }

}
