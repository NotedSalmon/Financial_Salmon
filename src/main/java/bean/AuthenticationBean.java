package bean;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@Named
@SessionScoped
public class AuthenticationBean implements Serializable {

    private String name;
    private String username;
    private String email;
    private String password;

    private boolean loggedIn = false;

    public String register() {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(username);

        try {
            if (docRef.get().get().exists()) {
                return "User already exists"; // redirect or show error
            }

            String hashedPassword = hashPassword(password);

            docRef.set(new User(name, username, email, hashedPassword));
            return "login.xhtml?faces-redirect=true";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "error.xhtml";
        }
    }

    public String login() {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(username);

        try {
            DocumentSnapshot snapshot = docRef.get().get();
            if (!snapshot.exists()) {
                return "login.xhtml?error=usernotfound";
            }

            User user = snapshot.toObject(User.class);
            if (user != null && user.getPassword().equals(hashPassword(password))) {
                this.name = user.getName();
                this.username = user.getUsername();
                loggedIn = true;

                HttpSession session = (HttpSession) FacesContext.
                        getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute("username", this.username);
                session.setAttribute("user", user);

                return "home.xhtml?faces-redirect=true";
            } else {
                return "login.xhtml?error=invalid";
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "error.xhtml";
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isLoggedIn() { return loggedIn; }
}
