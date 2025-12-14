package controllers;

import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class OverviewController {

    @FXML private ImageView editOverviewIcon ;

    @FXML
    public void initialize(){
        editOverviewIcon.setImage(new Image(getClass().getResource("/icons/edit.png").toExternalForm())) ;
        


    }

}
