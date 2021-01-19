package com.dam.leaf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.model.Venta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentasAdapter extends RecyclerView.Adapter<VentasAdapter.VentasViewHolder> implements Filterable {

    private List<Venta> ventas;
    private List<Venta> fullList;
    private FragmentActivity activity;

    public String dateInicial;
    public String dateFinal;

    public VentasAdapter(List<Venta> ventas, FragmentActivity activity) {
        this.ventas = ventas;
        this.activity = activity;
        fullList=new ArrayList<>(ventas);
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }


    @NonNull
    @Override
    public VentasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,parent,false);
        VentasAdapter.VentasViewHolder vh = new VentasViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VentasViewHolder holder, int position) {
        Venta venta = ventas.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(venta.getFecha_venta());

        holder.cliente.setText(venta.getCliente().getNombre_apellido());
        holder.fecha.setText(date);
        holder.costo.setText("$"+venta.getTotal().toString());
    }



    public class VentasViewHolder extends RecyclerView.ViewHolder {

        TextView cliente;
        TextView fecha;
        TextView costo;

        public VentasViewHolder(@NonNull View itemView) {
            super(itemView);

            cliente = itemView.findViewById(R.id.cliente_item_venta);
            fecha = itemView.findViewById(R.id.fecha_item_venta);
            costo = itemView.findViewById(R.id.costo_item_venta);

        }
    }

    @Override
    public Filter getFilter() {
        return filterFecha;
    }

    private Filter filterFecha = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Venta> filteredList = new ArrayList<>();

            FilterResults results = new FilterResults();
            if(dateInicial.isEmpty() || dateFinal.isEmpty()){
              filteredList=fullList;
            }
            else{
                Date inicio = format(dateInicial);
                Date fin = format(dateFinal);

                for(Venta v : fullList){
                    if(checkDates(inicio,fin,v)){
                        filteredList.add(v);
                    }
                }
            }

            results.values=filteredList;
            return results;

        }

        private Date format(String date) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
               return formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }

        private boolean checkDates(Date inicio, Date fin, Venta v) {
            if((v.getFecha_venta().after(inicio) && v.getFecha_venta().before(fin)) ||
                    (v.getFecha_venta().equals(inicio) && v.getFecha_venta().before(fin)) ||
                    (v.getFecha_venta().after(inicio) && v.getFecha_venta().equals(fin)) ||
                    (v.getFecha_venta().equals(inicio) && v.getFecha_venta().equals(fin))){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ventas.clear();
            ventas.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
