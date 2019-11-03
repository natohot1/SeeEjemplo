package com.example.seeejemplo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    DatosReaderDbHelper manager;
    Button valido;
    Cursor cur_bandera;
    String banString,banSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        banSelecionada="banbolivia.png";



        manager = new DatosReaderDbHelper(this);
        Context context=this;
        //BUSCA BANDERA
        manager.openDB();
        try {
            cur_bandera = manager.buscarMed("BANDERA");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        banString=cur_bandera.getString(2);

        NumberPicker np = findViewById(R.id.piker);


        final TextView tv = findViewById(R.id.textView);
        final ImageView imageView = findViewById(R.id.imageBandera);
        valido=findViewById(R.id.btnValidar);
        imageView.setImageResource(R.drawable.banbolivia);

        final String[] values= {"BOLIVIA","ESPAÑA", "PERU"};
        tv.setTextColor(Color.parseColor("#FF2C834F"));
        np.setMinValue(0);
        np.setMaxValue(values.length-1);
        np.setDisplayedValues(values);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tv.setText("Selecionado : "+values[newVal]);
                if (newVal == 0){
                    imageView.setImageResource(R.drawable.banbolivia);
                    banSelecionada= "banbolivia.png";
                }
                if (newVal == 1){
                    imageView.setImageResource(R.drawable.banespana);
                    banSelecionada= "banespana.png";
                }
                if (newVal == 2){
                    imageView.setImageResource(R.drawable.banperu);
                    banSelecionada= "banperu.png";
                }


            }
        });

        valido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarbandera(banSelecionada);
                Intent intent = new Intent(Main2Activity.this, Segundo.class);
                startActivity(intent);
            }
        });



    }

    private void modificarbandera(String value) {
        manager.openDB();
        manager.modificarbandera(value);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (banString) {
            case "banbolivia.png":
                getMenuInflater().inflate(R.menu.menubolivia, menu);
                break;
            case "banespana.png":
                getMenuInflater().inflate(R.menu.menuespana, menu);
                break;
            case "banperu.png":
                getMenuInflater().inflate(R.menu.menuperu, menu);
                break;

        }
        return true;
    }


    public void onCli2(MenuItem item) {
    }
}
