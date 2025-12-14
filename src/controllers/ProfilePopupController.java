package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfilePopupController {
    
    @FXML private Button btnProfile ;
    @FXML private Button btnLogout ;

    private StackPane scene ;

    public void initialize(){


    }

    @FXML
    private void handleProfile() throws IOException{

        VBox profile = FXMLLoader.load(getClass().getResource("/views/profile.fxml")) ;

        scene.getChildren().clear(); 

        scene.getChildren().add(profile) ;

        

        
    }


    public void setStackPane(StackPane scene){
        this.scene = scene ;
    }




}
