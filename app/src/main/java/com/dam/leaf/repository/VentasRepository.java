package com.dam.leaf.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.dam.leaf.dao.AppDb;
import com.dam.leaf.dao.VentasDao;
import com.dam.leaf.model.Venta;

import java.util.List;

public class VentasRepository {
    private VentasDao ventasDao;
    private VentasRepository.OnResultCallback callback;

    public VentasRepository(Application application, VentasRepository.OnResultCallback context){
        AppDb db = AppDb.getInstance(application);
        ventasDao = db.ventasDao();
        callback = context;
    }


    public interface OnResultCallback<T>{
        void onResultVenta(List<T> result);
        void onResultVenta(T result);
    }

    public void insertVenta(Venta venta){
        new VentasRepository.InsertarVenta(ventasDao).execute(venta);
    }

    public void findVentaById(Long id){
        new VentasRepository.BuscarVentaById(ventasDao,callback).execute(id);
    }

    public void findAllVentas(){
        new VentasRepository.BuscarVentas(ventasDao,callback).execute();
    }

    public void updateVenta(Venta venta){
        new VentasRepository.ActualizarVentas(ventasDao).execute(venta);
    }

    public void deleteVenta(Venta venta){
        new VentasRepository.BorrarVenta(ventasDao).execute(venta);
    }

    public void findVentasCompletas(){
        new VentasRepository.BuscarVentasCompletas(ventasDao,callback).execute();
    }


    private class InsertarVenta extends AsyncTask<Venta,Void,Void> {


        private VentasDao ventasDao;

        public InsertarVenta(VentasDao ventasDao) {
            this.ventasDao = ventasDao;
        }


        @Override
        protected Void doInBackground(Venta... ventas) {
            ventasDao.insert(ventas[0]);
            return null;
        }
    }

    private class BuscarVentaById extends AsyncTask<Long,Void,Venta> {

        private VentasDao ventasDao;
        private VentasRepository.OnResultCallback callback;

        public BuscarVentaById(VentasDao ventasDao, VentasRepository.OnResultCallback context) {
            this.ventasDao=ventasDao;
            this.callback=context;
        }

        @Override
        protected Venta doInBackground(Long... longs) {
            return ventasDao.getById(longs[0].toString());
        }

        @Override
        protected void onPostExecute(Venta venta) {
            super.onPostExecute(venta);
            callback.onResultVenta(venta);
        }
    }

    private class BuscarVentas extends AsyncTask<Void,Void,List<Venta>> {

        private VentasDao ventasDao;
        private VentasRepository.OnResultCallback callback;

        public BuscarVentas(VentasDao ventasDao, VentasRepository.OnResultCallback callback) {
            this.ventasDao=ventasDao;
            this.callback=callback;
        }

        @Override
        protected List<Venta> doInBackground(Void... voids) {
            return ventasDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Venta> ventas) {
            super.onPostExecute(ventas);

            callback.onResultVenta(ventas);
        }
    }

    private class ActualizarVentas extends AsyncTask<Venta,Void,Void>{
        private VentasDao ventasDao;


        public ActualizarVentas(VentasDao ventasDao) {
            this.ventasDao = ventasDao;
        }

        @Override
        protected Void doInBackground(Venta... ventas) {
            ventasDao.update(ventas[0]);
            return null;
        }
    }

    private class BorrarVenta extends AsyncTask<Venta,Void,Void> {
        private VentasDao ventasDao;
        public BorrarVenta(VentasDao ventasDao) {
            this.ventasDao=ventasDao;
        }

        @Override
        protected Void doInBackground(Venta... ventas) {
            ventasDao.delete(ventas[0]);
            return null;
        }
    }

    private class BuscarVentasCompletas extends AsyncTask<Void,Void,List<Venta>> {

        private VentasDao ventasDao;
        private VentasRepository.OnResultCallback callback;

        public BuscarVentasCompletas(VentasDao ventasDao, VentasRepository.OnResultCallback callback) {
            this.ventasDao=ventasDao;
            this.callback=callback;
        }

        @Override
        protected List<Venta> doInBackground(Void... voids) {
            return ventasDao.getPagosCompletos();
        }

        @Override
        protected void onPostExecute(List<Venta> ventas) {
            super.onPostExecute(ventas);
            callback.onResultVenta(ventas);
        }
    }

}
