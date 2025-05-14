module org.example.b {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires javafx.swing;
    requires java.desktop;
    requires com.google.gson;

    opens org.example.b to javafx.fxml, com.google.gson;
    exports org.example.b;
}