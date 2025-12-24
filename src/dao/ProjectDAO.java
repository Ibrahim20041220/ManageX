package dao;

import models.Project;
import database.OracleDB;
import database.tables.ProjectTable;
import models.ProjectMemberInfo; // Ajoutez cet import en haut
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // Récupérer tous les projets
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM PROJECT WHERE  ORDER BY updatedAt DESC";

        try (Connection conn = OracleDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Project project = extractProjectFromResultSet(rs);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des projets");
            e.printStackTrace();
        }

        return projects;
    }

    // Récupérer les projets récents (3 derniers)
    public List<Project> getRecentProjectsForUser(int userId, int limit) {
        List<Project> projects = new ArrayList<>();
        // Jointure entre PROJECT et PROJECTMEMBER
        String query = "SELECT p.* FROM PROJECT p " +
                "JOIN PROJECTMEMBER pm ON p.id = pm.projectId " +
                "WHERE pm.memberId = ? " +
                "ORDER BY p.updatedAt DESC " +
                "FETCH FIRST ? ROWS ONLY";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                projects.add(extractProjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public int countTotalMembers() {
        String query = "SELECT COUNT(DISTINCT memberId) FROM PROJECTMEMBER";
        try (Connection conn = OracleDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ProjectMemberInfo> getProjectMembersFullDetails(int projectId) {
        List<ProjectMemberInfo> members = new ArrayList<>();
        String query = "SELECT U.firstname, U.lastname, R.name as roleName " +
                "FROM USERS U " +
                "JOIN PROJECTMEMBER PM ON U.id = PM.memberId " +
                "JOIN ROLE R ON PM.roleId = R.id " +
                "WHERE PM.projectId = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                String roleName = rs.getString("roleName");
                members.add(new ProjectMemberInfo(fullName, roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // Récupérer un projet par ID
    public Project getProjectById(int id) {
        String query = "SELECT * FROM PROJECT WHERE id = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractProjectFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du projet");
            e.printStackTrace();
        }

        return null;
    }

    // Récupérer les projets par statut
    public List<Project> getProjectsByStatus(String status) {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM PROJECT WHERE status = ? ORDER BY updatedAt DESC";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = extractProjectFromResultSet(rs);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des projets par statut");
            e.printStackTrace();
        }

        return projects;
    }

    // Compter les projets
    public int countProjectsForUser(int userId) {
        String query = "SELECT COUNT(*) FROM PROJECTMEMBER WHERE memberId = ?";
        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // Compter les tâches d'un projet
    public int countTasksByProject(int projectId) {
        String query = "SELECT COUNT(*) FROM TASK WHERE projectId = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des tâches");
            e.printStackTrace();
        }

        return 0;
    }

    // Calculer la progression d'un projet basée sur les tâches complétées
    public double calculateProjectProgress(int projectId) {
        String query = "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) as completed " +
                "FROM TASK WHERE projectId = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int completed = rs.getInt("completed");

                if (total == 0) return 0.0;
                return (completed * 100.0) / total;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la progression");
            e.printStackTrace();
        }

        return 0.0;
    }

    // Ajouter un nouveau projet (utilise la méthode existante de database.tables.Project)
    public boolean addProject(Project project) {
        // Requêtes SQL
        String sqlProject = "INSERT INTO PROJECT (name, createdBy, description, code, endDate) VALUES (?, ?, ?, ?, ?)";
        String sqlRole = "SELECT id FROM ROLE WHERE name = 'ADMIN'";
        String sqlMember = "INSERT INTO PROJECTMEMBER (projectId, memberId, roleId) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = OracleDB.getConnection();
            conn.setAutoCommit(false); // On commence une transaction pour que tout passe ou rien

            // 1. INSÉRER LE PROJET et récupérer l'ID généré
            int generatedProjectId = -1;
            String[] generatedColumns = {"ID"};
            try (PreparedStatement pstmt = conn.prepareStatement(sqlProject, generatedColumns)) {
                pstmt.setString(1, project.getName());
                pstmt.setInt(2, project.getCreatedBy());
                pstmt.setString(3, project.getDescription());
                pstmt.setString(4, project.getCode());

                if (project.getEndDate() != null) {
                    pstmt.setDate(5, new java.sql.Date(project.getEndDate().getTime()));
                } else {
                    pstmt.setNull(5, Types.DATE);
                }

                pstmt.executeUpdate();

                // Récupérer l'ID que Oracle vient de générer
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedProjectId = rs.getInt(1);
                    }
                }
            }

            // 2. RÉCUPÉRER L'ID DU RÔLE 'ADMIN'
            int adminRoleId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlRole)) {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    adminRoleId = rs.getInt("id");
                }
            }

            if (adminRoleId == -1) {
                throw new SQLException("Rôle ADMIN introuvable dans la table ROLE.");
            }

            // 3. AJOUTER LE CRÉATEUR COMME PREMIER MEMBRE (ADMIN)
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMember)) {
                pstmt.setInt(1, generatedProjectId);
                pstmt.setInt(2, project.getCreatedBy());
                pstmt.setInt(3, adminRoleId);
                pstmt.executeUpdate();
            }

            // Si tout s'est bien passé, on valide la transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Erreur lors de la création du projet et de l'ajout du membre admin");
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Mettre à jour un projet
    public boolean updateProject(Project project) {
        String query = "UPDATE PROJECT SET name = ?, description = ?, code = ?, status = ?, " +
                "endDate = ?, updatedAt = SYSDATE WHERE id = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getDescription());
            pstmt.setString(3, project.getCode());
            pstmt.setString(4, project.getStatus());

            if (project.getEndDate() != null) {
                pstmt.setDate(5, new java.sql.Date(project.getEndDate().getTime()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            pstmt.setInt(6, project.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du projet");
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un projet
    public boolean deleteProject(int id) {
        String query = "DELETE FROM PROJECT WHERE id = ?";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du projet");
            e.printStackTrace();
            return false;
        }
    }

    // Rechercher des projets par nom
    public List<Project> searchProjectsByName(String searchText) {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM PROJECT WHERE LOWER(name) LIKE ? ORDER BY updatedAt DESC";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchText.toLowerCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = extractProjectFromResultSet(rs);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de projets");
            e.printStackTrace();
        }

        return projects;
    }

    // Méthode utilitaire pour extraire un projet du ResultSet
    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setName(rs.getString("name"));
        project.setCreatedBy(rs.getInt("createdBy"));
        project.setDescription(rs.getString("description"));
        project.setCode(rs.getString("code"));
        project.setStatus(rs.getString("status"));
        project.setCreatedAt(rs.getDate("createdAt"));
        project.setUpdatedAt(rs.getDate("updatedAt"));
        project.setEndDate(rs.getDate("endDate"));

        // Charger le nombre de tâches et la progression
        project.setTaskCount(countTasksByProject(project.getId()));
        project.setProgress(calculateProjectProgress(project.getId()));

        return project;
    }
    public Project getProjectByCode(String code) {
        String query = "SELECT * FROM PROJECT WHERE code = ?";
        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractProjectFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

