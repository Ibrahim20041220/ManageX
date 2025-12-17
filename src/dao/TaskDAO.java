package dao;

import models.Task;
import database.OracleDB;
import database.tables.TaskTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // Récupérer toutes les tâches avec le nom du projet
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = """
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            ORDER BY t.createdAt DESC
        """;

        try (Connection conn = OracleDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tâches");
            e.printStackTrace();
        }

        return tasks;
    }

    // Récupérer une tâche par ID
    public Task getTaskById(int id) {
        String query = """
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            WHERE t.id = ?
        """;

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la tâche");
            e.printStackTrace();
        }

        return null;
    }

    // Récupérer les tâches par statut
    public List<Task> getTasksByStatus(String status) {
        List<Task> tasks = new ArrayList<>();
        String query = """
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            WHERE t.status = ? 
            ORDER BY t.priority DESC, t.createdAt DESC
        """;

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tâches par statut");
            e.printStackTrace();
        }

        return tasks;
    }

    // Récupérer les tâches par projet
    public List<Task> getTasksByProject(int projectId) {
        List<Task> tasks = new ArrayList<>();
        String query = """
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            WHERE t.projectId = ? 
            ORDER BY t.priority DESC, t.createdAt DESC
        """;

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tâches du projet");
            e.printStackTrace();
        }

        return tasks;
    }

    // Récupérer les tâches par priorité
    public List<Task> getTasksByPriority(int minPriority, int maxPriority) {
        List<Task> tasks = new ArrayList<>();
        String query = """
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            WHERE t.priority BETWEEN ? AND ? 
            ORDER BY t.priority DESC, t.createdAt DESC
        """;

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, minPriority);
            pstmt.setInt(2, maxPriority);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tâches par priorité");
            e.printStackTrace();
        }

        return tasks;
    }

    // Filtrer les tâches (statut + priorité + projet)
    public List<Task> filterTasks(String status, String priority, Integer projectId) {
        List<Task> tasks = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
            SELECT t.*, p.name as projectName 
            FROM TASK t 
            LEFT JOIN PROJECT p ON t.projectId = p.id 
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Filtre par statut
        if (status != null && !status.equals("Tous")) {
            String dbStatus = convertStatusToDb(status);
            query.append(" AND t.status = ?");
            params.add(dbStatus);
        }

        // Filtre par priorité
        if (priority != null && !priority.equals("Toutes")) {
            if (priority.equals("Haute")) {
                query.append(" AND t.priority >= 4");
            } else if (priority.equals("Moyenne")) {
                query.append(" AND t.priority BETWEEN 2 AND 3");
            } else if (priority.equals("Basse")) {
                query.append(" AND t.priority <= 1");
            }
        }

        // Filtre par projet
        if (projectId != null && projectId > 0) {
            query.append(" AND t.projectId = ?");
            params.add(projectId);
        }

        query.append(" ORDER BY t.priority DESC, t.createdAt DESC");

        try (Connection conn = OracleDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage des tâches");
            e.printStackTrace();
        }

        return tasks;
    }

    // Compter les tâches
    public int countTasks() {
        return database.tables.TaskTable.count();
    }

    // Compter les tâches par statut
    public int countTasksByStatus(String status) {
        return database.tables.TaskTable.countByStatus(status);
    }

    // Ajouter une nouvelle tâche
    public boolean addTask(Task task) {
        return TaskTable.create(
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority()
        );
    }

    // Mettre à jour une tâche
    public boolean updateTask(Task task) {
        return TaskTable.update(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority()
        );
    }

    // Mettre à jour le statut d'une tâche
    public boolean updateTaskStatus(int id, String status) {
        return TaskTable.updateStatus(id, status);
    }

    // Basculer le statut d'une tâche (terminée/non terminée)
    public boolean toggleTaskCompletion(int id, boolean completed) {
        String newStatus = completed ? "Done" : "ToDo";
        return TaskTable.updateStatus(id, newStatus);
    }

    // Supprimer une tâche
    public boolean deleteTask(int id) {
        return TaskTable.delete(id);
    }

    // Méthode utilitaire pour extraire une tâche du ResultSet
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setProjectId(rs.getInt("projectId"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getInt("priority"));
        task.setCreatedAt(rs.getDate("createdAt"));
        task.setUpdatedAt(rs.getDate("updatedAt"));

        // Nom du projet (si disponible)
        try {
            task.setProjectName(rs.getString("projectName"));
        } catch (SQLException e) {
            task.setProjectName("Projet inconnu");
        }

        return task;
    }

    // Convertir le statut de l'affichage vers la DB
    private String convertStatusToDb(String displayStatus) {
        switch (displayStatus) {
            case "À faire": return "ToDo";
            case "En cours": return "InProgress";
            case "Terminée": return "Done";
            case "Annulée": return "Canceled";
            default: return displayStatus;
        }
    }
}