package models;

import java.util.Date;

public class Task {
    private int id;
    private int projectId;
    private String title;
    private String description;
    private String status; // ToDo, InProgress, Done, Canceled
    private int priority; // 0-5
    private Date createdAt;
    private Date updatedAt;

    // Champs supplémentaires pour l'affichage
    private String projectName;

    // Constructeurs
    public Task() {}

    public Task(int id, int projectId, String title, String description,
                String status, int priority, Date createdAt, Date updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Méthodes utilitaires
    public String getStatusDisplay() {
        switch (status) {
            case "ToDo": return "À faire";
            case "InProgress": return "En cours";
            case "Done": return "Terminée";
            case "Canceled": return "Annulée";
            default: return status;
        }
    }

    public String getPriorityDisplay() {
        switch (priority) {
            case 5:
            case 4: return "Haute";
            case 3:
            case 2: return "Moyenne";
            case 1:
            case 0: return "Basse";
            default: return "Moyenne";
        }
    }

    public boolean isCompleted() {
        return "Done".equals(status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                '}';
    }
}