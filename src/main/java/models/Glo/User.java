package models.Glo;

public class User {
    private String username;
    private String role;
    private String id;

    public User(String name, String id, String role) {
        this.username = name;
        this.role = role;
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

