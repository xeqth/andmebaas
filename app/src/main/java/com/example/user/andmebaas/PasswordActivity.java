package com.example.user.andmebaas;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    EditText salasõnaEpost;
    Button lähtestaParool;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        salasõnaEpost = findViewById(R.id.epost);
        lähtestaParool = findViewById(R.id.lähtesta);
        firebaseAuth = firebaseAuth.getInstance();

        lähtestaParool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kasutajaEpost = salasõnaEpost.getText().toString().trim();
                if (kasutajaEpost.equals("")){
                    teade("Sisestage E-Post");
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(kasutajaEpost).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                teade("Parool saadeti E-Mailile");
                                finish();
                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                            }
                            else {
                                teade("E-Maili ei saadetud");
                            }
                        }
                    });
                }
            }
        });
    }

    public void teade(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
