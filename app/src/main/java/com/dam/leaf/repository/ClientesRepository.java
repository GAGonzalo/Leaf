package com.dam.leaf.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.dam.leaf.dao.AppDb;
import com.dam.leaf.dao.ClientesDao;
import com.dam.leaf.model.Cliente;

import java.util.List;

public class ClientesRepository {
    private ClientesDao clientesDao;
    private OnResultCallback callback;

    public ClientesRepository(Application application, OnResultCallback context){
        AppDb db = AppDb.getInstance(application);
        clientesDao = db.clientesDao();
        callback = context;
    }


    public interface OnResultCallback<T>{
        void onResult(List<T> result);
        void onResult(T result);
    }

    public void insertCliente(Cliente cliente){
        new ClientesRepository.InsertarCliente(clientesDao).execute(cliente);
    }

    public void findClienteById(Long id){
        new ClientesRepository.BuscarClienteById(clientesDao,callback).execute(id);
    }

    public void findAllClientes(){
        new ClientesRepository.BuscarClientes(clientesDao,callback).execute();
    }

    public void updateCliente(Cliente cliente){
        new ClientesRepository.ActualizarClientes(clientesDao).execute(cliente);
    }

    public void deleteCliente(Cliente cliente){
        new ClientesRepository.BorrarCliente(clientesDao).execute(cliente);
    }


    private class InsertarCliente extends AsyncTask<Cliente,Void,Void> {


        private ClientesDao clientesDao;

        public InsertarCliente(ClientesDao clientesDao) {
            this.clientesDao = clientesDao;
        }


        @Override
        protected Void doInBackground(Cliente... clientes) {
            clientesDao.insert(clientes[0]);
            return null;
        }
    }

    private class BuscarClienteById extends AsyncTask<Long,Void,Cliente> {

        private ClientesDao clientesDao;
        private OnResultCallback callback;

        public BuscarClienteById(ClientesDao clientesDao, OnResultCallback context) {
            this.clientesDao=clientesDao;
            this.callback=context;
        }

        @Override
        protected Cliente doInBackground(Long... longs) {
            return clientesDao.getById(longs[0].toString());
        }

        @Override
        protected void onPostExecute(Cliente cliente) {
            super.onPostExecute(cliente);
            callback.onResult(cliente);
        }
    }

    private class BuscarClientes extends AsyncTask<Void,Void,List<Cliente>> {

        private ClientesDao clientesDao;
        private OnResultCallback callback;

        public BuscarClientes(ClientesDao clientesDao, OnResultCallback callback) {
            this.clientesDao=clientesDao;
            this.callback=callback;
        }

        @Override
        protected List<Cliente> doInBackground(Void... voids) {
            return clientesDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Cliente> clientes) {
            super.onPostExecute(clientes);

            callback.onResult(clientes);
        }
    }

    private class ActualizarClientes extends AsyncTask<Cliente,Void,Void>{
        private ClientesDao clientesDao;



        public ActualizarClientes(ClientesDao clientesDao) {
            this.clientesDao = clientesDao;
        }

        @Override
        protected Void doInBackground(Cliente... clientes) {
            clientesDao.update(clientes[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private class BorrarCliente extends AsyncTask<Cliente,Void,Void> {
        private ClientesDao clientesDao;
        public BorrarCliente(ClientesDao clientesDao) {
            this.clientesDao=clientesDao;
        }

        @Override
        protected Void doInBackground(Cliente... clientes) {
            clientesDao.delete(clientes[0]);
            return null;
        }
    }
}
