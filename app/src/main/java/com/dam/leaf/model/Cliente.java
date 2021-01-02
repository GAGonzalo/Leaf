package com.dam.leaf.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String nombre_apellido;
    private String numero_telefono;
    private String dni;
    private String direccion;

    public Cliente(Long id, String nombre_apellido, String numero_telefono, String dni, String direccion) {
        this.id = id;
        this.nombre_apellido = nombre_apellido;
        this.numero_telefono = numero_telefono;
        this.dni = dni;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_apellido() {
        return nombre_apellido;
    }

    public void setNombre_apellido(String nombre_apellido) {
        this.nombre_apellido = nombre_apellido;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre_apellido + '\'' +
                ", numero_telefono='" + numero_telefono + '\'' +
                ", dni='" + dni + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
