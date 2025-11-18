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
        field.getStyleClass().add("field") ;
        field.setPrefHeight(40);

        label.setTranslateY(10);
        label.setTranslateX(5);
        label.setMouseTransparent(true);

        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(field, label);
        this.setPadding(new Insets(5));

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal){
                floatLabel(true);
                field.setStyle(field.getStyle().replace("#d0d0d0", "#2196f3"));
            }

            else if (field.getText().isEmpty()){
                floatLabel(false);
                field.setStyle(field.getStyle().replace("#2196f3", "#d0d0d0"));
            }

            else {
                field.setStyle(field.getStyle().replace("#2196f3", "#d0d0d0"));

            }

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
