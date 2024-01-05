package com.example.lugaresturisticos.Modelos;

public class Categorias {
    private String id;
    private String descripcion;

    public Categorias(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

