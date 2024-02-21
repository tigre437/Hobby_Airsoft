package com.example.hobby_airsoft;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class controlarJugador {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    public Label lblTexto;

    @FXML
    Button btnBuscarApellido;

    @FXML
    Button btnBuscarCorreo;

    @FXML
    Button btnBuscarNick;

    @FXML
    Button btnBuscarNombre;

    @FXML
    Button btnBuscarTelefono;

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

    @FXML
    ChoiceBox<String> chRol; // Adaptado para la entidad Jugador

    private int id;

    public int id_partida;

    private static controlarJugador instancia;


    @FXML
    private void Aceptar(ActionEvent event) {
        try {
            insertarJugadorEnBaseDeDatos();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("Error al procesar la acción de añadir: " + e.getMessage());
        }
    }


    private void insertarJugadorEnBaseDeDatos() {
        try (Connection connection = DatabaseConnector.conectar()) {
            if (lblTexto.getText().equals("Añadir Jugador")) {
                String query = "INSERT INTO ASISTENCIA (id_jugador, id_partida, rol) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, id_partida);
                    preparedStatement.setString(3, chRol.getValue());
                    preparedStatement.executeUpdate();
                }
            } else if (lblTexto.getText().equals("Editar Jugador")) {
                String query = "UPDATE ASISTENCIA SET rol=? WHERE id_jugador=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, chRol.getValue());
                    preparedStatement.setString(2, String.valueOf(id));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")){
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Ese usuario ya estaba en la partida");
                alerta.show();
            }
            System.out.println("Error al insertar en la base de datos: " + e.getMessage());
        }
    }

    @FXML
    void Cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        assert btnAceptar != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert btnCancelar != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert lblTexto != null : "fx:id=\"lblTexto\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtApellido != null : "fx:id=\"txtApellido\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtCorreo != null : "fx:id=\"txtCorreo\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtNick != null : "fx:id=\"txtNick\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert txtTelefono != null : "fx:id=\"txtTelefono\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        assert chRol != null : "fx:id=\"chRol\" was not injected: check your FXML file 'añadirJugador.fxml'.";
        Tooltip tooltipAceptar = new Tooltip("Aceptar");
        Tooltip tooltipCancelar = new Tooltip("Cancelar");
        Tooltip tooltipBuscarApellido = new Tooltip("Buscar por Apellido");
        Tooltip tooltipBuscarCorreo = new Tooltip("Buscar por Correo");
        Tooltip tooltipBuscarNick = new Tooltip("Buscar por Nick");
        Tooltip tooltipBuscarNombre = new Tooltip("Buscar por Nombre");
        Tooltip tooltipBuscarTelefono = new Tooltip("Buscar por Teléfono");

        // Asignar ToolTip a cada botón
        Tooltip.install(btnAceptar, tooltipAceptar);
        Tooltip.install(btnCancelar, tooltipCancelar);
        Tooltip.install(btnBuscarApellido, tooltipBuscarApellido);
        Tooltip.install(btnBuscarCorreo, tooltipBuscarCorreo);
        Tooltip.install(btnBuscarNick, tooltipBuscarNick);
        Tooltip.install(btnBuscarNombre, tooltipBuscarNombre);
        Tooltip.install(btnBuscarTelefono, tooltipBuscarTelefono);
        chRol.getItems().addAll("Medico", "Fusilero", "DMR", "Apoyo", "Sniper", "Ingeniero", "Organizador"); // Agrega los roles necesarios
        btnAceptar.setDisable(true);
        controlarJugador.instancia = this;
    }


    @FXML
    void BuscarApellido(ActionEvent event) throws SQLException {
        buscarJugadores("apellido", txtApellido.getText(), "nick", event);
    }

    @FXML
    void BuscarCorreo(ActionEvent event) throws SQLException {
        buscarJugadores("correo", txtCorreo.getText(), "correo", event);
    }

    @FXML
    void BuscarNick(ActionEvent event) throws SQLException {
        buscarJugadores("nick", txtNick.getText(), "nick", event);
    }

    @FXML
    void BuscarNombre(ActionEvent event) throws SQLException {buscarJugadores("nombre", txtNombre.getText(), "nick", event);
    }

    @FXML
    void BuscarTelefono(ActionEvent event) throws SQLException {buscarJugadores("telefono", txtTelefono.getText(), "telefono", event);
    }

    private void buscarJugadores(String campo, String valorBuscado, String columna, Event event) throws SQLException {
        Map<Integer, String> mapa = new HashMap<>();

        // Realizar la conexión y ejecutar la consulta
        try (Connection connection = DatabaseConnector.conectar()) {
            if(valorBuscado == null || valorBuscado.isEmpty()) {
                String sql = "SELECT id, nombre, apellido, " + columna + " FROM jugadores";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        // Procesar los resultados (pueden haber múltiples filas si hay coincidencias)
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String nombre = resultSet.getString("nombre");
                            String apellido = resultSet.getString("apellido");
                            String valorColumna = resultSet.getString(columna);

                            // Construir la cadena con nombre, apellido y el valor de la columna
                            String valorMapa = String.format("%s %s (%s)", nombre, apellido, valorColumna);
                            mapa.put(id, valorMapa);

                            System.out.println(mapa.toString());
                            // Hacer algo con el resultado, como mostrarlo en una ventana, etc.
                        }


                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("resultado.fxml"));
                        Parent root = fxmlLoader.load();
                        Stage newStage = new Stage();
                        controlarBuscador controller = fxmlLoader.getController();
                        controller.buscador(mapa);
                        newStage.initModality(Modality.APPLICATION_MODAL);
                        newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
                        newStage.setScene(new Scene(root));
                        newStage.showAndWait();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else{

            // Consulta SQL para buscar jugadores por el campo especificado
            String sql = "SELECT id, nombre, apellido, " + columna + " FROM jugadores WHERE LOWER(" + campo + ") LIKE LOWER(?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + valorBuscado + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Procesar los resultados (pueden haber múltiples filas si hay coincidencias)
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nombre = resultSet.getString("nombre");
                        String apellido = resultSet.getString("apellido");
                        String valorColumna = resultSet.getString(columna);

                        // Construir la cadena con nombre, apellido y el valor de la columna
                        String valorMapa = String.format("%s %s (%s)", nombre, apellido, valorColumna);
                        mapa.put(id, valorMapa);

                        System.out.println(mapa.toString());
                        // Hacer algo con el resultado, como mostrarlo en una ventana, etc.
                    }


                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("resultado.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage newStage = new Stage();
                    controlarBuscador controller = fxmlLoader.getController();
                    controller.buscador(mapa);
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
                    newStage.setScene(new Scene(root));
                    newStage.showAndWait();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void rellenarCampos (List<String> lista){
        txtNombre.setText(lista.get(0));
        txtApellido.setText(lista.get(1));
        txtNick.setText(lista.get(2));
        txtTelefono.setText(lista.get(3));
        txtCorreo.setText(lista.get(4));
        id = Integer.parseInt(lista.get(5));
        btnAceptar.setDisable(false);
    }

    public void setLabelText(String text) {
        lblTexto.setText(text);
    }

    public static controlarJugador obtenerInstancia() {
        return instancia;
    }

    public static void setInstancia(controlarJugador instancia) {
        controlarJugador.instancia = instancia;
    }

    public void setIdPartidaJugador( int id){
        id_partida = id;
    }

    public int getIdPartida(){
        return id_partida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}

