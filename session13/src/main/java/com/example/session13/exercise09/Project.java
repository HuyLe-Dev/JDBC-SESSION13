package com.example.session13.exercise09;

import java.math.BigDecimal;

public class Project {
    private String name;
    private BigDecimal budget;

    public Project(String name, BigDecimal budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBudget() {
        return budget;
    }
}
