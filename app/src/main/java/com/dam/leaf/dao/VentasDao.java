package com.dam.leaf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dam.leaf.model.Venta;

import java.util.List;

@Dao
public interface VentasDao {
    @Insert
    void insert(Venta venta);

    @Delete
    void delete(Venta venta);

    @Update
    void update(Venta venta);

    @Query("SELECT * FROM venta WHERE id = :id LIMIT 1")
    Venta getById(String id);

    @Query("SELECT * FROM venta")
    List<Venta> getAll();
}
