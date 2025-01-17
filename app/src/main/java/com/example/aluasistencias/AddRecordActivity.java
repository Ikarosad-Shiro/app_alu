package com.example.aluasistencias;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {

    private EditText clientNameInput, addressInput;
    private LinearLayout productContainer;
    private Button addProductButton, saveRecordButton, openMapButton;
    private List<EditText> productInputs;
    private double latitude = 0.0, longitude = 0.0; // Para almacenar la latitud y longitud seleccionadas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        // Inicializar vistas
        clientNameInput = findViewById(R.id.clientNameInput);
        addressInput = findViewById(R.id.addressInput);
        productContainer = findViewById(R.id.productContainer);
        addProductButton = findViewById(R.id.addProductButton);
        saveRecordButton = findViewById(R.id.saveRecordButton);
        openMapButton = findViewById(R.id.openMapButton); // Botón para abrir el mapa

        productInputs = new ArrayList<>();

        // Botón para agregar productos
        addProductButton.setOnClickListener(v -> addProductField());

        // Botón para guardar el registro
        saveRecordButton.setOnClickListener(v -> saveRecord());

        // Botón para abrir el mapa y seleccionar la ubicación
        openMapButton.setOnClickListener(v -> openMap());
    }

    private void addProductField() {
        // Crear un nuevo campo de texto
        EditText productInput = new EditText(this);
        productInput.setHint("Producto");
        productInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Agregar el campo de texto al contenedor
        productContainer.addView(productInput);
        productInputs.add(productInput);
    }

    private void saveRecord() {
        String clientName = clientNameInput.getText().toString().trim();
        if (clientName.isEmpty()) {
            Toast.makeText(this, "El nombre del cliente es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recopilar todos los productos
        List<String> products = new ArrayList<>();
        for (EditText productInput : productInputs) {
            String product = productInput.getText().toString().trim();
            if (!product.isEmpty()) {
                products.add(product);
            }
        }

        if (products.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la lista de productos a una cadena separada por comas
        String productList = String.join(",", products);

        // Guardar en la base de datos, incluida la dirección
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean isInserted = dbHelper.insertRecord(clientName, productList, addressInput.getText().toString(), latitude, longitude);

        if (isInserted) {
            Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
            // Enviar resultado a Menujava y recargar la lista
            Intent intent = new Intent(AddRecordActivity.this, Menujava.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Recargar Menujava
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMap() {
        // Abrir la actividad del mapa para seleccionar una ubicación
        Intent intent = new Intent(AddRecordActivity.this, MapsActivity.class);
        startActivityForResult(intent, 1); // Solicitar el resultado
    }

    // Obtener la ubicación seleccionada del mapa
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);

            // Usar la API de geocodificación inversa de Google para obtener la dirección
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0); // Obtener la primera dirección
                    String addressText = address.getAddressLine(0); // Dirección completa
                    addressInput.setText(addressText); // Establecer la dirección en el campo de texto
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo obtener la dirección", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
