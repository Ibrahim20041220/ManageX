package controllers;

import java.io.IOException;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import models.User;
import models.UserSession;

public class ProfileController{

    @FXML private ImageView avatarImage;
    
    @FXML private ImageView mapImage;
    @FXML private ImageView emailImage;
    @FXML private ImageView phoneImage;
    @FXML private ImageView cameraImage ;
    @FXML private ImageView editImage ;


    @FXML private Button OverviewButton ;
    @FXML private Button SkillsButton ;
    @FXML private Button ProjetsButton ;
    @FXML private Button SettingsButton ;

    @FXML private Button editButton ;

    @FXML private StackPane scene ;

    @FXML private HBox viewMode;
    @FXML private HBox editMode;
    @FXML private TextField editName;


    @FXML private Label name;
    @FXML private Label profession ;
    @FXML private Label localisation ;
    @FXML private Label email ;
    @FXML private Label phone ;

    @FXML
    public void initialize()  throws IOException{

        avatarImage.setImage(new Image(getClass().getResource("/icons/user.png").toExternalForm())) ;
        cameraImage.setImage(new Image(getClass().getResource("/icons/camera.png").toExternalForm())) ;
        mapImage.setImage(new Image(getClass().getResource("/icons/localisateur.png").toExternalForm())) ;
        emailImage.setImage(new Image(getClass().getResource("/icons/email.png").toExternalForm())) ;
        phoneImage.setImage(new Image(getClass().getResource("/icons/telephone.png").toExternalForm())) ;
        editImage.setImage(new Image(getClass().getResource("/icons/edit.png").toExternalForm())) ;

        //Initialize scene to overview
        loadOverview(); 

        OverviewButton.setOnAction(e->{
            loadOverview();
        });

        SkillsButton.setOnAction(e->{
            loadSkills();
        });

        ProjetsButton.setOnAction(e->{
            loadProjets();
        });

        SettingsButton.setOnAction(e->{
            loadSettings();
        });

        editButton.setOnAction(e->{
            try{
                Popup editProfile = new Popup() ;
                editProfile.getContent().add(FXMLLoader.load(getClass().getResource("/views/components/EditProfile.fxml"))) ;

                editProfile.show(((Node) e.getSource()).getScene().getWindow()) ;

                editProfile.setAutoHide(true);
                editProfile.setHideOnEscape(true);


            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
       
        User user = UserSession.getInstance().getUser() ;
        name.setText(user.getFirstName() + " " +user.getLastName());
        profession.setText(user.getProfession());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());



    }


    private void loadOverview(){
        try {

            VBox overview = FXMLLoader.load(getClass().getResource("/views/components/Overview.fxml")) ;

            scene.getChildren().clear(); 

            scene.getChildren().add(overview) ;
            OverviewButton.setStyle(" -fx-border-color: transparent transparent black transparent ; -fx-border-width: 0 0 2 0; ");

            Stream.of(SkillsButton, ProjetsButton, SettingsButton).forEach(button -> button.setStyle("-fx-border-width: 0;"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSkills(){
        try {

            VBox Skills = FXMLLoader.load(getClass().getResource("/views/components/Skills.fxml")) ;

            scene.getChildren().clear(); 

            scene.getChildren().add(Skills) ;
            SkillsButton.setStyle(" -fx-border-color: transparent transparent black transparent ; -fx-border-width: 0 0 2 0; ");

            Stream.of(OverviewButton, ProjetsButton, SettingsButton).forEach(button -> button.setStyle("-fx-border-width: 0;"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadProjets(){

        try {

            ScrollPane Projets = FXMLLoader.load(getClass().getResource("/views/components/Projets.fxml")) ;

            scene.getChildren().clear(); 

            scene.getChildren().add(Projets) ;
            ProjetsButton.setStyle(" -fx-border-color: transparent transparent black transparent ; -fx-border-width: 0 0 2 0; ");

            Stream.of(OverviewButton,SkillsButton, SettingsButton).forEach(button -> button.setStyle("-fx-border-width: 0;"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadSettings(){
        try {

            VBox Settings = FXMLLoader.load(getClass().getResource("/views/components/Settings.fxml")) ;

            scene.getChildren().clear(); 

            scene.getChildren().add(Settings) ;
            SettingsButton.setStyle(" -fx-border-color: transparent transparent black transparent ; -fx-border-width: 0 0 2 0; ");

            Stream.of(OverviewButton, ProjetsButton, SkillsButton).forEach(button -> button.setStyle("-fx-border-width: 0;"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void switchToEditMode() {
        toggleMode(true);
        editName.requestFocus(); 
    }

    @FXML
    private void switchToViewMode() {
        toggleMode(false);
    }


    @FXML
    private void saveProfile() {
        // Logique de mise à jour des labels
        name.setText(editName.getText());
        
        // Puis on revient à la vue normale
        toggleMode(false);
    }

    private void toggleMode(boolean isEditing) {
        // La vue normale
        viewMode.setVisible(!isEditing);
        viewMode.setManaged(!isEditing);

        // La vue édition
        editMode.setVisible(isEditing);
        editMode.setManaged(isEditing);
    }

}
