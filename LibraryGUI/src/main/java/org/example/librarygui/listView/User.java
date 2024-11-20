package org.example.librarygui.listView;

public class User {

    public String username;
    public String password;
    public String email;
    public String name;
    public String address;

    public User() {
    }
    private User(String username, String password, String email, String name, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.address = address;
    }

}
