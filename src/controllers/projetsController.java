package controllers;

import dao.ProjectDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Project;
import java.util.Arrays;

import java.io.IOException;
import java.util.List;

public class projetsController {

    @FXML private GridPane gridProjets;
    @FXML private TextField txtSearch;
    @FXML private Button btnAddProject;
    @FXML private Button btnFilterAll;
    @FXML private Button btnFilterEnCours;
    @FXML private Button btnFilterTermines;
    @FXML private Button btnFilterEnPause;

    private ProjectDAO projectDAO;
    private int currentUserId; // ID de l'utilisateur connecté (à définir selon votre système d'authentification)
    private List<Project> allProjects;
    private String currentFilter = "ALL";

    public void initialize() {
        projectDAO = new ProjectDAO();
        currentUserId = 1; // À remplacer par l'ID de l'utilisateur connecté

        loadProjects();
        setupSearchListener();
    }

    /**
     * Définir l'ID de l'utilisateur connecté
     */
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    /**
     * Charger tous les projets
     */
    private void loadProjects() {
        allProjects = projectDAO.getAllProjects();
        displayProjects(allProjects);
    }

    /**
     * Afficher les projets dans la grille
     */
    private void displayProjects(List<Project> projects) {
        gridProjets.getChildren().clear();

        if (projects.isEmpty()) {
            showEmptyState();
            return;
        }

        int col = 0;
        int row = 0;
        int maxCols = 4; // MODIFIÉ : 3 colonnes au lieu de 2

        for (Project project : projects) {
            VBox projectCard = createProjectCard(project);

            // On s'assure que la carte prend une largeur raisonnable pour 3 colonnes
            projectCard.setPrefWidth(250);

            gridProjets.add(projectCard, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Créer une carte de projet
     */
    private VBox createProjectCard(Project project) {
        VBox card = new VBox(15);
        card.getStyleClass().add("project-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(350);
        card.setMinHeight(200);

        // En-tête avec statut
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Circle statusDot = new Circle(6);
        statusDot.getStyleClass().add("status-dot");

        switch (project.getStatus()) {
            case "ACTIVE":
                statusDot.getStyleClass().add("status-active");
                break;
            case "COMPLETED":
                statusDot.getStyleClass().add("status-completed");
                break;
            case "ARCHIVED":
                statusDot.getStyleClass().add("status-paused");
                break;
        }

        Label statusLabel = new Label(project.getStatusDisplay());
        statusLabel.getStyleClass().add("status-label");

        header.getChildren().addAll(statusDot, statusLabel);

        // Nom du projet
        Label nameLabel = new Label(project.getName());
        nameLabel.getStyleClass().add("project-name");
        nameLabel.setWrapText(true);

        // Code du projet
        Label codeLabel = new Label(project.getCode());
        codeLabel.getStyleClass().add("tasks-label");

        // Description
        Label descLabel = new Label(project.getDescription());
        descLabel.getStyleClass().add("tasks-label");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(40);

        // Progression
        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label progressLabel = new Label("PROGRESSION");
        progressLabel.getStyleClass().add("progress-label");

        Label progressValue = new Label(String.format("%.0f%%", project.getProgress()));
        progressValue.getStyleClass().add("progress-value");

        progressHeader.getChildren().addAll(progressLabel, spacer, progressValue);

        ProgressBar progressBar = new ProgressBar(project.getProgress() / 100.0);
        progressBar.getStyleClass().add("project-progress");
        progressBar.setPrefWidth(Double.MAX_VALUE);

        // Nombre de tâches
        Label tasksLabel = new Label(project.getTaskCount() + " tâches");
        tasksLabel.getStyleClass().add("tasks-label");

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(
                header,
                nameLabel,
                codeLabel,
                descLabel,
                progressHeader,
                progressBar,
                tasksLabel
        );

        // Rendre la carte cliquable
        card.setOnMouseClicked(event -> handleProjectClick(project));
        card.getStyleClass().add("clickable");

        return card;
    }

    /**
     * Afficher un état vide
     */
    private void showEmptyState() {
        VBox emptyState = new VBox(20);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(50));

        Label emptyLabel = new Label("Aucun projet trouvé");
        emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #6b7280; -fx-font-weight: bold;");

        Label emptySubLabel = new Label("Créez votre premier projet pour commencer");
        emptySubLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #9ca3af;");

        emptyState.getChildren().addAll(emptyLabel, emptySubLabel);
        gridProjets.add(emptyState, 0, 0, 2, 1);
    }

    /**
     * Gérer le clic sur un projet
     */
    private void handleProjectClick(Project project) {
        System.out.println("Projet cliqué : " + project.getName());
        // TODO: Ouvrir la page de détails du projet
    }

    /**
     * Gérer l'ajout d'un nouveau projet
     */
    @FXML
    private void handleAddProject() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addProject.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur
            AddProjectController controller = loader.getController();
            controller.setCurrentUserId(currentUserId);
            controller.setOnProjectAdded(this::loadProjects);

            // Créer une nouvelle fenêtre modale
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            // 1. CHANGER ICI : Passer en style TRANSPARENT pour permettre les arrondis
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.setTitle("Nouveau Projet");

            // 2. CONFIGURER LA SCÈNE
            Scene scene = new Scene(root);
            // Rendre le fond de la scène transparent pour voir les coins arrondis du CSS
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            stage.setScene(scene);

            // Centrer la fenêtre
            stage.centerOnScreen();

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }
    /**
     * Configurer le listener de recherche
     */
    private void setupSearchListener() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                applyFilter(currentFilter);
            } else {
                searchProjects(newValue.trim());
            }
        });
    }

    /**
     * Rechercher des projets
     */
    private void searchProjects(String searchText) {
        List<Project> results = projectDAO.searchProjectsByName(searchText);
        displayProjects(results);
    }

    /**
     * Filtrer tous les projets
     */
    @FXML
    private void filterAll() {
        currentFilter = "ALL";
        applyFilter("ALL");
        updateFilterButtons("ALL");
    }

    /**
     * Filtrer les projets en cours
     */
    @FXML
    private void filterEnCours() {
        currentFilter = "ACTIVE";
        applyFilter("ACTIVE");
        updateFilterButtons("ACTIVE");
    }

    /**
     * Filtrer les projets terminés
     */
    @FXML
    private void filterTermines() {
        currentFilter = "COMPLETED";
        applyFilter("COMPLETED");
        updateFilterButtons("COMPLETED");
    }

    /**
     * Filtrer les projets en pause
     */
    @FXML
    private void filterEnPause() {
        currentFilter = "ARCHIVED";
        applyFilter("ARCHIVED");
        updateFilterButtons("ARCHIVED");
    }

    /**
     * Appliquer un filtre
     */
    private void applyFilter(String status) {
        List<Project> filteredProjects;

        if ("ALL".equals(status)) {
            filteredProjects = projectDAO.getAllProjects();
        } else {
            filteredProjects = projectDAO.getProjectsByStatus(status);
        }

        displayProjects(filteredProjects);
    }

    /**
     * Mettre à jour l'apparence des boutons de filtre
     */
    private void updateFilterButtons(String activeFilter) {
        // Liste de tous les boutons pour simplifier le traitement
        List<Button> buttons = Arrays.asList(btnFilterAll, btnFilterEnCours, btnFilterTermines, btnFilterEnPause);

        // On retire la classe active de tous les boutons
        for (Button btn : buttons) {
            btn.getStyleClass().remove("filter-active");
        }

        // On ajoute la classe active au bouton correspondant
        switch (activeFilter) {
            case "ALL": btnFilterAll.getStyleClass().add("filter-active"); break;
            case "ACTIVE": btnFilterEnCours.getStyleClass().add("filter-active"); break;
            case "COMPLETED": btnFilterTermines.getStyleClass().add("filter-active"); break;
            case "ARCHIVED": btnFilterEnPause.getStyleClass().add("filter-active"); break;
        }
    }

    /**
     * Afficher une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleOpenJoinModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/joinProject.fxml"));
            Parent root = loader.load();

            JoinProjectController controller = loader.getController();
            controller.init(currentUserId, this::loadProjects);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}