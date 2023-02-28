package com.emaggi.controlapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    EditText contraseniaET, confirmcontraseniaET, usuarioET, correoET;
    Button registrar;
    TextView concuenta;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String contrasenia = "", confirmarcontrasenia = "", usuario = "", correo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        contraseniaET = findViewById(R.id.PasswordET);
        confirmcontraseniaET = findViewById(R.id.ConfirmPasswordET);
        usuarioET = findViewById(R.id.UsuarioET);
        correoET = findViewById(R.id.correoET);
        registrar = findViewById(R.id.RegistrarBTN);
        concuenta = findViewById(R.id.TengoCuenta);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        concuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                finish();
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaDatos();
            }
        });

    }
    private void validaDatos(){
        usuario = usuarioET.getText().toString().trim();
        contrasenia = contraseniaET.getText().toString().trim();
        confirmarcontrasenia = confirmcontraseniaET.getText().toString().trim();
        correo = correoET.getText().toString().trim();

        if (TextUtils.isEmpty(usuario)){
            Toast.makeText(this, "Ingrese el nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese el correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contrasenia)){
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmarcontrasenia)){
            Toast.makeText(this, "Confirme la contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (!contrasenia.equals(confirmarcontrasenia)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else{
            CrearCuenta();
        }
    }
    private void CrearCuenta(){
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, contrasenia)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GuardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistroActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su informacion");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("usuario", usuario);
        Datos.put("contrasenia", contrasenia);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistroActivity.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroActivity.this, MenuPrincipalActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}