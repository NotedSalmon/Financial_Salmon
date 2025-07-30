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

    public String goToMonth(String month) {
        return "month.xhtml?faces-redirect=true";
    }

    // Getters and setters omitted for brevity

}