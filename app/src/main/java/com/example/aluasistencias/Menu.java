package com.example.aluasistencias;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class Menu extends AppCompatActivity {

    private TextView welcomeText, repartidorName;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicializar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Configurar el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Inicializar los TextViews
        welcomeText = findViewById(R.id.welcome_text);
        repartidorName = findViewById(R.id.repartidor_name);

        // Obtener idUsuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String IdUsuario = sharedPreferences.getString("IdUsuario", null);
        Log.d("Id Shared", "ID de usuario recuperado: " + IdUsuario);  // Verifica el ID recuperado
        Log.d("Id Shared", "ID de usuario recuperado: " + IdUsuario);  // Verifica el ID recuperado


        if (IdUsuario == null || IdUsuario.isEmpty()) {
            showErrorDialog("Error de sesión", "ID de usuario no disponible.");
        } else {
            // Llamar a obtenerUsuario para cargar datos
            VolleyRepa volleyRepa = new VolleyRepa(this);
            volleyRepa.obtenerUsuario(IdUsuario, new VolleyRepa.VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        // Parsear el JSON recibido
                        JSONObject jsonObject = new JSONObject(response);
                        String estado = jsonObject.optString("estado", "Deshabilitado");
                        String nombreRepartidor = jsonObject.optString("nick", "Usuario desconocido");

                        // Validar y actualizar la interfaz según el estado
                        if (estado.equals("Deshabilitado")) {
                            repartidorName.setText(nombreRepartidor);
                            welcomeText.setText("Acceso denegado. Contacta con el administrador.");
                            setupButtonsForDeshabilitado();
                        } else if (estado.equals("Habilitado")) {
                            welcomeText.setText("Bienvenido");
                            repartidorName.setText(nombreRepartidor);
                            setupButtonsForHabilitado();
                        } else {
                            showErrorDialog("Error", "Estado desconocido.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog("Error", "Datos no válidos recibidos del servidor.");
                    }
                }

                @Override
                public void onError(String error) {
                    showErrorDialog("Error de conexión", "No se pudo conectar al servidor: " + error);
                }
            });
        }
    }

    private void setupButtonsForDeshabilitado() {
        Button btnRepartir = findViewById(R.id.btn_repartir);
        Button btnVerReparto = findViewById(R.id.btn_ver_reparto);

        btnRepartir.setOnClickListener(v -> mostrarCuadroDialogo());
        btnVerReparto.setOnClickListener(v -> mostrarCuadroDialogo());
    }

    private void setupButtonsForHabilitado() {
        Button btnRepartir = findViewById(R.id.btn_repartir);
        Button btnVerReparto = findViewById(R.id.btn_ver_reparto);

        btnRepartir.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, Repartos.class);
            startActivity(intent);
        });

        btnVerReparto.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, RepartoActual.class);
            startActivity(intent);
        });
    }

    private void mostrarCuadroDialogo() {
        new AlertDialog.Builder(this)
                .setTitle("Acceso Denegado")
                .setMessage("Solicita acceso con tu administrador y vuelve a iniciar sesión.")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear(); // Borra todos los datos de la sesión
                    editor.apply();

                    Intent intent = new Intent(Menu.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}