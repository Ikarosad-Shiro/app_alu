package com.example.aluasistencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        // Verificar si existe una sesion activa

        checkUserSession();

        // Vincular vistas con sus IDs
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Manejar clic en el botón de inicio de sesión
        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
              if (user.equals("admin")&& pass.equals("1234")){//simulacion de inicio de sesion
               saveUserSession(user); // Guardda la sesion del usuario
                  Intent intent = new Intent(MainActivity.this, Menu.class); // cambia al Menu, la actividad principal
                  startActivity(intent);
                  finish();
            }else{
              Toast.makeText(MainActivity.this,"Datos incorrectos",Toast.LENGTH_SHORT).show();}
            }
        });

        // Manejar clic en el texto de registro
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // Cambia RegisterActivity a tu actividad de registro
            startActivity(intent);
        });
    }
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
