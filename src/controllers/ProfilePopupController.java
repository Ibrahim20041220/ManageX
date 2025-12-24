package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.UserSession;
import utils.RememberMeUtil;

public class ProfilePopupController {
    
    // @FXML private Button btnProfile ;
    @FXML private Button btnLogout ;

    // @FXML private ImageView profileIcon ;
    @FXML private ImageView logoutIcon ;

    private StackPane scene ;

    public void initialize(){
        // profileIcon.setImage(new Image(getClass().getResource("/icons/profil.png").toExternalForm())) ;
        logoutIcon.setImage(new Image(getClass().getResource("/icons/logout.png").toExternalForm())) ;

        btnLogout.setOnAction(e->{
            UserSession.clearSession(); 
            RememberMeUtil.clearToken();

            Platform.exit();
            System.exit(0);

        });

        


 


    }

    // @FXML
    // private void handleProfile() throws IOException{

    //     VBox profile = FXMLLoader.load(getClass().getResource("/views/profile.fxml")) ;

    //     scene.getChildren().clear(); 

    //     scene.getChildren().add(profile) ;

        

        
    // }


    // public void setStackPane(StackPane scene){
    //     this.scene = scene ;
    // }




}
