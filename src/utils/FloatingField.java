package utils;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class FloatingField extends StackPane {
    private Label label;
    private TextField field;
    private boolean floated = false;

    public FloatingField() {
        label = new Label();
        field = new TextField();

        label.setStyle("-fx-text-fill: #999; -fx-background-color: white; -fx-padding: 0 5px;");
        field.setStyle("-fx-border-width:2px;-fx-border-color: #c1bdbdff ;");
        field.setPrefHeight(40);

        label.setTranslateY(10);
        label.setTranslateX(5);
        label.setMouseTransparent(true);

        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(field, label);
        this.setPadding(new Insets(5));

        Runnable updateFloatingState = () -> {
            boolean shouldFloat = field.isFocused()
                    || (field.getText() != null && !field.getText().isEmpty());
            floatLabel(shouldFloat);
        };

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            updateFloatingState.run();

            if (newVal) {
                // Champ focus → appliquer gradient
                field.setStyle("""
                    -fx-background-color: white;
                    -fx-border-radius: 5;
                    -fx-border-width: 3;
                    -fx-border-color: linear-gradient(to right, #cc0000, #001a33);
                """);
            } else {
                // Champ blur → style normal
                field.setStyle("""
                    -fx-background-color: white;
                    -fx-border-radius: 5;
                    -fx-border-width: 2;
                    -fx-border-color: #d0d0d0;
                """);
            }
        });


        // texte
        field.textProperty().addListener((obs, oldText, newText) -> {
            updateFloatingState.run();
        });

    }

    // Getter et setter pour FXML
    public void setLabelText(String text) {
        label.setText(text);
    }

    public String getLabelText() {
        return label.getText();
    }

    private void floatLabel(boolean up) {
        if (up && !floated) {
            TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), label);
            moveUp.setToY(-10);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), label);
            shrink.setToX(0.9);
            shrink.setToY(0.9);

            ParallelTransition anim = new ParallelTransition(moveUp, shrink);
            anim.play();
            floated = true;
        } else if (!up && floated) {
            TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), label);
            moveDown.setToY(10);
            ScaleTransition grow = new ScaleTransition(Duration.millis(200), label);
            grow.setToX(1);
            grow.setToY(1);

            ParallelTransition anim = new ParallelTransition(moveDown, grow);
            anim.play();
            floated = false;
        }
    }

    public TextField getField() {
        return field;
    }

    public String getText() {
        return field.getText();
    }
}
