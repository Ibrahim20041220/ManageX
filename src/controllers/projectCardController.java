package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class projectCardController {

    @FXML private Circle statusDot;
    @FXML private Label statusLabel;
    @FXML private Label projectTitle;
    @FXML private Label createdAtLabel ;
    @FXML private Label endDateLabel ;
    @FXML private Label taskCount;

    @FXML
    public void initialize() {
        // 🔹 valeurs statiques pour test
        statusLabel.setText("En cours");
        projectTitle.setText("hhhh");
        endDateLabel.setText("2020-12-20");
        createdAtLabel.setText("2019-11-23");
        taskCount.setText("0 tâches");

        statusDot.setStyle("-fx-fill: #22c55e;");
    }
}
