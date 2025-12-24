package controllers;

import dao.ProjectDAO;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Project;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class AddProjectController {

    @FXML private VBox mainForm;
    @FXML private VBox successOverlay;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker dateEndDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblGeneratedCode;

    @FXML private Label lblNameError;
    @FXML private Label lblDescriptionError;
    @FXML private Label lblEndDateError;

    @FXML private Button btnCancel;

    private ProjectDAO projectDAO;
    private int currentUserId;
    private Runnable onProjectAdded;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    public void initialize() {
        projectDAO = new ProjectDAO();
        cmbStatus.getItems().addAll("ACTIVE", "ARCHIVED", "COMPLETED");
        cmbStatus.setValue("ACTIVE");

        clearErrors();

        // Validation temps réel
        txtName.textProperty().addListener((obs, old, newVal) -> validateField(txtName, lblNameError));
        txtDescription.textProperty().addListener((obs, old, newVal) -> validateField(txtDescription, lblDescriptionError));
        // Permettre de déplacer la fenêtre
        mainForm.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainForm.setOnMouseDragged(event -> {
            Stage stage = (Stage) mainForm.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    public void setCurrentUserId(int userId) { this.currentUserId = userId; }
    public void setOnProjectAdded(Runnable callback) { this.onProjectAdded = callback; }

    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @FXML
    private void handleCopyCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(lblGeneratedCode.getText());
        clipboard.setContent(content);
    }

    @FXML
    private void handleSave() {
        if (!validateForm()) return;

        try {
            String code = generateCode();
            Project project = new Project();
            project.setName(txtName.getText().trim());
            project.setCode(code);
            project.setDescription(txtDescription.getText().trim());
            project.setStatus(cmbStatus.getValue());
            project.setCreatedBy(currentUserId);

            if (dateEndDate.getValue() != null) {
                Date date = Date.from(dateEndDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                project.setEndDate(date);
            }

            if (projectDAO.addProject(project)) {
                if (onProjectAdded != null) onProjectAdded.run();
                showSuccess(code);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showSuccess(String code) {
        lblGeneratedCode.setText(code);
        mainForm.setEffect(new GaussianBlur(15));
        successOverlay.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(400), successOverlay);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private boolean validateForm() {
        boolean v1 = validateField(txtName, lblNameError);
        boolean v2 = validateField(txtDescription, lblDescriptionError);
        return v1 && v2;
    }

    private boolean validateField(TextInputControl f, Label l) {
        boolean ok = !f.getText().trim().isEmpty();
        l.setVisible(!ok);
        f.setStyle(ok ? "" : "-fx-border-color: #ef4444;");
        return ok;
    }

    private void clearErrors() {
        lblNameError.setVisible(false);
        lblDescriptionError.setVisible(false);
        lblEndDateError.setVisible(false);
    }

    @FXML private void handleCancel() { closeWindow(); }
    @FXML private void handleClose() { closeWindow(); }
    private void closeWindow() { ((Stage) btnCancel.getScene().getWindow()).close(); }
}