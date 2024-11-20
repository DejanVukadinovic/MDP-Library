package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.example.MQ.RabbitMQHandler;
import org.example.entities.Invoice;
import org.example.entities.Order;
import org.example.remote.AccountingInterface;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import com.google.gson.Gson;

public class SupplierController {
    @FXML
    public Text orderInvoicePDV;
    @FXML
    public Button loadOrderButton;
    @FXML
    private ListView bookList;
    @FXML
    private TextField bookId;
    @FXML
    private Pane orderBox;
    @FXML
    private Text orderBookId;
    @FXML
    private Text orderBookTitle;
    @FXML
    private Text orderBookAmount;

    @FXML
    public void onLoadBooksClick(){
        bookList.getItems().add("Demo book");
        bookList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void onLoadNextOrder(){

        loadOrderButton.setDisable(true);
        RabbitMQHandler handler = RabbitMQHandler.getHandler();
        String message = handler.getMessage("");
        if (message==null) {
            orderInvoicePDV.setText("No new orders");
            orderInvoicePDV.setVisible(true);
            return;
        }
        System.out.println(message);
        Gson gson = new Gson();
        Order order = gson.fromJson(message, Order.class);
        System.out.println(order);

        orderBookId.setText(order.id());
        orderBookTitle.setText(order.title());
        orderBookAmount.setText(order.amount());

        orderBox.setVisible(true);
        orderInvoicePDV.setVisible(false);
    }

    @FXML
    public void onOrderFulfil(){
        System.out.println("Hello World!");
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AccountingInterface accounting = (AccountingInterface) registry.lookup("accounting");
            Invoice invoice = new Invoice(new ArrayList<>(List.of(1,2,3)), 2000, new Date());
            double result = accounting.processInvoice(invoice);
            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        orderBox.setVisible(false);
        orderInvoicePDV.setText("PDV za platiti: 17 BAM");
        orderInvoicePDV.setVisible(true);
        loadOrderButton.setDisable(false);
    }

    @FXML
    public void onOrderReject(){
        orderBox.setVisible(false);
        loadOrderButton.setDisable(false);
    }

    public static void sendFileWithPayload(String filePath, String body) throws IOException {
        // Create the HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create the HTTP POST request
            HttpPost uploadFile = new HttpPost("localhost:8080/libraryserver/registerBook");

            // Prepare the multipart entity
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("body", body);
            builder.addBinaryBody("file", new File(filePath));

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            // Send the request
            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(EntityUtils.toString(responseEntity));
            }
        }
    }

}
