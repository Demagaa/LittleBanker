package com.banker.models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * @author Aleksei Chursin
 */

// POJO class for Customer //
public class Customer {

    @Size(min = 2)
    private String name;

    @Size(min = 2)
    private String surname;

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
