package com.dam.leaf.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.dam.leaf.dao.AppDb;
import com.dam.leaf.dao.TipoPlantasDao;
import com.dam.leaf.model.TipoPlanta;

import java.util.List;

public class TipoPlantasRepository {
    private TipoPlantasDao tipoPlantasDao;
    private TipoPlantasRepository.OnResultCallback callback;

    public TipoPlantasRepository(Application application, TipoPlantasRepository.OnResultCallback context){
        AppDb db = AppDb.getInstance(application);
        tipoPlantasDao = db.tipoPlantasDao();
        callback = context;
    }


    public interface OnResultCallback<T>{
        void onResultTipoPlanta(List<T> result);
        void onResultTipoPlanta(T result);
    }

    public void insertTipoPlanta(TipoPlanta tipoPlanta){
        new TipoPlantasRepository.InsertarTipoPlanta(tipoPlantasDao).execute(tipoPlanta);
    }

    public void findTipoPlantaById(Long id){
        new TipoPlantasRepository.BuscarTipoPlantaById(tipoPlantasDao,callback).execute(id);
    }

    public void findAllTipoPlantas(){
        new TipoPlantasRepository.BuscarTipoPlantas(tipoPlantasDao,callback).execute();
    }

    public void updateTipoPlanta(TipoPlanta tipoPlanta){
        new TipoPlantasRepository.ActualizarTipoPlantas(tipoPlantasDao).execute(tipoPlanta);
    }

    public void deleteTipoPlanta(TipoPlanta tipoPlanta){
        new TipoPlantasRepository.BorrarTipoPlanta(tipoPlantasDao).execute(tipoPlanta);
    }


    private class InsertarTipoPlanta extends AsyncTask<TipoPlanta,Void,Void> {


        private TipoPlantasDao tipoPlantasDao;

        public InsertarTipoPlanta(TipoPlantasDao tipoPlantasDao) {
            this.tipoPlantasDao = tipoPlantasDao;
        }


        @Override
        protected Void doInBackground(TipoPlanta... tipoPlantas) {
            tipoPlantasDao.insert(tipoPlantas[0]);
            return null;
        }
    }

    private class BuscarTipoPlantaById extends AsyncTask<Long,Void,TipoPlanta> {

        private TipoPlantasDao tipoPlantasDao;
        private TipoPlantasRepository.OnResultCallback callback;

        public BuscarTipoPlantaById(TipoPlantasDao tipoPlantasDao, TipoPlantasRepository.OnResultCallback context) {
            this.tipoPlantasDao=tipoPlantasDao;
            this.callback=context;
        }

        @Override
        protected TipoPlanta doInBackground(Long... longs) {
            return tipoPlantasDao.getById(longs[0].toString());
        }

        @Override
        protected void onPostExecute(TipoPlanta tipoPlanta) {
            super.onPostExecute(tipoPlanta);
            callback.onResultTipoPlanta(tipoPlanta);
        }
    }

    private class BuscarTipoPlantas extends AsyncTask<Void,Void,List<TipoPlanta>> {

        private TipoPlantasDao tipoPlantasDao;
        private TipoPlantasRepository.OnResultCallback callback;

        public BuscarTipoPlantas(TipoPlantasDao tipoPlantasDao, TipoPlantasRepository.OnResultCallback callback) {
            this.tipoPlantasDao=tipoPlantasDao;
            this.callback=callback;
        }

        @Override
        protected List<TipoPlanta> doInBackground(Void... voids) {
            return tipoPlantasDao.getAll();
        }

        @Override
        protected void onPostExecute(List<TipoPlanta> tipoPlantas) {
            super.onPostExecute(tipoPlantas);

            callback.onResultTipoPlanta(tipoPlantas);
        }
    }

    private class ActualizarTipoPlantas extends AsyncTask<TipoPlanta,Void,Void>{
        private TipoPlantasDao tipoPlantasDao;


        public ActualizarTipoPlantas(TipoPlantasDao tipoPlantasDao) {
            this.tipoPlantasDao = tipoPlantasDao;
        }

        @Override
        protected Void doInBackground(TipoPlanta... tipoPlantas) {
            tipoPlantasDao.update(tipoPlantas[0]);
            return null;
        }
    }

    private class BorrarTipoPlanta extends AsyncTask<TipoPlanta,Void,Void> {
        private TipoPlantasDao tipoPlantasDao;
        public BorrarTipoPlanta(TipoPlantasDao tipoPlantasDao) {
            this.tipoPlantasDao=tipoPlantasDao;
        }

        @Override
        protected Void doInBackground(TipoPlanta... tipoPlantas) {
            tipoPlantasDao.delete(tipoPlantas[0]);
            return null;
        }
    }
}
