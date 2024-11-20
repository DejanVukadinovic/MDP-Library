module org.example.librarygui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;


    opens org.example.librarygui to javafx.fxml;
    exports org.example.librarygui;
    exports org.example.librarygui.listView;
}