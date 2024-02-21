package com.example.hobby_airsoft;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Usuario {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty nombre = new SimpleStringProperty();
    private final SimpleStringProperty apellido = new SimpleStringProperty();
    private final SimpleStringProperty nick = new SimpleStringProperty();
    private final SimpleStringProperty telefono = new SimpleStringProperty();
    private final SimpleStringProperty correo = new SimpleStringProperty();

    public Usuario() {
        // Constructor vacío necesario para JavaFX
    }

    public Usuario(int id, String nombre, String apellido, String nick, String telefono, String correo) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.apellido.set(apellido);
        this.nick.set(nick);
        this.telefono.set(telefono);
        this.correo.set(correo);
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

    public String getApellido() {
        return apellido.get();
    }

    public SimpleStringProperty apellidoProperty() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido.set(apellido);
    }

    public String getNick() {
        return nick.get();
    }

    public SimpleStringProperty nickProperty() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick.set(nick);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public SimpleStringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public String getCorreo() {
        return correo.get();
    }

    public SimpleStringProperty correoProperty() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo.set(correo);
    }
}
