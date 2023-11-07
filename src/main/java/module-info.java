module org.jbomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires com.google.gson;
    requires java.datatransfer;
    requires java.desktop;


    opens org.jbomberman to javafx.fxml;
    exports org.jbomberman;
}