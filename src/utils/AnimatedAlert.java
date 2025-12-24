// package utils;

// import javafx.animation.*;
// import javafx.geometry.Pos;
// import javafx.scene.control.Label;
// import javafx.scene.layout.Pane;
// import javafx.scene.layout.StackPane;
// import javafx.util.Duration;
// import javafx.scene.layout.Region;


// public class AnimatedAlert  extends StackPane {


//     public AnimatedAlert (String message) {

//         Label label = new Label(message);
//         label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

//         setAlignment(Pos.CENTER_LEFT);
//         setPrefSize(300, 60);
//         setStyle("""
//             -fx-background-color: #2c3e50;
//             -fx-background-radius: 10;
//             -fx-padding: 5;
//         """);

//         getChildren().add(label);

//         setMaxWidth(Region.USE_PREF_SIZE);
//         setMinWidth(Region.USE_PREF_SIZE);
//         setMaxHeight(Region.USE_PREF_SIZE);
//         setMinHeight(Region.USE_PREF_SIZE);

//     }

//     public void show(Pane root) {
//         root.getChildren().add(this);

//         // Position initiale (hors écran à droite)
//         setTranslateX(root.getWidth());

//         TranslateTransition in = new TranslateTransition(Duration.millis(400), this);
//         in.setFromX(root.getWidth());
//         in.setToX(0);
//         in.setInterpolator(Interpolator.EASE_OUT);

//         PauseTransition pause = new PauseTransition(Duration.seconds(2));

//         TranslateTransition out = new TranslateTransition(Duration.millis(400), this);
//         out.setFromX(0);
//         out.setToX(-root.getWidth());
//         out.setInterpolator(Interpolator.EASE_IN);

//         out.setOnFinished(e -> root.getChildren().remove(this));

//         SequentialTransition sequence = new SequentialTransition(in, pause, out);
//         sequence.play();
//     }
// }


package utils;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.layout.Region;

public class AnimatedAlert extends StackPane {

    public enum AlertType {
        SUCCESS, ERROR, INFO
    }

    public AnimatedAlert(String message, AlertType type) {

        // Icône
        Label icon = new Label(getIcon(type));
        icon.setFont(Font.font(18));
        icon.setTextFill(getColor(type));

        // Texte
        Label text = new Label(message);
        text.setTextFill(Color.WHITE);
        text.setFont(Font.font(14));
        text.setWrapText(true);

        HBox content = new HBox(10, icon, text);
        content.setAlignment(Pos.CENTER_LEFT);


        setPadding(new Insets(12, 18, 12, 18));
        setAlignment(Pos.CENTER_LEFT);

        setMaxWidth(Region.USE_PREF_SIZE);
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);
        setMinHeight(Region.USE_PREF_SIZE);


        setStyle("""
            -fx-background-color: #1e1e1e;
            -fx-background-radius: 14;
        """);

        // Ombre
        setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.35)));

        getChildren().add(content);
        setOpacity(0);
    }

    public void show(Pane root) {
        root.getChildren().add(this);

        // Position en haut à droite
        // StackPane.setAlignment(this, Pos.TOP_RIGHT);
        StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(this, new Insets(0,10,50, 0));

        setTranslateX(80);
        setTranslateY(20);

        // Animation d'entrée
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), this);
        slideIn.setFromX(80);
        slideIn.setToX(0);

        // Pause
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Animation de sortie
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), this);
        slideOut.setFromX(0);
        slideOut.setToX(80);

        ParallelTransition in = new ParallelTransition(fadeIn, slideIn);
        ParallelTransition out = new ParallelTransition(fadeOut, slideOut);

        out.setOnFinished(e -> root.getChildren().remove(this));

        new SequentialTransition(in, pause, out).play();
    }

    // Helpers
    private String getIcon(AlertType type) {
        return switch (type) {
            case SUCCESS -> "✔";
            case ERROR -> "✖";
            case INFO -> "ℹ";
        };
    }

    private Color getColor(AlertType type) {
        return switch (type) {
            case SUCCESS -> Color.web("#2ecc71");
            case ERROR -> Color.web("#e74c3c");
            case INFO -> Color.web("#3498db");
        };
    }
}

