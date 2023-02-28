package com.emaggi.controlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuPrincipalActivity extends AppCompatActivity {
    Button salir;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        salir = findViewById(R.id.SalirBTN);
        firebaseAuth = FirebaseAuth.getInstance();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MenuPrincipalActivity.this, MainActivity.class));
                Toast.makeText(MenuPrincipalActivity.this, "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}