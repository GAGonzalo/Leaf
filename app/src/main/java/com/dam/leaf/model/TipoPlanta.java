package com.dam.leaf.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TipoPlanta {
    @PrimaryKey
    private Long id;
    private String tipo;

    public TipoPlanta(Long id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }
}
