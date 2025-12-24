package controllers;

import java.io.File;

import database.tables.UserTable;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import models.User;
import models.UserSession;
import utils.FloatingField;

import utils.AnimatedAlert;

public class EditProfileController {
    
    @FXML private ImageView avatar ;
    @FXML private ImageView cameraIcon ;

    @FXML private FloatingField firstName ;
    @FXML private FloatingField lastName ;
    @FXML private FloatingField email ;
    @FXML private FloatingField phone ;
    @FXML private FloatingField profession ;

    @FXML
    private StackPane avatarStackPane;

    private ProfileController profileController ;
    private StackPane rootPane;
    private Popup popUp ;

    

    @FXML private void initialize(){
        cameraIcon.setImage(new Image(getClass().getResource("/icons/camera.png").toExternalForm()));

        User user = UserSession.getInstance().getUser() ;

        firstName.getField().setText(user.getFirstName());
        lastName.getField().setText(user.getLastName());
        email.getField().setText(user.getEmail());
        phone.getField().setText(user.getPhone());
        profession.getField().setText(user.getProfession());

        if(user.getProfilePic() != null){
            avatar.setImage(new Image(user.getProfilePic()));

        }else{
            avatar.setImage(new Image(getClass().getResource("/icons/user.png").toExternalForm())) ;
        }

        
        avatar.setFitWidth(64);
        avatar.setFitHeight(64);
        avatar.setPreserveRatio(false); // on force carré

        Circle circle = new Circle(avatar.getFitWidth()/2, avatar.getFitHeight()/2, avatar.getFitWidth()/2);
        avatar.setClip(circle);


        
    }

    
    @FXML
    private void Save(){
        if(UserTable.update(UserSession.getInstance().getUser().getId(),
                        firstName.getField().getText(),
                        lastName.getField().getText(),
                        email.getField().getText(),
                        profession.getField().getText(),
                        phone.getField().getText(),
                        avatar.getImage().getUrl()
                        )
        ){
            profileController.saveProfile(firstName.getField().getText(),
                                        lastName.getField().getText(),
                                        email.getField().getText(),
                                        phone.getField().getText(),
                                        profession.getField().getText(),
                                        avatar.getImage().getUrl());

            User userSession = UserSession.getInstance().getUser();
            userSession.setFirstName(firstName.getField().getText());
            userSession.setLastName(lastName.getField().getText());
            userSession.setEmail(email.getField().getText());
            userSession.setPhone(phone.getField().getText());
            userSession.setProfession(profession.getField().getText());
            userSession.setProfilePic(avatar.getImage().getUrl());

            

            popUp.hide() ;
        }
        else{
            AnimatedAlert erreur = new AnimatedAlert ("Failed to update profile information.",AnimatedAlert.AlertType.ERROR) ;
            erreur.show(rootPane) ;  
        }
    }

    @FXML
    private void Cancel(){
        popUp.hide(); 
    }

    @FXML
    private void onAvatarClick(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Images", "*.png", "*.jpg", "*.jpeg"
            )
        );

        File file = fileChooser.showOpenDialog(avatarStackPane.getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            avatar.setImage(image);
        }

    }

    public void setProfile(ProfileController profileController){
        this.profileController = profileController ;
    }
    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }
    public void setPopUp(Popup popUp){
        this.popUp = popUp ;
    }
}
