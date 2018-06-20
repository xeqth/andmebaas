package com.example.user.andmebaas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;



public class MainAndmed extends AppCompatActivity {
    EditText nimi, kogus, hind;
    Button btnLisa;
    ListView nimekiri;
    DbToode dbToode;
    DbToode.DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainandmed);

        nimi = findViewById(R.id.tootenimi);
        kogus = findViewById(R.id.tootekogus);
        hind = findViewById(R.id.tootehind);
        btnLisa = findViewById(R.id.btn_lisa);
        dbToode = new DbToode(this);

        laadi();


        btnLisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nimi = nimi.getText().toString();
                String kasutajaKogus = kogus.getText().toString();
                String kasutajaHind = hind.getText().toString();

                int Kogus = Integer.parseInt(kasutajaKogus);
                double Hind = Double.parseDouble(kasutajaHind);
                try {
                    dbToode.insert(Nimi, Kogus, Hind);
                    laadi();
                    teade("Andmed salvestati!");
                    nimi.setText("");
                    kogus.setText("");
                    hind.setText("");
                }
                catch (Exception e)
                {
                    teade ("Andmete salvestamisel esines viga");
                    e.printStackTrace();

                }
            }
        });

        nimekiri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor s = dbToode.valitud(id);
                Intent intent = new Intent(MainAndmed.this, KuvaDetailid.class);
                String saadaId = s.getString(0);
                String saadaNimi = s.getString(1);
                String saadaKogus = s.getString(2);
                String saadaHind = s.getString(3);

                intent.putExtra("saadaId", saadaId);
                intent.putExtra("saadaNimi", saadaNimi);
                intent.putExtra("saadaKogus", saadaKogus);
                intent.putExtra("saadaHind", saadaHind);

                startActivity(intent);
            }
        });
    }
    public void laadi(){
        Cursor cursor = null;
        try{
            dbToode.ava();
            cursor = dbToode.kuvaAndmed();
            teade("Andmete laadimine õnnestus!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String[] kust = new String[] {dbHelper.ID, dbHelper.NIMI, dbHelper.KOGUS, dbHelper.HIND};
        int [] kuhu = new int[]{R.id.nkId, R.id.nkNimi, R.id.nkKogus, R.id.nkHind};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainAndmed.this, R.layout.nimekiri, cursor, kust, kuhu);
        adapter.notifyDataSetChanged();
        nimekiri = findViewById(R.id.nimekiri);
        nimekiri.setAdapter(adapter);

    }
    public void teade(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




}
