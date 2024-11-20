module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.rabbitmq.client;
    requires com.google.gson;
    requires com.github.codemonstur.simplexml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpmime;


    opens org.example to javafx.fxml;

    opens org.example.entities to com.google.gson;
    exports org.example;

}