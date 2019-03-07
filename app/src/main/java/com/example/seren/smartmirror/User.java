package com.example.seren.smartmirror;

public class User {
    private String surname, name, email, password,image;

    public User() {
    }



    public User(String name, String surname, String email, String password, String image) {
        this.surname = surname;
        this.name = name;
        this.email = email;

        this.password = password;
        this.image = image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
