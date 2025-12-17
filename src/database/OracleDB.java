package database;
  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utils.Globals;
import database.tables.AttachmentTable;
import database.tables.CommentTable;
import database.tables.MemberPermissionTable;
import database.tables.NotificationTable;
import database.tables.PermissionTable;
import database.tables.ProjectMemberTable;
import database.tables.RoleTable;
import database.tables.RolePermissionTable;
import database.tables.TaskTable;
import database.tables.UserTable;
import database.tables.ProjectTable;
    
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

    public static void createTables(){

        UserTable.createTable();
        ProjectTable.createTable();
        TaskTable.createTable();
        NotificationTable.createTable();
        RoleTable.createTable();
        ProjectMemberTable.createTable();
        PermissionTable.createTable();
        RolePermissionTable.createTable();
        MemberPermissionTable.createTable();
        CommentTable.createTable();
        AttachmentTable.createTable();
    }


}
