package com.example.demo.entities;

public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imageUrl;
    private Double price;

    public Servicio() {}

    public Servicio(String imageUrl, String nombre, String descripcion) {
        this.imageUrl = imageUrl;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Servicio(String imageUrl, String nombre, String descripcion, Double price) {
        this.imageUrl = imageUrl;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.price = price;
    }

    public Servicio(Long id, String nombre, String descripcion, String imageUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
    }

    public Servicio(Long id, String nombre, String descripcion, String imageUrl, Double price) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
