package org.example.customergui.entities;

public class User{
    public String username;
    public String password;
        public String name;
        public String email;
        public String address;

    public User(String username, String password, String name, String email, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
