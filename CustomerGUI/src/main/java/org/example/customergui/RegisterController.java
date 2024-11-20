package org.example.customergui;

import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.customergui.entities.User;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterController {

    public TextField username;
    public PasswordField password;
    public TextField email;
    public TextField address;
    public TextField name;
    public PasswordField repeatPassword;


    public void handleRegister() {
        User user = new User(username.getText(), password.getText(), name.getText(), email.getText(), address.getText());
        Gson gson = new Gson();
        String json = gson.toJson(user);
        System.out.println(json);
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            // Create the HTTP POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8080/user/register"))
                    .header("Content-Type", "application/json") // Set content type as JSON
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Request body as JSON string
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Output the response details
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());



        }catch (Exception e){
            e.printStackTrace();}

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        try {
            username.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();}
    }
}
