package controllers;

import dao.ProjectDAO;
import models.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import database.tables.TaskTable;

public class homeController implements Initializable {

    private Label lblUsername;

    @FXML
    private Label lblProjetsCount;

    @FXML
    private Label lblTachesCount;

    @FXML
    private Label lblMembresCount;

    @FXML
    private VBox vboxProjetsRecents;

    @FXML
    private Circle profileCircle;

    private ProjectDAO projectDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsername = new Label() ;
        // Initialiser le DAO
        projectDAO = new ProjectDAO();

        // Charger les données de l'utilisateur
        loadUserData();

        // Charger les statistiques
        loadStatistics();

        // Charger les projets récents
        loadRecentProjects();

        
    }

    private void loadUserData() {
        // TODO: Récupérer le nom de l'utilisateur depuis la session
        lblUsername.setText("Amine Lamaizi");
    }

    private void loadStatistics() {
        // Récupérer le nombre de projets depuis la base de données
        int projectCount = projectDAO.countProjects();
        lblProjetsCount.setText(String.valueOf(projectCount));

        // Récupérer le nombre de tâches depuis la base de données
        int taskCount = TaskTable.count();
        lblTachesCount.setText(String.valueOf(taskCount));

        // TODO: Récupérer le nombre de membres depuis la base de données
        lblMembresCount.setText("0");
    }

    private void loadRecentProjects() {
        // Vider la liste
        vboxProjetsRecents.getChildren().clear();

        // Récupérer les 3 derniers projets depuis la base de données
        List<Project> projects = projectDAO.getRecentProjects(3);

        if (projects.isEmpty()) {
            Label noProjects = new Label("Aucun projet récent");
            noProjects.getStyleClass().add("no-projects-label");
            vboxProjetsRecents.getChildren().add(noProjects);
        } else {
            for (Project project : projects) {
                vboxProjetsRecents.getChildren().add(createProjectCard(project));
            }
        }
    }

    private Pane createProjectCard(Project project) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("project-card");
        card.setPadding(new Insets(15));

        // Indicateur de statut basé sur le statut réel du projet
        Pane statusIndicator = new Pane();
        statusIndicator.getStyleClass().add("status-indicator");
        statusIndicator.getStyleClass().add(getStatusClass(project.getStatus()));
        statusIndicator.setPrefSize(10, 10);
        statusIndicator.setMaxSize(10, 10);

        // Informations du projet
        VBox projectInfo = new VBox(5);

        Label nameLabel = new Label(project.getName());
        nameLabel.getStyleClass().add("project-name");

        // Calculer et afficher la date de dernière mise à jour
        String lastUpdate = getLastUpdateText(project.getUpdatedAt());
        Label dateLabel = new Label("Dernière mise à jour: " + lastUpdate);
        dateLabel.getStyleClass().add("project-date");

        projectInfo.getChildren().addAll(nameLabel, dateLabel);
        HBox.setHgrow(projectInfo, javafx.scene.layout.Priority.ALWAYS);

        // Bouton d'action
        Button btnView = new Button("Voir");
        btnView.getStyleClass().add("btn-view-project");
        btnView.setOnAction(e -> viewProject(project));

        card.getChildren().addAll(statusIndicator, projectInfo, btnView);

        return card;
    }

    private String getStatusClass(String status) {
        switch (status) {
            case "ACTIVE": return "status-active";
            case "COMPLETED": return "status-completed";
            case "ARCHIVED": return "status-paused";
            default: return "status-active";
        }
    }

    private String getLastUpdateText(Date updatedAt) {
        if (updatedAt == null) {
            return "Jamais";
        }

        Date now = new Date();
        long diffInMillies = Math.abs(now.getTime() - updatedAt.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diffInDays == 0) {
            return "Aujourd'hui";
        } else if (diffInDays == 1) {
            return "Hier";
        } else if (diffInDays < 7) {
            return "Il y a " + diffInDays + " jours";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(updatedAt);
        }
    }

    private void viewProject(String projectName) {
        // TODO: Implémenter l'affichage des détails du projet
        // Exemple: passer l'ID du projet au contrôleur de détails
        // navigateToProjectDetails(project.getId());
    }

    private void viewProject(Project project) {

        System.out.println("Affichage du projet: " + project.getName() + " (ID: " + project.getId() + ")");
        
    }

    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}