package com.dam.leaf.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.dam.leaf.R;
import com.dam.leaf.model.Cliente;
import com.dam.leaf.repository.ClientesRepository;

import java.util.List;

public class CrearClienteFragment<ClienteRepository> extends Fragment implements ClientesRepository.OnResultCallback<Cliente> {

    private static final int PICK_CONTACT = 1 ;
    private static final String TAG = "CREAR_CLIENTE_FRAGMENT" ;


    private EditText nombreET;
    private EditText numeroET;
    private EditText dniET;
    private EditText direccion;


    public CrearClienteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_cliente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button agendaBtn = view.findViewById(R.id.agendaButton);
        Button confirmarBtn = view.findViewById(R.id.confirmarClienteButton);

        nombreET = view.findViewById(R.id.nombreClienteET);
        numeroET = view.findViewById(R.id.nroTelefonoClienteET);
        dniET = view.findViewById(R.id.dniClienteET);
        direccion = view.findViewById(R.id.direccionClienteET);

        ClientesRepository clientesRepository;
        clientesRepository= new ClientesRepository(getActivity().getApplication(),this);


        //Listeners
        agendaBtn.setOnClickListener(v->{

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},PICK_CONTACT);
            }
            else{
                pickContact();
            }

        });

        confirmarBtn.setOnClickListener(v->{
            if(camposCompletos()){
                Cliente cliente = new Cliente(null,nombreET.getText().toString(), numeroET.getText().toString(), dniET.getText().toString(), direccion.getText().toString() );
                clientesRepository.insertCliente(cliente);
            }
            else {
                clientesRepository.findAllClientes();
                Toast.makeText(getActivity(), "No se creo el cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean camposCompletos() {

        boolean valor = (nombreET.getText().toString().isEmpty() || numeroET.getText().toString().isEmpty() || dniET.getText().toString().isEmpty() || direccion.getText().toString().isEmpty());
        if(valor){
            return false;
        }
        else return true;
    }


    //ABRIR AGENDA
    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }


    //RESULTADOS DE PERMISOS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PICK_CONTACT
                && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

            pickContact();
        }
        else{
            Log.d(TAG,"Permisos rechazados");
        }
    }



    //RESULTADO DE LA AGENDA
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        if (uri != null) {
            if (requestCode == PICK_CONTACT) {

                String numero=null;
                String nombre;

                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

                if (cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String tieneNumero = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    //Obtener nombre del contacto
                    nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //Verificar que sea un contacto con numero de telefono
                    if (tieneNumero.equalsIgnoreCase("1")) {

                        // Obtener Numero de telefono
                        Cursor telefonos = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                        if (telefonos.moveToFirst()) {
                            numero = telefonos.getString(telefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            telefonos.close();
                        }

                    }

                    nombreET.setText(nombre);
                    numeroET.setText(numero);
                }
                cursor.close();

            }
        }
    }

    @Override
    public void onResult(List<Cliente> result) {
    }

    @Override
    public void onResult(Cliente result) {

    }


}
