package models.Glo;

public class User {
    private String username;
    private String role;
    private String id;

    public User(String id) {
        this.id = id;
    }

    public User(String username, String id, String role) {
        this(id);
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

