package com.example.demo.entities;

public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imageUrl;

    public Servicio() {}

    public Servicio(String imageUrl, String nombre, String descripcion) {
        this.imageUrl = imageUrl;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Servicio(Long id, String nombre, String descripcion, String imageUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
