package com.emaggi.controlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaDeCarga extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);
        firebaseAuth = FirebaseAuth.getInstance();
        int Tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                VerificarCuenta();
            }
        }, Tiempo);
    }

    private void VerificarCuenta(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            startActivity(new Intent(PantallaDeCarga.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(PantallaDeCarga.this, MenuPrincipalActivity.class));
            finish();
        }
    }
}