package com.example.aluasistencias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;

public class Menujava extends AppCompatActivity {

    private static final String TAG = "MenuActivityDebug";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Enlazar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        // Configurar Toolbar como ActionBar
        setSupportActionBar(toolbar);

        // Configurar el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar eventos de NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                Toast.makeText(Menujava.this, "Profile seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(Menujava.this, "Settings seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logoutUser(); // Cerrar sesión
            }
            drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú deslizable
            return true;
        });
    }

    /**
     * Método para cerrar la sesión.
     */
    private void logoutUser() {
        // Limpiar preferencias compartidas (SharedPreferences)
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        preferences.edit().clear().apply();
        Log.d(TAG, "Sesión cerrada y preferencias limpiadas.");

        // Mostrar un mensaje de cierre de sesión
        Toast.makeText(Menujava.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Redirigir al usuario al inicio de sesión
        Intent intent = new Intent(Menujava.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual para evitar volver al menú con el botón atrás
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
