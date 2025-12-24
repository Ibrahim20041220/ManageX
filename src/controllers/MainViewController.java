package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import models.User;
import models.UserSession;
import javafx.scene.image.Image;
import javafx.scene.Parent;

public class MainViewController {
    @FXML
    private Button btnHome;

    @FXML
    private Button btnProjets;

    @FXML
    private Button btnTaches;

    @FXML 
    private Button btnProfile ;


    @FXML 
    private StackPane scene ;

    @FXML
    private Button profileButton ;

    @FXML 
    private ImageView profileImage ;


    private Popup profilePopup;

    @FXML private Label lblUsername ;


    public void initialize(){

        User userSession = UserSession.getInstance().getUser() ;

        lblUsername.setText(userSession.getFirstName()+" "+userSession.getLastName());

        if(userSession.getProfilePic()!=null){
            profileImage.setImage(new Image(userSession.getProfilePic()));
        }else{
            profileImage.setImage(new Image(getClass().getResource("/icons/user.png").toExternalForm()));
        }
        profileImage.setFitWidth(44);
        profileImage.setFitHeight(44);
        profileImage.setPreserveRatio(false);

        Circle circle = new Circle(profileImage.getFitWidth()/2, profileImage.getFitHeight()/2, profileImage.getFitWidth()/2);
        profileImage.setClip(circle);


        handleHomeClick() ;





    }
   

    @FXML
    private void handleHomeClick() {
       navigateToPage("../views/home.fxml");
       setActiveButton(btnHome);
    }


    @FXML
    private void handleProjetsClick() {
       navigateToPage("../views/projets.fxml");
       setActiveButton(btnProjets);
    }


    @FXML
    private void handleTachesClick() {
       navigateToPage("../views/taches.fxml") ;
       setActiveButton(btnTaches);
    }


    @FXML
    private void handleProfileClick() throws IOException{

        navigateToPage("../views/Profile.fxml");
        setActiveButton(btnProfile);
    }

    
    private void setActiveButton(Button activeBtn) {
        // Retirer la classe active de tous les boutons
        btnHome.getStyleClass().remove("active");
        btnProjets.getStyleClass().remove("active");
        btnTaches.getStyleClass().remove("active");

        // Ajouter la classe active au bouton sélectionné
        activeBtn.getStyleClass().add("active");
    }


    
    private void navigateToPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            scene.getChildren().clear(); 
            scene.getChildren().add(root) ;


            System.out.println("Navigation réussie vers: " + fxmlPath);

        } catch (IOException e) {
            System.err.println("Erreur lors de la navigation vers " + fxmlPath);
            e.printStackTrace();

        }
    }


    

}
