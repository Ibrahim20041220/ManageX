package controllers;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML ;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;


public class SignUpLoginController {

    @FXML private Button loginButton ;
    @FXML private Label welcomeLabel ;
    @FXML private Label enterDetailsLabel ;
    @FXML private VBox login ;
    @FXML private GridPane signUpLoginBox ;
    @FXML private StackPane rootStackPane ;
    @FXML private VBox signUp ;
    
    private VBox signup2;   
     private double height ;


   @FXML
    public void initialize() throws IOException {

        Platform.runLater(() -> {

            height = login.getHeight();

            login.heightProperty().addListener((obs, oldVal, newVal) -> {
                double radius = newVal.doubleValue() / 3;
                login.setStyle("-fx-background-radius: 0 " + radius + " " + radius + " 0;");
            });

            login.setStyle("-fx-background-radius: 0 " + (height/3) + " " + (height/3) + " 0;");

        //    loginButton.setOnAction(e -> {

        //         double width = signUp.getWidth();  // largeur d’une moitié

        //         loginButton.setText("Sign Up");

        //         // 1 — Faire sortir signUp vers la droite
        //         TranslateTransition slideSignUp = new TranslateTransition(Duration.seconds(1), signUp);
        //         slideSignUp.setByX(width);
        //         slideSignUp.play();

        //         // 2 — Faire sortir login vers la droite
        //         TranslateTransition slideLogin = new TranslateTransition(Duration.seconds(1), login);
        //         slideLogin.setByX(width);
        //         slideLogin.play();

        //         // 3 — Animation border-radius
        //         Timeline timeline = new Timeline();
        //         double maxRadius = height / 3;

        //         for (int r = 0; r <= maxRadius; r++) {
        //             int radius = r;
        //             timeline.getKeyFrames().add(
        //                 new KeyFrame(Duration.millis(r * 4), a -> {
        //                     login.setStyle("-fx-background-radius: "
        //                         + radius + " "
        //                         + (maxRadius - radius) + " "
        //                         + (maxRadius - radius) + " "
        //                         + radius + ";");
        //                 })
        //             );
        //         }
        //         timeline.play();


        //         // 4 — Charger le nouveau panneau signup2
        //         VBox signup2;
        //         try {
        //             signup2 = FXMLLoader.load(getClass().getResource("/views/signUp.fxml"));
        //         } catch (IOException ex) {
        //             ex.printStackTrace();
        //             return;
        //         }

        //         // Le mettre dans la colonne gauche
        //         GridPane.setColumnIndex(signup2, 0);
        //         GridPane.setRowIndex(signup2, 0);
        //         signUpLoginBox.getChildren().add(signup2);

        //         // Même taille que l’ancien login
        //         signup2.setPrefWidth(login.getWidth());
        //         signup2.setPrefHeight(login.getHeight());

        //         // Le placer hors écran à gauche
        //         signup2.setTranslateX(-width);

        //         // 5 — L’animer vers la position normale = moitié gauche
        //         TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), signup2);
        //         slideIn.setToX(0);  // → moitié gauche, colonne 0
        //         slideIn.play();
        //     });


        loginButton.setOnAction(e -> {

            double width = signUp.getWidth();

            // 👉 Si on est dans l'état "Login" → aller vers SignUp
            if (loginButton.getText().equals("Login")) {

                loginButton.setText("Sign Up");

                // Animation sortante
                TranslateTransition slideSignUp = new TranslateTransition(Duration.millis(300), signUp);
                slideSignUp.setByX(width);
                slideSignUp.play();

                TranslateTransition slideLogin = new TranslateTransition(Duration.millis(300), login);
                slideLogin.setByX(width);
                slideLogin.play();

                // Border radius animation
                playRadiusAnimation(true);

                // Charger panneau signup2
        
                try {
                    signup2 = FXMLLoader.load(getClass().getResource("/views/signUp.fxml"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                // Ajouter dans la colonne gauche
                GridPane.setColumnIndex(signup2, 0);
                GridPane.setRowIndex(signup2, 0);
                signUpLoginBox.getChildren().add(signup2);

                signup2.setPrefWidth(login.getWidth());
                signup2.setPrefHeight(login.getHeight());

                // Le placer à gauche hors écran
                signup2.setTranslateX(-width);

                // Le faire entrer
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), signup2);
                slideIn.setToX(0);
                slideIn.play();
            }

            // 👉 Si on est dans l'état "Sign Up" → revenir au Login
            else {

                loginButton.setText("Login");

                // Animation retour
                TranslateTransition slideBackSignUp = new TranslateTransition(Duration.millis(300), signUp);
                slideBackSignUp.setByX(-width);
                slideBackSignUp.play();

                TranslateTransition slideBackLogin = new TranslateTransition(Duration.millis(300), login);
                slideBackLogin.setByX(-width);
                slideBackLogin.play();

                // Animation radius inverse
                playRadiusAnimation(false);

                // Retrouver le pane SignUp chargé (colonne 0)
                for (javafx.scene.Node n : signUpLoginBox.getChildren()) {
                    if (n != login && n != signUp) {
                        signup2 = (VBox) n;
                    }
                }

                if (signup2 != null) {
                    // Le pousser vers la gauche
                    TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), signup2);
                    slideOut.setToX(-width);

                    slideOut.setOnFinished(ev -> {
                        signUpLoginBox.getChildren().remove(signup2);
                    });

                    slideOut.play();
                }
            }
        });

        });



    }


    private void playRadiusAnimation(boolean forward) {
        Timeline timeline = new Timeline();
        double maxRadius = height / 3;

        for (int r = 0; r <= maxRadius; r++) {
            int radius = forward ? r : (int)(maxRadius - r);

            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(r), a -> {
                    login.setStyle("-fx-background-radius: "
                        + radius + " "
                        + (maxRadius - radius) + " "
                        + (maxRadius - radius) + " "
                        + radius + ";");
                })
            );
        }

        timeline.play();
    }





}
