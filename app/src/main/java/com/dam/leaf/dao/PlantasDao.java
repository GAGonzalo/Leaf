package com.dam.leaf.dao;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dam.leaf.model.Planta;

import java.util.List;

@Dao
public interface PlantasDao {
    @Insert
    void insert(Planta planta);

    @Delete
    void delete(Planta planta);

    @Update
    void update(Planta planta);

    @Query("SELECT * FROM planta WHERE id = :id LIMIT 1")
    Planta getById(String id);

    @Query("SELECT * FROM planta")
    List<Planta> getAll();

}
