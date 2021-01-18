package com.dam.leaf.fragment.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dam.leaf.R;
import com.dam.leaf.model.TipoPlanta;
import com.dam.leaf.repository.TipoPlantasRepository;

import java.util.List;

public class CrearTipoPlantaDialog extends DialogFragment implements TipoPlantasRepository.OnResultCallback<TipoPlanta> {
    private CrearTipoPlantaListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TipoPlantasRepository tipoPlantasRepository = new TipoPlantasRepository(getActivity().getApplication(),this);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_new_tipoplanta, null);
        builder.setView(view);

        EditText tipoPlantaTextView = view.findViewById(R.id.nuevoTipoPlantaET);
        Button crearBtn  = view.findViewById(R.id.crearTipoPlantaButton);
        Button cancelarBtn = view.findViewById(R.id.cancelarTipoPlantaButton);

        crearBtn.setOnClickListener(v->{
            if(tipoPlantaTextView.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Ingrese un tipo de planta", Toast.LENGTH_LONG).show();
            }
            else {
                TipoPlanta tipoPlanta = new TipoPlanta(null,tipoPlantaTextView.getText().toString());
                tipoPlantasRepository.insertTipoPlanta(tipoPlanta);
                tipoPlantasRepository.findAllTipoPlantas();
                getDialog().dismiss();
            }


        });

        cancelarBtn.setOnClickListener(v->{
            getDialog().dismiss();
        });

        return builder.create();
    }

    @Override
    public void onResultTipoPlanta(List<TipoPlanta> result) {
        listener.onDialogPositiveClick(result);
    }

    @Override
    public void onResultTipoPlanta(TipoPlanta result) {

    }


    public interface CrearTipoPlantaListener {
        public void onDialogPositiveClick(List<TipoPlanta> result);
        public void onDialogNegativeClick();
    }

    public void setListener(CrearTipoPlantaListener listener) {
        this.listener = listener;
    }
}
