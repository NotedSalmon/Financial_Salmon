package controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import model.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@SessionScoped
public class BudgetController implements Serializable {
    private String email;
    private String password;
    private String currentUserId;

    public String login() {
        currentUserId = UUID.randomUUID().toString();
        return "home.xhtml?faces-redirect=true";
    }

    public String register() {
        currentUserId = UUID.randomUUID().toString();
        return "index.xhtml?faces-redirect=true";
    }

    public String goToMonth(String month) {
        return "month.xhtml?faces-redirect=true";
    }

    // Getters and setters omitted for brevity
}