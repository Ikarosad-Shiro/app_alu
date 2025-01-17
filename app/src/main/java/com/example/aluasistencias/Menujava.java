package com.example.aluasistencias;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Menujava extends AppCompatActivity {

    private final List<Record> recordList = new ArrayList<>(); // Lista de registros
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Enlazar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recordList);

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

        // Configurar los eventos de NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                Toast.makeText(Menujava.this, "Profile seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(Menujava.this, "Settings seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logoutUser(); // Cerrar sesión
            }
            drawerLayout.closeDrawer(GravityCompat.START); // Cerrar el menú deslizable
            return true;
        });

        // Configurar RecyclerView para mostrar registros
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        recordAdapter = new RecordAdapter(recordList, this);
        recyclerView.setAdapter(recordAdapter);

        // Cargar registros
        loadRecords();

        // Botón para agregar nuevo registro
        findViewById(R.id.addButton).setOnClickListener(v -> {
            Intent intent = new Intent(Menujava.this, AddRecordActivity.class);
            startActivity(intent);
        });
    }

    // Método para cargar los registros desde la base de datos
    private void loadRecords() {
        recordList.clear(); // Limpiar la lista para evitar duplicados
        Cursor cursor = dbHelper.getAllRecords(); // Obtener los registros
        if (cursor != null && cursor.moveToFirst()) { // Comprobar si hay registros
            // Verificar los índices de las columnas
            int clientNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_CLIENT_NAME);
            int productListIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_LIST);
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COL_ID); // Asegurándonos de que el ID también está disponible
            int isCompletedIndex = cursor.getColumnIndex(DatabaseHelper.COL_IS_COMPLETED); // Verificando el índice para is_completed
            int addressIndex = cursor.getColumnIndex(DatabaseHelper.COL_ADDRESS); // Verificando el índice para la dirección

            if (clientNameIndex != -1 && productListIndex != -1 && idIndex != -1 && isCompletedIndex != -1 && addressIndex != -1) {
                do {
                    // Obtener los valores de cada columna
                    String clientName = cursor.getString(clientNameIndex);
                    String productList = cursor.getString(productListIndex);
                    String address = cursor.getString(addressIndex); // Obtener la dirección
                    int id = cursor.getInt(idIndex); // Obtener el ID del registro
                    int isCompleted = cursor.getInt(isCompletedIndex); // Obtener el estado de completado

                    // Crear un nuevo objeto Record con el ID, nombre del cliente, lista de productos, estado de completado y dirección
                    Record record = new Record(id, clientName, productList, isCompleted, address, 0.0, 0.0);  // Asignar latitud y longitud como 0.0 por ahora
                    recordList.add(record); // Agregar a la lista
                } while (cursor.moveToNext()); // Continuar si hay más registros
            } else {
                // Si no se encuentran las columnas esperadas, mostrar un error
                Toast.makeText(Menujava.this, "Columnas no encontradas en la base de datos", Toast.LENGTH_SHORT).show();
            }
        }
        if (cursor != null) {
            cursor.close(); // No olvidar cerrar el cursor para evitar posibles fugas de memoria
        }
        recordAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado para que se reflejen en la interfaz
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(); // Recargar registros al volver al activity
    }

    /**
     * Método para cerrar la sesión.
     */
    private void logoutUser() {
        // Limpiar preferencias compartidas (SharedPreferences)
        getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(Menujava.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Redirigir al usuario al inicio de sesión
        Intent intent = new Intent(Menujava.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual para evitar volver al menú con el botón atrás
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
