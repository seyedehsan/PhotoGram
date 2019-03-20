package com.example.mobileapplicationproject;

public class User {
    private String firstName;
    private String lastName;
    private String gender;
    private String bio;


    public User(String firstName, String lastName, String gender, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bio = bio;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

}
