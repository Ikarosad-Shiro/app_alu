package com.example.aluasistencias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecordActivity extends AppCompatActivity {

    private EditText clientNameInput, productListInput, addressInput;
    private Button saveButton, cancelButton;
    private int recordId;

    // Variables para latitud y longitud
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        // Enlazar vistas
        clientNameInput = findViewById(R.id.clientNameEditText);
        productListInput = findViewById(R.id.productListEditText);
        addressInput = findViewById(R.id.addressInput);  // Agregado campo de dirección
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);  // Enlazar el botón Cancelar

        // Obtener el ID del registro
        recordId = getIntent().getIntExtra("recordId", -1);

        // Verificar si el ID es válido
        if (recordId == -1) {
            Toast.makeText(this, "Error: no se pasó el ID del registro", Toast.LENGTH_SHORT).show();
            finish();  // Salir de la actividad si no se recibe un ID válido
        }

        // Cargar los datos actuales del registro
        loadRecordData(recordId);

        // Guardar los cambios
        saveButton.setOnClickListener(v -> saveRecord());

        // Funcionalidad del botón Cancelar
        cancelButton.setOnClickListener(v -> {
            finish(); // Cerrar la actividad actual y regresar a la anterior
        });
    }

    private void loadRecordData(int id) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Record record = dbHelper.getRecordById(id);
        if (record != null) {
            clientNameInput.setText(record.getClientName());
            productListInput.setText(record.getProductList());
            addressInput.setText(record.getAddress()); // Mostrar la dirección
            latitude = record.getLatitude(); // Obtener latitud
            longitude = record.getLongitude(); // Obtener longitud
        }
    }

    private void saveRecord() {
        String clientName = clientNameInput.getText().toString().trim();
        String productList = productListInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim(); // Obtener dirección

        if (clientName.isEmpty() || productList.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza el registro con la dirección, latitud y longitud
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean isUpdated = dbHelper.updateRecord(recordId, clientName, productList, address, latitude, longitude);

        if (isUpdated) {
            Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad después de guardar
        } else {
            Toast.makeText(this, "Error al actualizar el registro", Toast.LENGTH_SHORT).show();
        }
    }
}
