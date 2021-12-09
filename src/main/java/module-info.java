module com.example.passwordrepository {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.passwordrepository to javafx.fxml;
    exports com.example.passwordrepository;
}