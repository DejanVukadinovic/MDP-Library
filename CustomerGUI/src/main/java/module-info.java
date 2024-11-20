module org.example.customergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpcore;
requires org.apache.httpcomponents.httpclient;
    requires java.net.http;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;


    opens org.example.customergui to javafx.fxml;
    exports org.example.customergui;
    exports  org.example.customergui.entities;
    exports org.example.customergui.listview;
}