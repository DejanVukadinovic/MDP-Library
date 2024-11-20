package org.example.customergui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.customergui.listview.BookItem;
import org.example.customergui.listview.ListBookCell;
import org.example.customergui.multicast.MulticastAgent;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CustomerController {
    public Button loadButton;
    public ListView<BookItem> bookList;
    public Button chooseButton;
    public Button chatButton;
    public TextField bookId;

    public void loadBooks() {


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
        }

    }
    public void chooseBook(){
        BookItem item = bookList.getSelectionModel().getSelectedItem();
        System.out.println(item);
    }
    public void chat(){
        System.out.println("Chat");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("chat.fxml"));
            chatButton.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();}
    }

    public void requestBook(MouseEvent mouseEvent) {
        System.out.println(bookId.getText());
        MulticastAgent multicastAgent = MulticastAgent.getInstance();
        multicastAgent.sendMessage(bookId.getText());
    }
}
