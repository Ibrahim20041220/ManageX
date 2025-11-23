import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Connection;
import database.OracleDB; 

import views.signUp ;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Connection conn = OracleDB.getConnection();
        if (conn != null) {
            System.out.println("Connexion OK !");
        }
        
        StackPane root = new StackPane(new signUp());
        Scene scene = new Scene(root);

        stage.setTitle("Test JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
