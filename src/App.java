import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import database.OracleDB;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Connection conn = OracleDB.getConnection();
        if (conn != null) {
            System.out.println("Connexion OK !");
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/signIn.fxml"));
            Scene scene = new Scene(root, 400, 300);
            stage.setTitle("ManageX - SignIn");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
