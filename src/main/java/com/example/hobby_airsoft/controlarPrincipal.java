package com.example.hobby_airsoft;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class controlarPrincipal {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bntAsistencia;

    @FXML
    private Button bntVersus;

    @FXML
    private Button btnJugadores;

    @FXML
    private ImageView imagenLogo;

    @FXML
    private Label lblAsistencia;

    @FXML
    private Label lblJugadores;

    @FXML
    private Label lblPartidas;

    @FXML
    void activar(ActionEvent event) {
        try {


            // Cargar el nuevo archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tablaUsuarios.fxml"));

            // Crear una nueva escena con el contenido del nuevo archivo FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el escenario actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);

            // Mostrar el escenario actualizado
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }
    }

    @FXML
    void initialize() {
        assert bntAsistencia != null : "fx:id=\"bntAsistencia\" was not injected: check your FXML file 'principal.fxml'.";
        assert bntVersus != null : "fx:id=\"bntVersus\" was not injected: check your FXML file 'principal.fxml'.";
        assert btnJugadores != null : "fx:id=\"btnJugadores\" was not injected: check your FXML file 'principal.fxml'.";
        assert imagenLogo != null : "fx:id=\"imagenLogo\" was not injected: check your FXML file 'principal.fxml'.";
        assert lblAsistencia != null : "fx:id=\"lblAsistencia\" was not injected: check your FXML file 'principal.fxml'.";
        assert lblJugadores != null : "fx:id=\"lblJugadores\" was not injected: check your FXML file 'principal.fxml'.";
        assert lblPartidas != null : "fx:id=\"lblPartidas\" was not injected: check your FXML file 'principal.fxml'.";
        Tooltip tooltipJugadores = new Tooltip("Ir a Jugadores");
        Tooltip tooltipAsistencia = new Tooltip("Ir a Asistencia");
        Tooltip tooltipPartidas = new Tooltip("Ir a Partidas");

        btnJugadores.setTooltip(tooltipJugadores);
        bntAsistencia.setTooltip(tooltipAsistencia);
        bntVersus.setTooltip(tooltipPartidas);
    }


    @FXML
    void onMouseEntered(MouseEvent event) {
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            // Ajustar el tamaño del botón cuando el ratón entra
            button.setScaleX(1.1);
            button.setScaleY(1.1);
        }
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            // Restaurar el tamaño original del botón cuando el ratón sale
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        }
    }

    @FXML
    public void activar2(ActionEvent event) {
        try {
            // Cargar el nuevo archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("partidasInicio.fxml"));

            // Crear una nueva escena con el contenido del nuevo archivo FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el escenario actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);

            // Mostrar el escenario actualizado
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }
    }

    @FXML
    public void activar3(ActionEvent event) {
        try {
            // Cargar el nuevo archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tablaPartidas.fxml"));

            // Crear una nueva escena con el contenido del nuevo archivo FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el escenario actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);

            // Mostrar el escenario actualizado
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }
    }

}