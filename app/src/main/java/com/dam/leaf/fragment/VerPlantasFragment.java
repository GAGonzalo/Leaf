package com.dam.leaf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.activities.CarritoActivity;
import com.dam.leaf.R;
import com.dam.leaf.adapters.PlantasAdapter;
import com.dam.leaf.fragment.dialogs.ModificarPlantaDialog;
import com.dam.leaf.model.Planta;
import com.dam.leaf.model.TipoPlanta;
import com.dam.leaf.repository.PlantasRepository;
import com.dam.leaf.repository.TipoPlantasRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VerPlantasFragment extends Fragment implements PlantasRepository.OnResultCallback<Planta>, PlantasAdapter.OnPlantaSelectedCallback, TipoPlantasRepository.OnResultCallback<TipoPlanta>, ModificarPlantaDialog.ModificarPlantaListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText nombrePlantaEditText;
    private Button buscarBtn;
    private Spinner tipos;

    private Boolean vender;
    private List<Planta> list;
    private ArrayList<Planta> pedido;
    private List<TipoPlanta> tipoPlantas;
    private Planta plantaParaModif;
    private PlantasRepository plantasRepository;
    private TipoPlantasRepository tipoPlantasRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getArguments()!=null){
            vender=getArguments().getBoolean("Vender");
            System.out.println(vender);
        }

        return inflater.inflate(R.layout.fragment_ver_plantas,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        pedido = new ArrayList<>();

        nombrePlantaEditText = view.findViewById(R.id.buscar_plantas_et);
        buscarBtn = view.findViewById(R.id.buscar_plantas_btn);
        tipos = view.findViewById(R.id.buscar_plantas_tipo_spinner);



        FloatingActionButton fbtn = view.findViewById(R.id.floating_carrito);

        if (vender) {
            fbtn.show();
        } else {
            fbtn.hide();
        }

        fbtn.setOnClickListener(v->{
            if(pedido.size()>0){
                Intent intent = new Intent(getActivity(), CarritoActivity.class);
                intent.putExtra("Pedido",pedido);
                startActivityForResult(intent,32);

            }
            else{
                Toast.makeText(getActivity(), "Elegir almenos un producto", Toast.LENGTH_SHORT).show();
            }
        });

        plantasRepository = new PlantasRepository(getActivity().getApplication(),this);
        plantasRepository.findAllPlantas();

        tipoPlantasRepository = new TipoPlantasRepository(getActivity().getApplication(),this);
        tipoPlantasRepository.findAllTipoPlantas();



        recyclerView = view.findViewById(R.id.plantas_recyclerView);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!= null){
            pedido= (ArrayList<Planta>) data.getExtras().get("Pedido");
        }
    }

    @Override
    public void onResult(List<Planta> result) {
        list = result;        configPlantasAdapter(list);
    }
    @Override
    public void onResult(Planta result) {

    }

    @Override
    public void onResult(Long result) {

    }

    private void configPlantasAdapter(List l){
        layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PlantasAdapter(l,this.getActivity(),this, vender);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        buscarBtn.setOnClickListener(v->{
            PlantasAdapter adapter = (PlantasAdapter) mAdapter;

            //  Camino facil pero claramente incorrecto para agregar otro tipo de filtro ademas del nombre.
            adapter.tipo = ((TipoPlanta) tipos.getSelectedItem()).getTipo();

            adapter.getFilter().filter(nombrePlantaEditText.getText());

        });



    }
    private void configTipoPlantasAdapter(List l){
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, l );
        tipos.setAdapter(arrayAdapter);

    }


    @Override
    public void addCarrito(Planta p) {
        pedido.add(p);
        plantaParaModif = p;
    }

    @Override
    public void modificarPlanta(Planta p) {
        crearDialog(p);
        plantaParaModif=p;
    }

    private void crearDialog(Planta p) {
        ModificarPlantaDialog modificarPlantaDialog = new ModificarPlantaDialog(this,p.getCantidad(),p.getPrecio());
        modificarPlantaDialog.show(getActivity().getSupportFragmentManager(),"Modificar Planta");
    }

    @Override
    public void onResultTipoPlanta(List<TipoPlanta> result) {
        tipoPlantas = result;

        //  Sirve para agregar un valor por defecto para no seleccionar ningun tipo de planta en el spinner
        tipoPlantas.add(0,new TipoPlanta(null,"Ningun Tipo"));

        configTipoPlantasAdapter(tipoPlantas);
    }

    @Override
    public void onResultTipoPlanta(TipoPlanta result) {

    }

    @Override
    public void onGuardarModificacion(Integer cantidad, Float precio,boolean eliminar) {

        if(eliminar) {
            plantasRepository.deletePlanta(plantaParaModif);
            plantasRepository.findAllPlantas();
        }
        else{
            PlantasAdapter adapter = (PlantasAdapter) recyclerView.getAdapter();
            plantaParaModif.setPrecio(precio);
            plantaParaModif.setCantidad(cantidad);
            plantasRepository.updatePlanta(plantaParaModif);
            adapter.notifyDataSetChanged();
        }
    }
}
