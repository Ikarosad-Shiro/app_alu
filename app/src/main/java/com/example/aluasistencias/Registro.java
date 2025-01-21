package com.example.aluasistencias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Spinner spRole;
    private Button btnRegister, btnCancel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();









        // Configuración del Spinner de roles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        // Lógica del botón Registrarse
        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String role = spRole.getSelectedItem().toString();

            if (!validarCampos(name, email, password, confirmPassword)) {
                return;
            }

            registerUserFirebase(name, email, password, role);
        });

        // Lógica del botón Cancelar
        btnCancel.setOnClickListener(view -> finish());
    }

    private boolean validarCampos(String name, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, ingresa una contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUserFirebase(String name, String email, String password, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String idUsuario = user.getUid(); // Obtener UID de Firebase
                            user.sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(Registro.this, "¡Registro exitoso! Verifica tu correo antes de iniciar sesión.", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();

                                            agregarUsuarioSQLite(idUsuario, name, email, password, role);

                                            String nick = email.length() >= 6 ? email.substring(0, 6) : email;

                                            agregarUsuarioMariaDB(idUsuario, name, role, email, nick, password);

                                            startActivity(new Intent(Registro.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Registro.this, "Error al enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                        Toast.makeText(Registro.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarUsuarioSQLite(String idUsuario, String name, String email, String password, String role) {
        OpenHelper dbHelper = new OpenHelper(this);
        String estado = "Deshabilitado";
        dbHelper.insertarUsuario(idUsuario, role, email, name, password, estado);
    }

    private void agregarUsuarioMariaDB(String idUsuario, String name, String rol, String email, String nick, String pass) {
        String url = "http://192.168.212.7/BackAlu/Backend/registro.php";

        VolleyRepa volleyRepa = new VolleyRepa(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if (message.equalsIgnoreCase("success")) {
                            Toast.makeText(this, "Usuario agregado en MariaDB", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error en el registro: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IdUsuario", idUsuario);
                params.put("Nombre_completo", name);
                params.put("rol", rol);
                params.put("email", email);
                params.put("nick", nick);
                params.put("pass", pass);
                params.put("estado", "Deshabilitado");
                return params;
            }
        };

        volleyRepa.getRequestQueue().add(stringRequest);
    }
}