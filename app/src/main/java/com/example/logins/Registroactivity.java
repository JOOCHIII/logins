package com.example.logins;

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

import Connection.ConnectionDB;

public class Registroactivity extends AppCompatActivity {

    EditText nombreapellidos, email, telefono, usuario, clave;
    Button registrar;
    TextView ingresar;
    Connection con;

    public Registroactivity() {
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroactivity);

        nombreapellidos = findViewById(R.id.txtnomapellidos);
        email = findViewById(R.id.txtemail);
        telefono = findViewById(R.id.txttelefono);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtclave);
        registrar = findViewById(R.id.btnregistrar);
        ingresar = findViewById(R.id.lbliniciarsesion);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(view);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingresar = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(ingresar);
                finish();
            }
        });
    }

    public void registrar(View view) {
        try {
            if (con == null) {
                Toast.makeText(Registroactivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                return;
            } else {
                PreparedStatement stm = con.prepareStatement("INSERT INTO usuarios (nombrecompleto, correo, telefono, usuario, contrasena) VALUES(?,?,?,?,?)");
                stm.setString(1, nombreapellidos.getText().toString());
                stm.setString(2, email.getText().toString());
                stm.setString(3, telefono.getText().toString());
                stm.setString(4, usuario.getText().toString());
                stm.setString(5, clave.getText().toString());

                stm.executeUpdate();

                Toast.makeText(Registroactivity.this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();

                nombreapellidos.setText("");
                email.setText("");
                telefono.setText("");
                usuario.setText("");
                clave.setText("");
            }

        } catch (Exception e) {
            // Log de error
            Log.e("Error de conexion", e.getMessage());
        }
    }
}
