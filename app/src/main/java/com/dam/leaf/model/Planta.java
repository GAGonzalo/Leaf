package com.dam.leaf.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.intellij.lang.annotations.Identifier;

@Entity
public class Planta {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Integer cantidad;


    public Planta(Long id, String nombre, String descripcion, String tipo, Integer cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.cantidad = cantidad;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Planta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
