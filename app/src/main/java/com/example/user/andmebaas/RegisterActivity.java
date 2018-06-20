package com.example.user.andmebaas;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    EditText eesNimi, pereNimi, epost, salasõna;
    Button btnregister, btn_pilt;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String _eesnimi, _perenimi, _epost, _salasõna;

    ImageView profiilipilt;
    private StorageReference storageReference;
    private static final int CAMERA_REQUEST_OUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        profiilipilt = findViewById(R.id.profile_pic);
        btn_pilt = findViewById(R.id.btnlisa);
        eesNimi = findViewById(R.id.eesNimi);
        pereNimi = findViewById(R.id.pereNimi);
        epost = findViewById(R.id.epost);
        salasõna = findViewById(R.id.password);
        btnregister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        btn_pilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pilt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pilt, CAMERA_REQUEST_OUT);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valideeri()){
                    String k_epost = epost.getText().toString().trim();
                    String k_salasõna = salasõna.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(k_epost, k_salasõna).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.setMessage("Andmete üleandmine võtab aega," + "Palun oodake");
                                progressDialog.show();
                                pildiUpload();
                                saadaEpostiKinnitus();
                            }
                            else {
                                if (task.getException()instanceof FirebaseAuthInvalidUserException){}
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_OUT && resultCode == RESULT_OK && data != null){
            Bitmap bitmap =(Bitmap)data.getExtras().get("data");
            profiilipilt.setImageBitmap(bitmap);
        }
    }
    private void pildiUpload(){
        progressDialog.setMessage("Pildi üleslaadimine");
        progressDialog.show();
        StorageReference pathReference = storageReference.child("pildid/profiilipilt.jpg");
        profiilipilt.setDrawingCacheEnabled(true);
        profiilipilt.buildDrawingCache();
        Bitmap bitmap = profiilipilt.getDrawingCache();
        ByteArrayOutputStream baitOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baitOS);
        byte[] data = baitOS.toByteArray();
        UploadTask uploadTask = pathReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Picasso.get().load(downloadUrl).fit().centerCrop().into(profiilipilt);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                teade("Pilti ei saanud saata");
            }
        });
    }
    private boolean valideeri(){
        boolean tulemus = false;
        _eesnimi = eesNimi.getText().toString();
        _perenimi = pereNimi.getText().toString();
        _epost = epost.getText().toString();
        _salasõna = salasõna.getText().toString();

        if (_eesnimi.isEmpty() || _perenimi.isEmpty() || _epost.isEmpty() || _salasõna.isEmpty()){
            teade("Kõik väljad peavad olema täidetud");
        }
        else {
            tulemus = true;
        }
        return tulemus;
    }
    private void saadaEpostiKinnitus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        saadaKasutajaAndmed();
                        teade("Registreerimine õnnestus, kiri saadetud E-Mailile");
                        finish();
                        firebaseAuth.signOut ();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                    else {
                        teade("Kinnitus E-Maili ei saanud saata");
                    }
                }
            });
        }
    }

    public void teade(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void saadaKasutajaAndmed() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfileData userProfileData = new UserProfileData(_eesnimi, _perenimi, _epost);
        databaseReference.setValue(userProfileData);
    }
}

