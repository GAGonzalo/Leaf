package com.dam.leaf.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.List;

@Entity
public class Venta {
    @PrimaryKey
    private Long id;
    private List<Planta> pedido;
    private Float total;
    private Float pago;
    private Cliente cliente;
    private Date fecha_venta;


    public Venta(Long id, List<Planta> pedido, Float total, Float pago, Cliente cliente, Date fecha_venta) {
        this.id = id;
        this.pedido = pedido;
        this.total = total;
        this.pago = pago;
        this.cliente = cliente;
        this.fecha_venta = fecha_venta;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getPago() {
        return pago;
    }

    public void setPago(Float pago) {
        this.pago = pago;
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

    public static class PlantasConverter {
        @TypeConverter
        public List<Planta> fromString(String listString){
            return new Gson().fromJson(listString, new TypeToken<List<Planta>>() {}.getType());
        }

        @TypeConverter
        public String saveList(List<Planta> listOfPlatos) {
            return new Gson().toJson(listOfPlatos);
        }

    }
    public static class ClienteConverter {
        @TypeConverter
        public Cliente fromString(String cliente){
            return new Gson().fromJson(cliente, new TypeToken<Cliente>() {}.getType());
        }

        @TypeConverter
        public String saveCliente(Cliente cliente) {
            return new Gson().toJson(cliente);
        }

    }
    public static class DateConverter {
        @TypeConverter
        public Date fromString(String date){
            return new Gson().fromJson(date, new TypeToken<Date>() {}.getType());
        }

        @TypeConverter
        public String saveDate(Date date) {
            return new Gson().toJson(date);
        }

    }
}
