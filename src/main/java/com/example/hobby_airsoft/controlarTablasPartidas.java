package com.example.hobby_airsoft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;


public class controlarTablasPartidas {

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnAñadir;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Label lblOperacion;

    @FXML
    private Label lblPaginas;

    @FXML
    private TableView<Partida> tbtPartidas;

    @FXML
    private TableColumn<Partida, Integer> tcId;

    @FXML
    private TableColumn<Partida, String> tcNombre;

    @FXML
    private TableColumn<Partida, String> tcLugar;

    @FXML
    private TableColumn<Partida, String> tcFecha;

    @FXML
    private TableColumn<Partida, String> tcDesc;

    @FXML
    private TableColumn<Partida, String> tcPortada;

    @FXML
    private Label lblTexto;

    @FXML
    private TextField tfFiltro;

    @FXML
    void atras(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void añadir(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Partida.fxml"));
            Parent root = fxmlLoader.load();

            controlarPartida controller = fxmlLoader.getController();
            controller.setLabelText("Añadir Partida");

            // Crear un nuevo escenario (Stage) para la ventana emergente
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            newStage.setScene(new Scene(root));

            // Mostrar la nueva ventana emergente y esperar hasta que se cierre
            newStage.showAndWait();

            // Actualizar la tabla después de agregar una nueva partida
            cargarDatosDesdeBD();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al abrir la ventana de añadir partida", e.getMessage());
        }
    }

    @FXML
    void editar(ActionEvent event) {
        // Obtener la partida seleccionada
        Partida partidaSeleccionada = tbtPartidas.getSelectionModel().getSelectedItem();

        // Verificar si hay una partida seleccionada
        if (partidaSeleccionada != null) {
            // Llamar al método para editar la partida
            editarPartida(partidaSeleccionada);
        } else {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Ninguna fila seleccionada");
            warning.show();
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        // Obtener la partida seleccionada
        Partida partidaSeleccionada = tbtPartidas.getSelectionModel().getSelectedItem();

        // Verificar si hay una partida seleccionada
        if (partidaSeleccionada != null) {
            // Mostrar una alerta de confirmación
            String mensaje = String.format("¿Estás seguro de que deseas eliminar %s el (%s)?",
                    partidaSeleccionada.getNombre(), partidaSeleccionada.getFecha());



            // Obtener la respuesta del usuario
            Optional<ButtonType> respuesta = alertas.mostrarAlertaConfirmacion("Confirmar Eliminación", "Eliminar Partida", mensaje);

            System.out.println(respuesta.get().toString());
            if (respuesta.get() == ButtonType.OK) {
                eliminarPartida(partidaSeleccionada);

                // Limpia la selección en la TableView
                tbtPartidas.getSelectionModel().clearSelection();
                alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partida Eliminada",
                        String.format("La partida %s del %s ha sido eliminado.", partidaSeleccionada.getNombre(),
                                partidaSeleccionada.getFecha()));
            }
        } else {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Ninguna fila seleccionada");
            warning.show();
        }
    }

    private void eliminarPartida(Partida usuario) {
        try (Connection connection = DatabaseConnector.conectar()) {
            String query = "DELETE FROM Partidas WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, usuario.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar de la base de datos: " + e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }

        // Vuelve a cargar los datos desde la base de datos para actualizar la TableView
        cargarDatosDesdeBD();
    }

    public void initialize() {
        // Configurar las columnas
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tcDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcPortada.setCellValueFactory(new PropertyValueFactory<>("portada"));

        Tooltip tooltipAtras = new Tooltip("Volver a la pantalla principal");
        Tooltip tooltipAnadir = new Tooltip("Añadir nueva partida");
        Tooltip tooltipEditar = new Tooltip("Editar partida seleccionada");
        Tooltip tooltipEliminar = new Tooltip("Eliminar partida seleccionada");


        btnAtras.setTooltip(tooltipAtras);
        btnAñadir.setTooltip(tooltipAnadir);
        btnEditar.setTooltip(tooltipEditar);
        btnEliminar.setTooltip(tooltipEliminar);

        tbtPartidas.setRowFactory(tv -> {
            TableRow<Partida> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && !row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY)) {
                        // Obtener la partida seleccionada
                        Partida partidaSeleccionada = row.getItem();

                        // Llamar al método para editar la partida
                        editarPartida(partidaSeleccionada);
                    }
                }
            });
            return row;
        });


        // Obtener datos de la base de datos y llenar el TableView
        cargarDatosDesdeBD();
    }

    private void editarPartida(Partida partida) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Partida.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            controlarPartida controller = fxmlLoader.getController();

            // Configura el nuevo escenario para la ventana emergente
            Stage newStage = new Stage();
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(tbtPartidas.getScene().getWindow());
            newStage.setScene(scene);
            controller.lblTexto.setText("Editar Partida");

            // Llama al método en el controlador para establecer el texto
            controller.id = partida.getId();
            controller.txtNombre.setText(partida.getNombre());
            controller.txtLugar.setText(partida.getLugar());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(partida.getFecha(), formatter);
            controller.txtFecha.setValue(fecha);
            controller.txtDescripcion.setText(partida.getDescripcion());
            controller.txtPortada.setText(partida.getPortada());

            // Mostrar la nueva ventana emergente
            newStage.showAndWait();

            // Actualizar la tabla después de la edición
            cargarDatosDesdeBD();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al abrir la ventana de editar partida", e.getMessage());
        }
    }

    private void cargarDatosDesdeBD() {
        tbtPartidas.getItems().clear();
        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "SELECT * FROM Partidas";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<Partida> datos = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        Partida partida = new Partida(
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("lugar"),
                                formatDate(resultSet.getDate("fecha")), // Formatear fecha como String
                                resultSet.getString("descripcion"),
                                resultSet.getString("portada")
                        );
                        System.out.println(formatDate(resultSet.getDate("fecha")));
                        datos.add(partida);
                    }
                    tbtPartidas.setItems(datos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para formatear la fecha como String
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Puedes ajustar el formato según tus necesidades
        return dateFormat.format(date);
    }
}
