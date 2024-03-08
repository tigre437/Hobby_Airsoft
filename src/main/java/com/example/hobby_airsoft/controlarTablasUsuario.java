package com.example.hobby_airsoft;

import com.example.hobby_airsoft.retrofit.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private Button btnInforme;

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
    
    private Call<List<Usuario>> callLeer;
    
    private Call<Usuario> callBorrar;
    
    private String baseUrl = "http://localhost:81/crud/";
    
    private Retrofit retrofit;

    
    public void atras(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/principal.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/Usuario.fxml"));
            controlarUsuario controller = fxmlLoader.getController();
            Parent root = fxmlLoader.load();
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
                    usuarioSeleccionado.getNombre(), usuarioSeleccionado.getApellidos(), usuarioSeleccionado.getNick());

            Optional<ButtonType> respuesta = alertas.mostrarAlertaConfirmacion("Confirmar Eliminación", "Eliminar Usuario", mensaje);

            // Si el usuario confirma, eliminar el usuario de la base de datos
            System.out.println(respuesta.get());
            if (respuesta.get() == ButtonType.OK) {
                eliminarUsuarioDeBaseDeDatos(usuarioSeleccionado);

                // Limpia la selección en la TableView
                tbtJugadores.getSelectionModel().clearSelection();
                alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario Eliminado",
                        String.format("El usuario %s %s ha sido eliminado.", usuarioSeleccionado.getNombre(),
                                usuarioSeleccionado.getApellidos()));
            }
        } else {
            alertas.mostrarAlerta(Alert.AlertType.WARNING, "Error", "Fila no seleccionada",
                    "No se ha seleccionado la fila a eliminar");
        }
    }
    
    
    private void eliminarUsuarioDeBaseDeDatos(Usuario usuario) {
        BorrarJugador servicioBorrar = retrofit.create(BorrarJugador.class);
        callBorrar = servicioBorrar.borrarUsuario(usuario.getId());
        try {       
            callBorrar.execute();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            cargarDatosDesdeBD();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void initialize() {
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tcApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellidos()));
        tcNick.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNick()));
        tcTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        tcCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));

        Gson gson = new GsonBuilder().setLenient().create();
        //Instancia a retrofit agregando la baseURL y el convertidor GSON
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        
        
        try {
            cargarDatosDesdeBD();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/Usuario.fxml"));
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
            controller.txtApellido.setText(usuario.getApellidos());
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


    private void cargarDatosDesdeBD() throws IOException {
        LeerUsuario servicioLeer = retrofit.create(LeerUsuario.class);
        tbtJugadores.getItems().clear();
        callLeer = servicioLeer.getUsuario();
        List<Usuario> listausuarios = callLeer.execute().body();
        if(listausuarios != null){
            ObservableList<Usuario> observableUsuarios = FXCollections.observableArrayList(listausuarios);
            tbtJugadores.setItems(observableUsuarios);
        }
    }
    
    @FXML
    private void lanzarinforme() throws SQLException {
        try {
           InputStream inputStream = getClass().getResourceAsStream("/com/example/hobby_airsoft/listadoJugadores.jrxml");
           JasperReport report = JasperCompileManager.compileReport(inputStream);
           Map parametros = new HashMap<>();
           JasperPrint print = JasperFillManager.fillReport(report, parametros, DatabaseConnector.conectar());
           JasperViewer.viewReport(print, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
