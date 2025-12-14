package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.TilePane;
import java.io.IOException;

public class projectController {
    
    @FXML private TilePane root ;

    public void initialize() throws IOException {
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;
        root.getChildren().add(FXMLLoader.load(getClass().getResource("../views/components/ProjectCard.fxml"))) ;

    }
}
