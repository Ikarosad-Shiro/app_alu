package com.example.aluasistencias;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class Menujava extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, usernameField, passwordField;
    private Button createButton, updateButton, deleteButton, cleanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Enlazar vistas
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        createButton = findViewById(R.id.createButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        cleanButton = findViewById(R.id.cleanButton);

        // Configurar botones
        cleanButton.setOnClickListener(view -> {
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
        });

        // Agrega más lógica para Create, Update, y Delete
    }
}
