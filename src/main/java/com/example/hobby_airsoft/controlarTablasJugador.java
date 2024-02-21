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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



public class controlarTablasJugador {

    @FXML
    private ImageView btnAtras;

    @FXML
    private AnchorPane panelGeneral;

    @FXML
    private ImageView btnAñadir;

    @FXML
    private ImageView btnEditar;

    @FXML
    private ImageView btnEliminar;

    @FXML
    private Label lblOperacion;

    @FXML
    private Label lblPaginas;

    @FXML
    private TableView<Jugador> tbtJugadores;

    @FXML
    private TableColumn<Jugador, String> tcNombre;

    @FXML
    private TableColumn<Jugador, String> tcApellido;

    @FXML
    private TableColumn<Jugador, String> tcNick;

    @FXML
    private TableColumn<Jugador, String> tcTelefono;

    @FXML
    private TableColumn<Jugador, String> tcCorreo;

    @FXML
    private TableColumn<Jugador, String> tcRol;

    @FXML
    private TextField tfFiltro;

    public int id_partida;

    @FXML
    private Button btnInforme;


    @FXML
    void initialize() {
        assert btnAtras != null : "fx:id=\"btnAtras\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert btnAñadir != null : "fx:id=\"btnAñadir\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert btnEditar != null : "fx:id=\"btnEditar\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert btnEliminar != null : "fx:id=\"btnEliminar\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert lblOperacion != null : "fx:id=\"lblOperacion\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert lblPaginas != null : "fx:id=\"lblPaginas\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert tbtJugadores != null : "fx:id=\"tbtJugadores\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        assert tfFiltro != null : "fx:id=\"tfFiltro\" was not injected: check your FXML file 'tablaJugadores.fxml'.";
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        tcNick.setCellValueFactory(new PropertyValueFactory<>("nick"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tcCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tcRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        cargarDatosDesdeBD(id_partida);
        Tooltip tooltipAtras = new Tooltip("Regresar");
        Tooltip tooltipAñadir = new Tooltip("Añadir Jugador");
        Tooltip tooltipEditar = new Tooltip("Editar Jugador");
        Tooltip tooltipEliminar = new Tooltip("Eliminar Jugador");

        // Asignar ToolTip a cada botón
        Tooltip.install(btnAtras, tooltipAtras);
        Tooltip.install(btnAñadir, tooltipAñadir);
        Tooltip.install(btnEditar, tooltipEditar);
        Tooltip.install(btnEliminar, tooltipEliminar);

        tbtJugadores.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Jugador> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && !row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY)) {
                        // Obtener la fila seleccionada
                        Jugador jugadorSeleccionado = row.getItem();

                        // Llamar al método para editar el jugador
                        editarJugador(jugadorSeleccionado);
                    }
                }
            });
            return row;
        });
    }

    private void editarJugador(Jugador jugador) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Jugador.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            controlarJugador controller = fxmlLoader.getController();

            // Configurar el controlador para la edición
            controller.setLabelText("Editar Jugador");
            controller.setIdPartidaJugador(id_partida);
            controller.setId(jugador.getId());
            controller.txtNombre.setText(jugador.getNombre());
            controller.txtApellido.setText(jugador.getApellido());
            controller.txtNick.setText(jugador.getNick());
            controller.txtTelefono.setText(jugador.getTelefono());
            controller.txtCorreo.setText(jugador.getCorreo());
            controller.chRol.setValue(jugador.getRol());
            controller.txtNombre.setDisable(true);
            controller.txtApellido.setDisable(true);
            controller.txtNick.setDisable(true);
            controller.txtTelefono.setDisable(true);
            controller.txtCorreo.setDisable(true);
            controller.btnAceptar.setDisable(false);
            controller.btnBuscarNombre.setVisible(false);
            controller.btnBuscarApellido.setVisible(false);
            controller.btnBuscarNick.setVisible(false);
            controller.btnBuscarCorreo.setVisible(false);
            controller.btnBuscarTelefono.setVisible(false);

            // Mostrar la nueva ventana emergente
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(tbtJugadores.getScene().getWindow());
            newStage.setScene(scene);
            newStage.showAndWait();

            // Actualizar la tabla después de la edición
            cargarDatosDesdeBD(id_partida);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }
    }


    public void atras(ActionEvent event) {
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

    public void añadir(ActionEvent event) {
        try {
            // Cargar el nuevo archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Jugador.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            controlarJugador controller = fxmlLoader.getController();
            controller.setLabelText("Añadir Jugador");
            controller.setIdPartidaJugador(id_partida);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.showAndWait();
            cargarDatosDesdeBD(id_partida);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
        }
    }

    @FXML
    void editar(ActionEvent event) {
        // Obtener el jugador seleccionado
        Jugador jugadorSeleccionado = tbtJugadores.getSelectionModel().getSelectedItem();

        // Verificar si hay un jugador seleccionado
        if (jugadorSeleccionado != null) {
            // Llamar al método para editar el jugador
            editarJugador(jugadorSeleccionado);
        } else {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Ninguna fila seleccionada");
            warning.show();
        }
    }

    private void mostrarConfirmacionEliminar() {
        Jugador jugadorSeleccionado = tbtJugadores.getSelectionModel().getSelectedItem();

        // Verifica que se haya seleccionado un jugador
        if (jugadorSeleccionado != null) {
            Optional<ButtonType> respuesta = alertas.mostrarAlertaConfirmacion(
                    "Confirmar Eliminación",
                    "Eliminar Jugador",
                    "¿Estás seguro de eliminar a " + jugadorSeleccionado.getNombre() + " " +
                            jugadorSeleccionado.getApellido() + " (" + jugadorSeleccionado.getNick() + ") ?"
            );

            respuesta.ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Realiza la eliminación en la base de datos
                    eliminarJugadorDeAsistencia(jugadorSeleccionado.getId());

                    // Actualiza la tabla de jugadores después de la eliminación
                    cargarDatosDesdeBD(id_partida);

                    // Muestra alerta de éxito
                    alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador Eliminado",
                            "El jugador ha sido eliminado exitosamente.");
                }
            });
        }
    }

    public void eliminar(ActionEvent event) {
        Jugador jugadorSeleccionado = tbtJugadores.getSelectionModel().getSelectedItem();
        if (jugadorSeleccionado != null) {
            // Llamar al método para editar el jugador
            mostrarConfirmacionEliminar();
        } else {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Ninguna fila seleccionada");
            warning.show();
        }
    }


    private void eliminarJugadorDeAsistencia(int idJugador) {
        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "DELETE FROM asistencia WHERE id_jugador = ? AND id_partida = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idJugador);
                preparedStatement.setInt(2, id_partida);

                // Ejecutar la sentencia DELETE
                int filasAfectadas = preparedStatement.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Jugador eliminado de la tabla de asistencia.");
                } else {
                    System.out.println("No se pudo eliminar al jugador de la tabla de asistencia.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar jugador de la tabla de asistencia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getId_partida() {
        return id_partida;
    }

    public void setId_partida(int id_partida) {
        this.id_partida = id_partida;
        cargarDatosDesdeBD(id_partida);
    }

    public void setLabelText(String text) {
        lblOperacion.setText(text + " > Jugadores");
    }

    private void cargarDatosDesdeBD(int idPartida) {
        tbtJugadores.getItems().clear();
        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "SELECT jugadores.*, asistencia.rol FROM jugadores INNER JOIN asistencia ON jugadores.id = asistencia.id_jugador WHERE asistencia.id_partida = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idPartida);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<Jugador> datos = FXCollections.observableArrayList();
                    while (resultSet.next()) {

                        Jugador jugador = new Jugador(
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("apellido"),
                                resultSet.getString("nick"),
                                resultSet.getString("telefono"),
                                resultSet.getString("correo")
                        );
                        jugador.setRol(resultSet.getString("rol"));
                        datos.add(jugador);
                    }
                    tbtJugadores.setItems(datos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnGenerarInforme;

    @FXML
    private void generarInforme(ActionEvent event) {
        try {
            // Compilar el archivo jrxml para obtener el reporte
            JasperReport report = JasperCompileManager.compileReport("src/main/java/clases/report2.jrxml");

            // Parámetros del reporte, si es que los tienes
            Map<String, Object> parametros = new HashMap<>();

            // Llenar el reporte con datos de la base de datos
            JasperPrint print = JasperFillManager.fillReport(report, parametros, DatabaseConnector.conectar());

            // Crear un visor de JasperReports y establecer su tamaño
            JRViewer viewer = new JRViewer(print);
            viewer.setPreferredSize(new Dimension((int) panelGeneral.getWidth(), (int) panelGeneral.getHeight()));

            // Establecer el factor de zoom (opcional)
            double zoomFactor = 0.7; // Zoom del 70%
            double originalWidth = print.getPageWidth();
            double scaledWidth = originalWidth * zoomFactor;
            viewer.setZoomRatio((float) (scaledWidth / originalWidth));

            // Crear un SwingNode para integrar el visor en JavaFX
            SwingNode swingNode = new SwingNode();
            swingNode.setContent(viewer);

            // Limpiar el panel y agregar el visor
            panelGeneral.getChildren().clear();
            panelGeneral.getChildren().add(swingNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


