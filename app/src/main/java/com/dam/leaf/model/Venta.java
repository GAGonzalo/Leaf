package com.dam.leaf.model;

import java.sql.Date;
import java.util.List;

public class Venta {
    private Long id;
    private List<Planta> pedido;
    private Cliente cliente;
    private Date fecha_venta;

    public Venta(Long id, List<Planta> pedido, Cliente cliente, Date fecha_venta) {
        this.id = id;
        this.pedido = pedido;
        this.cliente = cliente;
        this.fecha_venta = fecha_venta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Planta> getPedido() {
        return pedido;
    }

    public void setPedido(List<Planta> pedido) {
        this.pedido = pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(Date fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", cliente=" + cliente +
                ", fecha_venta=" + fecha_venta +
                '}';
    }
}
