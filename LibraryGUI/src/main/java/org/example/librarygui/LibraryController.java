package org.example.librarygui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.example.librarygui.listView.BookItem;
import org.example.librarygui.listView.ListBookCell;
import org.example.librarygui.listView.User;
import org.example.librarygui.multicast.MulticastAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;


public class LibraryController {
    public ListView<BookItem> bookList;
    public TextField bookId;
    public Pane orderBox;
    public Text orderBookId;
    public Button fulfilBtn;
    public Button rejectBtn;
    public ListView<String> userList;


    public void onLoadBooksClick(MouseEvent mouseEvent) {
        ObservableList<BookItem> items = FXCollections.observableArrayList();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8080/libraryserver"))
                    //.header("Content-Type", "application/json") // Set content type as JSON
                    .GET() // Request body as JSON string
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<BookItem>>(){}.getType();
            List<BookItem> list = gson.fromJson(response.body(), listType);
            items.addAll(list);
            bookList.setItems(items);
            bookList.setCellFactory(param -> new ListBookCell());
        }catch (Exception e){
            e.printStackTrace();
        }    }

    public void onLoadNextOrder(ActionEvent actionEvent) {
        MulticastAgent agent = MulticastAgent.getInstance();
        String message = agent.getMessage();
        orderBookId.setText(message);
        if(message.equals("No requests")){
            fulfilBtn.setDisable(true);
            rejectBtn.setDisable(true);
        }else{
            fulfilBtn.setDisable(false);
            rejectBtn.setDisable(false);
        }
        orderBox.setVisible(true);
    }

    public void onOrderFulfil(ActionEvent actionEvent) {
       MulticastAgent agent = MulticastAgent.getInstance();
         agent.sendMessage("#order/fulfilled#"+orderBookId.getText());
    }

    public void onOrderReject(ActionEvent actionEvent) {
        orderBox.setVisible(false);
    }

    public void addBookToLibrary(MouseEvent mouseEvent) {
        System.out.println(bookId.getText());
        sendMessage("#book/send#"+bookId.getText());
    }
    private void sendMessage(String message){
        try(Socket socket = new Socket("localhost", 3000)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            while(true){
                String line = in.readLine();
                if(line == null){
                    break;
                }
                System.out.println(line);
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadUsers(MouseEvent mouseEvent) {
        userList.getItems().clear();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8080/user"))
                    //.header("Content-Type", "application/json") // Set content type as JSON
                    .GET() // Request body as JSON string
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> list = gson.fromJson(response.body(), listType);
            list.stream().forEach(user -> userList.getItems().add(user.username));

        }catch (Exception e){
            e.printStackTrace();
        }    }

    public void blockUser(MouseEvent mouseEvent) {
        String username = userList.getSelectionModel().getSelectedItem();
        try{
            HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/user/block/"+username))
                //.header("Content-Type", "application/json") // Set content type as JSON
                .GET() // Request body as JSON string
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());}
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
