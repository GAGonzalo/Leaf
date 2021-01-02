package com.dam.leaf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dam.leaf.model.Cliente;

import java.util.List;

@Dao
public interface ClientesDao {
    @Insert
    void insert(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Update
    void update(Cliente cliente);

    @Query("SELECT * FROM cliente WHERE id = :id LIMIT 1")
    Cliente getById(String id);

    @Query("SELECT * FROM cliente")
    List<Cliente> getAll();
}
