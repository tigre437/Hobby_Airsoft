package com.example.hobby_airsoft;

import com.example.hobby_airsoft.retrofit.ActualizarJugador;
import com.example.hobby_airsoft.retrofit.InsertarJugador;
import com.example.hobby_airsoft.retrofit.LeerUsuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    
    private Call<Usuario> callInsertar;  
    
    private String baseUrl = "http://localhost:81/crud/";
    
    private Retrofit retrofit;
    
    private Call<Usuario> callActualizar;

    public controlarUsuario() {
    }

    
    

    @FXML
    private void Aceptar() {
        try {
            Usuario nuevoUsuario = obtenerDatosUsuario();

            // Realizar comprobaciones de campos
            if (comprobarCampos(nuevoUsuario)) {
                insertarUsuarioEnBaseDeDatos(nuevoUsuario);
                Stage stage = (Stage) btnAceptar.getScene().getWindow();
                stage.close();
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
        if (usuario.getNombre().isEmpty() || usuario.getApellidos().isEmpty()) {
            mostrarNotificacionError("Nombre y apellido son campos obligatorios.");
            return false;
        }

        if (usuario.getTelefono() != null && usuario.getTelefono().length() != 9) {
            mostrarNotificacionError("El teléfono debe tener 9 caracteres.");
            return false;
        }

        if (!usuario.getNombre().matches("[\\p{L} ]+") || !usuario.getApellidos().matches("[\\p{L} ]+")) {
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
        System.out.println(lblTexto.getText());
        if (!lblTexto.getText().equals("Editar Usuario")) {
            InsertarJugador servicioInsertar = retrofit.create(InsertarJugador.class);
            callInsertar = servicioInsertar.insertarJugador(usuario);
            encolaInsercion();
        }else{
            ActualizarJugador servicioActualizar = retrofit.create(ActualizarJugador.class);
            callActualizar = servicioActualizar.actualizarJugador(usuario);
            encolaActualizar();
        }
        
    }

    @FXML
    private void Cancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        Gson gson = new GsonBuilder().setLenient().create();
        //Instancia a retrofit agregando la baseURL y el convertidor GSON
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
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
    
    
    public void encolaInsercion() {
        //la llamada es asíncrona: 
        //Retrofit descarga y analiza los datos del API en un subproceso en
        //paralelo y entrega los resultados a traves de los metodos
        //onFailure o onResponse
        //Si se usa enqueue sigue con el procesamiento en las líneas posteriores
        callInsertar.enqueue(new Callback<Usuario>() {

            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
//es un método de la clase Platform de JavaFX que permite ejecutar una tarea en el hilo de interfaz de usuario (UI thread) de forma asíncrona.
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Platform.runLater(() -> { //
                    System.out.println(response.code());
                    System.out.println("Respuesta INSERTAR: " + response.message());
                    if (response.isSuccessful()) {
                        
                            alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario añadido",
                                    "El usuario se ha añadido correctamente.");
                        
                    } else {
                       
                            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no añadido",
                                    "El usuario no se ha añadido correctamente.");
                        
                    }
                });
            }
        });
    }
    
    public void encolaActualizar() {
        //la llamada es asíncrona: 
        //Retrofit descarga y analiza los datos del API en un subproceso en
        //paralelo y entrega los resultados a traves de los metodos
        //onFailure o onResponse
        //Si se usa enqueue sigue con el procesamiento en las líneas posteriores
        callActualizar.enqueue(new Callback<Usuario>() {

            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
//es un método de la clase Platform de JavaFX que permite ejecutar una tarea en el hilo de interfaz de usuario (UI thread) de forma asíncrona.
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Platform.runLater(() -> { //
                    System.out.println(response.code());
                    System.out.println("Respuesta INSERTAR: " + response.message());
                    if (response.isSuccessful()) {
                        
                            alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario editado",
                                    "El usuario se ha editado correctamente.");
                        
                    } else {
                        
                        
                            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no editado",
                                    "El usuario no se ha editado correctamente.");
                        
                    }
                });
            }
        });
    }
}
