package controllers;

import dao.ProjectDAO;
import dao.MemberDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Project;

public class JoinProjectController {
    @FXML private VBox paneSearch, panePreview;
    @FXML private TextField txtJoinCode;
    @FXML private Label lblProjectName, lblProjectDesc, lblError;

    private ProjectDAO projectDAO = new ProjectDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private Project foundProject;
    private int userId;
    private Runnable onJoined;

    public void init(int userId, Runnable onJoined) {
        this.userId = userId;
        this.onJoined = onJoined;
    }

    @FXML
    private void handleVerify() {
        String code = txtJoinCode.getText().trim();
        foundProject = projectDAO.getProjectByCode(code);

        if (foundProject != null) {
            lblError.setVisible(false);
            showPreview();
        } else {
            lblError.setVisible(true);
        }
    }

    private void showPreview() {
        lblProjectName.setText(foundProject.getName());
        lblProjectDesc.setText(foundProject.getDescription());
        paneSearch.setVisible(false); paneSearch.setManaged(false);
        panePreview.setVisible(true); panePreview.setManaged(true);
    }

    @FXML
    private void handleJoin() {
        if (memberDAO.isUserAlreadyMember(foundProject.getId(), userId)) {
            new Alert(Alert.AlertType.WARNING, "Vous faites déjà partie de ce projet.").show();
            return;
        }

        if (memberDAO.addMemberToProject(foundProject.getId(), userId, "MEMBRE")) {
            if (onJoined != null) onJoined.run();
            handleClose();
        } else {
            // Optionnel : Afficher une alerte si ça échoue
            System.err.println("Échec de l'adhésion au projet.");
        }
    }

    @FXML private void handleBack() {
        panePreview.setVisible(false); panePreview.setManaged(false);
        paneSearch.setVisible(true); paneSearch.setManaged(true);
    }

    @FXML private void handleClose() {
        ((Stage) txtJoinCode.getScene().getWindow()).close();
    }
}