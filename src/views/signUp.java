package views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import utils.FloatingField;
import javafx.stage.FileChooser;
import java.io.File;

public class signUp extends VBox {

    public signUp() {

        Label Header = new Label("Sign Up") ;
        Header.setId("Header") ;

        
        Button uploadButton = new Button("Drag here to attach or upload your profile picture") ;
        uploadButton.setWrapText(true);
        uploadButton.setId("uploadButton");



        ImageView profilePic = new ImageView(new Image(getClass().getResourceAsStream("../icons/user.png"))) ;
        profilePic.setFitWidth(40) ;
        profilePic.setFitHeight(40);
        StackPane imageContainer = new StackPane(profilePic);
        imageContainer.setStyle(
            "-fx-border-color: #765e54; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 50%; " +
            "-fx-background-radius: 50%; " +
            "-fx-padding: 2;"   
        );


        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

             if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());

                profilePic.setImage(image);
            }

        });


       



        HBox profileHBox = new HBox(imageContainer,uploadButton) ;
        profileHBox.setStyle("-fx-padding : 10px  ;-fx-border-style : dashed ; -fx-border-width : 2 ; -fx-border-radius : 5px ;");
        profileHBox.setSpacing(20);



        // Création des champs avec label flottant
        FloatingField firstName = new FloatingField("First Name");
        FloatingField lastName = new FloatingField("Last Name");
        FloatingField email = new FloatingField("Email");
        FloatingField phone = new FloatingField("Phone Number");
        FloatingField profession = new FloatingField("Profession");
        FloatingField password = new FloatingField("Password");
        FloatingField confirm = new FloatingField("Confirm Password");

        Button ok = new Button("OK");

        this.getChildren().addAll(Header,profileHBox,firstName, lastName, email, phone, profession, password, confirm, ok);
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(300);
        this.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        this.setStyle("-fx-background-color : white ;");
    }
}
