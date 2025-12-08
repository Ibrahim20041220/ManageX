package database;
  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utils.Globals;
import database.tables.Attachment;
import database.tables.Comment;
import database.tables.MemberPermission;
import database.tables.Notification;
import database.tables.Permission;
import database.tables.Project;
import database.tables.ProjectMember;
import database.tables.Role;
import database.tables.RolePermission;
import database.tables.Task;
import database.tables.User;
    
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

        User.createTable();
        Project.createTable();
        Task.createTable();
        Notification.createTable();
        Role.createTable();
        ProjectMember.createTable();
        Permission.createTable();
        RolePermission.createTable();
        MemberPermission.createTable();
        Comment.createTable();
        Attachment.createTable();
    }


}
