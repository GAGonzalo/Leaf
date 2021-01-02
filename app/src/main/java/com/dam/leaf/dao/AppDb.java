package com.dam.leaf.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.dam.leaf.model.Cliente;
import com.dam.leaf.model.Planta;
import com.dam.leaf.model.TipoPlanta;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Planta.class, Cliente.class, TipoPlanta.class}, version = 1)
public abstract class AppDb extends RoomDatabase {
    private static final String DB_NAME="leaf_db";
    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static AppDb INSTANCE;
    public static AppDb getInstance(final Context context) {
        if(INSTANCE ==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDb.class,DB_NAME).build();
        }
        return INSTANCE;
    }

    public abstract PlantasDao plantasDao();

    public abstract ClientesDao clientesDao();

    public abstract TipoPlantasDao tipoPlantasDao();
}
