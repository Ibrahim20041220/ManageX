package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML ;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.Parent;


public class SignUpLoginController {

    @FXML private Button loginButton ;
    @FXML private Label welcomeLabel ;
    @FXML private Label enterDetailsLabel ;
    @FXML private VBox login ;
    @FXML private GridPane signUpLoginBox ;
    @FXML private StackPane rootStackPane ;
    @FXML private VBox signUp ;


    @FXML
    public void initialize() {
        login.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = newVal.doubleValue() / 3;
            login.setStyle("-fx-background-radius: 0 " + radius + " " + radius + " 0;");
        });

        double radiusInit = login.getHeight() / 3;
        login.setStyle("-fx-background-radius: 0 " + radiusInit + " " + radiusInit + " 0;");


        loginButton.setOnAction(e -> {
            
            double width = signUp.getWidth() ;
           
            TranslateTransition transition = new TranslateTransition(Duration.seconds(5),signUp) ;
            transition.setByX(width);
            transition.setByY(0);
            transition.play(); 

            TranslateTransition transition1 = new TranslateTransition(Duration.seconds(5),login) ;
            transition1.setByX(width);
            transition1.setByY(0);
            transition1.play();

            Timeline timeline = new Timeline();
            for (int radius = 0; radius <= 400; radius++) {
                int r = radius;
                timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(radius * 10), a -> {
                        login.setStyle("-fx-background-radius: " + r + " 0 0 "+ r + ";");
                    })
                );
            }
            timeline.play();




        });
    }




}
