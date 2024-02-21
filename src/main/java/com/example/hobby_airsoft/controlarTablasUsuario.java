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
import java.util.Optional;

public class controlarTablasUsuario {

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
    private TableView<Usuario> tbtJugadores;

    @FXML
    private TableColumn<Usuario, Integer> tcId;

    @FXML
    private TableColumn<Usuario, String> tcNombre;

    @FXML
    private TableColumn<Usuario, String> tcApellido;

    @FXML
    private TableColumn<Usuario, String> tcNick;

    @FXML
    private TableColumn<Usuario, String> tcTelefono;

    @FXML
    private TableColumn<Usuario, String> tcCorreo;

    @FXML
    private TextField tfFiltro;

    public void atras(ActionEvent event) {
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
    private void añadir(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Usuario.fxml"));
            Parent root = fxmlLoader.load();
            controlarUsuario controller = fxmlLoader.getController();
            controller.setLabelText("Añadir Usuario");
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.showAndWait();
            cargarDatosDesdeBD();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        // Obtener el usuario seleccionado
        Usuario usuarioSeleccionado = tbtJugadores.getSelectionModel().getSelectedItem();

        // Verificar si hay un usuario seleccionado
        if (usuarioSeleccionado != null) {
            // Llamar al método para editar el usuario
            editarUsuario(usuarioSeleccionado);
        } else {
            alertas.mostrarAlerta(Alert.AlertType.WARNING, "Error", "Fila no seleccionada", "No se ha seleccionado la fila a editar");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        // Obtener el usuario seleccionado
        Usuario usuarioSeleccionado = tbtJugadores.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            String mensaje = String.format("¿Estás seguro de que deseas eliminar a %s %s (%s)?",
                    usuarioSeleccionado.getNombre(), usuarioSeleccionado.getApellido(), usuarioSeleccionado.getNick());

            Optional<ButtonType> respuesta = alertas.mostrarAlertaConfirmacion("Confirmar Eliminación", "Eliminar Usuario", mensaje);

            // Si el usuario confirma, eliminar el usuario de la base de datos
            System.out.println(respuesta.get());
            if (respuesta.get() == ButtonType.OK) {
                eliminarUsuarioDeBaseDeDatos(usuarioSeleccionado);

                // Limpia la selección en la TableView
                tbtJugadores.getSelectionModel().clearSelection();
                alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario Eliminado",
                        String.format("El usuario %s %s ha sido eliminado.", usuarioSeleccionado.getNombre(),
                                usuarioSeleccionado.getApellido()));
            }
        } else {
            alertas.mostrarAlerta(Alert.AlertType.WARNING, "Error", "Fila no seleccionada",
                    "No se ha seleccionado la fila a eliminar");
        }
    }



    private void eliminarUsuarioDeBaseDeDatos(Usuario usuario) {
        try (Connection connection = DatabaseConnector.conectar()) {
            String query = "DELETE FROM Jugadores WHERE id = ?";
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
        tcId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        tcNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        tcApellido.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        tcNick.setCellValueFactory(cellData -> cellData.getValue().nickProperty());
        tcTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        tcCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        cargarDatosDesdeBD();

        // Agregar ToolTips a los botones
        Tooltip tooltipAtras = new Tooltip("Volver a la pantalla principal");
        Tooltip tooltipAnadir = new Tooltip("Añadir nuevo usuario");
        Tooltip tooltipEditar = new Tooltip("Editar usuario seleccionado");
        Tooltip tooltipEliminar = new Tooltip("Eliminar usuario seleccionado");

        btnAtras.setTooltip(tooltipAtras);
        btnAñadir.setTooltip(tooltipAnadir);
        btnEditar.setTooltip(tooltipEditar);
        btnEliminar.setTooltip(tooltipEliminar);


        // Configurar el manejador de eventos para el doble clic en la tabla
        tbtJugadores.setRowFactory(tv -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && !row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY)) {
                        // Obtener el usuario seleccionado
                        Usuario usuarioSeleccionado = row.getItem();

                        // Llamar al método para editar el usuario
                        editarUsuario(usuarioSeleccionado);
                    }
                }
            });
            return row;
        });
    }


    private void editarUsuario(Usuario usuario) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Usuario.fxml"));
            Scene scene = new Scene(fxmlLoader.load());


            controlarUsuario controller = fxmlLoader.getController();

            // Configura el nuevo escenario para la ventana emergente
            Stage newStage = new Stage();
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(tbtJugadores.getScene().getWindow());
            newStage.setScene(scene);
            controller.lblTexto.setText("Editar Usuario");

            // Llama al método en el controlador para establecer el texto
            controller.id = usuario.getId();
            controller.txtNombre.setText(usuario.getNombre());
            controller.txtApellido.setText(usuario.getApellido());
            controller.txtNick.setText(usuario.getNick());
            controller.txtTelefono.setText(usuario.getTelefono());
            controller.txtCorreo.setText(usuario.getCorreo());

            // Mostrar la nueva ventana emergente
            newStage.showAndWait();

            // Actualizar la tabla después de la edición
            cargarDatosDesdeBD();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al abrir la ventana de editar usuario", e.getMessage());
        }
    }


    private void cargarDatosDesdeBD() {
        tbtJugadores.getItems().clear();
        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "SELECT * FROM Jugadores";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<Usuario> datos = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        Usuario usuario = new Usuario(
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("apellido"),
                                resultSet.getString("nick"),
                                resultSet.getString("telefono"),
                                resultSet.getString("correo")
                        );
                        datos.add(usuario);
                    }
                    tbtJugadores.setItems(datos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
