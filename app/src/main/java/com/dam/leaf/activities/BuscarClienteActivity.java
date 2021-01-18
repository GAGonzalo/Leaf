package com.dam.leaf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.adapters.ClientesAdapter;
import com.dam.leaf.model.Cliente;
import com.dam.leaf.repository.ClientesRepository;

import java.util.List;

public class BuscarClienteActivity extends AppCompatActivity implements ClientesAdapter.onClienteSelectedCallback, ClientesRepository.OnResultCallback<Cliente> {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Cliente> list;
    private EditText nombreEditText;
    private EditText dniEditText;
    private Button buscarBtn;

    private List<Cliente> clientes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buscar_cliente);

        ClientesRepository clientesRepository = new ClientesRepository(getApplication(),this);
        clientesRepository.findAllClientes();

        buscarBtn = findViewById(R.id.button);
        recyclerView = findViewById(R.id.clientesRecycler);
        nombreEditText =  findViewById(R.id.nombre_buscarCliente_et);
        dniEditText = findViewById(R.id.dni_buscarCliente_et);


    }
    @Override
    public void modificarCliente(Cliente c) {
        Intent i = new Intent();
        i.putExtra("Cliente",c);
        setResult(RESULT_OK,i);
        finish();
    }

    @Override
    public void onResult(List<Cliente> result) {
        clientes = result;
        configurarClientesAdapter();
    }

    private void configurarClientesAdapter() {
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new ClientesAdapter(clientes,this,this,true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        buscarBtn.setOnClickListener(v->{
            ((ClientesAdapter) mAdapter).dni=dniEditText.getText().toString();
            ((ClientesAdapter) mAdapter).getFilter().filter(nombreEditText.getText());
        });
    }

    @Override
    public void onResult(Cliente result) {

    }
}
