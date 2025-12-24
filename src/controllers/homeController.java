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
    @FXML private Label lblProjetsCount, lblTachesCount, lblMembresCount;
    @FXML private VBox vboxProjetsRecents;

    private ProjectDAO projectDAO = new ProjectDAO();
    private int currentUserId = 1; // À récupérer depuis votre session réelle

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadStatistics();
        loadRecentProjects();
    }

    private void loadStatistics() {
        // Stats filtrées pour l'utilisateur
        int userProjects = projectDAO.countProjectsForUser(currentUserId);
        lblProjetsCount.setText(String.valueOf(userProjects));

        // On peut aussi filtrer les tâches ici si nécessaire
        lblTachesCount.setText(String.valueOf(TaskTable.count()));
        lblMembresCount.setText(String.valueOf(projectDAO.countTotalMembers()));
    }

    private void loadRecentProjects() {
        vboxProjetsRecents.getChildren().clear();
        // APPEL DE LA NOUVELLE MÉTHODE FILTRÉE
        List<Project> projects = projectDAO.getRecentProjectsForUser(currentUserId, 3);

        if (projects.isEmpty()) {
            Label noProjects = new Label("Vous n'avez rejoint aucun projet.");
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

    private void viewProject(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/projectDetails.fxml"));
            Parent root = loader.load();

            ProjectDetailsController controller = loader.getController();
            controller.setProjectData(project);

            Stage stage = new Stage();
            stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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