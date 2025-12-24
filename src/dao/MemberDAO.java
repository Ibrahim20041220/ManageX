package dao;

import database.OracleDB;
import java.sql.*;

public class MemberDAO {
    public boolean addMemberToProject(int projectId, int userId, String roleName) {
        // 1. Requête pour trouver l'ID du rôle à partir de son nom
        String findRoleSql = "SELECT id FROM ROLE WHERE name = ?";
        String insertMemberSql = "INSERT INTO PROJECTMEMBER (projectId, memberId, roleId) VALUES (?, ?, ?)";

        try (Connection conn = OracleDB.getConnection()) {
            int roleId = -1;

            // Étape A : Trouver l'ID du rôle
            try (PreparedStatement pstmt = conn.prepareStatement(findRoleSql)) {
                pstmt.setString(1, roleName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    roleId = rs.getInt("id");
                }
            }

            // Si le rôle n'existe pas en base
            if (roleId == -1) {
                System.err.println("Erreur : Le rôle '" + roleName + "' n'existe pas dans la table ROLE.");
                return false;
            }

            // Étape B : Insérer le membre avec le bon ID trouvé
            try (PreparedStatement pstmt = conn.prepareStatement(insertMemberSql)) {
                pstmt.setInt(1, projectId);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, roleId);
                return pstmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserAlreadyMember(int projectId, int userId) {
        String query = "SELECT COUNT(*) FROM PROJECTMEMBER WHERE projectId = ? AND memberId = ?";
        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
