package com.banker.models;


/**
 * @author Aleksei Chursin
 */

// POJO class for Customer //
public class Customer {

    private String name;

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
