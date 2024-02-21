package com.example.hobby_airsoft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class controlarPartida {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    Label lblTexto;

    @FXML
    TextField txtDescripcion;

    @FXML
    DatePicker txtFecha;

    @FXML
    TextField txtLugar;

    @FXML
    TextField txtNombre;

    @FXML
    TextField txtPortada;

    int id;

    @FXML
    private void Aceptar(ActionEvent event) {
        try {
            String nombreError = comprobarNombre();
            String imagenError = comprobarImagen();

            if (nombreError == null  && imagenError == null) {
                Partida nuevaPartida = obtenerDatosPartida();
                insertarPartidaEnBaseDeDatos(nuevaPartida);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                // Muestra alerta de éxito
                alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partida Guardada",
                        "La información de la partida ha sido guardada exitosamente.");
            } else {
                String mensaje = "";
                if (nombreError != null) {
                    mensaje += nombreError + "\n";
                }
                if (imagenError != null) {
                    mensaje += imagenError;
                }
                alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Campos inválidos o incompletos", mensaje);
            }
        } catch (IllegalArgumentException e) {
            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Fecha inválida",
                    "La fecha proporcionada no tiene el formato correcto.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String comprobarNombre() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) {
            return "El campo nombre no puede estar vacío.";
        } else if (!nombre.matches("^[a-zA-Z0-9]+$")){
            return "El nombre no puede contener caracteres especiales.";
        }
        return null;
    }
    private String comprobarImagen() {
        File file = new File(txtPortada.getText());
        if (!txtPortada.getText().isEmpty()) {
            if (!file.exists() || !file.isFile()) {
                return "La imagen seleccionada no es válida.";
            }
        }
        return null;
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void seleccionarPortada(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg"),
                new ExtensionFilter("Todos los archivos", "*.*")
        );

        // Abre el diálogo de selección de archivo
        java.io.File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Obtén el nombre del archivo y realiza la copia
            String nombreArchivo = selectedFile.getName();
            String destinoPath = "./src/main/resources/com/example/hobby_airsoft/portadas/" + nombreArchivo;

            try {
                // Copia la imagen al directorio destino
                Files.copy(selectedFile.toPath(), Paths.get(destinoPath), StandardCopyOption.REPLACE_EXISTING);

                // Establece la ruta en el campo de texto
                txtPortada.setText(destinoPath);
            } catch (IOException e) {
                alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al copiar la imagen",
                        "Hubo un error al copiar la imagen: " + e.getMessage());
            }
        }
    }

    @FXML
    void initialize() {
        assert btnAceptar != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'Partida.fxml'.";
        assert btnCancelar != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'Partida.fxml'.";
        assert lblTexto != null : "fx:id=\"lblTexto\" was not injected: check your FXML file 'Partida.fxml'.";
        assert txtDescripcion != null : "fx:id=\"txtDescripcion\" was not injected: check your FXML file 'Partida.fxml'.";
        assert txtFecha != null : "fx:id=\"txtFecha\" was not injected: check your FXML file 'Partida.fxml'.";
        assert txtLugar != null : "fx:id=\"txtLugar\" was not injected: check your FXML file 'Partida.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'Partida.fxml'.";
        assert txtPortada != null : "fx:id=\"txtPortada\" was not injected: check your FXML file 'Partida.fxml'.";
        Tooltip tooltipAceptar = new Tooltip("Aceptar");
        Tooltip tooltipCancelar = new Tooltip("Cancelar");

        btnAceptar.setTooltip(tooltipAceptar);
        btnCancelar.setTooltip(tooltipCancelar);
    }

    public void setLabelText(String text) {
        lblTexto.setText(text);
    }

    public Partida obtenerDatosPartida() {
        String nombre = txtNombre.getText();
        String lugar = txtLugar.getText();

        // Obtener la fecha del DatePicker
        LocalDate fecha = txtFecha.getValue();
        String fechaStr = "";
        String descripcion = txtDescripcion.getText();
        String portadaPath = txtPortada.getText();

        // Extraer el nombre del archivo de la ruta completa
        String nombreArchivo = new File(portadaPath).getName();

        return new Partida(id, nombre, lugar, fechaStr, descripcion, nombreArchivo);
    }

    private void insertarPartidaEnBaseDeDatos(Partida partida) throws SQLException {
        try (Connection connection = DatabaseConnector.conectar()) {
            if (lblTexto.getText().equals("Añadir Partida")) {
                if (!partida.getFecha().isEmpty()) {
                    String query = "INSERT INTO Partidas (nombre, lugar, fecha, descripcion, portada) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, partida.getNombre());
                        if (!partida.getLugar().isEmpty()) {
                            preparedStatement.setString(2, partida.getLugar());
                        } else {
                            preparedStatement.setString(2, null);
                        }
                        if (!partida.getFecha().isEmpty()) {
                            preparedStatement.setString(3, partida.getFecha());
                        }
                        if (!partida.getDescripcion().isEmpty()) {
                            preparedStatement.setString(4, partida.getDescripcion());
                        } else {
                            preparedStatement.setString(4, null);
                        }
                        if (!partida.getPortada().isEmpty()) {
                            preparedStatement.setString(5, partida.getPortada());
                        } else {
                            preparedStatement.setString(5, null);
                        }

                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else{
                    String query = "INSERT INTO Partidas (nombre, lugar,  descripcion, portada) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, partida.getNombre());
                        if (!partida.getLugar().isEmpty()) {
                            preparedStatement.setString(2, partida.getLugar());
                        } else {
                            preparedStatement.setString(2, null);
                        }
                        if (!partida.getDescripcion().isEmpty()) {
                            preparedStatement.setString(3, partida.getDescripcion());
                        } else {
                            preparedStatement.setString(3, null);
                        }
                        if (!partida.getPortada().isEmpty()) {
                            preparedStatement.setString(4, partida.getPortada());
                        } else {
                            preparedStatement.setString(4, null);
                        }

                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}

