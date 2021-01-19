package com.dam.leaf.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.leaf.R;
import com.dam.leaf.model.Planta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PlantasAdapter extends RecyclerView.Adapter<PlantasAdapter.PlantaViewHolder> implements Filterable {

    public String tipo;
    private List<Planta> plantas;
    private List<Planta> filteredByName;
    private List<Planta> plantaFullList;
    private FragmentActivity activity;
    private OnPlantaSelectedCallback onPlantaSelectedCallback;
    private Boolean vender;
    private FirebaseStorage storage;

    @Override
    public Filter getFilter() {
        return filterNombre;
    }


    public interface OnPlantaSelectedCallback {
        void addCarrito(Planta p);
        void modificarPlanta(Planta p);
    }

    public PlantasAdapter(List<Planta> plantas, FragmentActivity activity, OnPlantaSelectedCallback callback, Boolean vender){
        this.plantas = plantas;
        this.activity = activity;
        this.onPlantaSelectedCallback = callback;
        this.vender = vender;
        plantaFullList = new ArrayList<>(plantas);
    }

    @NonNull
    @Override
    public PlantaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planta,parent,false);
        PlantaViewHolder vh = new PlantaViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantaViewHolder holder, int position) {
        storage = FirebaseStorage.getInstance();
        final Planta planta = plantas.get(position);
        if (vender) {
            holder.aceptarModifButton.setText("Agregar");
        } else {
            holder.aceptarModifButton.setText("Editar");
        }
        holder.titulo.setText(planta.getNombre());
        holder.valor.setText(planta.getPrecio().toString());
        holder.cantidad.setText(planta.getCantidad().toString());
        if (planta.getCantidad() < 6) {
            holder.cantidad.setTextColor(Color.RED);
        }

        holder.aceptarModifButton.setOnClickListener(v->{
            String textButton = ((Button) v).getText().toString();
            if(textButton.equals("Agregar")) {
                int cantidad = planta.getCantidad();
                if (cantidad > 0) {

                    cantidad -= 1;

                    planta.setCantidad(cantidad);
                    onPlantaSelectedCallback.addCarrito(planta);
                    notifyDataSetChanged();
                }
            }
            else{
                onPlantaSelectedCallback.modificarPlanta(planta);
            }

        });

        holder.detallesBtn.setOnClickListener(v->{
            crearDialog(planta);
        });

       cargarFoto(holder.imageView,planta.getId());

    }

    private void cargarFoto(ImageView imageView, Long id) {
        // Creamos una referencia al storage con la Uri de la img
        FirebaseStorage storage;
        storage = FirebaseStorage.getInstance();

        StorageReference gsReference = storage.getReferenceFromUrl("gs://leaf-44c0b.appspot.com/images/planta_"+id+".jpg");

        final long THREE_MEGABYTE = 3 * 1024 * 1024;
        gsReference.getBytes(THREE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Exito
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap scaled = Bitmap.createScaledBitmap(bm,imageView.getWidth(),imageView.getHeight(),false);
            imageView.setImageBitmap(scaled);
        }).addOnFailureListener(exception -> {
            // Error - Cargar una imagen por defecto
            System.out.println("Error al cargar la imagen: " + exception.getMessage());

        });
    }

    private void crearDialog(Planta planta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(planta.getDescripcion());
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return plantas.size();
    }

    public class PlantaViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView titulo;
        TextView valor;
        TextView cantidad;
        Button detallesBtn;
        Button aceptarModifButton;

        public PlantaViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.verPlantasCardView);
            imageView = itemView.findViewById(R.id.planta_img_view);
            titulo = itemView.findViewById(R.id.titulo_planta_card);
            valor = itemView.findViewById(R.id.valor_planta_card_text);
            cantidad = itemView.findViewById(R.id.cantidad_card);
            detallesBtn = itemView.findViewById(R.id.detalles_planta_card_btn);
            aceptarModifButton = itemView.findViewById(R.id.agregar_planta_card_btn);

        }
    }

    private Filter filterNombre = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Planta> filteredList = new ArrayList<>();

            //Filtra por nombre vacio -> Filtra por tipo
            if(constraint == null || constraint.length()==0){
                if(!tipo.equals("Ningun Tipo")){
                    for(Planta p : plantaFullList){
                        if(p.getTipo().equals(tipo)){
                            filteredList.add(p);
                        }
                    }
                }
                else{
                    filteredList.addAll(plantaFullList);
                }

            }
            //Filtra por nombre y por tipo
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Planta p : plantaFullList){
                    if(p.getNombre().toLowerCase().contains(filterPattern) &&  (p.getTipo().equals(tipo) || tipo.equals("Ningun Tipo"))){
                        filteredList.add(p);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();

            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            plantas.clear();
            plantas.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}
