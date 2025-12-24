package controllers;

import database.tables.UserTable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.UserSession;
import utils.AnimatedAlert;

public class SettingsController {

    // ===== FXML Elements =====
    @FXML private Button togglePasswordFormBtn;
    @FXML private VBox passwordForm;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label passwordErrorLabel;
    @FXML private HBox deleteInitialPane;
    @FXML private VBox deleteConfirmPane;

    @FXML
    public void initialize() {
        // Pas d'initialisation requise ici
    }

    // ================= GESTION DU FORMULAIRE MOT DE PASSE =================
    @FXML
    private void togglePasswordFormVisibility() {
        if (passwordForm.isVisible()) {
            hidePasswordForm();
        } else {
            showPasswordForm();
        }
    }

    private void showPasswordForm() {
        passwordForm.setVisible(true);
        passwordForm.setManaged(true);
        togglePasswordFormBtn.setDisable(true);
    }

    @FXML
    private void hidePasswordForm() {
        passwordForm.setVisible(false);
        passwordForm.setManaged(false);
        togglePasswordFormBtn.setDisable(false);
        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        hideErrorLabel();
    }

    @FXML
    private void handleUpdatePassword() {
        hideErrorLabel();

        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();
        int userId = UserSession.getInstance().getUser().getId();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showErrorLabel("Tous les champs sont obligatoires.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showErrorLabel("Les nouveaux mots de passe ne correspondent pas.");
            return;
        }

        if (!UserTable.verifyPassword(userId, oldPass)) {
            showErrorLabel("L'ancien mot de passe est incorrect.");
            return;
        }

        boolean success = UserTable.updatePassword(userId, newPass);

        if (success) {
            showAnimatedAlert("Mot de passe mis à jour avec succès.", AnimatedAlert.AlertType.SUCCESS);
            hidePasswordForm();
        } else {
            showAnimatedAlert("Une erreur est survenue lors de la mise à jour.", AnimatedAlert.AlertType.ERROR);
        }
    }

    // ================= GESTION DE LA SUPPRESSION DE COMPTE =================
    @FXML
    private void showDeleteConfirmation() {
        deleteInitialPane.setVisible(false);
        deleteInitialPane.setManaged(false);
        deleteConfirmPane.setVisible(true);
        deleteConfirmPane.setManaged(true);
    }

    @FXML
    private void hideDeleteConfirmation() {
        deleteConfirmPane.setVisible(false);
        deleteConfirmPane.setManaged(false);
        deleteInitialPane.setVisible(true);
        deleteInitialPane.setManaged(true);
    }

    @FXML
    private void handleConfirmDelete() {
        int userId = UserSession.getInstance().getUser().getId();
        boolean success = UserTable.deleteUser(userId);

        if (success) {
            UserSession.getInstance().clearSession();

            // === EXCEPTION : Utilisation d'une alerte standard ici ===
            // C'est nécessaire car l'application se ferme juste après,
            // et une alerte animée n'aurait pas le temps de s'afficher.
            Alert finalAlert = new Alert(Alert.AlertType.INFORMATION);
            finalAlert.setTitle("Compte supprimé");
            finalAlert.setHeaderText(null);
            finalAlert.setContentText("Votre compte a été supprimé. L'application va maintenant se fermer.");
            finalAlert.showAndWait(); // Attend que l'utilisateur clique sur OK

            Platform.exit();

        } else {
            // Pour les erreurs, on utilise l'alerte animée.
            showAnimatedAlert("Une erreur est survenue lors de la suppression du compte.", AnimatedAlert.AlertType.ERROR);
            hideDeleteConfirmation();
        }
    }

    // ================= GESTIONNAIRES D'INTERFACE =================
    private void showErrorLabel(String message) {
        passwordErrorLabel.setText(message);
        passwordErrorLabel.setVisible(true);
        passwordErrorLabel.setManaged(true);
    }

    private void hideErrorLabel() {
        passwordErrorLabel.setVisible(false);
        passwordErrorLabel.setManaged(false);
    }

    /**
     * Crée et affiche une instance de votre AnimatedAlert.
     * @param message Le message à afficher dans l'alerte.
     * @param type Le type de l'alerte (SUCCESS, ERROR, INFO).
     */
    private void showAnimatedAlert(String message, AnimatedAlert.AlertType type) {
        try {
            // On récupère le conteneur racine pour que l'alerte flotte au-dessus de tout.
            Pane rootPane = (Pane) passwordForm.getScene().getRoot();
            if (rootPane != null) {
                AnimatedAlert alert = new AnimatedAlert(message, type);
                alert.show(rootPane);
            } else {
                // Si la scène n'est pas prête, on utilise une alerte de secours.
                showAlertFallback(message, type);
            }
        } catch (Exception e) {
            System.err.println("Impossible d'afficher l'alerte animée : " + e.getMessage());
            showAlertFallback(message, type);
        }
    }
    
    /**
     * Affiche une alerte JavaFX standard comme solution de secours.
     */
    private void showAlertFallback(String message, AnimatedAlert.AlertType type) {
        Alert.AlertType fxType = switch (type) {
            case SUCCESS, INFO -> Alert.AlertType.INFORMATION;
            case ERROR -> Alert.AlertType.ERROR;
        };
        Alert alert = new Alert(fxType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}