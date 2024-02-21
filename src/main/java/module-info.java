module com.example.hobby_airsoft {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires jasperreports;
    requires java.datatransfer;
    requires java.desktop;

    opens com.example.hobby_airsoft to javafx.fxml;
    exports com.example.hobby_airsoft;
}