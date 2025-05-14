module org.example.b {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jdk.jfr;

    exports org.example.b;
    opens org.example.b;
}