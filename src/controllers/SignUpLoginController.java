package controllers;
import java.io.IOException;

import database.tables.UserTable;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML ;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.User;
import models.UserSession;
import utils.RememberMeUtil;



public class SignUpLoginController {

    @FXML private Button loginButton ;
    @FXML private Label welcomeLabel ;
    @FXML private Label enterDetailsLabel ;
    @FXML private VBox login ;
    @FXML private GridPane signUpLoginBox ;
    @FXML private StackPane rootStackPane ;
    @FXML private VBox signUp ;

    private VBox signup2;
    private signInController loginController ;
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

            // loginButton.setOnAction(e -> {

            //     double width = signUp.getWidth();

            //     // 👉 Si on est dans l'état "Login" → aller vers SignUp
            //     if (loginButton.getText().equals("Login")) {

            //         loginButton.setText("Sign Up");

            //         // Animation sortante
            //         TranslateTransition slideSignUp = new TranslateTransition(Duration.millis(300), signUp);
            //         slideSignUp.setByX(width);
            //         slideSignUp.play();

            //         TranslateTransition slideLogin = new TranslateTransition(Duration.millis(300), login);
            //         slideLogin.setByX(width);
            //         slideLogin.play();

            //         // Border radius animation
            //         playRadiusAnimation(true);

            //         // Charger panneau signup2

            //         try {
            //             signup2 = FXMLLoader.load(getClass().getResource("/views/signIn.fxml"));
            //             // loginController = signup2

            //             // FXMLLoader loader = new FXMLLoader(
            //             //         getClass().getResource("/views/signIn.fxml")
            //             // );

            //             // signup2 = loader.load();

            //             // loginController = loader.getController();

            //             // loginController.fillInputs() ; 

            //         } catch (IOException ex) {
            //             ex.printStackTrace();
            //             return;
            //         }

            //         // Ajouter dans la colonne gauche
            //         GridPane.setColumnIndex(signup2, 0);
            //         GridPane.setRowIndex(signup2, 0);
            //         signUpLoginBox.getChildren().add(signup2);

            //         signup2.setPrefWidth(login.getWidth());
            //         signup2.setPrefHeight(login.getHeight());

            //         // Le placer à gauche hors écran
            //         signup2.setTranslateX(-width);

            //         // Le faire entrer
            //         TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), signup2);
            //         slideIn.setToX(0);
            //         slideIn.play();
            //     }

            //     // 👉 Si on est dans l'état "Sign Up" → revenir au Login
            //     else {

            //         loginButton.setText("Login");

            //         // Animation retour
            //         TranslateTransition slideBackSignUp = new TranslateTransition(Duration.millis(300), signUp);
            //         slideBackSignUp.setByX(-width);
            //         slideBackSignUp.play();

            //         TranslateTransition slideBackLogin = new TranslateTransition(Duration.millis(300), login);
            //         slideBackLogin.setByX(-width);
            //         slideBackLogin.play();

            //         // Animation radius inverse
            //         playRadiusAnimation(false);

            //         // Retrouver le pane SignUp chargé (colonne 0)
            //         for (javafx.scene.Node n : signUpLoginBox.getChildren()) {
            //             if (n != login && n != signUp) {
            //                 signup2 = (VBox) n;
            //             }
            //         }

            //         if (signup2 != null) {
            //             // Le pousser vers la gauche
            //             TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), signup2);
            //             slideOut.setToX(-width);

            //             slideOut.setOnFinished(ev -> {
            //                 signUpLoginBox.getChildren().remove(signup2);
            //             });

            //             slideOut.play();
            //         }
            //     }
            // });

            loginButton.setOnAction(e -> {
                if (loginButton.getText().equals("Login")) {
                    showSignUpAnimation();
                } else {
                    showLoginAnimation();
                }
            });

        });
        
        //login automatique
        String token = RememberMeUtil.getToken() ;

        // if(token!=null){
        //     User user = UserTable.loginWithToken(RememberMeUtil.getToken()) ;
        //     if(user!=null){
        //         UserSession.getInstance().setUser(user);

        //         loginButton.fire();
        //     }
        // }
                    System.out.println(token);

        
        if (token != null) {
            User user = UserTable.loginWithToken(token);
            if (user != null) {
                UserSession.getInstance().setUser(user);

                Platform.runLater(this::showSignUpAnimation);

                Platform.runLater(() -> loginController.fillInputs());
                
                PauseTransition pause = new PauseTransition(Duration.seconds(2));

                pause.setOnFinished(event -> {
                    try {
                        rootStackPane.getChildren().clear();
                        rootStackPane.getChildren().add(
                            FXMLLoader.load(getClass().getResource("/views/MainView.fxml"))
                        );
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                pause.play();



                

            }
        }

        
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


    private void showSignUpAnimation() {

        double width = signUp.getWidth();

        loginButton.setText("Sign Up");

        // Animation sortante
        TranslateTransition slideSignUp = new TranslateTransition(Duration.millis(300), signUp);
        slideSignUp.setByX(width);

        TranslateTransition slideLogin = new TranslateTransition(Duration.millis(300), login);
        slideLogin.setByX(width);

        slideSignUp.play();
        slideLogin.play();

        // Animation radius
        playRadiusAnimation(true);

        // Charger la vue signIn
        try {
            // signup2 = FXMLLoader.load(getClass().getResource("/views/signIn.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signIn.fxml")) ;
            signup2 = loader.load() ;

            loginController = loader.getController() ;

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        // Ajouter dans la GridPane
        GridPane.setColumnIndex(signup2, 0);
        GridPane.setRowIndex(signup2, 0);
        signUpLoginBox.getChildren().add(signup2);

        signup2.setPrefWidth(login.getWidth());
        signup2.setPrefHeight(login.getHeight());

        // Position hors écran
        signup2.setTranslateX(-width);

        // Animation entrée
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), signup2);
        slideIn.setToX(0);
        slideIn.play();
    }

    private void showLoginAnimation() {

        double width = signUp.getWidth();

        loginButton.setText("Login");

        TranslateTransition slideBackSignUp = new TranslateTransition(Duration.millis(300), signUp);
        slideBackSignUp.setByX(-width);

        TranslateTransition slideBackLogin = new TranslateTransition(Duration.millis(300), login);
        slideBackLogin.setByX(-width);

        slideBackSignUp.play();
        slideBackLogin.play();

        playRadiusAnimation(false);

        if (signup2 != null) {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), signup2);
            slideOut.setToX(-width);

            slideOut.setOnFinished(ev -> {
                signUpLoginBox.getChildren().remove(signup2);
                signup2 = null;
            });

            slideOut.play();
        }
    }



    

    
}
