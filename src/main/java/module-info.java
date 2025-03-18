module org.example.b {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example.b to javafx.fxml;
    exports org.example.b;
}