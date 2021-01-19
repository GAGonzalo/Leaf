package com.dam.leaf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.adapters.CarritoAdapter;
import com.dam.leaf.model.Cliente;
import com.dam.leaf.model.Planta;
import com.dam.leaf.model.Venta;
import com.dam.leaf.repository.PlantasRepository;
import com.dam.leaf.repository.VentasRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarritoActivity extends AppCompatActivity implements VentasRepository.OnResultCallback<Venta>,PlantasRepository.OnResultCallback<Planta>, CarritoAdapter.OnRemoveCallback {

    private ArrayList<Planta> pedido;
    private Cliente cliente;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView;
    private TextView precioTotal;
    private EditText nombre;
    private EditText dni;
    private EditText tel;
    private EditText importe;
    private Button venderBtn;

    private float total;
    private VentasRepository ventasRepository;
    private PlantasRepository plantasRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_carrito);

        total=0;

        ventasRepository = new VentasRepository(getApplication(),this);
        plantasRepository = new PlantasRepository(getApplication(),this);

        pedido = (ArrayList<Planta>) getIntent().getExtras().get("Pedido");

        recyclerView = findViewById(R.id.carrito_recycler);
        precioTotal = findViewById(R.id.total_carrito);
        nombre = findViewById(R.id.nombre_cliente_carrito);
        dni = findViewById(R.id.dni_cliente_carrito);
        tel = findViewById(R.id.tel_cliente_carrito);
        importe = findViewById(R.id.importe_carrito);
        venderBtn = findViewById(R.id.venderButton);


        layoutManager = new LinearLayoutManager(this);
        mAdapter = new CarritoAdapter(pedido,this,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);



        for(Planta p : pedido){
            total+=p.getPrecio();
        }
        precioTotal.setText(String.valueOf(total));

        Button buscarClienteButton = findViewById(R.id.buscarClienteBtn);



        buscarClienteButton.setOnClickListener(v->{
            Intent intent = new Intent(this,BuscarClienteActivity.class);
            intent.putExtra("Vender",true);
            startActivityForResult(intent,32);
        });


        venderBtn.setOnClickListener(v->{
            Date date = new Date(System.currentTimeMillis());
            Venta venta = new Venta(null,pedido,total,Float.valueOf(importe.getText().toString()),cliente,date);
            ventasRepository.insertVenta(venta);


            // Remuevo los repetidos de la lista de pedidos, ya que,
            // la lista viene con las plantas y la cantidad de stock
            // que le quedaria luego de la venta y asi despues updatear
            // en la base de datos la cantidad de stock

            ArrayList<Planta> list = removerRepetidos();

            for(Planta p : list){
                plantasRepository.updatePlanta(p);
            }
            Toast.makeText(this, "Venta realizada.", Toast.LENGTH_LONG).show();
            finish();


        });
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent();
        intent.putExtra("Pedido",pedido);
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }

    private ArrayList<Planta> removerRepetidos(){
        ArrayList<Planta> list ;

        list = new ArrayList<>();
        for(Planta p : pedido){
            if(!list.contains(p)){
                list.add(p);
            }
        }

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(data!= null){
                cliente = (Cliente) data.getExtras().get("Cliente");
                nombre.setText(cliente.getNombre_apellido());
                dni.setText(cliente.getDni());
                tel.setText(cliente.getNumero_telefono());
            }

    }

    @Override
    public void onResultVenta(List<Venta> result) {

    }

    @Override
    public void onResultVenta(Venta result) {

    }

    @Override
    public void onResult(List<Planta> result) {

    }

    @Override
    public void onResult(Planta result) {

    }

    @Override
    public void onResult(Long result) {

    }

    @Override
    public void onRemove(Planta p) {
        total -= p.getPrecio();
        precioTotal.setText(String.valueOf(total));
        pedido.remove(p);

    }
}
