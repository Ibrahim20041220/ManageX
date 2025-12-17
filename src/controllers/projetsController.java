package controllers;

import dao.ProjectDAO;
import models.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class projetsController implements Initializable {


    @FXML private TextField txtSearch;
    @FXML private Button btnAddProject;
    @FXML private GridPane gridProjets;

    private ProjectDAO projectDAO;
    private String currentFilter = "ALL"; // ALL, ACTIVE, COMPLETED, ARCHIVED

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser le DAO
        projectDAO = new ProjectDAO();


        
        loadProjects();

        // Listener pour la recherche
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterProjects(newVal));
    }

    private void loadProjects() {
        gridProjets.getChildren().clear();

        // Récupérer les projets selon le filtre actuel
        List<Project> projects;

        switch (currentFilter) {
            case "ACTIVE":
                projects = projectDAO.getProjectsByStatus("ACTIVE");
                break;
            case "COMPLETED":
                projects = projectDAO.getProjectsByStatus("COMPLETED");
                break;
            case "ARCHIVED":
                projects = projectDAO.getProjectsByStatus("ARCHIVED");
                break;
            default:
                projects = projectDAO.getAllProjects();
                break;
        }

        // Si la recherche est active, filtrer les résultats
        String searchText = txtSearch.getText();
        if (searchText != null && !searchText.trim().isEmpty()) {
            projects = projectDAO.searchProjectsByName(searchText);
        }

        if (projects.isEmpty()) {
            Label noProjects = new Label("Aucun projet trouvé");
            noProjects.getStyleClass().add("no-projects-label");
            GridPane.setColumnIndex(noProjects, 0);
            GridPane.setRowIndex(noProjects, 0);
            gridProjets.getChildren().add(noProjects);
            return;
        }

        int col = 0;
        int row = 0;

        for (Project project : projects) {
            VBox card = createProjectCard(project);
            gridProjets.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createProjectCard(Project project) {
        VBox card = new VBox(15);
        card.getStyleClass().add("project-card");
        card.setPrefWidth(220);
        card.setPrefHeight(180);
        card.setPadding(new Insets(20));

        // En-tête avec statut
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Pane statusDot = new Pane();
        statusDot.getStyleClass().add("status-dot");
        statusDot.getStyleClass().add(getStatusClass(project.getStatus()));
        statusDot.setPrefSize(10, 10);
        statusDot.setMaxSize(10, 10);

        Label statusLabel = new Label(project.getStatusDisplay());
        statusLabel.getStyleClass().add("status-label");

        header.getChildren().addAll(statusDot, statusLabel);

        // Nom du projet
        Label nameLabel = new Label(project.getName());
        nameLabel.getStyleClass().add("project-name");
        nameLabel.setWrapText(true);

        // Barre de progression
        VBox progressBox = new VBox(5);

        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Label progressLabel = new Label("Progression");
        progressLabel.getStyleClass().add("progress-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // TODO: Calculer la progression réelle basée sur les tâches
        double progressValue = calculateProjectProgress(project);
        Label progressValueLabel = new Label(String.format("%.0f%%", progressValue));
        progressValueLabel.getStyleClass().add("progress-value");
        progressHeader.getChildren().addAll(progressLabel, spacer, progressValueLabel);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(progressValue / 100.0);
        progressBar.setPrefWidth(180);
        progressBar.getStyleClass().add("project-progress");

        progressBox.getChildren().addAll(progressHeader, progressBar);

        // Footer avec nombre de tâches
        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER_LEFT);

        // TODO: Récupérer le nombre réel de tâches depuis la base de données
        int taskCount = project.getTaskCount(); // Vous devrez ajouter une méthode dans ProjectDAO
        Label tasksLabel = new Label(taskCount + " tâches");
        tasksLabel.getStyleClass().add("tasks-label");
        footer.getChildren().add(tasksLabel);

        card.getChildren().addAll(header, nameLabel, progressBox, footer);

        // Événement de clic
        card.setOnMouseClicked(e -> viewProjectDetails(project));
        card.getStyleClass().add("clickable");

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

    private double calculateProjectProgress(Project project) {
        // TODO: Calculer la progression basée sur les tâches complétées
        // Pour l'instant, retourner 100% si le projet est terminé, sinon un pourcentage basé sur l'état
        if ("COMPLETED".equals(project.getStatus())) {
            return 100.0;
        } else if ("ARCHIVED".equals(project.getStatus())) {
            return 25.0;
        } else {
            return 60.0; // Valeur par défaut pour les projets actifs
        }
    }

    private void filterProjects(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadProjects();
        } else {
            gridProjets.getChildren().clear();
            List<Project> projects = projectDAO.searchProjectsByName(searchText);

            if (projects.isEmpty()) {
                Label noProjects = new Label("Aucun projet trouvé pour \"" + searchText + "\"");
                noProjects.getStyleClass().add("no-projects-label");
                GridPane.setColumnIndex(noProjects, 0);
                GridPane.setRowIndex(noProjects, 0);
                gridProjets.getChildren().add(noProjects);
                return;
            }

            int col = 0;
            int row = 0;
            for (Project project : projects) {
                VBox card = createProjectCard(project);
                gridProjets.add(card, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    @FXML
    private void handleAddProject() {
        System.out.println("Ajouter un nouveau projet");

        // TODO: Ouvrir un dialogue pour ajouter un projet
        // Exemple de dialogue simple
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau Projet");
        dialog.setHeaderText("Créer un nouveau projet");
        dialog.setContentText("Nom du projet:");

        dialog.showAndWait().ifPresent(projectName -> {
            if (!projectName.trim().isEmpty()) {
                Project newProject = new Project();
                newProject.setName(projectName);
                newProject.setDescription(""); // TODO: Demander la description
                newProject.setCode("PROJ-" + System.currentTimeMillis()); // Code temporaire
                newProject.setStatus("ACTIVE");
                newProject.setCreatedBy(1); // TODO: Récupérer l'ID de l'utilisateur connecté

                if (projectDAO.addProject(newProject)) {
                    showSuccessAlert("Succès", "Le projet a été créé avec succès!");
                    loadProjects(); // Recharger la liste
                } else {
                    showErrorAlert("Erreur", "Impossible de créer le projet.");
                }
            }
        });
    }

    @FXML
    private void filterAll() {
        System.out.println("Filtre: Tous");
        currentFilter = "ALL";
        loadProjects();
    }

    @FXML
    private void filterEnCours() {
        System.out.println("Filtre: En cours");
        currentFilter = "ACTIVE";
        loadProjects();
    }

    @FXML
    private void filterTermines() {
        System.out.println("Filtre: Terminés");
        currentFilter = "COMPLETED";
        loadProjects();
    }

    @FXML
    private void filterEnPause() {
        System.out.println("Filtre: En pause");
        currentFilter = "ARCHIVED";
        loadProjects();
    }

    private void viewProjectDetails(Project project) {
        System.out.println("Voir détails du projet: " + project.getName() + " (ID: " + project.getId() + ")");
        // TODO: Navigation vers page de détails avec l'ID du projet
        // navigateToProjectDetails(project.getId());
    }

   
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}