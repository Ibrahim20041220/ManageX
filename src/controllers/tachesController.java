package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class tachesController implements Initializable {

    @FXML private Button btnHome;
    @FXML private Button btnProjets;
    @FXML private Button btnTaches;
    @FXML private Label lblUsername;
    @FXML private Circle profileCircle;
    @FXML private ComboBox<String> cboPriorite;
    @FXML private ComboBox<String> cboProjet;
    @FXML private Button btnAddTask;
    @FXML private Label lblTodoCount;
    @FXML private Label lblInProgressCount;
    @FXML private Label lblCompletedCount;
    @FXML private VBox vboxTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsername.setText("Amine Lamaizi");

        if (profileCircle != null) {
            profileCircle.setOnMouseClicked(e -> handleProfileClick());
            profileCircle.getStyleClass().add("clickable");
        }

        // Initialiser les ComboBox
        cboPriorite.getItems().addAll("Toutes", "Haute", "Moyenne", "Basse");
        cboPriorite.setValue("Toutes");

        cboProjet.getItems().addAll("Tous les projets", "Application Mobile", "Site Web", "Base de Données");
        cboProjet.setValue("Tous les projets");

        loadTasks();
    }

    private void loadTasks() {
        vboxTasks.getChildren().clear();

        // Données factices - TODO: récupérer de la base de données
        String[][] tasks = {
                {"Conception de l'interface utilisateur", "Application Mobile", "Haute", "À faire", "2024-01-15"},
                {"Développement API REST", "Application Mobile", "Haute", "En cours", "2024-01-20"},
                {"Tests unitaires", "Application Mobile", "Moyenne", "À faire", "2024-01-18"},
                {"Refonte page d'accueil", "Site Web", "Moyenne", "En cours", "2024-01-16"},
                {"Optimisation des requêtes", "Base de Données", "Haute", "En cours", "2024-01-14"},
                {"Documentation technique", "Application Mobile", "Basse", "À faire", "2024-01-25"},
                {"Migration des données", "Base de Données", "Haute", "Terminée", "2024-01-10"},
                {"Design système", "Application Mobile", "Moyenne", "Terminée", "2024-01-08"}
        };

        for (String[] task : tasks) {
            vboxTasks.getChildren().add(createTaskCard(task[0], task[1], task[2], task[3], task[4]));
        }
    }

    private HBox createTaskCard(String title, String project, String priority, String status, String date) {
        HBox card = new HBox(15);
        card.getStyleClass().add("task-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));

        // Checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setSelected(status.equals("Terminée"));
        checkBox.setOnAction(e -> toggleTaskStatus(title, checkBox.isSelected()));

        // Info principale
        VBox mainInfo = new VBox(8);
        HBox.setHgrow(mainInfo, Priority.ALWAYS);

        // Titre
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("task-title");
        if (status.equals("Terminée")) {
            titleLabel.getStyleClass().add("task-completed");
        }

        // Détails (projet + date)
        HBox details = new HBox(15);
        details.setAlignment(Pos.CENTER_LEFT);

        Label projectLabel = new Label("📁 " + project);
        projectLabel.getStyleClass().add("task-project");

        Label dateLabel = new Label("📅 " + date);
        dateLabel.getStyleClass().add("task-date");

        details.getChildren().addAll(projectLabel, dateLabel);
        mainInfo.getChildren().addAll(titleLabel, details);

        // Badge priorité
        Label priorityBadge = new Label(priority);
        priorityBadge.getStyleClass().add("priority-badge");
        priorityBadge.getStyleClass().add(getPriorityClass(priority));
        priorityBadge.setPadding(new Insets(5, 12, 5, 12));

        // Badge statut
        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().add("status-badge");
        statusBadge.getStyleClass().add(getStatusBadgeClass(status));
        statusBadge.setPadding(new Insets(5, 12, 5, 12));

        // Bouton actions
        Button btnAction = new Button("•••");
        btnAction.getStyleClass().add("btn-action");
        btnAction.setOnAction(e -> showTaskMenu(title));

        card.getChildren().addAll(checkBox, mainInfo, priorityBadge, statusBadge, btnAction);

        return card;
    }

    private String getPriorityClass(String priority) {
        switch (priority.toLowerCase()) {
            case "haute": return "priority-high";
            case "moyenne": return "priority-medium";
            case "basse": return "priority-low";
            default: return "priority-medium";
        }
    }

    private String getStatusBadgeClass(String status) {
        switch (status.toLowerCase()) {
            case "à faire": return "status-todo";
            case "en cours": return "status-inprogress";
            case "terminée": return "status-done";
            default: return "status-todo";
        }
    }

    private void toggleTaskStatus(String taskTitle, boolean completed) {
        System.out.println("Tâche " + taskTitle + " - Terminée: " + completed);
        // TODO: Mettre à jour dans la base de données
        loadTasks(); // Recharger pour mettre à jour l'affichage
    }

    private void showTaskMenu(String taskTitle) {
        System.out.println("Menu pour: " + taskTitle);
        // TODO: Afficher un menu contextuel (éditer, supprimer, etc.)
    }

    @FXML
    private void handleAddTask() {
        System.out.println("Ajouter une nouvelle tâche");
        // TODO: Ouvrir un dialogue
    }

    @FXML
    private void showAllTasks() {
        System.out.println("Afficher toutes les tâches");
        loadTasks();
    }

    @FXML
    private void showTodoTasks() {
        System.out.println("Afficher tâches à faire");
    }

    @FXML
    private void showInProgressTasks() {
        System.out.println("Afficher tâches en cours");
    }

    @FXML
    private void showCompletedTasks() {
        System.out.println("Afficher tâches terminées");
    }

    @FXML
    private void handleHomeClick() {
        navigateToPage("/views/home.fxml", "Home - ManageX");
    }

    @FXML
    private void handleProjetsClick() {
        navigateToPage("/views/projets.fxml", "Projets - ManageX");
    }

    @FXML
    private void handleTachesClick() {
        // Déjà sur cette page
    }

    @FXML
    private void handleProfileClick() {
        navigateToPage("/views/profile.fxml", "Profile - ManageX");
    }

    private void navigateToPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur de navigation", "Impossible de charger la page.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}