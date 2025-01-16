package com.example.aluasistencias;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth; // Instancia de FirebaseAuth

    private static final String TAG = "FirebaseAuthDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa FirebaseApp
        FirebaseApp.initializeApp(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        // Verificar si existe una sesion activa

        checkUserSession();
=======

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
>>>>>>> origin/main

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // **Cerrar sesión activa al iniciar la aplicación**
        mAuth.signOut();

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
<<<<<<< HEAD
              if (user.equals("admin")&& pass.equals("1234")){//simulacion de inicio de sesion
               saveUserSession(user); // Guardda la sesion del usuario
                  Intent intent = new Intent(MainActivity.this, Menu.class); // cambia al Menu, la actividad principal
                  startActivity(intent);
                  finish();
            }else{
              Toast.makeText(MainActivity.this,"Datos incorrectos",Toast.LENGTH_SHORT).show();}
=======
                // Intentar iniciar sesión directamente
                loginUser(email, pass);
>>>>>>> origin/main
            }
        });

        // Manejar clic en el texto de registro
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
<<<<<<< HEAD
    // Guardar sesion del usuario
    private void saveUserSession(String username){
        SharedPreferences sharedPreferences = getSharedPreferences("Usersession",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);// guarda el nombre del usuario
        editor.putBoolean("isLoggedIn",true); // indica que la session esta activa

        editor.apply();//confirmacionde cambios
        Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();
    }
    // Vertifica si la sesion activada
    private void checkUserSession(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession",MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        if (isLoggedIn){
            //Redirigir al usuario a la actividad principal
            Intent intent = new Intent(this, Menu.class); //cambia al menu principal
            startActivity(intent);
            finish();//Cierra la pantalla de inicio de sesión
        }
    }
}
=======

    /**
     * Método para intentar iniciar sesión.
     */
    private void loginUser(String email, String pass) {
        Log.d(TAG, "Intentando login para: " + email);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // Correo verificado, permitir el acceso
                            Log.d(TAG, "Inicio de sesión exitoso para: " + email);
                            Toast.makeText(MainActivity.this, "Bienvenido, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            navigateToMenu();
                        } else if (user != null && !user.isEmailVerified()) {
                            // Correo no verificado
                            Log.w(TAG, "Correo no verificado: " + email);
                            Toast.makeText(MainActivity.this, "Por favor verifica tu correo antes de iniciar sesión.", Toast.LENGTH_SHORT).show();
                            mAuth.signOut(); // Cerrar sesión automáticamente
                        } else {
                            Log.w(TAG, "Usuario desconocido o error inesperado.");
                            Toast.makeText(MainActivity.this, "Error inesperado, por favor intenta nuevamente.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Manejar errores en el inicio de sesión
                        String errorMessage = "Error desconocido";
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            errorMessage = "El correo no está registrado. Por favor verifica o regístrate.";
                            Log.w(TAG, "Correo no registrado: " + email);
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            errorMessage = "Contraseña incorrecta. Intenta nuevamente.";
                            Log.w(TAG, "Contraseña incorrecta para: " + email);
                        } else {
                            Log.e(TAG, "Error desconocido: " + task.getException().getMessage());
                        }
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Método para redirigir al menú principal.
     */
    private void navigateToMenu() {
        Intent intent = new Intent(MainActivity.this, Menujava.class);
        startActivity(intent);
        finish(); // Finalizar esta actividad para que no esté en la pila de actividades
    }
}
>>>>>>> origin/main
