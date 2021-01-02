package com.dam.leaf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dam.leaf.model.TipoPlanta;

import java.util.List;

@Dao
public interface TipoPlantasDao {
    @Insert
    void insert(TipoPlanta tipoplanta);

    @Delete
    void delete(TipoPlanta tipoplanta);

    @Update
    void update(TipoPlanta tipoplanta);

    @Query("SELECT * FROM tipoplanta WHERE id = :id LIMIT 1")
    TipoPlanta getById(String id);

    @Query("SELECT * FROM tipoplanta")
    List<TipoPlanta> getAll();

}
