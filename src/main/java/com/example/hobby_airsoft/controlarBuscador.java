package com.example.hobby_airsoft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class controlarBuscador {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ListView<String> lstResultado;

    private Map mapa;

    int id;

    @FXML
    void aceptar(ActionEvent event) throws IOException {

        // Reemplaza chResultado por lstResultado
        String valorSeleccionado = lstResultado.getSelectionModel().getSelectedItem().toString();
        Integer claveAsociada = getKeyByValue(mapa, valorSeleccionado);

        System.out.println(obtenerDatosJugadorPorId(claveAsociada).toString());
        List<String> lista = obtenerDatosJugadorPorId(claveAsociada);

        controlarJugador instanciaJugador = controlarJugador.obtenerInstancia();
        instanciaJugador.rellenarCampos(lista);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    // Método para obtener la clave por el valor en un Map
    private Integer getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // Manejar el caso cuando no se encuentra la clave
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        assert btnAceptar != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'resultado.fxml'.";
        assert btnCancelar != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'resultado.fxml'.";
        assert lstResultado != null : "fx:id=\"lstResultado\" was not injected: check your FXML file 'resultado.fxml'.";
    }

    public void buscador(Map<Integer, String> lista) {
        mapa = lista;
        lstResultado.getItems().addAll(lista.values());
        lstResultado.getSelectionModel().selectFirst(); // Seleccionar el primer elemento por defecto
    }



    public static List<String> obtenerDatosJugadorPorId(int id) {
        List<String> datosJugador = new ArrayList<>();

        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "SELECT id, nombre, apellido, nick, telefono, correo FROM jugadores WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Obtener los datos del resultado y añadirlos a la lista
                        datosJugador.add(resultSet.getString("nombre"));
                        datosJugador.add(resultSet.getString("apellido"));
                        datosJugador.add(resultSet.getString("nick"));
                        datosJugador.add(resultSet.getString("telefono"));
                        datosJugador.add(resultSet.getString("correo"));
                        datosJugador.add(resultSet.getString("id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datosJugador;
    }

}




