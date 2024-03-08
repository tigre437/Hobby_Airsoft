package com.example.hobby_airsoft;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.stage.Stage;

        import java.io.ByteArrayInputStream;
        import java.io.File;
        import java.io.IOException;
import java.io.InputStream;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Base64;
import java.util.HashMap;
        import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class controlarPartidasPrinicipal {

    public Label lblNoHayPartidas;
    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnOp1;

    @FXML
    private Button btnOp2;

    @FXML
    private Button btnOp3;

    @FXML
    private Button btnSiguiente;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private Label lblOpcion1;

    @FXML
    private Label lblOpcion2;

    @FXML
    private Label lblOpcion3;
    
    @FXML
    private Button btnInforme;

    @FXML
    private TextField txtBuscador;

    private List<Partida> partidas = new ArrayList<>();
    private int indiceActual = 0;

    @FXML
    void Opcion1(ActionEvent event) {
        int offset = 0;
        Partida partida = partidas.get((indiceActual + offset) % partidas.size());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/tablaJugadores.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            controlarTablasJugador controller = fxmlLoader.getController();
            controller.setId_partida(partida.getId());
            System.out.println(controller.getId_partida());
            controller.setLabelText(partida.getNombre());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void Opcion2(ActionEvent event) {
        int offset = 1;
        Partida partida = partidas.get((indiceActual + offset) % partidas.size());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/tablaJugadores.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            controlarTablasJugador controller = fxmlLoader.getController();
            controller.setLabelText(partida.getNombre());
            controller.setId_partida(partida.getId());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void Opcion3(ActionEvent event) {
        int offset = 2;
        Partida partida = partidas.get((indiceActual + offset) % partidas.size());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/tablaJugadores.fxml"));


            Scene scene = new Scene(fxmlLoader.load());
            controlarTablasJugador controller = fxmlLoader.getController();
            controller.setLabelText(partida.getNombre());
            controller.setId_partida(partida.getId());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void atras(ActionEvent event) {
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
    void derecha(ActionEvent event) {
        indiceActual = (indiceActual + 1) % partidas.size();
        cargarImagenesPartidaActual();
    }

    @FXML
    void izquierda(ActionEvent event) {
        indiceActual = (indiceActual - 1 + partidas.size()) % partidas.size();
        cargarImagenesPartidaActual();
    }

    @FXML
    void Buscar(ActionEvent event) {
        String textoBusqueda = txtBuscador.getText().toLowerCase().trim();

        if (textoBusqueda.isEmpty()) {
            cargarPartidasDesdeBD();
            configurarVisibilidad();

            // Cargar la primera partida
            cargarImagenesPartidaActual();
        } else {
            // Filtrar la lista de partidas basándose en el texto del buscador
            List<Partida> partidasFiltradas = new ArrayList<>();
            for (Partida partida : partidas) {
                if (partida.getNombre().toLowerCase().contains(textoBusqueda)) {
                    partidasFiltradas.add(partida);
                }
            }

            // Actualizar la lista de partidas y cargar la primera partida (si hay alguna)
            partidas = partidasFiltradas;
            if (partidasFiltradas.size() != 0) {
                indiceActual = 0;

                cargarImagenesPartidaActual();
                configurarVisibilidad();
            } else {
                cargarPartidasDesdeBD();
                alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se ha encontrado ninguna partida", "Pruebe con un filtro un poco mas amplio");
            }
        }
    }

    @FXML
    void initialize() {
        // Cargar la lista de partidas desde la base de datos
        Tooltip tooltipAtras = new Tooltip("Ir a la pantalla principal");
        Tooltip tooltipIzquierda = new Tooltip("Ver la partida anterior");
        Tooltip tooltipDerecha = new Tooltip("Ver la siguiente partida");
        Tooltip tooltipOpcion1 = new Tooltip("Ir a la tabla de la partida 1");
        Tooltip tooltipOpcion2 = new Tooltip("Ir a la tabla de la partida 2");
        Tooltip tooltipOpcion3 = new Tooltip("Ir a la tabla de la partida 3");

        btnAtras.setTooltip(tooltipAtras);
        btnAnterior.setTooltip(tooltipIzquierda);
        btnSiguiente.setTooltip(tooltipDerecha);
        btnOp1.setTooltip(tooltipOpcion1);
        btnOp2.setTooltip(tooltipOpcion2);
        btnOp3.setTooltip(tooltipOpcion3);

        cargarPartidasDesdeBD();

        configurarVisibilidad();

        // Cargar la primera partida
        cargarImagenesPartidaActual();
    }

    private void configurarVisibilidad() {
        int size = partidas.size();

        // Configurar visibilidad de botones
        btnAtras.setVisible(size > 0);
        btnSiguiente.setVisible(size > 0);

        // Configurar visibilidad de botones de opción
        btnOp1.setVisible(size >= 1);
        btnOp2.setVisible(size >= 2);
        btnOp3.setVisible(size >= 3);

        // Configurar visibilidad de etiquetas
        lblOpcion1.setVisible(size >= 1);
        lblOpcion2.setVisible(size >= 2);
        lblOpcion3.setVisible(size >= 3);

        // Configurar visibilidad del mensaje "NO HAY PARTIDAS"
        lblNoHayPartidas.setVisible(size == 0);
    }

    private void cargarPartidasDesdeBD() {
        try (Connection connection = DatabaseConnector.conectar()) {
            String sql = "SELECT * FROM Partidas";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    partidas = new ArrayList<>();
                    while (resultSet.next()) {
                        Partida partida = new Partida(
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("lugar"),
                                resultSet.getString("fecha"),
                                resultSet.getString("descripcion"),
                                resultSet.getString("portada")
                        );
                        partidas.add(partida);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarImagenesPartidaActual() {
        List<ImageView> imageList = Arrays.asList(image1, image2, image3);
        List<Label> labelList = Arrays.asList(lblOpcion1, lblOpcion2, lblOpcion3);

        for (int i = 0; i < imageList.size(); i++) {
            int index = (indiceActual + i) % partidas.size();
            Partida partidaActual = partidas.get(index);

            // Obtener la ruta de la imagen desde la base de datos
            String rutaImagen = "/portadas/" + partidaActual.getPortada();
            Label lbl = labelList.get(i);
            lbl.setText(partidaActual.getNombre());
            // Crear una nueva Image desde la ruta de la imagen
            Image nuevaImagen = new Image(new File(rutaImagen).toURI().toString());

            // Establecer la nueva Image en la ImageView correspondiente a la posición actual
            ImageView imageViewActual = imageList.get(i);
            imageViewActual.setImage(nuevaImagen);


        }
    }
    
    @FXML
    private void GenerarInforme() throws SQLException {
        try {
           InputStream inputStream = getClass().getResourceAsStream("graficoAsistencia.jrxml");
           JasperReport report = JasperCompileManager.compileReport(inputStream);
           Map parametros = new HashMap<>();
           JasperPrint print = JasperFillManager.fillReport(report, parametros, DatabaseConnector.conectar());
           JasperViewer.viewReport(print, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
