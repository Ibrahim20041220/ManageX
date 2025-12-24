package models;

/**
 * Classe simple pour transporter les informations des membres du projet
 */
public class ProjectMemberInfo {
    private String fullName;
    private String roleName;

    public ProjectMemberInfo(String fullName, String roleName) {
        this.fullName = fullName;
        this.roleName = roleName;
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getRoleName() { return roleName; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}