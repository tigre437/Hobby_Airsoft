package com.example.hobby_airsoft;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class controlarUsuario {

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
    TextField txtApellido;

    @FXML
    TextField txtCorreo;

    @FXML
    TextField txtNick;

    @FXML
    TextField txtNombre;

    @FXML
    TextField txtTelefono;

    int id;

    @FXML
    private void Aceptar() {
        try {
            Usuario nuevoUsuario = obtenerDatosUsuario();

            // Realizar comprobaciones de campos
            if (comprobarCampos(nuevoUsuario)) {
                insertarUsuarioEnBaseDeDatos(nuevoUsuario);
                Stage stage = (Stage) btnAceptar.getScene().getWindow();
                stage.close();
                if (lblTexto.getText().equals("Añadir Usuario")) {
                    alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario añadido",
                            "El usuario se ha añadido correctamente.");
                }
                else{
                    alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario editado",
                            "El usuario se ha editado correctamente.");
                }
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("telefono invalido")) {
                alertas.mostrarAlerta(Alert.AlertType.WARNING, "Error", "Telefono inválido",
                        "El campo teléfono contiene caracteres no válidos.");
            }
            System.out.println("Error al procesar la acción de añadir: " + e.getMessage());
        }
    }

    private boolean comprobarCampos(Usuario usuario) {
        if (usuario.getNombre().isEmpty() || usuario.getApellido().isEmpty()) {
            mostrarNotificacionError("Nombre y apellido son campos obligatorios.");
            return false;
        }

        if (usuario.getTelefono() != null && usuario.getTelefono().length() != 9) {
            mostrarNotificacionError("El teléfono debe tener 9 caracteres.");
            return false;
        }

        if (!usuario.getNombre().matches("[\\p{L} ]+") || !usuario.getApellido().matches("[\\p{L} ]+")) {
            mostrarNotificacionError("Nombre y apellido no deben contener números ni caracteres especiales.");
            return false;
        }


        if (!usuario.getCorreo().isEmpty() && !usuario.getCorreo().contains("@")) {
            mostrarNotificacionError("Formato de correo electrónico inválido.");
            return false;
        }

        if (!usuario.getNick().isEmpty() && !usuario.getNick().matches("[\\p{L}0-9 ]+")) {
            mostrarNotificacionError("El nick solo debe contener letras y números.");
            return false;
        }

        if (!usuario.getTelefono().isEmpty() && !usuario.getTelefono().matches("[0-9]+")) {
            mostrarNotificacionError("El teléfono solo debe contener números.");
            return false;
        }

        return true;
    }

    private void mostrarNotificacionError(String mensaje) {
        alertas.mostrarAlerta(Alert.AlertType.WARNING, "Error", "Validación de campos", mensaje);
    }

    private void insertarUsuarioEnBaseDeDatos(Usuario usuario) throws SQLException {
        try (Connection connection = DatabaseConnector.conectar()) {
            if (lblTexto.getText().equals("Añadir Usuario")) {
                System.out.println("añadir");
                String query = "INSERT INTO JUGADORES (nombre, apellido, nick, telefono, correo) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    if (!usuario.getTelefono().isEmpty()) {
                        preparedStatement.setString(1, usuario.getNombre());
                        preparedStatement.setString(2, usuario.getApellido());
                        preparedStatement.setString(3, usuario.getNick());
                        preparedStatement.setString(4, usuario.getTelefono());
                        preparedStatement.setString(5, usuario.getCorreo());
                    } else {
                        preparedStatement.setString(1, usuario.getNombre());
                        preparedStatement.setString(2, usuario.getApellido());
                        preparedStatement.setString(3, usuario.getNick());
                        preparedStatement.setString(4, null);
                        preparedStatement.setString(5, usuario.getCorreo());
                    }

                    preparedStatement.executeUpdate();
                }
            } else if (lblTexto.getText().equals("Editar Usuario")) {
                System.out.println("Editar");
                System.out.println(usuario.getId());
                String query = "UPDATE JUGADORES SET nombre=?, apellido=?, nick=?, telefono=?, correo=? WHERE id=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, usuario.getNombre());
                    preparedStatement.setString(2, usuario.getApellido());
                    preparedStatement.setString(3, usuario.getNick());
                    preparedStatement.setString(4, usuario.getTelefono());
                    preparedStatement.setString(5, usuario.getCorreo());
                    preparedStatement.setInt(6, usuario.getId());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("telefono invalido");
        }
    }

    @FXML
    private void Cancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        assert btnAceptar != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert btnCancelar != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert lblTexto != null : "fx:id=\"lblTexto\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtApellido != null : "fx:id=\"txtApellido\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtCorreo != null : "fx:id=\"txtCorreo\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtNick != null : "fx:id=\"txtNick\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtTelefono != null : "fx:id=\"txtTelefono\" was not injected: check your FXML file 'añadirJugador.fxml'.";
    }

    public void setLabelText(String text) {
        lblTexto.setText(text);
    }

    public Usuario obtenerDatosUsuario() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String nick = txtNick.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        return new Usuario(id, nombre, apellido, nick, telefono, correo);
    }
}
