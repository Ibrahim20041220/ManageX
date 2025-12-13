package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profileController implements Initializable {

    @FXML private Button btnHome;
    @FXML private Button btnProjets;
    @FXML private Button btnTaches;
    @FXML private Label lblUsername;
    @FXML private Circle profileCircle;
    @FXML private Label lblFullName;
    @FXML private Label lblEmail;
    @FXML private Label lblRole;
    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtDepartment;
    @FXML private VBox vboxInfoTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger les données du profil
        loadProfileData();
    }

    private void loadProfileData() {
        // TODO: Récupérer les vraies données depuis la session/base de données
        String fullName = "Amine Lamaizi";
        String email = "amine.lamaizi@example.com";
        String role = "Chef de Projet";

        lblUsername.setText(fullName);
        lblFullName.setText(fullName);
        lblEmail.setText(email);
        lblRole.setText(role);

        txtFullName.setText(fullName);
        txtEmail.setText(email);
        txtPhone.setText("+212 6 12 34 56 78");
        txtDepartment.setText("Développement");
    }

    @FXML
    private void handleChangePhoto() {
        System.out.println("Changer la photo de profil");
        // TODO: Ouvrir un FileChooser pour sélectionner une image
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Changer la photo");
        alert.setHeaderText(null);
        alert.setContentText("Fonctionnalité en cours de développement");
        alert.showAndWait();
    }

    @FXML
    private void handleEditProfile() {
        System.out.println("Modifier le profil");

        // Activer l'édition des champs
        boolean isEditable = !txtFullName.isEditable();

        txtFullName.setEditable(isEditable);
        txtEmail.setEditable(isEditable);
        txtPhone.setEditable(isEditable);
        txtDepartment.setEditable(isEditable);

        if (isEditable) {
            // Mode édition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mode édition");
            alert.setHeaderText(null);
            alert.setContentText("Vous pouvez maintenant modifier vos informations.\nCliquez à nouveau pour sauvegarder.");
            alert.showAndWait();
        } else {
            // Sauvegarder les modifications
            saveProfileChanges();
        }
    }

    private void saveProfileChanges() {
        // TODO: Sauvegarder les modifications dans la base de données
        String newName = txtFullName.getText();
        String newEmail = txtEmail.getText();
        String newPhone = txtPhone.getText();
        String newDepartment = txtDepartment.getText();

        // Mettre à jour l'affichage
        lblFullName.setText(newName);
        lblEmail.setText(newEmail);
        lblUsername.setText(newName);

        System.out.println("Profil mis à jour:");
        System.out.println("Nom: " + newName);
        System.out.println("Email: " + newEmail);
        System.out.println("Téléphone: " + newPhone);
        System.out.println("Département: " + newDepartment);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Vos modifications ont été enregistrées avec succès!");
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Déconnexion");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir vous déconnecter?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Déconnexion...");
                // TODO: Nettoyer la session
                // TODO: Rediriger vers la page de connexion
                navigateToPage("/views/signIn.fxml", "Connexion - ManageX");
            }
        });
    }

    @FXML
    private void showInfoTab() {
        System.out.println("Afficher onglet Informations");
        vboxInfoTab.setVisible(true);
        vboxInfoTab.setManaged(true);
    }

    @FXML
    private void showActivityTab() {
        System.out.println("Afficher onglet Activité");
        // TODO: Créer et afficher le contenu de l'onglet Activité
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Activité");
        alert.setHeaderText(null);
        alert.setContentText("Fonctionnalité en cours de développement");
        alert.showAndWait();
    }

    @FXML
    private void showSettingsTab() {
        System.out.println("Afficher onglet Paramètres");
        // TODO: Créer et afficher le contenu de l'onglet Paramètres
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Paramètres");
        alert.setHeaderText(null);
        alert.setContentText("Fonctionnalité en cours de développement");
        alert.showAndWait();
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
        navigateToPage("/views/taches.fxml", "Tâches - ManageX");
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