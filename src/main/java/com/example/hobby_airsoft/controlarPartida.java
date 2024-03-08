package com.example.hobby_airsoft;

import com.example.hobby_airsoft.retrofit.ActualizarPartida;
import com.example.hobby_airsoft.retrofit.InsertarPartida;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    
    private Call<Partida> callInsertar;  
    
    private String baseUrl = "http://localhost:81/crud/";
    
    private Retrofit retrofit;
    
    private Call<Partida> callActualizar;
    

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
        String portadaPath = "/com/example/hobby_airsoft/portadas/" + txtPortada.getText(); // Ruta del recurso en el paquete
        InputStream inputStream = getClass().getResourceAsStream(portadaPath);
        if (inputStream == null) {
            return "La imagen seleccionada no es válida.";
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
        try {
            // Obtén el flujo de entrada del archivo seleccionado
            InputStream inputStream = new FileInputStream(selectedFile);

            // Construye la ruta completa del directorio de imágenes relativo al paquete
            String directorioImagenes = "com/example/hobby_airsoft/portadas";

            // Obtén la ruta del directorio de imágenes como un recurso
            URL urlDirectorio = getClass().getClassLoader().getResource(directorioImagenes);

            // Verifica si el directorio de imágenes existe
            if (urlDirectorio != null) {
                // Construye la ruta completa del archivo de destino
                String nombreArchivo = selectedFile.getName();
                String destinoPath = urlDirectorio.getPath() + "/" + nombreArchivo;

                // Corregir la URL para que pueda ser utilizada como URI válida
                URI uri = urlDirectorio.toURI();
                Path path = Paths.get(uri);

                // Agregar el nombre del archivo al path
                Path archivoDestinoPath = path.resolve(nombreArchivo);

                // Copiar el archivo al directorio de imágenes
                Files.copy(inputStream, archivoDestinoPath, StandardCopyOption.REPLACE_EXISTING);

                // Establecer la ruta en el campo de texto
                txtPortada.setText(archivoDestinoPath.toString());
            } else {
                System.err.println("El directorio de imágenes no se encontró.");
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al copiar la imagen",
                    "Hubo un error al copiar la imagen: " + e.getMessage());
        }
    }



    }


    @FXML
    void initialize() {
        Gson gson = new GsonBuilder().setLenient().create();
        //Instancia a retrofit agregando la baseURL y el convertidor GSON
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
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
        String descripcion = txtDescripcion.getText();
        String portadaPath = txtPortada.getText();

        // Extraer el nombre del archivo de la ruta completa
        String nombreArchivo = new File(portadaPath).getName();

        return new Partida(id, nombre, lugar, fecha.toString(), descripcion, nombreArchivo);
    }

    private void insertarPartidaEnBaseDeDatos(Partida partida) throws SQLException {
        if (!lblTexto.getText().equals("Editar Partida")) {
            InsertarPartida servicioInsertar = retrofit.create(InsertarPartida.class);
            callInsertar = servicioInsertar.insertarPartida(partida);
            RequestBody requestBody = callInsertar.request().body();
            encolaInsercion();
        }else{
            ActualizarPartida servicioActualizar = retrofit.create(ActualizarPartida.class);
            callActualizar = servicioActualizar.actualizarPartida(partida);
            encolaActualizar();
        }
        
    }
    
    
    public void encolaInsercion() {

        callInsertar.enqueue(new Callback<Partida>() {

            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Partida> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
//es un método de la clase Platform de JavaFX que permite ejecutar una tarea en el hilo de interfaz de usuario (UI thread) de forma asíncrona.
            public void onResponse(Call<Partida> call, Response<Partida> response) {
                Platform.runLater(() -> { //
                    System.out.println(call.request().body().toString());
                    System.out.println(response.code());
                    System.out.println("Respuesta INSERTAR: " + response.message());
                    if (response.isSuccessful()) {
                        
                            alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partida Guardada",
                        "La información de la partida ha sido guardada exitosamente.");
                        
                    } else {
                        
                        
                            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Partida no Guardada",
                        "La información de la partida no ha sido guardada exitosamente.");
                        
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
        callActualizar.enqueue(new Callback<Partida>() {

            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Partida> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
//es un método de la clase Platform de JavaFX que permite ejecutar una tarea en el hilo de interfaz de usuario (UI thread) de forma asíncrona.
            public void onResponse(Call<Partida> call, Response<Partida> response) {
                Platform.runLater(() -> { //
                    System.out.println(response.code());
                    System.out.println("Respuesta INSERTAR: " + response.message());
                    if (response.isSuccessful()) {
                        
                            alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partida Guardada",
                        "La información de la partida ha sido guardada exitosamente.");
                        
                    } else {
                        
                        
                            alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Partida no Guardada",
                        "La información de la partida no ha sido guardada exitosamente.");
                        
                    }
                });
            }
        });
    }
}

