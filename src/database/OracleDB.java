package database;
  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement ;
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

 

    public static void createProcedureToken() {
        String plSql = """
            CREATE OR REPLACE FUNCTION save_remember_token(
                p_userId INTEGER
            ) RETURN VARCHAR2 IS 
                v_token  VARCHAR2(250);
            BEGIN
                v_token := SYS_GUID();

                UPDATE USERS
                SET remember_token = v_token
                WHERE id = p_userId;
                
                COMMIT;

                RETURN v_token;

            EXCEPTION
                WHEN OTHERS THEN
                    ROLLBACK;
                    RETURN NULL;
            
            END save_remember_token;
            """;

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.execute(plSql);

            System.out.println("Fonction save_remember_token créée avec succès !");
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de la fonction save_remember_token !");
            e.printStackTrace();
        }
    }




    public static void createTables_Procedures(){

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


        createProcedureToken();
    }


}
