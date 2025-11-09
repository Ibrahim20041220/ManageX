package utils;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class FloatingField extends StackPane {
    private final Label label;
    private final TextField field;
    private boolean floated = false;

    public FloatingField(String labelText) {
        this.label = new Label(labelText);
        this.field = new TextField();

        label.setStyle("-fx-text-fill: #999; -fx-background-color: white; -fx-padding: 0 5px;");
        field.setStyle("-fx-border-color: #0078ff; -fx-border-width: 1px; -fx-background-color: transparent; -fx-padding: 10 5 5 5; -fx-border-radius : 5px; ");
        field.setPrefHeight(40);

        // Position du label initialement sur le champ
        label.setTranslateY(10);
        label.setTranslateX(5);
        label.setMouseTransparent(true); // pour ne pas bloquer le clic

        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(field, label);
        this.setPadding(new Insets(5));

        // Animation quand le focus change
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                floatLabel(true);
            } else if (field.getText().isEmpty()) {
                floatLabel(false);
            }
        });
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
