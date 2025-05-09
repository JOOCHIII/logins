package com.example.logins;

import Connection.ConnectionDB;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, contrasena;
    TextView textviewRegistro, textviewAdmin;
    Button botoningresar;
    Connection con;

    public LoginActivity() {
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = (EditText) findViewById(R.id.usuario);
        contrasena = (EditText) findViewById(R.id.contrasena);
        textviewRegistro = (TextView) findViewById(R.id.textviewRegistro);
        textviewAdmin = (TextView) findViewById(R.id.textviewAdmin);
        botoningresar = (Button) findViewById(R.id.botoniniciarsesion);

        // Crear un ExecutorService para manejar el hilo en segundo plano
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        botoningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                });
            }
        });

        textviewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(registro);
            }
        });

        textviewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(getApplicationContext(), LoginAdminActivity.class);
                startActivity(admin);
            }
        });
    }

    // Método de login, ahora reemplazado sin AsyncTask
    public void login() {
        if (con == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "No hay conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            try {
                String usuarioInput = usuario.getText().toString().trim();
                String contrasenaInput = contrasena.getText().toString().trim();

                // 1. Verificar si el usuario existe
                String checkUserSql = "SELECT contrasena FROM usuarios WHERE usuario = ?";
                PreparedStatement pst = con.prepareStatement(checkUserSql);
                pst.setString(1, usuarioInput);
                ResultSet rs = pst.executeQuery();

                if (!rs.next()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // 2. Verificar si la contraseña coincide
                    String contrasenaCorrecta = rs.getString("contrasena");
                    if (!contrasenaCorrecta.equals(contrasenaInput)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();
                                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                                menu.putExtra("nombre_usuario", usuarioInput);
                                startActivity(menu);
                            }
                        });
                    }
                }

                usuario.setText("");
                contrasena.setText("");

            } catch (Exception e) {
                Log.e("Error de conexión", e.getMessage());
            }
        }
    }
}
