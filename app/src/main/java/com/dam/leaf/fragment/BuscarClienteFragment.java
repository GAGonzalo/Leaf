package com.dam.leaf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.adapters.ClientesAdapter;
import com.dam.leaf.fragment.dialogs.ModificarClienteDialog;
import com.dam.leaf.model.Cliente;
import com.dam.leaf.repository.ClientesRepository;

import java.util.List;


public class BuscarClienteFragment extends Fragment implements ClientesAdapter.onClienteSelectedCallback, ClientesRepository.OnResultCallback<Cliente>, ModificarClienteDialog.ModificarClienteListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Cliente> list;
    private EditText nombreEditText;
    private EditText dniEditText;
    private Button buscarBtn;

    private List<Cliente> clientes;
    private boolean vender;
    private Cliente clienteParaModif;
    private ClientesRepository clientesRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_buscar_cliente,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        clientesRepository = new ClientesRepository(getActivity().getApplication(),this);
        clientesRepository.findAllClientes();

        buscarBtn = view.findViewById(R.id.button);
        recyclerView = view.findViewById(R.id.clientesRecycler);
        nombreEditText =  view.findViewById(R.id.nombre_buscarCliente_et);
        dniEditText = view.findViewById(R.id.dni_buscarCliente_et);


    }

    @Override
    public void modificarCliente(Cliente c) {
        clienteParaModif = c;
        modificarDialog(c);
    }

    private void configurarClientesAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ClientesAdapter(clientes,getActivity(),this,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        buscarBtn.setOnClickListener(v->{
            ((ClientesAdapter) mAdapter).dni=dniEditText.getText().toString();
            ((ClientesAdapter) mAdapter).getFilter().filter(nombreEditText.getText());
        });
    }

    @Override
    public void onResult(List<Cliente> result) {
        clientes = result;
        configurarClientesAdapter();
    }

    @Override
    public void onResult(Cliente result) {

    }

    private void modificarDialog(Cliente cliente) {
        ModificarClienteDialog modificarClienteDialog = new ModificarClienteDialog(this,cliente.getDireccion(),cliente.getNumero_telefono());
        modificarClienteDialog.show(getActivity().getSupportFragmentManager(),"Modificar Cliente");

    }

    @Override
    public void onGuardarModificacion(String direccion, String telefono, boolean eliminar) {

        if(! (direccion.equals(clienteParaModif.getDireccion()) && telefono.equals(clienteParaModif.getNumero_telefono())) && !eliminar){
            ClientesAdapter adapter = (ClientesAdapter) recyclerView.getAdapter();

            clienteParaModif.setNumero_telefono(telefono);
            clienteParaModif.setDireccion(direccion);

            clientesRepository.updateCliente(clienteParaModif);

            adapter.notifyDataSetChanged();
        }
        else{
            clientesRepository.deleteCliente(clienteParaModif);
            clientesRepository.findAllClientes();
        }

    }
}
