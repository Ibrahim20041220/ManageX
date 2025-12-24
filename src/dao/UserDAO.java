package dao;

import database.OracleDB;
import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO (Data Access Object)
 * Cette classe gère toutes les opérations de base de données liées à l'entité User.
 */
public class UserDAO {

    /**
     * Récupère la liste de tous les utilisateurs de la base de données.
     * Les utilisateurs sont triés par ordre alphabétique de prénom puis de nom.
     * Cette méthode est utilisée pour peupler les listes déroulantes (ComboBox)
     * où l'on doit assigner une tâche à un membre.
     *
     * @return Une liste d'objets User (List<User>). Si une erreur survient ou si aucun
     *         utilisateur n'est trouvé, une liste vide est retournée.
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        
        // Requête SQL pour sélectionner toutes les informations nécessaires pour créer un objet User.
        String sql = "SELECT id, firstName, lastName, email, phone, profession, profilePic FROM USERS ORDER BY firstName, lastName";

        // Utilisation du try-with-resources pour garantir la fermeture automatique des ressources
        try (Connection conn = OracleDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itération sur chaque ligne retournée par la requête
            while (rs.next()) {
                // Création d'un nouvel objet User pour chaque enregistrement
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profession"),
                    rs.getString("profilePic")
                );
                // Ajout de l'utilisateur à la liste
                userList.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération de tous les utilisateurs : " + e.getMessage());
            e.printStackTrace();
            // En cas d'erreur, on retourne une liste vide pour éviter les NullPointerException
        }
        
        return userList;
    }
    
    // Vous pouvez ajouter d'autres méthodes ici si nécessaire, par exemple :
    // public User getUserById(int id) { ... }
}