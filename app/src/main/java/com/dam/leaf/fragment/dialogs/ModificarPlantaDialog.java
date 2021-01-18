package com.dam.leaf.fragment.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dam.leaf.R;

public class ModificarPlantaDialog extends DialogFragment {

    public interface ModificarPlantaListener{
        public void onGuardarModificacion(Integer cantidad, Float precio, boolean eliminar);
    }

    private ModificarPlantaListener listener;
    private Integer cantidad;
    private Float precio;

    public ModificarPlantaDialog(ModificarPlantaListener listener, Integer cantidad, Float precio) {
        this.listener = listener;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_modif_planta, null);


        builder.setView(v);


        Button cancelar = v.findViewById(R.id.cancelar_modif_planta);
        Button aceptar = v.findViewById(R.id.aceptar_modif_planta);

        Button eliminar = v.findViewById(R.id.eliminar_modif_planta);
        EditText cantidad = v.findViewById(R.id.cantidad_modif_planta);
        EditText precio = v.findViewById(R.id.precio_modif_planta);


        cantidad.setText(this.cantidad.toString());
        precio.setText(this.precio.toString());

        eliminar.setOnLongClickListener(view->{
            listener.onGuardarModificacion(null,null,true);

            getDialog().dismiss();
            return true;
        });

        aceptar.setOnClickListener(view->{

            if(cantidad.getText().length()>0 && precio.getText().length()>0) {
                listener.onGuardarModificacion(Integer.valueOf(cantidad.getText().toString()), Float.valueOf(precio.getText().toString()),false);
                getDialog().dismiss();
            }
            else{
                Toast.makeText(getActivity(), "Llenar los campos vacios.", Toast.LENGTH_SHORT).show();
            }
        });

        cancelar.setOnClickListener(view->{
            getDialog().dismiss();
        });


        return builder.create();

    }
}
