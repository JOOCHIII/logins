package com.example.logins;
import Connection.ConnectionDB;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, contrasena;
    TextView textviewRegistro;
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
        botoningresar = (Button) findViewById(R.id.botoniniciarsesion);

        botoningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login login = new login();
                login.execute();
            }
        });

        textviewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(getApplicationContext(), Registroactivity.class);
                startActivity(registro);
            }
        });
    }

    public class login extends AsyncTask<String, String, String> {
        String z = null;
        Boolean exito = false;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        protected String doInBackground(String... strings) {
            if (con == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "No hay conexión", Toast.LENGTH_SHORT).show();
                    }
                });
                z = "No hay conexión";
            } else {
                try {
                    // Se usa PreparedStatement para evitar SQL Injection
                    String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, usuario.getText().toString());
                    pst.setString(2, contrasena.getText().toString());

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();
                                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(menu);
                            }
                        });

                        usuario.setText("");
                        contrasena.setText("");
                        exito = true;
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error en la contraseña o usuario", Toast.LENGTH_SHORT).show();
                            }
                        });

                        usuario.setText("");
                        contrasena.setText("");
                    }
                } catch (Exception e) {
                    exito = false;
                    Log.e("Error de conexión", e.getMessage());
                }
            }
            return z;
        }
    }
}
