package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import models.Project;
import dao.ProjectDAO;
import java.util.List;
import models.ProjectMemberInfo;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.List;
import models.Project;
import models.ProjectMemberInfo;
import dao.ProjectDAO;

public class ProjectDetailsController {
    @FXML private Label lblFullProjectName, lblProjectCode, lblFullDescription, lblDetailStatus, lblDetailEndDate;
    @FXML private FlowPane flowMembers;

    public void setProjectData(Project project) {
        lblFullProjectName.setText(project.getName());
        lblProjectCode.setText("CODE : " + project.getCode());
        lblFullDescription.setText(project.getDescription());
        lblDetailStatus.setText(project.getStatus());
        lblDetailEndDate.setText(project.getEndDate() != null ? project.getEndDate().toString() : "Pas d'échéance");

        // Charger les membres
        ProjectDAO dao = new ProjectDAO();
        List<ProjectMemberInfo> members = dao.getProjectMembersFullDetails(project.getId());

        flowMembers.getChildren().clear();

        for (ProjectMemberInfo member : members) {
            HBox memberChip = new HBox(10);
            memberChip.setAlignment(Pos.CENTER_LEFT);
            memberChip.setStyle("-fx-background-color: #f8fafc; -fx-padding: 8 15; -fx-background-radius: 10; -fx-border-color: #e2e8f0; -fx-border-radius: 10;");

            // Utilisation des getters : getFullName() et getRoleName()
            Label nameLabel = new Label(member.getFullName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");

            Label roleBadge = new Label(member.getRoleName().toUpperCase());

            if ("ADMIN".equalsIgnoreCase(member.getRoleName())) {
                roleBadge.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #dc2626; -fx-font-size: 10px; -fx-padding: 2 8; -fx-background-radius: 5; -fx-font-weight: bold;");
            } else {
                roleBadge.setStyle("-fx-background-color: #f0fdf4; -fx-text-fill: #16a34a; -fx-font-size: 10px; -fx-padding: 2 8; -fx-background-radius: 5; -fx-font-weight: bold;");
            }

            memberChip.getChildren().addAll(nameLabel, roleBadge);
            flowMembers.getChildren().add(memberChip);
        }
    }


    @FXML private void handleClose() {
        ((Stage) lblFullProjectName.getScene().getWindow()).close();
    }
}