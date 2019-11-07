package com.example.seeejemplo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class Asma2 extends AppCompatActivity {
    String medicamen_final,nombreGenerico;
    private boolean banderaselec;
    TextView txTitulo, txCantidad, txCada, txGravedad,txOtras, txPrecausiones,txPeso;
    Button btGrave,btModerada,btLeve,btOtra1,btOtra2,btOtra3,btOtra4,btGravedad,btConsidere;
    SeekBar seekBar;
    DatosReaderDbHelper manager;
    String[]jara_arraComercial;
    Bundle datos;
    Cursor cur_bandera;
    String banString,medGenerico;
    String arrayMedica[],arrayNombesComer[],graveAsma[],asmaArray[],arrayInhaladores[];
    boolean inhala,nebulizador,salbuta,bromuro,terbuta;
    int control=0,position=0,cada_int;
    Double varibleSalbutamol=1.0;
    ImageButton btVolver, btBandera;
    String [][]array_datos=new String[20][10];
    DecimalFormat formato = new DecimalFormat("#.##");
    DecimalFormat formato1 = new DecimalFormat("#.#");
    Guideline guideline2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asma2);

        datos = getIntent().getExtras();
        medicamen_final = datos.getString("medigene");

        //PARA SACAR EL NOMBRE GENERICO
        //   medGenerico=nomFinal(medicamen_final);


        manager = new DatosReaderDbHelper(this);


        Context context=this;
        inicializarComponentes();
        try {
            encabezados();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txGravedad.setText(graveAsma[0]);
        txOtras.setText(arrayNombesComer[2]);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pintarPeso(progress);
                dosis();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        componentesPrimarios(medicamen_final);

        //DOSIS SEGUN TIPO
        dosis();

    }



    private void dosis() {
        if (inhala || medicamen_final.contains("BUDESONIDA")){
            inhalaDosis();
        }
        if(nebulizador){
            nebuDosis();
        }
    }

    private void componentesPrimarios(String medicamen_final) {
        //  String aBuscar = nomFinal(medicamen_final);  YA ESTA EN INICILIZAR COMPONENTES
        //DATOS TABLA MEDICAMENTOS2
        asmaArray = gravedadAsma(nombreGenerico);
        // PRESENTACIONES Y NOMBRES COMERECIALES
        presentaComercilaes(medicamen_final);
    }

    private void presentaComercilaes(String medicamen_final) {
        int contador = 0;
        ////YA ESTAN E INCIALIZA COMPONENETES
        //String inhaladores = arrayMedica[10];
        // arrayInhaladores = sacarGuion(inhaladores);
        int cuantos = arrayInhaladores.length;
        if(bromuro && nebulizador){
            while (contador<cuantos){
                arrayInhaladores[contador]="Amp. "+arrayInhaladores[contador]+" mg";
                contador++;
            }
        }
        if(nebulizador){
            while (contador<cuantos){
                arrayInhaladores[contador]=arrayInhaladores[contador]+" mg/ml";
                contador++;
            }
        }else {
            while (contador<cuantos){
                arrayInhaladores[contador]=arrayInhaladores[contador]+ " ug/puf";
                contador++;
            }
        }
        configurarSegmento(arrayInhaladores);
    }

    private void configurarSegmento(String[] arrayInhaladores) {
        //CONFIGURA OTRAS PRESENTACIONES
        int largo=arrayInhaladores.length;
        if(largo==1){
            btOtra4.setVisibility(View.GONE);
            btOtra3.setVisibility(View.GONE);
            btOtra2.setVisibility(View.GONE);
            btOtra1.setText(arrayInhaladores[0]);
        }
        if(largo==2){
            btOtra4.setVisibility(View.GONE);
            btOtra3.setVisibility(View.GONE);
            btOtra1.setText(arrayInhaladores[0]);
            btOtra2.setText(arrayInhaladores[1]);
        }


        if (largo==3) {
            btOtra4.setVisibility(View.GONE);
            btOtra1.setText(arrayInhaladores[0]);
            btOtra2.setText(arrayInhaladores[1]);
            btOtra3.setText(arrayInhaladores[2]);
        }
        if (largo==4) {
            btOtra1.setText(arrayInhaladores[0]);
            btOtra2.setText(arrayInhaladores[1]);
            btOtra3.setText(arrayInhaladores[2]);
            btOtra4.setText(arrayInhaladores[3]);
        }
    }

    private String[] sacarGuion(String inhaladores) {
        String resul[]=inhaladores.split("-");
        return resul;
    }

    // ARRAYS TIPOS DE ASMA
    private String[] gravedadAsma(String nomGenerico) {
        int cont=0;

        manager.openDB();
        String query= "select * FROM " + DatosReaderDbHelper.TABLA_ASMA+ " WHERE " + Medicamento.CN_medicamento + " = \"" + nomGenerico + "\"";
        Cursor c = manager.myDataBase.rawQuery(query,null);
        c.moveToFirst();
        int largo=c.getColumnCount();
        String resultado1[]=new String[largo];
        while (cont <largo){
            resultado1[cont]=c.getString(cont);
            cont++;
        }
        manager.close();
        return resultado1;
    }

    private void inicializarComponentes() {
        manager.openDB();
        inhala=medicamen_final.contains("INHALADOR");
        nebulizador=medicamen_final.contains("NEBULIZADOR");
        salbuta=medicamen_final.contains("SALBUTAMOL");
        bromuro=medicamen_final.contains("BROMURO");
        terbuta=medicamen_final.contains("TERBUTALINA INHALADOR");
        txTitulo = findViewById(R.id.txdTitulo);
        txCantidad = findViewById(R.id.txdDosis);
        txCada = findViewById(R.id.txdCada);
        txGravedad = findViewById(R.id.txdGravedad);
        txOtras = findViewById(R.id.txdOtras);
        txPrecausiones = findViewById(R.id.txdPrecausiones);
        txPeso = findViewById(R.id.txdPeso);
        btGrave = findViewById(R.id.btdGrave);
        btModerada = findViewById(R.id.btdModerada);
        btLeve = findViewById(R.id.btdLeve);
        btOtra1 = findViewById(R.id.btdPri);
        btOtra2 = findViewById(R.id.btdSeg);
        btOtra3 = findViewById(R.id.btdTer);
        btOtra4 = findViewById(R.id.btdCuar);
        btGravedad = findViewById(R.id.btdIndice);
        btConsidere = findViewById(R.id.btdConsidere);
        btConsidere.setVisibility(View.GONE);
        btVolver =findViewById(R.id.btdAtras);
        btBandera = findViewById(R.id.btBandera);
        guideline2=findViewById(R.id.guideline2);

        //VALORES SEEKBAR
        seekBar=findViewById(R.id.seebDefi);
        seekBar.setMax(40);
        seekBar.setProgress(6);
        txPeso.setText("6"+ " Kg");


        nombreGenerico = nomFinal(medicamen_final);
        //DATOS TABLA MEDICAMENTOS2
        arrayMedica=arrayMedicamento(nombreGenerico);


        try {
            cur_bandera = manager.buscarMed("BANDERA");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        banString=cur_bandera.getString(2);
        cambioBandera(banString);

        graveAsma = gravedadAsma();
        //BOTON GRAVE SELECIONADAO
        btGrave.setTextColor(Color.BLUE);
        btGrave.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
        btModerada.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
        btModerada.setTextColor(Color.WHITE);
        btLeve.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
        btLeve.setTextColor(Color.WHITE);
        btGravedad.setBackground(this.getResources().getDrawable(R.drawable.botonazulredon));
        //INDIDE DE GRAVEDAD
        btGravedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Asma2.this, IndiceGravedadAsma.class);
                Bundle bundle = new Bundle();
                bundle.putString("medcinas", medicamen_final);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //cofigura BOTON CONSIDERE
        considere();
        //CONFIDIRAR CADA
        cada();
        //BOTON BOTRO1 SELECIONADAO
        btOtra1.setTextColor(Color.BLUE);
        btOtra1.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
        btOtra2.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
        btOtra2.setTextColor(Color.WHITE);
        btOtra3.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
        btOtra3.setTextColor(Color.WHITE);
        btOtra4.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
        btOtra4.setTextColor(Color.WHITE);

        // SACAR PAISES TODO********++++++++++++++++

        String inhaladores = "";
        if (banString.contains("banespana.png")){
            inhaladores = arrayMedica[10];
        }
        if (banString.contains("banbolivia.png")){
            inhaladores = arrayMedica[9];
        }
        arrayInhaladores = sacarGuion(inhaladores);
    }

    //dosisInhalador()
    private void inhalaDosis() {
        int peso=seekBar.getProgress();
        Double dosisRedondeo1;
        int dosisRedondeo2;
        dosisRedondeo1=peso/3.0;
        if(control==2){
            dosisRedondeo1=dosisRedondeo1*0.35;
        }
        if(varibleSalbutamol==2.5){
            dosisRedondeo1=dosisRedondeo1*0.5;
        }
        dosisRedondeo2 = redondeoFun(dosisRedondeo1);
        if(medicamen_final.contains("TERBUTALINA")){
            dosisAlternativas();
            return;
        }
        if(nombreGenerico.contains("BUDESO")){
            dosisAlternativas();
            return;
        }

        if(dosisRedondeo2 > 10){
            dosisRedondeo2 = 10;
        }
        if(varibleSalbutamol ==2.5){
            if(dosisRedondeo2 < 1){
                dosisRedondeo2 = 1;
                txCantidad.setText(""+dosisRedondeo2+" puf/dosis");
                return;
            }
        }
        if(dosisRedondeo2 < 2){
            dosisRedondeo2 = 2;
        }

        txCantidad.setText(""+dosisRedondeo2+" puf/dosis");
        if(peso<=3){
            txCantidad.setText("");
            txCada.setText("");
        }else {
            cada();
        }
    }

    private void dosisAlternativas() {
        seekBar.setVisibility(View.GONE);
        seekBar.setMinimumHeight(1);
        if(medicamen_final.contains("TERBUTAL") &&  (control==0 || control==1)){
            txCantidad.setText("1 a 2 pu/dosis");
            txCada.setText("Max 8 pul/dia");
            txPeso.setText("          NIÑOS 3 a 12 AÑOS");
        }else {
            txCantidad.setText("1 pul/dosis");
            txCada.setText("Max 8 pul/dia");
            txPeso.setText("          NIÑOS 3 a 12 AÑOS");
        }
        if (medicamen_final.contains("BUDESON")){
            txCantidad.setText("0,25 a 1 mg");
            txCada.setText("12-24 /hrs");
            guideline2.setGuidelinePercent((float) 0.88);
            txPeso.setLines(2);
            txPeso.setText(" De 6 meses a 12 años, maximo 2 mg/dia, en casos muy graves hasta 4 mg/dia");
        }
    }

    private int redondeoFun(Double numero) {
        Double redonDouble;
        String redonString;
        int devolver;
        String redon=decimal(numero);
        redonDouble=Double.valueOf(redon);
        redonString =String.valueOf(redonDouble);
        String redonString2=redonString.replace(".",";");

        String[]partes=redonString2.split(";");

        String first = String.valueOf(partes[1].charAt(0));
        int ter=Integer.valueOf(first);

        if (ter>5){
            int primera = Integer.valueOf(partes[0]);
            primera=primera+1;
            devolver=primera;
        }else {
            devolver=Integer.valueOf(partes[0]);
        }
        return devolver;
    }

    ///if esNebuloizador{
    private void nebuDosis() {
        //SALIMOS SI ES BUDEDONIDA
        if(medicamen_final.contains("BUDESONIDA")){
            return;
        }
        //if esNebuloizador TODO
        int peso=seekBar.getProgress();
        Double pesoInter = (peso*0.03)*varibleSalbutamol;
        Double pesoMg = peso*0.15;
        String pesoInterdeci=decimal(pesoInter);
        String pesoMgdeci=decimal(pesoMg);
        txCantidad.setText(""+formato.format(pesoInter)+" ml = "+formato1.format(pesoMg)+"  mg.");
        if(peso<=3){
            txCantidad.setText("");
            txCada.setText("");
        }else {
            cada();
        }
    }

    private void pintarPeso(int progress) {
        txPeso.setText(""+progress+ " Kg");
    }

    private void dosisNebulizador(){
        if(nebulizador && bromuro){
            //dosisEdad1
            update();
            return;
        }
        if(nebulizador){
            //if esNebuloizador{
            nebuDosis();
        }else {
            // dosisInhalador();
            inhalaDosis();
        }
    }

    //DOSISEDAD1
    private void update() {
        //  private void dosisEdada1() {
        int control1=0;
        if (varibleSalbutamol ==1){
            control1=0;
        }
        if (varibleSalbutamol ==2.5){
            control1=1;
        }
        if (varibleSalbutamol ==5){
            control1=2;
        }
        Double variableJarabe;
        String arrayJarabesAmostrar[]=new String[4];
        //PAISES TODO**************
        String jar = "";
        if (banString.contains("banespana.png")){
            jar = arrayMedica[10];
        }
        if (banString.contains("banbolivia.png")){
            jar = arrayMedica[9];
        }

        arrayJarabesAmostrar=jar.split("-");
        variableJarabe = Double.parseDouble(arrayJarabesAmostrar[control1]);
        int peso=seekBar.getProgress();
        dosisEdad(peso,variableJarabe);
    }

    private void dosisEdad(int peso, Double variableJarabe) {
        int pesoDiez=peso*10;
        String medicamentoAsacar = arrayMedica[1];
        String dosisEdadStr[] = dato(medicamen_final,"dosespecial");

        int primeraEdad = Integer.getInteger(dosisEdadStr[5])*10;
        int segundaEdad = Integer.getInteger(dosisEdadStr[6])*10;

        int primeraEdad1 = Integer.getInteger(dosisEdadStr[10])*10;
        int segundaEdad1 = Integer.getInteger(dosisEdadStr[11])*10;

        int primeraEdad2 = Integer.getInteger(dosisEdadStr[15])*10;
        int segundaEdad2 = Integer.getInteger(dosisEdadStr[16])*10;

        if(pesoDiez >= primeraEdad && pesoDiez<=segundaEdad){
            Double dosisEnvio = Double.parseDouble(dosisEdadStr[3]);
            dosisSegunCantidad(dosisEnvio);//TODO  PENDIENTE

        }
    }

    private void dosisSegunCantidad(Double dosisEnvio) {
        Double ampolla2cc = 2.0;
        Double ampollaCantidad;
        Double dosisFinal=0.0;
        if(varibleSalbutamol ==1){
            ampollaCantidad = 250.0;
            dosisFinal = (ampolla2cc*dosisEnvio) /ampollaCantidad;
        }
        if(varibleSalbutamol ==2.5){
            ampollaCantidad = 500.0;
            dosisFinal = (ampolla2cc*dosisEnvio) /ampollaCantidad;
        }
        String dosisFin = redondeoDecimal(dosisFinal);
        txCantidad.setText(""+dosisFin+" cc");

    }

    private String redondeoDecimal(Double numero) {
        Double redondeoDouble;
        String redondeoString;
        String devolver;
        String redon = decimal(numero);
        redondeoDouble=Double.valueOf(redon);
        redondeoString=String.valueOf(redondeoDouble);
        String [] partes=redondeoString.split(".");
        int segunda=Integer.parseInt(partes[1]);
        if (segunda ==0){
            devolver=String.valueOf(partes[0]);
        }else {
            devolver= redon;
        }
        return devolver;
    }

    private String decimal(Double numero) {
        String devuelto;
        String devuStr = String.valueOf(numero);
        //crea array con dos partes entero y decimal
        String devuStr2 = devuStr.replace(".",";");
        String[] devuSpli= devuStr2.split(";");
        //saca dedimal
        String segunSpli=devuSpli[1];
        String priSpli=devuSpli[0];
        if (priSpli == "0"){
            devuelto = devuSpli[0];
        }else {
            devuelto = (priSpli+"."+segunSpli);
        }
        return devuelto;
    }

    private String[] dato(String medicamento, String tabla) {
        banString=cur_bandera.getString(2);
        String querySQL="",resInt;
        int conta=0,conta32;

        if(tabla=="medicamentos2"){
            querySQL="select * FROM " + Medicamento.DATOS_TABLA_MEDICAMENTOS+ " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
        }
        if(tabla=="jarabes"){
            if(banString =="banespana.png") {
                querySQL = "select * FROM " + DatosReaderDbHelper.TABLA_JARABES + " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
            }
            if(banString =="banbolivia.png") {
                querySQL = "select * FROM " + DatosReaderDbHelper.TABLA_JARABES2 + " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
            }
        }

        if(tabla=="comercial"){
            if (banString.contains("banespana")) {
                querySQL = "select * FROM " + DatosReaderDbHelper.TABLA_COMERCIAL + " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
            }
            if (banString.contains("banbolivia")) {
                querySQL = "select * FROM " + DatosReaderDbHelper.TABLA_COMERCIAL2 + " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
            }
        }

        if(tabla=="dosespecial"){
            querySQL="select * FROM " + DatosReaderDbHelper.TABLA_DOSESPECIAL+ " WHERE " + Medicamento.CN_medicamento + " = \"" + medicamento + "\"";
        }
        manager.openDB();
        Cursor cursor=manager.myDataBase.rawQuery(querySQL,null);
        int largo1 = cursor.getColumnCount();
        cursor.moveToFirst();
        String arrayResul[]=new String[largo1];
        while (conta<largo1){
            resInt=cursor.getString(2);
            resInt=arrayResul[conta];
            conta++;
        }
        manager.close();
        return arrayResul;
    }

    private void cada() {

        if(control == 0 || control ==1 ){
            txCada.setText("Cada 20 min");
        }
        else {
            txCada.setText("Cada 6-8/hrs");
        }
        if(medicamen_final.contains("TERBUTALINA INHALADOR")){
            txCada.setText("Cada 6-8/hrs");
        }
        if(medicamen_final.contains("BUDESONIDA")){
            txCada.setText("Cada 12-24/hrs");
        }
    }

    private void considere() {
        if(inhala && salbuta){
            btConsidere.setVisibility(View.VISIBLE);
        }
        if(bromuro && inhala){
            btConsidere.setVisibility(View.VISIBLE);
        }
        if(terbuta){
            btConsidere.setVisibility(View.INVISIBLE);
        }
        if (medicamen_final.contains("BUDESONIDA")&& (control==0 || control ==1)){
            btConsidere.setVisibility(View.VISIBLE);
            btConsidere.setText("  DEBERIA USAR SALBUTAMOL Y/O ADEMAS");
        }
    }

    private String[] gravedadAsma() {
        manager.openDB();
        //BOTON CONSIDERE

        if (nebulizador){
            btConsidere.setBackground(this.getResources().getDrawable(R.drawable.botonazulredon));
            btConsidere.setText("CONSIDERE INHALADOR");
        }
        if (inhala){
            btConsidere.setBackground(this.getResources().getDrawable(R.drawable.botonazulredon));
            btConsidere.setText("CONSIDERE NEBULIZADOR");
        }
        String selection = AsmaTabla.CN_medicamento+ " = ? ";
        String selectionArgs[] = new String[]{nombreGenerico};

        Cursor cur =manager.myDataBase.query("asma", null, selection, selectionArgs, null, null, null);
        cur.moveToFirst();
        int largo=cur.getColumnCount();
        String devul[] = new String[largo];
        int cont=0;
        while (cont<largo){
            devul[cont]=cur.getString(cont);
            cont++;
        }
        cur.close();
        manager.close();
        //DEVUL CONTIENE  EL CONTENIDO ENTERO
        //return devul;   NEVUL2 SOLOR LA GRAVEDAD
        String gravedad[]=new String[3];
        gravedad[0]=devul[2];gravedad[1]=devul[3];gravedad[2]=devul[4];
        return gravedad;
    }

    private void encabezados() throws SQLException {
        int tamanoTitulzxo = medicamen_final.length();
        String titulo=medicamen_final;
        txTitulo.setTypeface(null, Typeface.BOLD);

        // txTitulo.setText(medicamen_final);
        //  if(medicamen_final.contains("BROMURO DE IPRATROPIO NEBULIZADOR")){
        //      titulo="B. IPRATROPIO NEBU.";
        //  }

        //  if(medicamen_final.contains("BROMURO DE IPRATROPIO INHALADOR")){
        //      titulo="B. IPRATROPIO INHALA.";
//
        //  }
        txTitulo.setText(titulo);

        //PINTAR CONTRAINDICACIONES
        String contra = arrayMedica[11];
        txPrecausiones.setText(contra);
        //SACA NOMBRES COMERCIALES CON NOMBRE GENERICO
        //***********************************

        arrayNombesComer = array_nombresComerciales(medicamen_final);
        //CONFIGURAR SEGMENTO
    }

    //region FUNCIONES DE EXTRACCION DE DATOS
    private String[] arrayMedicamento(String medicina){
        Cursor medi=null;
        try {
            medi= manager.buscarMed(medicina);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        medi.moveToFirst();
        int largo=medi.getColumnCount();
        String devul[]=new String[largo];
        int cont=0;
        while (cont<largo){
            devul[cont]=medi.getString(cont);
            cont++;
        }
        medi.close();
        manager.close();
        return devul;
    }

    private String[] array_nombresComerciales(String medicina){
        String jar="";
        //SE ELIJE ENTRE LOS PAISES EXISTENTES**************
        if(banString.contains("banespana")){
            jar = "jarabes";
        }
        if(banString.contains("banbolivia")){
            jar = "jarabes2";
        }
        manager.openDB();
        String selection = Jarabes.CN_medicamento+ " = ? ";
        String selectionArgs[] = new String[]{medicina};
        Cursor c =manager.myDataBase.query(
                //   "jarabes",
                jar,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        int largo=c.getColumnCount();
        String devul[]=new String[largo];

        int contaddor=0;
        while (contaddor<largo){
            String valor=c.getString(contaddor);
            devul[contaddor]=valor;
            contaddor++;
        }
        manager.close();
        return devul;
    }

    private String nomFinal(String medFinal){
        String resultado="";
        manager.openDB();
        String query ="select * FROM " + DatosReaderDbHelper.TABLA_COMERCIAL+ " WHERE " + Medicamento.CN_medicamento + " = \"" + medFinal + "\"";
        Cursor cursor =manager.myDataBase.rawQuery(query,null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);
        manager.close();
        return resultado;
    }
    //region METODOS PARA CONFIGURAR ELEMENTOS
    private void setupSlider() {

    }

    public void onCli2(MenuItem item) {
        Intent intent = new Intent(Asma2.this, BanderaActivity.class);
        startActivity(intent);
    }

    //FUNCION BOTONES PARA GRAVEDAD
    public void onClick(View view) {
        switch (view.getId()){
            //BOTONES GRAVE A LEVE CONFIGURAR
            case R.id.btdGrave:
                btGrave.setTextColor(Color.BLUE);
                btGrave.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btModerada.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btModerada.setTextColor(Color.WHITE);
                btLeve.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btLeve.setTextColor(Color.WHITE);
                txGravedad.setText(graveAsma[0]);
                if(nebulizador){
                    btConsidere.setVisibility(View.GONE);
                }
                if(inhala){
                    btConsidere.setVisibility(View.VISIBLE);
                }
                if(medicamen_final.contains("BUDESONIDA")){
                    btConsidere.setVisibility(View.VISIBLE);
                }
                control=0;
                cada();
                dosis();
                break;
            case R.id.btdModerada:
                btModerada.setTextColor(Color.BLUE);
                btModerada.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btGrave.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btGrave.setTextColor(Color.WHITE);
                btLeve.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btLeve.setTextColor(Color.WHITE);
                txGravedad.setText(graveAsma[1]);
                if(nebulizador){
                    btConsidere.setVisibility(View.GONE);
                }
                if(inhala && salbuta){
                    btConsidere.setVisibility(View.VISIBLE);
                }
                if(medicamen_final.contains("BUDESONIDA")){
                    btConsidere.setVisibility(View.VISIBLE);
                }
                control=1;
                cada();
                dosis();
                break;
            case R.id.btdLeve:
                btLeve.setTextColor(Color.BLUE);
                btLeve.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btGrave.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btGrave.setTextColor(Color.WHITE);
                btModerada.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btModerada.setTextColor(Color.WHITE);
                txGravedad.setText(graveAsma[2]);
                if(inhala && salbuta){
                    btConsidere.setVisibility(View.GONE);
                }
                if(nebulizador && salbuta){
                    btConsidere.setVisibility(View.VISIBLE);
                }
                if(bromuro && inhala){
                    btConsidere.setVisibility(View.GONE);
                }
                if(medicamen_final.contains("BUDESONIDA")){
                    btConsidere.setVisibility(View.GONE);
                }
                control=2;
                cada();
                dosis();
                break;
        }
    }

    //FUNCION BOTONES PARA OTRAS PRESENTACIONES
    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btdPri:
                btOtra1.setTextColor(Color.BLUE);
                btOtra1.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btOtra2.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra2.setTextColor(Color.WHITE);
                btOtra3.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra3.setTextColor(Color.WHITE);
                btOtra4.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra4.setTextColor(Color.WHITE);
                txOtras.setText(arrayNombesComer[2]);
                position=0;
                varibleSalbutamol=1.0;
                nebuDosis();
                if (inhala){
                    inhalaDosis();
                }

                break;
            case R.id.btdSeg:
                btOtra2.setTextColor(Color.BLUE);
                btOtra2.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btOtra1.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra1.setTextColor(Color.WHITE);
                btOtra3.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra3.setTextColor(Color.WHITE);
                btOtra4.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra4.setTextColor(Color.WHITE);
                txOtras.setText(arrayNombesComer[3]);
                position=1;
                varibleSalbutamol=2.5;
                nebuDosis();
                if (inhala){
                    inhalaDosis();
                }

                break;
            case R.id.btdTer:
                btOtra3.setTextColor(Color.BLUE);
                btOtra3.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btOtra1.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra1.setTextColor(Color.WHITE);
                btOtra2.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra2.setTextColor(Color.WHITE);
                btOtra4.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra4.setTextColor(Color.WHITE);
                position=2;
                txOtras.setText(arrayNombesComer[4]);
                varibleSalbutamol=5.0;
                nebuDosis();
                if (inhala){
                    inhalaDosis();
                }

                break;
            case R.id.btdCuar:
                btOtra4.setTextColor(Color.BLUE);
                btOtra4.setBackground(this.getResources().getDrawable(R.drawable.redondeado));
                btOtra1.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra1.setTextColor(Color.WHITE);
                btOtra2.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra2.setTextColor(Color.WHITE);
                btOtra3.setBackground(this.getResources().getDrawable(R.drawable.refodogris));
                btOtra3.setTextColor(Color.WHITE);
                position=3;
                txOtras.setText(arrayNombesComer[5]);
                varibleSalbutamol=5.0;
                nebuDosis();
                if (inhala){
                    inhalaDosis();
                }

                break;
        }
    }

    public void onClick6(View view) {
        Intent intent = new Intent(Asma2.this, Segundo.class);
        startActivity(intent);
    }

    //CAMBIA A ASMA2
    public void onClick7(View view){
        Intent intent = new Intent(Asma2.this, Asma.class);
        String text = selector(medicamen_final);
        Bundle bundle = new Bundle();
        bundle.putString("medigene", text);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private String selector(String inter) {
        String inhala= "INHALADOR",salbuta="SALBUTAMOL",atro="IPRATROPIO",nebuli="NEBULIZADOR",variable="";
        if (inter.contains(inhala)&&inter.contains(salbuta)){
            variable="SALBUTAMOL NEBULIZADOR";
        }
        if (inter.contains(inhala)&&inter.contains(atro)){
            variable="BROMURO DE IPRATROPIO NEBULIZADOR";
        }
        if (inter.contains(nebuli)&&inter.contains(salbuta)){
            variable="SALBUTAMOL INHALADOR";
        }
        if (inter.contains(nebuli)&&inter.contains(atro)){
            variable="BROMURO DE IPRATROPIO INHALADOR";
        }

        return variable;
    }


    public void onClickBandera(View view) {
        Intent intent = new Intent(Asma2.this, BanderaActivity.class);
        startActivity(intent);
    }

    //CAMBIO ICONO BANDERA
    private void cambioBandera(String bandera){
        if (bandera.contains("bolivia")){
            btBandera.setBackground(this.getResources().getDrawable(R.drawable.banbolivia));}
        if (bandera.contains("espana")){
            btBandera.setBackground(this.getResources().getDrawable(R.drawable.banespana));}
        if (bandera.contains("peru")){
            btBandera.setBackground(this.getResources().getDrawable(R.drawable.banperu));}
        if (bandera.contains("chile")){
            btBandera.setBackground(this.getResources().getDrawable(R.drawable.banchile));}
    }




}
