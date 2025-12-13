package controllers;

import dao.ProjectDAO;
import dao.TaskDAO;
import models.Project;
import models.Task;
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
import java.text.SimpleDateFormat;
import java.util.List;
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

    private TaskDAO taskDAO;
    private ProjectDAO projectDAO;
    private String currentStatusFilter = "Tous"; // Tous, ToDo, InProgress, Done

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les DAOs
        taskDAO = new TaskDAO();
        projectDAO = new ProjectDAO();

        lblUsername.setText("Amine Lamaizi");

        if (profileCircle != null) {
            profileCircle.setOnMouseClicked(e -> handleProfileClick());
            profileCircle.getStyleClass().add("clickable");
        }

        // Initialiser les ComboBox
        cboPriorite.getItems().addAll("Toutes", "Haute", "Moyenne", "Basse");
        cboPriorite.setValue("Toutes");

        // Charger les projets dans le ComboBox
        loadProjectsComboBox();

        // Listeners pour les filtres
        cboPriorite.setOnAction(e -> applyFilters());
        cboProjet.setOnAction(e -> applyFilters());

        // Charger les statistiques
        loadStatistics();

        // Charger les tâches
        loadTasks();
    }

    private void loadProjectsComboBox() {
        cboProjet.getItems().clear();
        cboProjet.getItems().add("Tous les projets");

        List<Project> projects = projectDAO.getAllProjects();
        for (Project project : projects) {
            cboProjet.getItems().add(project.getName());
        }

        cboProjet.setValue("Tous les projets");
    }

    private void loadStatistics() {
        // Compter les tâches par statut
        int todoCount = taskDAO.countTasksByStatus("ToDo");
        int inProgressCount = taskDAO.countTasksByStatus("InProgress");
        int completedCount = taskDAO.countTasksByStatus("Done");

        lblTodoCount.setText(String.valueOf(todoCount));
        lblInProgressCount.setText(String.valueOf(inProgressCount));
        lblCompletedCount.setText(String.valueOf(completedCount));
    }

    private void loadTasks() {
        vboxTasks.getChildren().clear();

        // Récupérer les tâches selon le filtre actuel
        List<Task> tasks;

        if (currentStatusFilter.equals("Tous")) {
            tasks = taskDAO.getAllTasks();
        } else {
            tasks = taskDAO.getTasksByStatus(currentStatusFilter);
        }

        if (tasks.isEmpty()) {
            Label noTasks = new Label("Aucune tâche trouvée");
            noTasks.getStyleClass().add("no-tasks-label");
            vboxTasks.getChildren().add(noTasks);
        } else {
            for (Task task : tasks) {
                vboxTasks.getChildren().add(createTaskCard(task));
            }
        }
    }

    private void applyFilters() {
        vboxTasks.getChildren().clear();

        String priority = cboPriorite.getValue();
        String projectName = cboProjet.getValue();

        // Convertir le nom du projet en ID (si ce n'est pas "Tous les projets")
        Integer projectId = null;
        if (!projectName.equals("Tous les projets")) {
            List<Project> projects = projectDAO.getAllProjects();
            for (Project p : projects) {
                if (p.getName().equals(projectName)) {
                    projectId = p.getId();
                    break;
                }
            }
        }

        // Appliquer les filtres
        List<Task> tasks = taskDAO.filterTasks(currentStatusFilter, priority, projectId);

        if (tasks.isEmpty()) {
            Label noTasks = new Label("Aucune tâche trouvée avec ces filtres");
            noTasks.getStyleClass().add("no-tasks-label");
            vboxTasks.getChildren().add(noTasks);
        } else {
            for (Task task : tasks) {
                vboxTasks.getChildren().add(createTaskCard(task));
            }
        }
    }

    private HBox createTaskCard(Task task) {
        HBox card = new HBox(15);
        card.getStyleClass().add("task-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));

        // Checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setSelected(task.isCompleted());
        checkBox.setOnAction(e -> toggleTaskStatus(task.getId(), checkBox.isSelected()));

        // Info principale
        VBox mainInfo = new VBox(8);
        HBox.setHgrow(mainInfo, Priority.ALWAYS);

        // Titre
        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title");
        if (task.isCompleted()) {
            titleLabel.getStyleClass().add("task-completed");
        }

        // Détails (projet + date)
        HBox details = new HBox(15);
        details.setAlignment(Pos.CENTER_LEFT);

        String projectName = task.getProjectName() != null ? task.getProjectName() : "Sans projet";
        Label projectLabel = new Label("📁 " + projectName);
        projectLabel.getStyleClass().add("task-project");

        String dateStr = formatDate(task.getCreatedAt());
        Label dateLabel = new Label("📅 " + dateStr);
        dateLabel.getStyleClass().add("task-date");

        details.getChildren().addAll(projectLabel, dateLabel);
        mainInfo.getChildren().addAll(titleLabel, details);

        // Badge priorité
        Label priorityBadge = new Label(task.getPriorityDisplay());
        priorityBadge.getStyleClass().add("priority-badge");
        priorityBadge.getStyleClass().add(getPriorityClass(task.getPriority()));
        priorityBadge.setPadding(new Insets(5, 12, 5, 12));

        // Badge statut
        Label statusBadge = new Label(task.getStatusDisplay());
        statusBadge.getStyleClass().add("status-badge");
        statusBadge.getStyleClass().add(getStatusBadgeClass(task.getStatus()));
        statusBadge.setPadding(new Insets(5, 12, 5, 12));

        // Bouton actions
        Button btnAction = new Button("•••");
        btnAction.getStyleClass().add("btn-action");
        btnAction.setOnAction(e -> showTaskMenu(task));

        card.getChildren().addAll(checkBox, mainInfo, priorityBadge, statusBadge, btnAction);

        return card;
    }

    private String getPriorityClass(int priority) {
        if (priority >= 4) {
            return "priority-high";
        } else if (priority >= 2) {
            return "priority-medium";
        } else {
            return "priority-low";
        }
    }

    private String getStatusBadgeClass(String status) {
        switch (status) {
            case "ToDo": return "status-todo";
            case "InProgress": return "status-inprogress";
            case "Done": return "status-done";
            case "Canceled": return "status-canceled";
            default: return "status-todo";
        }
    }

    private String formatDate(java.util.Date date) {
        if (date == null) {
            return "Date inconnue";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private void toggleTaskStatus(int taskId, boolean completed) {
        if (taskDAO.toggleTaskCompletion(taskId, completed)) {
            loadStatistics();
            applyFilters();
            showSuccessMessage("Statut de la tâche mis à jour !");
        } else {
            showErrorAlert("Erreur", "Impossible de mettre à jour le statut de la tâche.");
        }
    }

    private void showTaskMenu(Task task) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Modifier");
        editItem.setOnAction(e -> editTask(task));

        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(e -> deleteTask(task));

        contextMenu.getItems().addAll(editItem, deleteItem);
        contextMenu.show(vboxTasks.getScene().getWindow());
    }

    private void editTask(Task task) {
        System.out.println("Modifier la tâche: " + task.getTitle());
        // TODO: Ouvrir un dialogue d'édition
    }

    private void deleteTask(Task task) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la tâche");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette tâche ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (taskDAO.deleteTask(task.getId())) {
                    showSuccessMessage("Tâche supprimée avec succès !");
                    loadStatistics();
                    applyFilters();
                } else {
                    showErrorAlert("Erreur", "Impossible de supprimer la tâche.");
                }
            }
        });
    }

    @FXML
    private void handleAddTask() {
        // Dialogue simple pour ajouter une tâche
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Tâche");
        dialog.setHeaderText("Créer une nouvelle tâche");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Titre");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);

        ComboBox<String> projectCombo = new ComboBox<>();
        List<Project> projects = projectDAO.getAllProjects();
        for (Project p : projects) {
            projectCombo.getItems().add(p.getName());
        }
        if (!projects.isEmpty()) {
            projectCombo.setValue(projects.get(0).getName());
        }

        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("Basse (1)", "Moyenne (3)", "Haute (5)");
        priorityCombo.setValue("Moyenne (3)");

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Projet:"), 0, 2);
        grid.add(projectCombo, 1, 2);
        grid.add(new Label("Priorité:"), 0, 3);
        grid.add(priorityCombo, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Task newTask = new Task();
                newTask.setTitle(titleField.getText());
                newTask.setDescription(descField.getText());
                newTask.setStatus("ToDo");

                // Trouver l'ID du projet
                String selectedProject = projectCombo.getValue();
                for (Project p : projects) {
                    if (p.getName().equals(selectedProject)) {
                        newTask.setProjectId(p.getId());
                        break;
                    }
                }

                // Convertir la priorité
                String priorityStr = priorityCombo.getValue();
                if (priorityStr.contains("5")) newTask.setPriority(5);
                else if (priorityStr.contains("3")) newTask.setPriority(3);
                else newTask.setPriority(1);

                return newTask;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(task -> {
            if (taskDAO.addTask(task)) {
                showSuccessMessage("Tâche créée avec succès !");
                loadStatistics();
                applyFilters();
            } else {
                showErrorAlert("Erreur", "Impossible de créer la tâche.");
            }
        });
    }

    @FXML
    private void showAllTasks() {
        currentStatusFilter = "Tous";
        applyFilters();
    }

    @FXML
    private void showTodoTasks() {
        currentStatusFilter = "ToDo";
        applyFilters();
    }

    @FXML
    private void showInProgressTasks() {
        currentStatusFilter = "InProgress";
        applyFilters();
    }

    @FXML
    private void showCompletedTasks() {
        currentStatusFilter = "Done";
        applyFilters();
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

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setHeaderText(message);
        alert.show();
    }
}