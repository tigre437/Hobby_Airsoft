module com.example.hobby_airsoft {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires java.sql;
    requires jasperreports;
    requires java.datatransfer;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.hobby_airsoft to javafx.fxml, javafx.web, gson;
    exports com.example.hobby_airsoft;
}