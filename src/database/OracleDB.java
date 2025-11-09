package database;
  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utils.Globals;
    
public class OracleDB {
    private static final String URL = Globals.URL_DB;
    private static final String USER = Globals.USER_DB;
    private static final String PASSWORD = Globals.PASSWORD_DB;

    
    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver"); 
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à Oracle !");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à Oracle !");
            e.printStackTrace();
        }
        return null;
    }
}
