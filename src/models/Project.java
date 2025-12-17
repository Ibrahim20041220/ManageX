package models;

import java.util.Date;

public class Project {
    private int id;
    private String name;
    private int createdBy;
    private String description;
    private String code;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Date endDate;
    private int taskCount; // Pour stocker le nombre de tâches
    private double progress; // Pour calculer la progression

    // Constructeurs
    public Project() {}

    public Project(int id, String name, String description, String code, String status,
                   int createdBy, Date createdAt, Date updatedAt, Date endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.endDate = endDate;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getStatusDisplay() {
        switch (status) {
            case "ACTIVE": return "En cours";
            case "COMPLETED": return "Terminé";
            case "ARCHIVED": return "En pause";
            default: return status;
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}