import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import database.OracleDB;
import javafx.fxml.FXMLLoader;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Connection conn = OracleDB.getConnection();
        if (conn != null) {
            System.out.println("Connexion OK !");
            OracleDB.createTables();
        }

        //Parent root = FXMLLoader.load(getClass().getResource("/views/signUp.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/signUpLoginView.fxml"));
        StackPane rooth = new StackPane(root);
        Scene scene = new Scene(rooth,700,600);
        scene.getStylesheets().addAll(getClass().getResource("/styles/main.css").toExternalForm(),
                getClass().getResource("/styles/signUp.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}