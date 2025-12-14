package controllers;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.Parent;

public class MainViewController {
    @FXML
    private Button btnHome;

    @FXML
    private Button btnProjets;

    @FXML
    private Button btnTaches;

    @FXML 
    private StackPane scene ;

    @FXML
    private Button profileButton ;

    @FXML 
    private ImageView profileImage ;


    private Popup profilePopup;



    

    public void initialize(){
        profileImage.setImage(new Image(getClass().getResource("/icons/user.png").toExternalForm()));





    }
   

    @FXML
    private void handleHomeClick() {
        try{
            scene.getChildren().clear(); 
            scene.getChildren().add(FXMLLoader.load(getClass().getResource("../views/home.fxml"))) ;
            setActiveButton(btnHome);
            // TODO: Navigation vers la page Home (déjà sur cette page)
            System.out.println("Navigation: Home");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void handleProjetsClick() {
        setActiveButton(btnProjets);
        // TODO: Navigation vers la page Projets
        System.out.println("Navigation: Projets");
    }


    @FXML
    private void handleTachesClick() {
        setActiveButton(btnTaches);
        // TODO: Navigation vers la page Tâches
        System.out.println("Navigation: Tâches");
    }


    @FXML
    private void handleProfileClick(MouseEvent event) throws IOException{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/components/ProfilePopup.fxml"));
        VBox popupContent = loader.load();
    
        profilePopup = new Popup();
        profilePopup.getContent().add(popupContent) ;
        profilePopup.setAutoHide(true);
        profilePopup.setHideOnEscape(true);

        profilePopup.show(((Node) event.getSource()).getScene().getWindow(),
            event.getScreenX() - 150,
            event.getScreenY() + 10) ;

        ProfilePopupController popupController = loader.getController();

        popupController.setStackPane(scene)  ;

        System.out.println("Navigation: Profile");
    }

    
    private void setActiveButton(Button activeBtn) {
        // Retirer la classe active de tous les boutons
        btnHome.getStyleClass().remove("active");
        btnProjets.getStyleClass().remove("active");
        btnTaches.getStyleClass().remove("active");

        // Ajouter la classe active au bouton sélectionné
        activeBtn.getStyleClass().add("active");
    }





}
