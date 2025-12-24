package controllers;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.AnimatedAlert;
import database.tables.UserTable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

public class OverviewController {

    @FXML private Label overviewLabel;
    @FXML private TextArea overviewArea;
    @FXML private Button editOverview;
    @FXML private ImageView editOverviewIcon ;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @FXML private VBox rootPane; 



    private boolean editMode = false;


    @FXML
    public void initialize(){
        editOverviewIcon.setImage(new Image(getClass().getResource("/icons/edit.png").toExternalForm())) ;
        overviewLabel.setText(UserTable.getOverview());
        


    }


    @FXML
    private void toggleEdit() {


        if (!editMode) {

            overviewArea.setText(overviewLabel.getText());

            overviewArea.setVisible(true);
            overviewArea.setManaged(true);

            overviewLabel.setVisible(false);
            overviewLabel.setManaged(false);

            editOverview.setVisible(false);
            editOverview.setManaged(false);

            saveButton.setVisible(true);
            saveButton.setManaged(true);

            cancelButton.setVisible(true);
            cancelButton.setManaged(true);


        }else{

            overviewLabel.setText(overviewArea.getText());

            overviewLabel.setVisible(true);
            overviewLabel.setManaged(true);

            overviewArea.setVisible(false);
            overviewArea.setManaged(false);


        }
    }

    @FXML
    private void saveOverview() {
        if(UserTable.updateOverview(overviewArea.getText())){
            overviewLabel.setText(overviewArea.getText());

        }else{
            AnimatedAlert alert = new AnimatedAlert("ErrorFailed to update overview. Please try again.",AnimatedAlert.AlertType.ERROR) ;
            alert.show(rootPane);
        }

        exitEditMode();
    }

    @FXML
    private void cancelEdit() {
        exitEditMode();
    }

    private void exitEditMode() {
        editOverview.setVisible(true);
        editOverview.setManaged(true);

        overviewArea.setVisible(false);
        overviewArea.setManaged(false);

        overviewLabel.setVisible(true);
        overviewLabel.setManaged(true);

        saveButton.setVisible(false);
        saveButton.setManaged(false);

        cancelButton.setVisible(false);
        cancelButton.setManaged(false);

        editMode = false;
    }

}
