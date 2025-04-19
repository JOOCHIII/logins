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
import java.sql.ResultSet;

import Connection.ConnectionDB;

public class RegistroActivity extends AppCompatActivity {

    EditText nombreapellidos, email, telefono, usuario, clave, confirmaclave;
    Button registrar;
    TextView ingresar;
    Connection con;

    public RegistroActivity() {
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
        confirmaclave = findViewById(R.id.txtconfirmaclave);
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
            // Comprobar si el correo tiene un '@'
            String correoInput = email.getText().toString();
            if (!correoInput.contains("@")) {
                Toast.makeText(RegistroActivity.this, "El correo debe contener @", Toast.LENGTH_SHORT).show();
                return;
            }

            // Comprobar que las contraseñas coinciden
            String password = clave.getText().toString();
            String confirmPassword = confirmaclave.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar que no se repita el nombre de usuario, correo o teléfono
            String consulta = "SELECT COUNT(*) FROM usuarios WHERE usuario = ? OR correo = ? OR telefono = ?";
            PreparedStatement pst = con.prepareStatement(consulta);
            pst.setString(1, usuario.getText().toString());
            pst.setString(2, email.getText().toString());
            pst.setString(3, telefono.getText().toString());

            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Si el usuario está repetido
                if (isUsuarioRepetido()) {
                    Toast.makeText(this, "El usuario ya está registrado", Toast.LENGTH_SHORT).show();
                }
                // Si el correo está repetido
                else if (isCorreoRepetido()) {
                    Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                }
                // Si el teléfono está repetido
                else if (isTelefonoRepetido()) {
                    Toast.makeText(this, "El teléfono ya está registrado", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // Insertar nuevo usuario si todo es válido
            PreparedStatement stm = con.prepareStatement("INSERT INTO usuarios (nombrecompleto, correo, telefono, usuario, contrasena) VALUES(?,?,?,?,?)");
            stm.setString(1, nombreapellidos.getText().toString());
            stm.setString(2, email.getText().toString());
            stm.setString(3, telefono.getText().toString());
            stm.setString(4, usuario.getText().toString());
            stm.setString(5, clave.getText().toString());

            stm.executeUpdate();

            Toast.makeText(RegistroActivity.this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();

            // Limpiar campos después del registro
            nombreapellidos.setText("");
            email.setText("");
            telefono.setText("");
            usuario.setText("");
            clave.setText("");
            confirmaclave.setText("");

        } catch (Exception e) {
            Log.e("Error de conexion", e.getMessage());
        }
    }

    // Verificar si el usuario ya existe
    private boolean isUsuarioRepetido() {
        try {
            String usuarioInput = usuario.getText().toString();
            String consulta = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
            PreparedStatement pst = con.prepareStatement(consulta);
            pst.setString(1, usuarioInput);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Verificar si el correo ya existe
    private boolean isCorreoRepetido() {
        try {
            String correoInput = email.getText().toString();
            String consulta = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
            PreparedStatement pst = con.prepareStatement(consulta);
            pst.setString(1, correoInput);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Verificar si el teléfono ya existe
    private boolean isTelefonoRepetido() {
        try {
            String telefonoInput = telefono.getText().toString();
            String consulta = "SELECT COUNT(*) FROM usuarios WHERE telefono = ?";
            PreparedStatement pst = con.prepareStatement(consulta);
            pst.setString(1, telefonoInput);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
