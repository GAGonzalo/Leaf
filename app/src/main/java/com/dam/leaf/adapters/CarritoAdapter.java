package com.dam.leaf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.model.Planta;

import org.w3c.dom.Text;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Planta> pedido;
    private FragmentActivity activity;

    public CarritoAdapter(List<Planta> pedido, FragmentActivity activity) {
        this.pedido = pedido;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_carrito,parent,false);
        CarritoViewHolder vh = new CarritoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        final Planta planta = pedido.get(position);

        holder.nombre.setText(planta.getNombre());
        holder.precio.setText("$"+planta.getPrecio().toString());

    }

    @Override
    public int getItemCount() {
        return pedido.size();
    }

    public class CarritoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView precio;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre_item_carrito);
            precio = itemView.findViewById(R.id.precio_item_carrito);

        }
    }
}
