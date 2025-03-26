module org.example.b {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    opens org.example.b to javafx.fxml,com.google.gson;
    exports org.example.b;
}