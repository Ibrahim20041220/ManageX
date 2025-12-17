package models;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String profession;
    private String email;
    private String phone;
    private String profilePic;

    public User(int id,
                String firstName,
                String lastName,
                String email,
                String phone,
                String profession,
                String profilePic) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
        this.email = email;
        this.phone = phone;
        this.profilePic = profilePic;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfession() {
        return profession;
    }

   

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
