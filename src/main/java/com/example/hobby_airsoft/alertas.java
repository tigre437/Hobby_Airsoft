package com.example.hobby_airsoft;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class alertas {

    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        personalizarEstiloAlerta(alerta);
        alerta.showAndWait();
    }

    public static Optional<ButtonType> mostrarAlertaConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        personalizarEstiloAlerta(alerta);
        return alerta.showAndWait();
    }

    private static void personalizarEstiloAlerta(Alert alerta) {
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED); // Puedes personalizar el estilo aquí según tus preferencias
    }
}
