package controllers;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.layout.StackPane;
import utils.FloatingField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.stage.Popup;
import database.tables.UserTable;

import java.io.File;

public class SignUpController {

    @FXML private StackPane imageContainer;

    @FXML private ImageView profilePic;

    @FXML private Button uploadButton;

    @FXML private FloatingField firstName;
    @FXML private FloatingField lastName;
    @FXML private FloatingField email;
    @FXML private FloatingField password;
    @FXML private FloatingField confirm;
    @FXML private Button okButton;


    String urlProfilePic ;

    @FXML
    public void initialize() {
        // Upload photo
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            if (selectedFile != null) {
                urlProfilePic = selectedFile.toURI().toString() ;
                Image image = new Image(urlProfilePic);
                profilePic.setImage(image);
            }

        });

        okButton.setOnAction(e -> {
         
            toggleInvalid(firstName.getField(),!verify(firstName.getText()),"First name must contain at least 2 letters and only alphabetic characters.");

            toggleInvalid(lastName.getField(),!verify(lastName.getText()),"Last name must contain at least 2 letters and only alphabetic characters.");


            toggleInvalid(email.getField(),!verifyEmail(),"Please enter a valid email address (e.g., name@example.com).");


            toggleInvalid(password.getField(),!verifyPassword(),"Password must be at least 8 characters long and include uppercase, lowercase letters, and a number.");

            toggleInvalid(confirm.getField(),!password.getField().getText().equals(confirm.getField().getText()),"Password and confirmation must match.");

            
            enableRealtimeValidation();

            if(verifyAll()){
                //okButton.setDisable(true);
                //okButton.getStyleClass().add("okButtonWait") ;
                okButton.setText("");
                ProgressIndicator spinner = new ProgressIndicator();
                spinner.setMaxSize(20, 20);
                okButton.setGraphic(spinner);

                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() {

                        return UserTable.create(
                                firstName.getText(),
                                lastName.getText(),
                                email.getText(),
                                urlProfilePic,
                                password.getText()
                        );
                    }
                };
            

                task.setOnSucceeded(ev -> {
                    okButton.setDisable(false);
                    okButton.setGraphic(null);
                    okButton.setText("Sign Up");

                    if (task.getValue()) {
                        System.out.println("User created successfully !");
                    } else {
                        System.out.println("Failed to create user.");
                    }
                });

                task.setOnFailed(ev -> {
                    okButton.setDisable(false);
                    okButton.setGraphic(null);
                    okButton.setText("Sign Up");

                    task.getException().printStackTrace();
                });

                new Thread(task).start();
            }

        });

    
    }

    private boolean verify(String name){
        return name.matches("^[a-zA-Z]{2,}$") ;
    }

    private boolean verifyEmail(){
        return email.getText().matches("^[\\w.-_]+@[a-zA-Z0-9.-_]+\\.[a-zA-Z]{2,6}$") ;
    }

    // private boolean verifyPhone(){
    //     return phone.getText().matches("^0[67]\\d{8}$") ;
    // }

    private boolean  verifyPassword(){
        return password.getText().matches( "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$") ;
       
    }

    private boolean verifyAll(){

        return  verify(firstName.getField().getText()) &&
                verify(lastName.getField().getText()) &&
                verifyEmail() &&
                verifyPassword() ;
    }


    private void toggleInvalid(TextField field, boolean invalid,String msgTooltip) {
        if (invalid) {
            if (!field.getStyleClass().contains("field_invalid")) {
                field.getStyleClass().add("field_invalid");
            }

            showPopupError(field, msgTooltip);

        } else {
            field.getStyleClass().remove("field_invalid");
        }
    }

    private void toggleInvalid(TextField field,boolean invalid) {
        if (invalid) {
            if (!field.getStyleClass().contains("field_invalid")) {
                field.getStyleClass().add("field_invalid");
            }
        } else {
            field.getStyleClass().remove("field_invalid");
            removeErrorTooltip(field);
        }
    }


    private void showToolTip(TextField field,String msg){
        Tooltip toolTip = new Tooltip(msg) ;
        toolTip.getStyleClass().add("error-tooltip");
        toolTip.setShowDelay(Duration.ZERO);
        toolTip.setHideDelay(Duration.ZERO);
        toolTip.setShowDuration(Duration.seconds(5));
        field.setTooltip(toolTip);

    }

    private void showPopupError(TextField field, String message) {
        Label msg = new Label(message);
        msg.setId("msgPopUp") ;

        Popup popup = new Popup();
        popup.getContent().add(msg);

        Point2D p = field.localToScreen(field.getBoundsInLocal().getWidth()+5,0);
        popup.show(field, p.getX(), p.getY());

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> popup.hide());
        pause.play();
    }


    private void removeErrorTooltip(TextField field) {
        Tooltip.uninstall(field, field.getTooltip());
    }


    private void enableRealtimeValidation() {

        firstName.getField().textProperty().addListener((obs, oldValue, newValue) -> {
            toggleInvalid(firstName.getField(), !verify(newValue));
        });

        lastName.getField().textProperty().addListener((obs, oldValue, newValue) -> {
            toggleInvalid(lastName.getField(), !verify(newValue));
        });

        email.getField().textProperty().addListener((obs, oldValue, newValue) -> {
            toggleInvalid(email.getField(), !verifyEmail());
        });

       
        

        password.getField().textProperty().addListener((obs, oldValue, newValue) -> {
            toggleInvalid(password.getField(), !verifyPassword());
        });

        confirm.getField().textProperty().addListener((obs, oldValue, newValue) -> {
            toggleInvalid(confirm.getField(),!confirm.getField().getText().equals(password.getField().getText()));
        });

    }


}
