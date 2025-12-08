package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProjets;

    @FXML
    private Button btnTaches;

    @FXML
    private Button btnProfile;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblProjetsCount;

    @FXML
    private Label lblTachesCount;

    @FXML
    private Label lblMembresCount;

    @FXML
    private VBox vboxProjetsRecents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        // TODO: Récupérer les vraies données depuis la base de données
        lblProjetsCount.setText("5");
        lblTachesCount.setText("12");
        lblMembresCount.setText("8");
    }

    private void loadRecentProjects() {
        // Vider la liste
        vboxProjetsRecents.getChildren().clear();

        // TODO: Récupérer les vrais projets depuis la base de données
        // Exemple de projets factices
        String[] projets = {
                "Développement Application Mobile",
                "Refonte Site Web",
                "Migration Base de Données"
        };

        for (String projetNom : projets) {
            vboxProjetsRecents.getChildren().add(createProjectCard(projetNom));
        }

        // Si aucun projet, afficher un message
        if (projets.length == 0) {
            Label noProjects = new Label("Aucun projet récent");
            noProjects.getStyleClass().add("no-projects-label");
            vboxProjetsRecents.getChildren().add(noProjects);
        }
    }


    private Pane createProjectCard(String projectName) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("project-card");
        card.setPadding(new Insets(15));

        // Indicateur de statut
        Pane statusIndicator = new Pane();
        statusIndicator.getStyleClass().add("status-indicator");
        statusIndicator.setPrefSize(10, 10);
        statusIndicator.setMaxSize(10, 10);

        // Informations du projet
        VBox projectInfo = new VBox(5);

        Label nameLabel = new Label(projectName);
        nameLabel.getStyleClass().add("project-name");

        Label dateLabel = new Label("Dernière mise à jour: Aujourd'hui");
        dateLabel.getStyleClass().add("project-date");

        projectInfo.getChildren().addAll(nameLabel, dateLabel);
        HBox.setHgrow(projectInfo, javafx.scene.layout.Priority.ALWAYS);

        // Bouton d'action
        Button btnView = new Button("Voir");
        btnView.getStyleClass().add("btn-view-project");
        btnView.setOnAction(e -> viewProject(projectName));

        card.getChildren().addAll(statusIndicator, projectInfo, btnView);

        return card;
    }


    @FXML
    private void handleHomeClick() {
        setActiveButton(btnHome);
        // TODO: Navigation vers la page Home (déjà sur cette page)
        System.out.println("Navigation: Home");
    }


    @FXML
    private void handleProjetsClick() {
        setActiveButton(btnProjets);
        // TODO: Navigation vers la page Projets
        System.out.println("Navigation: Projets");
    }


    @FXML
    private void handleTachesClick() {
        setActiveButton(btnTaches);
        // TODO: Navigation vers la page Tâches
        System.out.println("Navigation: Tâches");
    }


    @FXML
    private void handleProfileClick() {
        setActiveButton(btnProfile);
        // TODO: Navigation vers la page Profile
        System.out.println("Navigation: Profile");
    }


    private void setActiveButton(Button activeBtn) {
        // Retirer la classe active de tous les boutons
        btnHome.getStyleClass().remove("active");
        btnProjets.getStyleClass().remove("active");
        btnTaches.getStyleClass().remove("active");
        btnProfile.getStyleClass().remove("active");

        // Ajouter la classe active au bouton sélectionné
        activeBtn.getStyleClass().add("active");
    }


    private void viewProject(String projectName) {
        // TODO: Implémenter l'affichage des détails du projet
        System.out.println("Affichage du projet: " + projectName);
    }
}