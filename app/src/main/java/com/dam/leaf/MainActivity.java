package com.dam.leaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dam.leaf.R;
import com.dam.leaf.fragment.BuscarClienteFragment;
import com.dam.leaf.fragment.CrearClienteFragment;
import com.dam.leaf.fragment.CrearPlantaFragment;
import com.dam.leaf.fragment.HomeFragment;
import com.dam.leaf.fragment.VerPlantasFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Bundle b = new Bundle();
        Fragment fr =  new VerPlantasFragment();

        switch (item.getItemId()){
            case R.id.home_fragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).addToBackStack(null).commit();
                break;
            case R.id.agregar_planta_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new CrearPlantaFragment()).addToBackStack(null).commit();
                break;
            case R.id.listar_plantas_item:
                b.putBoolean("Vender",false);
                fr.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fr).addToBackStack(null).commit();
                break;
            case R.id.vender_plantas_item:
                b.putBoolean("Vender",true);
                fr.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fr).addToBackStack(null).commit();
                break;
            case R.id.agregar_cliente_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new CrearClienteFragment()).addToBackStack(null).commit();
                break;
            case R.id.ver_clientes_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new BuscarClienteFragment()).addToBackStack(null).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Exito
                        Log.d("Main activity", "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // Error
                        Log.w("Main activity", "signInAnonymously:failure", task.getException());
                    }
                });
    }
}