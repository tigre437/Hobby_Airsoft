package com.example.hobby_airsoft;


import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;

import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/hobby_airsoft/FXML/principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("HOBBY AIRSOFT DATABASE");
        stage.setScene(scene);
        stage.show();
        
        scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.F1) {
                    mostrarPopup(stage);
                }
            });
    }
    
    
    private void mostrarPopup(Stage ownerStage) {
        try {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            URL url = getClass().getResource("ayuda.html");
            webEngine.load(url.toExternalForm());

            StackPane root = new StackPane();
            root.getChildren().add(webView);

            Scene scene = new Scene(root, 800, 800);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(scene);
            stage.setTitle("Ayuda");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
} 