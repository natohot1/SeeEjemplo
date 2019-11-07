package com.example.seeejemplo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class Segundo extends AppCompatActivity {
    AutoCompleteTextView auto;
    int position;
    String botonActivo ="todo";
    DatosReaderDbHelper manager;
    Button btAntibiotico, btAnalgesicos, btTos,btAlergia,btAsma,btTodo;
    ImageButton btBanera;
    Bundle datos;


    boolean cambios=false;
    Cursor cur_bandera;
    String banString;
    String mediGenerico;
    String[] medicaAu= {"uno","dos"};
    String[] medicaLi= {"uno","dos"};
    ArrayAdapter<String> adapterLi;
    ArrayAdapter<String> adapterAu;
    ListView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segundo);

        manager = new DatosReaderDbHelper(this);
        Context context=this;

        cargarcomponentes();

        banString=cur_bandera.getString(2);
        if (banString.equals("banespana.png")) {
            medicaAu = getResources().getStringArray(R.array.medica_array);
            medicaLi = getResources().getStringArray(R.array.medica_array);
        }
        if (banString.equals("banbolivia.png")) {
            medicaAu = getResources().getStringArray(R.array.medica_arrayBolivia);
            medicaLi = getResources().getStringArray(R.array.medica_arrayBolivia);
        }


        datos = getIntent().getExtras();
        botonActivo = datos.getString("boton");

        //SE ESTABLECE BOTON ACTIVO
        if (botonActivo.contains("todo")){
            activarTodo();
        }
        if (botonActivo.contains("asma")){
            activarAsma();
        }
        if (botonActivo.contains("tos")){
            activarTos();
        }
        if (botonActivo.contains("antibiotico")){
            activarAntibiotico();
        }
        if (botonActivo.contains("antialergico")){
            activarAlergia();
        }
        if (botonActivo.contains("analgesico")){
            activarAnalgesico();
        }



        //********BORRAR
     //   final String nombre = getIntent().getStringExtra("nomb");
        final int nombre1=6;



        //******BORRAR

      //  actualizarBotones(btTodo);


        adapterAu = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,medicaAu);
        adapterLi = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,medicaLi);

        lista.setAdapter(adapterLi);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String medicinaSeleccionada=medicaLi[position];
               String generico = manager.mediComer(medicinaSeleccionada);
               String clase = "jarabe";
               if (generico.contains("NEBULIZA") || generico.contains("INHALA") ){
                   clase = "asma";
               }
               pasarJarabe2(generico,clase,medicinaSeleccionada);
            }
        });
        auto.setThreshold(1);
        auto.setAdapter(adapterAu);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Intent intent = new Intent(Segundo.this, Main4Activity.class);
                String clase = "jarabe";
                String obtenido = auto.getText().toString();
                mediGenerico = manager.mediComer(obtenido);
                if (mediGenerico.contains("NEBULIZA") || mediGenerico.contains("INHALA") ){
                    clase = "asma";
                }
                pasarJarabe2(mediGenerico,clase,obtenido);
            }
        });
        ImageButton bt = (ImageButton) findViewById(R.id.bor);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto.setText("");
            }
        });
    }

    private void pasarJarabe2(String mediGen, String clase, String mediSelec) {
        //PASA MEDIANTE BOTONES DISTINGUE ASMA Y ACTIVITY4 MEDIANTE VARIABLE CLASE
        Intent intent = new Intent(Segundo.this, Main4Activity.class);
        if (clase.equals("asma")) {
            intent = new Intent(Segundo.this, Asma.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString("boton", botonActivo);
        bundle.putString("medigene", mediGen);
        bundle.putString("obtenido", mediSelec);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void cargarcomponentes() {
        auto = findViewById(R.id.autoCompleteTextView);
        // auto.requestFocusFromTouch();
        btAnalgesicos= findViewById(R.id.btdAnalgesicos);
        btAntibiotico = findViewById(R.id.btdAntibiticos);
        btTos = findViewById(R.id.btdTos);
        btAlergia = (Button)findViewById(R.id.btdAntialergicos);
        btAsma = findViewById(R.id.btdAsma);
        btTodo = findViewById(R.id.btdTodo);
        lista = findViewById(R.id.miLIsta);
        btBanera = findViewById(R.id.btBandera);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels

        int anchoP=0;
        int ancho=horizotal(width,height);

        if(ancho<1000){
            anchoP=15;
        }
        if(ancho>1000 && ancho<2000){
            anchoP=20;
        }
        if(ancho>2000){
            anchoP=25;
        }

        btAntibiotico.setTextSize(anchoP);
        btAnalgesicos.setTextSize(anchoP);
        btTos.setTextSize(anchoP);
        btAlergia.setTextSize(anchoP);
        btAsma.setTextSize(anchoP);
        btTodo.setTextSize(anchoP);

        //ESTABLECE BANDERA
        try {
            cur_bandera = manager.buscarMed("BANDERA");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        banString=cur_bandera.getString(2);
        cambioBandera(banString);
    }

    private int horizotal(int width, int height) {
        int devulto=width;
        if(height<width){
            devulto=width/2;}
        return devulto;
    }

    //CONFIGURACION BOTONES

    public void onClick1(View view) {
        Intent intent;
        switch (view.getId()){
            case (R.id.btdAnalgesicos):
                activarAnalgesico();
                break;
            case (R.id.btdAntialergicos):
                activarAlergia();
                break;
            case (R.id.btdAntibiticos):
                activarAntibiotico();
                break;
            case (R.id.btdTos):
                activarTos();
                break;
            case (R.id.btdAsma):
                activarAsma();
                break;
            case (R.id.btdTodo):
                activarTodo();
                break;
        }
    }

    private void activarTodo() {
        botonActivo = "todo";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.medica_array);
            medicaAu = getResources().getStringArray(R.array.medica_array);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.medica_arrayBolivia);
            medicaAu = getResources().getStringArray(R.array.medica_arrayBolivia);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btTodo.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btTodo);
        auto.setHint("DOS LETRAS TODOS");
    }

    private void activarAsma() {
        botonActivo = "asma";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.ASMA);
            medicaAu = getResources().getStringArray(R.array.ASMA);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.ASMA);
            medicaAu = getResources().getStringArray(R.array.ASMA);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btAsma.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btAsma);
        auto.setHint("DOS LETRAS ASMA");
    }

    private void activarTos() {
        botonActivo ="tos";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTITUSIGENOS);
            medicaAu = getResources().getStringArray(R.array.ANTITUSIGENOS);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTITUSIGENOS_BOLIVIA);
            medicaAu = getResources().getStringArray(R.array.ANTITUSIGENOS_BOLIVIA);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btTos.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btTos);
        auto.setHint("DOS LETRAS ANTITUSIVOS");
    }

    private void activarAntibiotico() {
        botonActivo ="antibiotico";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTIBIOTICOS);
            medicaAu = getResources().getStringArray(R.array.ANTIBIOTICOS);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTIBIOTICOS_BOLIVIA);
            medicaAu = getResources().getStringArray(R.array.ANTIBIOTICOS_BOLIVIA);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btAntibiotico.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btAntibiotico);
        auto.setHint("DOS LETRAS ANTIBIOTICOS");
    }

    private void activarAlergia() {
        botonActivo ="antialergico";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTIALERGICOS);
            medicaAu = getResources().getStringArray(R.array.ANTIALERGICOS);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.ANTIALERGICOS_BOLIVIA);
            medicaAu = getResources().getStringArray(R.array.ANTIALERGICOS_BOLIVIA);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btAnalgesicos.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btAlergia);
        auto.setHint("DOS LETRAS ANTIALERGICOS");
    }

    private void activarAnalgesico() {
        botonActivo ="analgesico";
        if (banString.equals("banespana.png")) {
            medicaLi = getResources().getStringArray(R.array.ANALGESICOS);
            medicaAu = getResources().getStringArray(R.array.ANALGESICOS);
        }
        if (banString.equals("banbolivia.png")) {
            medicaLi = getResources().getStringArray(R.array.ANALGESICOS_BOLIVIA);
            medicaAu = getResources().getStringArray(R.array.ANALGESICOS_BOLIVIA);}
        adapterLi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaLi);
        adapterAu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicaAu);
        auto.setAdapter(adapterAu);
        lista.setAdapter(adapterLi);
        adapterLi.notifyDataSetChanged();
        adapterAu.notifyDataSetChanged();
        btAnalgesicos.setBackgroundColor(getResources().getColor(R.color.botnActivo));
        actualizarBotones(btAnalgesicos);
        auto.setHint("DOS LETRAS ANALGESICOS");
    }

    private void actualizarBotones(Button boton){
        btTodo.setBackgroundColor(getResources().getColor(R.color.botonDes));
        btAnalgesicos.setBackgroundColor(getResources().getColor(R.color.botonDes));
        btAlergia.setBackgroundColor(getResources().getColor(R.color.botonDes));
        btAsma.setBackgroundColor(getResources().getColor(R.color.botonDes));
        btTos.setBackgroundColor(getResources().getColor(R.color.botonDes));
        btAntibiotico.setBackgroundColor(getResources().getColor(R.color.botonDes));
        boton.setBackgroundColor(getResources().getColor(R.color.botnActivo));

    }


    public void onClickBandera(View view) {
        Intent intent = new Intent(Segundo.this, BanderaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("boton", botonActivo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onClick6(View view) {
    }

    //CAMBIO ICONO BANDERA
    private void cambioBandera(String bandera){
        if (bandera.contains("bolivia")){
            btBanera.setBackground(this.getResources().getDrawable(R.drawable.banbolivia));}
        if (bandera.contains("espana")){
            btBanera.setBackground(this.getResources().getDrawable(R.drawable.banespana));}
        if (bandera.contains("peru")){
            btBanera.setBackground(this.getResources().getDrawable(R.drawable.banperu));}
        if (bandera.contains("chile")){
            btBanera.setBackground(this.getResources().getDrawable(R.drawable.banchile));}
    }
}















