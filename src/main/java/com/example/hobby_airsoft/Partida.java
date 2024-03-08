package com.example.hobby_airsoft;

public class Partida {

    private int id;
    private String  nombre;
    private String lugar;
    private String fecha;
    private String descripcion;
    private String portada;

    public Partida() {
    }

    public Partida(int id, String nombre, String lugar, String fecha, String descripcion, String portada) {
        this.id = id;
        this.nombre = nombre;
        if(lugar == null){
            this.lugar = null;
        }else {
            this.lugar = lugar;
        }
        if(fecha == null){
            this.fecha = null;
        }else {
            this.fecha = fecha;
        }
        if(descripcion == null){
            this.descripcion = null;
        }else {
            this.descripcion = descripcion;
        }

        if(portada == null){
            this.portada = null;
        }else {
            this.portada = portada;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    

    public String getPortada() {
        if (portada != null) {
            return portada;
        }
        else{
            return "";
        }
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }


}
