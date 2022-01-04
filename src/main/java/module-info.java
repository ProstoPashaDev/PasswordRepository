module com.Pavel.passwordrepository {
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.controls;

    opens com.Pavel.passwordrepository to javafx.fxml;
    exports com.Pavel.passwordrepository;

}
