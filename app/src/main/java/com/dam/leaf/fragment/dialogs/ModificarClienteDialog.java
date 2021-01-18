package com.dam.leaf.fragment.dialogs;

import android.app.Dialog;
import android.content.Context;
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

public class ModificarClienteDialog extends DialogFragment  {


    public interface ModificarClienteListener{
        public void onGuardarModificacion(String direccion, String telefono, boolean eliminar);
    }

    private ModificarClienteListener listener;
    private String direccion;
    private String telefono;

   public ModificarClienteDialog(ModificarClienteListener listener, String direccion, String numero_telefono){
       this.listener=listener;
       this.direccion=direccion;
       this.telefono=numero_telefono;
   }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_modif_cliente, null);


        builder.setView(v);


        Button aceptar = v.findViewById(R.id.aceptar_modificar_cliente);
        Button cancelar = v.findViewById(R.id.cancelar_modificar_cliente);
        Button eliminar = v.findViewById(R.id.eliminar_modif_cliente);

        EditText dir = v.findViewById(R.id.direccion_modificar_cliente);
        EditText tel = v.findViewById(R.id.telefono_modificar_cliente);

        dir.setText(direccion);
        tel.setText(telefono);

        eliminar.setOnLongClickListener(view->{
            listener.onGuardarModificacion("","",true);
            getDialog().dismiss();
            return true;
        });

        aceptar.setOnClickListener(view->{

            if(dir.getText().length()>0 && tel.getText().length()>0) {
                listener.onGuardarModificacion(dir.getText().toString(), tel.getText().toString(), true);
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
