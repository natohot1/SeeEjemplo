package com.example.seeejemplo;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Medicina extends AppCompatActivity {
    ListView milista;
    String[] variables;
    TextView titulo;
    String encaezado;
    DatosReaderDbHelper manager;
    Cursor cur_bandera;
    String banString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicina);
        manager = new DatosReaderDbHelper(this);
        manager.openDB();

        //BUSCA BANDERA
        try {
            cur_bandera = manager.buscarMed("BANDERA");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        banString=cur_bandera.getString(2);


        final String nombre = getIntent().getStringExtra("nomb");
        final int nombre1=Integer.parseInt(nombre);

        variables=sacarVariable(nombre);

        titulo=(TextView)findViewById(R.id.txdTitulo);
        titulo.setTextColor(getResources().getColor(R.color.colorAzul));
        titulo.setText(encaezado);
        milista=(ListView)findViewById(R.id.miLista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,variables);
        milista.setAdapter(adapter);
        milista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clase="";
                String esTerbu=variables[position];
                if (esTerbu.equals("TERBUTALINA JARABE")){
                    pasarJarabe(position, clase);
                }else {
                    if (nombre1 == 5) {
                        clase="ActivityAsma";
                        pasarJarabe(position,clase);
                    } else {
                        clase="Activity4";
                        pasarJarabe(position,clase);
                    }
                }
            }
        });

    }

    private void pasarJarabe(int position, String clase) {
        //PASA MEDIANTE BOTONES DISTINGUE ASMA Y ACTIVITY4 MEDIANTE VARIABLE CLASE
        Intent intent = new Intent(Medicina.this, Main4Activity.class);
        if (clase.equals("ActivityAsma")) {
            intent = new Intent(Medicina.this, Asma.class);
        }
        String text = variables[position];
        String mandar = manager.mediComer(text);
        Bundle bundle = new Bundle();
        bundle.putString("medcinas", text);
        bundle.putString("obtenido", mandar);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String[] sacarVariable(String nombre) {
        int nuevo= Integer.parseInt(nombre);
        String []valore=null;
        if (nuevo ==1){
            if(banString.equals("banespana.png")) {
                valore = getResources().getStringArray(R.array.ANALGESICOS);
            }
            if(banString.equals("banbolivia.png")) {
                valore = getResources().getStringArray(R.array.ANALGESICOS_BOLIVIA);
            }
            encaezado="ANALGESICOS";
        }
        if (nuevo ==2){
            if(banString.equals("banespana.png")) {
                valore = getResources().getStringArray(R.array.ANTIALERGICOS);
            }
            if(banString.equals("banbolivia.png")) {
                valore = getResources().getStringArray(R.array.ANTIALERGICOS_BOLIVIA);
            }
            encaezado="ANTIALERGICOS";

        }
        if (nuevo ==3){
            if(banString.equals("banespana.png")) {
                valore = getResources().getStringArray(R.array.ANTIBIOTICOS);
            }
            if(banString.equals("banbolivia.png")) {
                valore = getResources().getStringArray(R.array.ANTIBIOTICOS_BOLIVIA);
            }
            encaezado="ANTIBIOTICOS";
        }
        if (nuevo ==4){
            if(banString.equals("banespana.png")) {
                valore = getResources().getStringArray(R.array.ANTITUSIGENOS);
            }
            if(banString.equals("banbolivia.png")) {
                valore = getResources().getStringArray(R.array.ANTITUSIGENOS_BOLIVIA);
            }
            encaezado="ANTITUSIGENOS";
        }
        if (nuevo ==5){
            valore =getResources().getStringArray(R.array.ASMA);
            encaezado="ANTIASMATICOS";
        }
        return valore;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Medicina.this, Segundo.class);
        startActivity(intent);
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
        Intent intent = new Intent(Medicina.this,Main2Activity.class);
        startActivity(intent);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(Medicina.this, Segundo.class);
        startActivity(intent);
    }
}
