package com.dam.leaf.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.leaf.R;
import com.dam.leaf.model.Planta;
import com.dam.leaf.model.TipoPlanta;
import com.dam.leaf.repository.PlantasRepository;
import com.dam.leaf.repository.TipoPlantasRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CrearPlantaFragment extends Fragment implements PlantasRepository.OnResultCallback<Planta>, TipoPlantaDialogFragment.NoticeDialogListener,TipoPlantasRepository.OnResultCallback<TipoPlanta> {

    private static String TAG = "CREAR PLANTA FRAGMENT";
    static final int CAMARA_REQUEST = 1;
    static final int GALERIA_REQUEST = 2;

    private EditText nombrePlanta;
    private EditText descripcion;
    private EditText cantidad;
    private Spinner tipoPlantaSpinner;
    private ImageView imageView;


    public CrearPlantaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_planta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlantasRepository plantasRepository = new PlantasRepository(getActivity().getApplication(),this);
        TipoPlantasRepository tipoPlantasRepository = new TipoPlantasRepository(getActivity().getApplication(),this);
        tipoPlantasRepository.findAllTipoPlantas();

        nombrePlanta = view.findViewById(R.id.nombrePlantaET);
        descripcion = view.findViewById(R.id.descripcionPlantaET);
        tipoPlantaSpinner = view.findViewById(R.id.tipoPlantaSpinner);
        imageView = view.findViewById(R.id.plantaImageView);
        cantidad = view.findViewById(R.id.cantidadET);
        Button agregarFoto = view.findViewById(R.id.fotoButton);
        Button confirmar = view.findViewById(R.id.confirmarPlantaButton);
        ImageButton agregarTipoPlanta = view.findViewById(R.id.addTipoPlantaButton);

      /*  ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.listaTipos,R.layout.support_simple_spinner_dropdown_item);
        tipoPlantaSpinner.setAdapter(arrayAdapter);*/



        agregarFoto.setOnClickListener(v->{
            Log.d(TAG,"AGREGAR FOTO APRETADO");
            crearTipoMultimediaDialog();

        });

        confirmar.setOnClickListener(v->{
            if(camposCompletos()){
                Planta planta = new Planta(null,nombrePlanta.getText().toString(),descripcion.getText().toString(),tipoPlantaSpinner.getSelectedItem().toString(),Integer.valueOf(cantidad.getText().toString()));
                plantasRepository.insertPlanta(planta);
                Log.d(TAG,planta.toString());
            }
            else {
                plantasRepository.findAllPlantas();
                Toast.makeText(getActivity(), "No se creo la planta", Toast.LENGTH_SHORT).show();
            }
        });

        agregarTipoPlanta.setOnClickListener(v->{
            crearDialogNuevoTipoPlanta();
        });




    }

    private void crearDialogNuevoTipoPlanta() {
        TipoPlantaDialogFragment newFragment = new TipoPlantaDialogFragment();
        newFragment.setListener(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "TipoPlanta");
    }


    private boolean camposCompletos() {
        if(tipoPlantaSpinner.getSelectedItem()==null || cantidad.getText().toString().isEmpty() || nombrePlanta.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty()){
            return false;
        }
        else {
            return true;
        }

    }


    private void crearTipoMultimediaDialog(){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Que desea utilizar").setItems(R.array.listaMultimedia, (dialog, which) -> {
            switch (which){
                case 0 :
                    tomarFoto();
                    break;
                case 1 :
                    abrirGaleria();
                    break;
            }
        });

        builder.create().show();
    }


    private void tomarFoto() {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent, CAMARA_REQUEST);
    }

    private void abrirGaleria() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALERIA_REQUEST);
        } else {
            Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galeriaIntent, GALERIA_REQUEST);
        }
    }

    byte[] image;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMARA_REQUEST && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            image = baos.toByteArray(); // Imagen en arreglo de bytes
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);
        }

        if(requestCode==GALERIA_REQUEST && resultCode==RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            image = baos.toByteArray();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALERIA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galeriaIntent, GALERIA_REQUEST);
            } else {
                Log.d(TAG, "El pedido de permiso fue cancelado");
            }
        }
    }




    public void onResult(List<Planta> result) {
        System.out.println(result);
    }

    @Override
    public void onResult(Planta result) {

    }

    @Override
    public void onDialogPositiveClick(List<TipoPlanta> result) {
        setSpinner(result);
    }

    @Override
    public void onDialogNegativeClick() {
        System.out.println("negativo");

    }

    @Override
    public void onResultTipoPlanta(List<TipoPlanta> result) {
        setSpinner(result);
    }


    @Override
    public void onResultTipoPlanta(TipoPlanta result) {

    }

    private void setSpinner(List<TipoPlanta> result) {
        ArrayList<String> list = new ArrayList<>();
        for(TipoPlanta t : result){
            list.add(t.toString());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, list );
        tipoPlantaSpinner.setAdapter(arrayAdapter);
        System.out.println("positivo");
    }
}