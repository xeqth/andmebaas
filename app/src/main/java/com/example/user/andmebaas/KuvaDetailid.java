package com.example.user.andmebaas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class KuvaDetailid extends AppCompatActivity {
    EditText nimiDetail, kogusDetail, hindDetail;
    Button btnKustuta, btnUuenda;
    DbToode dbToode = new DbToode(this);
    String id, nimi, kogus, hind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuva_detailid);

        nimiDetail = findViewById(R.id.nimiDetail);
        kogusDetail = findViewById(R.id.kogusDetail);
        hindDetail = findViewById(R.id.hindDetail);
        btnKustuta = findViewById(R.id.btn_kustuta);
        btnUuenda = findViewById(R.id.btn_uuenda);

        Intent intent = getIntent();

        id = intent.getStringExtra("saadaId");
        nimi = intent.getStringExtra("saadaNimi");
        kogus = intent.getStringExtra("saadaKogus");
        hind = intent.getStringExtra("saadaHind");

        nimiDetail.setText(nimi);
        kogusDetail.setText(kogus);
        hindDetail.setText(hind);

        btnKustuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbToode.kustuta(Long.parseLong(id));
                tagasi();
            }
        });
        btnUuenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbToode.uuenda(Long.parseLong(id), nimiDetail.getText().toString(),
                        Integer.parseInt(kogusDetail.getText().toString()),
                        Double.parseDouble(hindDetail.getText().toString()));


                tagasi();
            }
        });

    }
    public void tagasi(){
        Intent intent = new Intent(this, MainAndmed.class);
        startActivity(intent);
    }
}
