package com.proyecto.sistembebidosii;

public class Ubicacion {
    int id;
    String titulo;
    String direccion;
    double latitud;
    double longitud;
    int activo;

    public Ubicacion(){
    }

    public Ubicacion(String titulo, String direccion, double latitud, double longitud, int activo){
        this.titulo = titulo;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activo = activo;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getTitulo(){
        return this.titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public double getLatitud(){
        return this.latitud;
    }

    public void setLatitud(double latitud){
        this.latitud = latitud;
    }

    public double getLongitud(){
        return this.longitud;
    }

    public void setLongitud(double longitud){
        this.longitud = longitud;
    }

    public int getActivo(){
        return this.activo;
    }

    public void setActivo(int activo){
        this.activo = activo;
    }
}