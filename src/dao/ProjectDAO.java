package dao;

import models.Project;
import database.OracleDB;
import database.tables.ProjectTable;

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
    public List<Project> getRecentProjects(int limit) {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM PROJECT ORDER BY updatedAt DESC FETCH FIRST ? ROWS ONLY";

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = extractProjectFromResultSet(rs);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des projets récents");
            e.printStackTrace();
        }

        return projects;
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
    public int countProjects() {
        String query = "SELECT COUNT(*) FROM PROJECT";

        try (Connection conn = OracleDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des projets");
            e.printStackTrace();
        }

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
        java.sql.Date endDate = null;
        if (project.getEndDate() != null) {
            endDate = new java.sql.Date(project.getEndDate().getTime());
        }

        return ProjectTable.create(
                project.getName(),
                project.getCreatedBy(),
                project.getDescription(),
                project.getCode(),
                endDate
        );
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
}