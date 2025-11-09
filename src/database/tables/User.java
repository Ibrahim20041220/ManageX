package database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.OracleDB;


public class User {
    
    public static void createTable(){
        String sql = "CREATE TABLE User IF NOT EXIST ("+
                        "id NUMBER PRIMARY KEY , " +
                        "firstName VARCHAR2(20) , " +
                        "lastName VARCHAR2(20) , " +
                        "email VARCHAR2(20) , " +
                        "password VARCHAR(50) , " +
                        "phone VARCHAR(50) , " +
                        "connectedAt DATE , " +
                        "status (ON,OFF) , " +
                        "profilePic VARCHAR2(20) , " +
                        "createdAt DATE )"   ;

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table ETUDIANT créée");
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) {
                System.out.println("Table ETUDIANT déjà existante");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void create(String firstName, String lastName, String email, String phone, String password) {
        String sql = "INSERT INTO USERS (firstName, lastName, email, phone, password, status, createdAt) " +
                 "VALUES (?, ?, ?, ?, ?, 'OFF', SYSDATE)";

        try (Connection conn = OracleDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, password);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Utilisateur créé avec succès !");
            } else {
                System.out.println("Échec de la création de l'utilisateur.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
