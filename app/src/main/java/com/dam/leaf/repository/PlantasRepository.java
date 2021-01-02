package com.dam.leaf.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.dam.leaf.dao.AppDb;
import com.dam.leaf.dao.PlantasDao;
import com.dam.leaf.dao.PlantasDao;
import com.dam.leaf.model.Planta;
import com.dam.leaf.model.Planta;

import java.util.List;

public class PlantasRepository {
    private PlantasDao plantasDao;
    private OnResultCallback callback;

    public PlantasRepository(Application application, OnResultCallback context){
        AppDb db = AppDb.getInstance(application);
        plantasDao = db.plantasDao();
        callback = context;
    }


    public interface OnResultCallback<T>{
        void onResult(List<T> result);
        void onResult(T result);
    }

    public void insertPlanta(Planta planta){
        new InsertarPlanta(plantasDao).execute(planta);
    }

    public void findPlantaById(Long id){
        new BuscarPlantaById(plantasDao,callback).execute(id);
    }

    public void findAllPlantas(){
        new BuscarPlantas(plantasDao,callback).execute();
    }

    public void updatePlanta(Planta planta){
        new ActualizarPlantas(plantasDao).execute(planta);
    }

    public void deletePlanta(Planta planta){
        new BorrarPlanta(plantasDao).execute(planta);
    }


    private class InsertarPlanta extends AsyncTask<Planta,Void,Void> {


        private PlantasDao plantasDao;

        public InsertarPlanta(PlantasDao plantasDao) {
            this.plantasDao = plantasDao;
        }


        @Override
        protected Void doInBackground(Planta... plantas) {
            plantasDao.insert(plantas[0]);
            return null;
        }
    }

    private class BuscarPlantaById extends AsyncTask<Long,Void,Planta> {

        private PlantasDao plantasDao;
        private PlantasRepository.OnResultCallback callback;

        public BuscarPlantaById(PlantasDao plantasDao, PlantasRepository.OnResultCallback context) {
            this.plantasDao=plantasDao;
            this.callback=context;
        }

        @Override
        protected Planta doInBackground(Long... longs) {
            return plantasDao.getById(longs[0].toString());
        }

        @Override
        protected void onPostExecute(Planta planta) {
            super.onPostExecute(planta);
            callback.onResult(planta);
        }
    }

    private class BuscarPlantas extends AsyncTask<Void,Void,List<Planta>> {

        private PlantasDao plantasDao;
        private PlantasRepository.OnResultCallback callback;

        public BuscarPlantas(PlantasDao plantasDao, PlantasRepository.OnResultCallback callback) {
            this.plantasDao=plantasDao;
            this.callback=callback;
        }

        @Override
        protected List<Planta> doInBackground(Void... voids) {
            return plantasDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Planta> plantas) {
            super.onPostExecute(plantas);

            callback.onResult(plantas);
        }
    }

    private class ActualizarPlantas extends AsyncTask<Planta,Void,Void>{
        private PlantasDao plantasDao;


        public ActualizarPlantas(PlantasDao plantasDao) {
            this.plantasDao = plantasDao;
        }

        @Override
        protected Void doInBackground(Planta... plantas) {
            plantasDao.update(plantas[0]);
            return null;
        }
    }

    private class BorrarPlanta extends AsyncTask<Planta,Void,Void> {
        private PlantasDao plantasDao;
        public BorrarPlanta(PlantasDao plantasDao) {
            this.plantasDao=plantasDao;
        }

        @Override
        protected Void doInBackground(Planta... plantas) {
            plantasDao.delete(plantas[0]);
            return null;
        }
    }
}
