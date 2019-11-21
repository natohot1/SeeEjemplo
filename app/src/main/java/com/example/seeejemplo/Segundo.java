package com.example.seeejemplo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class Segundo extends AppCompatActivity {
    AutoCompleteTextView auto;
    int position;
    String botonActivo ="todo", clase = "segundo";
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

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


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

        //SACAMOS DAOTOS BOTON Y CLASE
        botonActivo=cur_bandera.getString(4);




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

       // actualizarBotones(btTodo);


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

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarBotones();
            }
        });


        ImageButton bt = (ImageButton) findViewById(R.id.bor);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto.setText("");
                ponerBotones();
                closeKeyboard();
            }
        });

        //FOCO EN AUTOCOMPLETE
        quitar();

    }

    public void quitar(){
        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    quitarBotones();
                }else {
                    closeKeyboard();
                }
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
        btAnalgesicos= findViewById(R.id.btdAnalgesicos);
        btAntibiotico = findViewById(R.id.btdAntibiticos);
        btTos = findViewById(R.id.btdTos);
        btAlergia = findViewById(R.id.btdAntialergicos);
        btAsma = findViewById(R.id.btdAsma);
        btTodo = findViewById(R.id.btdTodo);
        lista = findViewById(R.id.miLIsta);
        lista.requestFocusFromTouch();
        btBanera = findViewById(R.id.btBandera);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels

        int anchoP=0;
        int ancho=horizotal(width,height);

        if(ancho<1000){
            anchoP=7;
        }
        if(ancho>1000 && ancho<2000){
            anchoP=10;
        }
        if(ancho>2000){
            anchoP=15;
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

    //METODOS ONCLICK
    public void onClickAtras(View view) {
    }
    public void onClick1(View view) {
        switch (view.getId()){
            case (R.id.btdAnalgesicos):
                activarAnalgesico();
                closeKeyboard();
                break;
            case (R.id.btdAntialergicos):
                activarAlergia();
                closeKeyboard();
                break;
            case (R.id.btdAntibiticos):
                activarAntibiotico();
                closeKeyboard();
                break;
            case (R.id.btdTos):
                activarTos();
                closeKeyboard();
                break;
            case (R.id.btdAsma):
                activarAsma();
                closeKeyboard();
                break;
            case (R.id.btdTodo):
                activarTodo();
                closeKeyboard();
                break;
        }
    }
    public void onClickBandera(View view) {
        Intent intent = new Intent(Segundo.this, BanderaActivity.class);
        startActivity(intent);
    }

    //QUITAR TECLADO
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    //CONFIGURACION BOTONES
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
        btTodo.setTextColor(getResources().getColor(R.color.blanco));
        actualizarBotones(btTodo);
        auto.setHint("DOS LETRAS TODOS");
        manager.pasarDatos("Segundo",botonActivo);
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
        btAsma.setTextColor(getResources().getColor(R.color.blanco));
        auto.setHint("DOS LETRAS ASMA");
        manager.pasarDatos("Segundo",botonActivo);
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
        btTos.setTextColor(getResources().getColor(R.color.blanco));
        auto.setHint("DOS LETRAS ANTITUSIVOS");
        manager.pasarDatos("Segundo",botonActivo);
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
        btAntibiotico.setTextColor(getResources().getColor(R.color.blanco));
        auto.setHint("DOS LETRAS ANTIBIOTICOS");
        manager.pasarDatos("Segundo",botonActivo);
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
        btAlergia.setTextColor(getResources().getColor(R.color.blanco));
        auto.setHint("DOS LETRAS ANTIALERGICOS");
        manager.pasarDatos("Segundo",botonActivo);
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
        btAnalgesicos.setTextColor(getResources().getColor(R.color.blanco));
        auto.setHint("DOS LETRAS ANALGESICOS");
        manager.pasarDatos("Segundo",botonActivo);
    }

    private void actualizarBotones(Button boton){
        btTodo.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        btAnalgesicos.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        btAlergia.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        btAsma.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        btTos.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        btAntibiotico.setBackgroundColor(getResources().getColor(R.color.botnInactivo));
        boton.setBackgroundColor(getResources().getColor(R.color.botnActivo));

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

    public void quitarBotones() {
        btAnalgesicos.setVisibility(View.GONE);
        btAlergia.setVisibility(View.GONE);
        btAnalgesicos.setVisibility(View.GONE);
        btTos.setVisibility(View.GONE);
        btTodo.setVisibility(View.GONE);
        btAntibiotico.setVisibility(View.GONE);
        btAsma.setVisibility(View.GONE);
    }
    public void ponerBotones(){
        btAnalgesicos.setVisibility(View.VISIBLE);
        btAlergia.setVisibility(View.VISIBLE);
        btAnalgesicos.setVisibility(View.VISIBLE);
        btTos.setVisibility(View.VISIBLE);
        btTodo.setVisibility(View.VISIBLE);
        btAntibiotico.setVisibility(View.VISIBLE);
        btAsma.setVisibility(View.VISIBLE);

    }
}















