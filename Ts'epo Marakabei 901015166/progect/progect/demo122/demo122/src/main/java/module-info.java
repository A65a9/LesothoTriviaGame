module com.example.demo122 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.demo122 to javafx.fxml;
    exports com.example.demo122;
}