package com.dam.leaf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.adapters.VentasAdapter;
import com.dam.leaf.model.Venta;
import com.dam.leaf.repository.VentasRepository;

import java.util.List;

public class VerVentasFragment extends Fragment implements VentasRepository.OnResultCallback<Venta> {

    private VentasRepository ventasRepository;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private VentasAdapter ventasAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listar_ventas,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ventas_rv);
        ventasRepository = new VentasRepository(getActivity().getApplication(),this);
        ventasRepository.findVentasCompletas();

        TextView dateInicial = view.findViewById(R.id.fecha_inicial_filtrar_ventas);
        TextView dateFinal = view.findViewById(R.id.fecha_final_filtrar_ventas);
        Button btn = view.findViewById(R.id.filtrar_ventas_btn);

        btn.setOnClickListener(v->{
            ventasAdapter.dateInicial=dateInicial.getText().toString();
            ventasAdapter.dateFinal=dateFinal.getText().toString();

            ventasAdapter.getFilter().filter("");
        });

    }

    @Override
    public void onResultVenta(List<Venta> result) {

        ventasAdapter = new VentasAdapter(result,getActivity());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ventasAdapter);
    }

    @Override
    public void onResultVenta(Venta result) {

    }
}
