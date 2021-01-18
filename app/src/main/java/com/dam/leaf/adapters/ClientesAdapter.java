package com.dam.leaf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> implements Filterable {

    private List<Cliente> clientes;
    private List<Cliente> clientesFullList;
    private FragmentActivity activity;
    private onClienteSelectedCallback callback;
    private boolean vender;

    public String dni;

    public ClientesAdapter(List<Cliente> clientes, FragmentActivity activity, onClienteSelectedCallback callback, boolean vender) {
        this.clientes = clientes;
        this.activity = activity;
        this.callback = callback;
        this.vender = vender;
        clientesFullList=new ArrayList<>(clientes);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    public interface onClienteSelectedCallback {
        public void modificarCliente(Cliente c);
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente,parent,false);
        ClienteViewHolder vh = new ClienteViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente c = clientes.get(position);
        holder.nombre.setText(c.getNombre_apellido());
        holder.dni.setText(c.getDni());
        holder.tel.setText(c.getNumero_telefono());
        holder.direccion.setText(c.getDireccion());

        if(vender){
            holder.btn.setText("Seleccionar");
        }
        else{
            holder.btn.setText("Modificar");
        }

        holder.btn.setOnClickListener(v->{
            this.callback.modificarCliente(c);
        });

    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }



    public class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView dni;
        TextView tel;
        TextView direccion;
        Button btn;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.item_cliente_nombre);
            dni=itemView.findViewById(R.id.item_cliente_dni);
            tel=itemView.findViewById(R.id.item_cliente_tel);
            direccion=itemView.findViewById(R.id.item_cliente_dir);
            btn = itemView.findViewById(R.id.item_cliente_btn);
        }
    }

    private Filter filter = new Filter(){


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cliente> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                if(dni==null || dni.length()==0){
                    filteredList= clientesFullList;
                }
                else{
                    for(Cliente c : clientesFullList){
                        if(c.getDni().equals(dni)){
                            filteredList.add(c);
                        }
                    }
                }
            }
            else{

                for(Cliente c : clientesFullList){
                    String nombre = c.getNombre_apellido().toLowerCase().trim();
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    if(nombre.contains(filterPattern) && (dni.length()==0 || dni== null || c.getDni().equals(dni))){
                        filteredList.add(c);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clientes.clear();
            clientes.addAll( (List) results.values);
            notifyDataSetChanged();
        }
    };
}
