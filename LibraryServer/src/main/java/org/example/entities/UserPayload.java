package org.example.entities;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserPayload {
    private String username;
    private String password;
    private String repeatPassword;


    private String email;
    private String name;
    private String address;

    public UserPayload(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public UserPayload(String username, String password, String repeatPassword, String email, String name, String address) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.email = email;
        this.name = name;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
