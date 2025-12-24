package controllers;

import dao.ProjectDAO;
import dao.TaskDAO;
import models.Project;
import models.Task;
import utils.AnimatedAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class tachesController implements Initializable {


   
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

    @FXML private StackPane rootPane;
    @FXML private VBox addTaskPopup;

    @FXML private TextField txtTitle;
    @FXML private TextArea txtDescription;
    @FXML private ComboBox<String> popupProject;
    @FXML private ComboBox<String> popupPriority;

    @FXML private Region backgroundRect;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les DAOs
        taskDAO = new TaskDAO();
        projectDAO = new ProjectDAO();


        

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
            //showSuccessMessage("Statut de la tâche mis à jour !");

            // AnimatedAlert alert = new AnimatedAlert("Statut de la tâche mis à jour !") ;
            // alert.show(rootPane);
            AnimatedAlert alert =
        new AnimatedAlert("Statut de la tâche mis à jour !",AnimatedAlert.AlertType.SUCCESS);

alert.show(rootPane);


        } else {
            // showErrorAlert("Erreur", "Impossible de mettre à jour le statut de la tâche.");
            //AnimatedAlert alert = new AnimatedAlert("Impossible de mettre à jour le statut de la tâche.") ;
            AnimatedAlert alert = new AnimatedAlert("Impossible de mettre à jour la tâche",AnimatedAlert.AlertType.ERROR);

            alert.show(rootPane);
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
        popupProject.getItems().clear();
        for (Project p : projectDAO.getAllProjects()) {
            popupProject.getItems().add(p.getName());
        }

        popupPriority.getItems().setAll("Basse", "Moyenne", "Haute");
        popupPriority.setValue("Moyenne");


        backgroundRect.prefWidthProperty().bind(rootPane.widthProperty());
        backgroundRect.prefHeightProperty().bind(rootPane.heightProperty());

        addTaskPopup.setVisible(true);
        addTaskPopup.setManaged(true);
    }


    @FXML
    private void closeAddTaskPopup() {
        addTaskPopup.setVisible(false);
        addTaskPopup.setManaged(false);
    }

    @FXML
    private void confirmAddTask() {
        Task task = new Task();
        task.setTitle(txtTitle.getText());
        task.setDescription(txtDescription.getText());
        task.setStatus("ToDo");

        taskDAO.addTask(task);

        closeAddTaskPopup();
        loadStatistics();
        applyFilters();
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