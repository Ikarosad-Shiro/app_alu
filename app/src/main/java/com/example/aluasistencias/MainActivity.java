package com.example.aluasistencias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth; // Instancia de FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa FirebaseApp
        FirebaseApp.initializeApp(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si ya hay un usuario autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si el usuario ya está autenticado, redirigir directamente al menú
            navigateToMenu();
        }

        // Vincular vistas con sus IDs
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Manejar clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(view -> {
            String email = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Verificar si el correo está registrado antes de intentar iniciar sesión
                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                boolean emailExists = task.getResult().getSignInMethods() != null && !task.getResult().getSignInMethods().isEmpty();
                                if (emailExists) {
                                    // Intentar iniciar sesión si el correo está registrado
                                    loginUser(email, pass);
                                } else {
                                    // El correo no está registrado
                                    Toast.makeText(MainActivity.this, "El correo no está registrado. Por favor verifica o regístrate.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Error al verificar el correo. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Manejar clic en el texto de registro
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Método para intentar iniciar sesión.
     */
    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Bienvenido, " + (user != null ? user.getEmail() : "Usuario"), Toast.LENGTH_SHORT).show();

                        // Redirigir al menú
                        navigateToMenu();
                    } else {
                        // Manejar errores en el inicio de sesión
                        String errorMessage = "Error desconocido";
                        if (task.getException() != null) {
                            String errorCode = task.getException().getMessage();
                            if (errorCode != null && errorCode.contains("password is invalid")) {
                                errorMessage = "Contraseña incorrecta. Intenta nuevamente.";
                            } else if (errorCode != null && errorCode.contains("no user record")) {
                                errorMessage = "El correo no está registrado. Por favor verifica o regístrate.";
                            }
                        }
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Método para redirigir al menú principal.
     */
    private void navigateToMenu() {
        Intent intent = new Intent(MainActivity.this, Menujava.class); // Cambiar Menujava si el nombre es diferente
        startActivity(intent);
        finish(); // Finalizar esta actividad para que no esté en la pila de actividades
    }
}
