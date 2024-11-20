package org.example.customergui;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.customergui.entities.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LoginController {
    public TextField username;
    public TextField password;

    public void onLogin(ActionEvent actionEvent) {

        User user = new User(username.getText(), password.getText());
        Gson gson = new Gson();
        String json = gson.toJson(user);
        System.out.println(json);
        System.out.println(username.getText()+ " "+ password.getText());
        System.out.println(user.name + " " + user.password);
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            // Create the HTTP POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8080/user/login"))
                    .header("Content-Type", "application/json") // Set content type as JSON
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Request body as JSON string
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Output the response details
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            if(response.body().equals("ok")){
                try {
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("customer.fxml"));
                    //username.
                    username.getScene().setRoot(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }catch (Exception e){
            e.printStackTrace();}


    }
    public void handleRegister() {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));

        try {
            username.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();}
    }
}
