package com.example.logins;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivityAdmin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main); // puedes usar otro layout si quieres

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Cargar menú admin en vez del normal
        navigationView.getMenu().clear(); // limpiar primero
        navigationView.inflateMenu(R.menu.menu_navigation_admin); // inflar el menú admin

        // Fragment por defecto al iniciar
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new HomeFragment()) // o un fragment admin si quieres
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home_admin);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new HomeFragment())
                            .commit();
                } else if (id == R.id.nav_usuarios_admin) {
                    // Fragment o Activity para gestionar usuarios
                    Toast.makeText(MainActivityAdmin.this, "Gestión de usuarios", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_settings_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new SettingsFragment())
                            .commit();
                } else if (id == R.id.nav_logout_admin) {
                    Toast.makeText(MainActivityAdmin.this, "Cerrando sesión de administrador...", Toast.LENGTH_SHORT).show();
                    finish(); // o volver a LoginAdminActivity
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
