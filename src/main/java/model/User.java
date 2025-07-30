package model;

public class User {
    private String id;
    private String email;
    private String password;
    private String username;
    private String name;

    public User(String name, String username, String email, String hashedPassword) {}

    // Getters and Setters

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}