package com.example.hobby_airsoft;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Partida {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty lugar;
    private final SimpleStringProperty fecha;
    private final SimpleStringProperty descripcion;
    private final SimpleStringProperty portada;

    public Partida() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.lugar = new SimpleStringProperty();
        this.fecha = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.portada = new SimpleStringProperty();
    }

    public Partida(int id, String nombre, String lugar, String fecha, String descripcion, String portada) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        if(lugar == null){
            this.lugar = null;
        }else {
            this.lugar = new SimpleStringProperty(lugar);
        }
        if(fecha == null){
            this.fecha = null;
        }else {
            this.fecha = new SimpleStringProperty(fecha);
        }
        if(descripcion == null){
            this.descripcion = null;
        }else {
            this.descripcion = new SimpleStringProperty(descripcion);
        }

        if(portada == null){
            this.portada = null;
        }else {
            this.portada = new SimpleStringProperty(portada);
        }
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getLugar() {
        if (lugar != null) {
            return lugar.get();
        }else {
            return "";
        }
    }

    public SimpleStringProperty lugarProperty() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar.set(lugar);
    }

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getDescripcion() {
        if (descripcion != null) {
            return descripcion.get();
        }else{
            return "";
        }
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getPortada() {
        if (portada != null) {
            return portada.get();
        }
        else{
            return "";
        }
    }


}
