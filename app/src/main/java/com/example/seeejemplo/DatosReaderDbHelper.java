package com.example.seeejemplo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class DatosReaderDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sentencia.sqlite";
    public static final int DATABASE_VERSION = 1;
    public Context myContext;
    public SQLiteDatabase myDataBase;
    public static final String TABLA_MEDICAMENTOS2 = "medicamentos2";
    public static final String TABLA_DOSESPECIAL = "dosespecial";
    public static final String TABLA_COMERCIAL = "comercial";
    public static final String TABLA_COMERCIAL2 = "comercial2";
    public static final String TABLA_PESO_EDAD = "peso_edad";
    public static final String TABLA_JARABES = "jarabes";
    public static final String TABLA_JARABES2 = "jarabes2";
    public static final String TABLA_ASMA = "asma";

    private static final String COLUMNA_MEDICAMENTO = "medicamento";
    private static final String COLUMNA_PESO = "peso";

    DecimalFormat df = new DecimalFormat("0.0");
    DecimalFormat d = new DecimalFormat("0");

    //Ruta por defecto de las bases de datos en el sistema Android
    private static String DB_PATH;


    public DatosReaderDbHelper(Context myContext) {
        super(myContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = myContext;
        this.DB_PATH = this.myContext.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    /**
     * Crea una base de datos vacia en el sistema y la reescribe con nuestro fichero de base de datos.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //la base de datos existe y no hacemos nada.
        } else {
            //Llamando a este metodo se crea la base de datos vac�a en la ruta por defecto del sistema
            //de nuestra aplicaci�n por lo que podremos sobreescribirla con nuestra base de datos.
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copiando Base de Datos");
            }
        }
    }

    /**
     * Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicacion.
     *
     * @return true si existe, false si no existe
     */
    private boolean checkDataBase() {

        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();

        // SQLiteDatabase checkDB = null;
        //
        // try {
        // String myPath = DB_PATH + DB_NAME;
        // checkDB = SQLiteDatabase.openDatabase(myPath, null,
        // SQLiteDatabase.OPEN_READWRITE); // changing to writable
        //
        // } catch (SQLiteException e) {
        //
        // // database does't exist yet.
        //
        // }
        //
        // if (checkDB != null) {
        //
        // checkDB.close();
        //
        // }
        //
        // return checkDB != null ? true : false;
    }


    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    /**
     * Copia nuestra base de datos desde la carpeta assets a la recien creada
     * base de datos en la carpeta de sistema, desde donde podremos acceder a ella.
     * Esto se hace con bytestream.
     */
    private void copyDataBase() throws IOException {
        //Abrimos el fichero de base de datos como entrada
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        //Ruta a la base de datos vacia recien creada
        String outFileName = DB_PATH + DATABASE_NAME;

        //Abrimos la base de datos vacìa como salida
        OutputStream myOutput = new FileOutputStream(outFileName);

        //Transferimos los bytes desde el fichero de entrada al de salida
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Liberamos los streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDB() {
        try {
            createDataBase();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("DataBaseHelper", e.toString());
        }

        openDataBase();
        getWritableDatabase();
        getReadableDatabase();

    }

    public void openDataBase() throws android.database.SQLException {

        // Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE // this was readable only
                        | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    public SQLiteDatabase getInstanceOfDB() {
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE // this was readable only
                        | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public static double[] sacar_num(String valor) {
        int largo, contador;
        Boolean saltar = true;
        contador = 0;
        String resul_string[];
        resul_string = valor.split("-");
        largo = resul_string.length;
        double dosis_finales[] = new double[largo];
        while (saltar) {
            try {
                dosis_finales[contador] = Double.parseDouble(resul_string[contador]);
                contador++;
            } catch (Exception e) {
                saltar = false;
            }
        }
        return dosis_finales;
    }

    public static String[] sacar_numStrin(String valor) {
        int largo, contador;
        Boolean saltar = true;
        contador = 0;
        String resul_string[];
        resul_string = valor.split("-");
        return resul_string;
    }



    public static void dosis(double dosis[], int progre, double cada[], double var_jar, double ins_r, TextView most) {
        double primera_dosis, segun_dosis;
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat d = new DecimalFormat("0");
        try {
            primera_dosis = (((dosis[0] * ins_r) * progre) / (24 / cada[0])) / (var_jar / 5);
            segun_dosis = (((dosis[1] * ins_r) * progre) / (24 / cada[0])) / (var_jar / 5);
            most.setText("Jarabe  " + d.format(var_jar) + "        " + df.format(primera_dosis) + "  a " + df.format(segun_dosis) + "  cc");
        } catch (Exception e) {
            primera_dosis = (((dosis[0] * ins_r) * progre) / (24 / cada[0])) / ((var_jar / 5));
            most.setText("Jarabe   " + d.format(var_jar) + "      " + df.format(primera_dosis) + "  cc");
        }
    }


    public void modificarbandera(String value){
        openDB();
        String var_bandera = "BANDERA";
        ContentValues registro = new ContentValues();
        registro.put(Medicamento.CN_dosisAddia,value);
        myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS,registro,
                Medicamento.CN_medicamento + "=?", new String[]{var_bandera});
        close();
    }
    public void pasarDatos(String clase3, String botonAc4){
        openDB();
        String var_bandera = "BANDERA";
        ContentValues registro = new ContentValues();
        registro.put(Medicamento.CN_DosisP,clase3);
        registro.put(Medicamento.CN_DosisP1,botonAc4);
        myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS,registro,
                Medicamento.CN_medicamento + "=?", new String[]{var_bandera});
        close();
    }


    //obtener cur_medicamento por nombre
    public Cursor buscarMed(String medicina) throws SQLException {
        openDB();
        String selection = Medicamento.CN_medicamento+ " = ? ";
        String selectionArgs[] = new String[]{medicina};
        Cursor c =myDataBase.query(
                "medicamentos2",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    //DEVULEVE EL NOMBRE GENERICO INRODUCIDO
    public String mediComer(String medicina){
        openDB();
        String devol=null;
        String selection = Comercial.CN_nobrecom+ " = ? ";
        String selectionArgs[] = new String[]{medicina};
        Cursor c =myDataBase.query(
                "comercial",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c != null) {
            c.moveToFirst();
            devol=c.getString(1);
        }
        return devol;

    }








    //BUSCAR Y EXTRER SI HAY EN TABLA DOSIS ESPECIALES

    public Cursor dos_esp(String medicina) throws SQLException {
        openDB();
        String query = "select * FROM " + TABLA_DOSESPECIAL + " WHERE " + COLUMNA_MEDICAMENTO + " = \"" + medicina + "\"";
        Cursor ce = myDataBase.rawQuery(query,null);

        if (ce != null) {
            ce.moveToFirst();
        }
        return ce;
    }


    public Cursor nom_com(String nombrcomer) throws SQLException {
        openDB();
        String query = "select * FROM " + TABLA_COMERCIAL + " WHERE " + Medicamento.CN_nobrecom + " = \"" + nombrcomer + "\"";
        Cursor cex = myDataBase.rawQuery(query,null);

        if (cex != null) {
            cex.moveToFirst();
        }
        return cex;
    }


    public Cursor edad(String peso)throws SQLException {
        openDB();
        String query = "select * FROM " + TABLA_PESO_EDAD + " WHERE " + COLUMNA_PESO + " = \"" + peso + "\"";
        Cursor ce = myDataBase.rawQuery(query,null);

        if(ce != null){
            ce.moveToFirst();
        }
        return ce;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }





    //  INSERTAR DATOS DE INICIO EN AMOXICILINA
    public void val_inicio() throws SQLException {
        openDB();
        String var_paraceta ="PARACETAMOL";
        String var_amoxic="AMOXICILINA";
        Cursor p=buscarMed(var_paraceta);
        Cursor a=buscarMed(var_amoxic);


        int primeraVezP=p.getInt(6);
        int paraFecha,amoFecha;

        //TODO pendiene de hacer

        paraFecha=p.getInt(6);
        amoFecha=a.getInt(6);


        int masdias=1;

        Date fecha = new Date();
        Date masfecha = new Date();

        masfecha=sumarRestarDiasFecha(fecha,masdias);

        String fechaString=fecha.toString();
        String fe_mas=masfecha.toString();

        if ((primeraVezP == 0) || (paraFecha<=amoFecha)) {
            primeraVezP=1;
            ContentValues registro = new ContentValues();
            ContentValues registro1 = new ContentValues();
            registro.put(Medicamento.CN_nebulizada, fechaString);
            registro.put(Medicamento.CN_DosisP2,primeraVezP);
            registro1.put(Medicamento.CN_nebulizada,fe_mas);
            myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS,registro,Medicamento.CN_medicamento + "=?", new String[]{var_paraceta});
            myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS,registro1,Medicamento.CN_medicamento + "=?", new String[]{var_amoxic});
        }

    }



    public Date sumarRestarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

    }

    public void cambioBandera(int bandera){
        int valor;
        if(bandera ==0){
            String var_amoxi="AMOXICILINA";
            valor=0;
            ContentValues registro = new ContentValues();
            registro.put(Medicamento.CN_DosisP2,valor);
            myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS, registro,
                    Medicamento.CN_medicamento + "=?", new String[]{var_amoxi});
        }
        else{
            String var_amoxi="AMOXICILINA";
            valor=1;
            ContentValues registro = new ContentValues();
            registro.put(Medicamento.CN_DosisP2,valor);
            myDataBase.update(DatosRegistro.DATOS_TABLA_MEDICAMENTOS, registro,
                    Medicamento.CN_medicamento + "=?", new String[]{var_amoxi});
        }

    }





    public Boolean validarBandera() throws SQLException {
        Boolean esBoli=null;
        Cursor para=buscarMed("AMOXICILINA");
        int valorBandera=para.getInt(5);
        if (valorBandera==0){
            esBoli=true;
        }
        else {
            esBoli=false;
        }
        return esBoli;
    }

    public String[][] sacar_datos_en_array (String medicamen,String bandera) throws
            java.sql.SQLException {
        String[][] array_inter = new String[20][9];
        Cursor c = null;
        try {
            c = buscarMed(medicamen);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        c.moveToFirst();
        int m = 0, j = 0, d = 0;

        while (m <= 19) {
            array_inter[m][0] = c.getString(m);
            m++;
        }
        c.close();
        try {
            c = jar_NombreComerciales(medicamen, bandera);
        } catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        while (j <= 7) {
            array_inter[0][j] = c.getString(j);
            j++;
        }
        c.close();
        return array_inter;

    }

    //DOSIS ESPECIALES
    public double[] sacar_dosis_especial (String medicamen_final) {
        Cursor dosesp=null;
        double[] resultado = new double[18];
        int cont = 0;
        try {
            //SACAMOS DOSIS ESPECIALES
            dosesp = dos_esp(medicamen_final);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        while (cont < 17) {
            resultado[cont] = dosesp.getDouble(cont);
            cont++;
        }
        return resultado;
    }



    //obtener lista de jarabes
    public Cursor jar_NombreComerciales(String medicamento, String tablaPais) throws SQLException {
        String pais=null;
        if(tablaPais.equals("banbolivia.png")){
            pais="jarabes2";}
        if(tablaPais.equals("banespana.png")){
            pais="jarabes";}


        String query = "select * FROM " + pais + " WHERE " + COLUMNA_MEDICAMENTO + " = \"" + medicamento + "\"";
        Cursor c = myDataBase.rawQuery(query,null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Vector jarabes_No_Comer(String medicamento, String comer, String jarabes[],String pais) throws SQLException {
        String query=null;
        if (pais.equals("banespana.png")) {
            query = "select * FROM " + TABLA_JARABES + " WHERE " + COLUMNA_MEDICAMENTO + " = \"" + medicamento + "\"";
        }
        if (pais.equals("banbolivia.png")) {
            query = "select * FROM " + TABLA_JARABES2 + " WHERE " + COLUMNA_MEDICAMENTO + " = \"" + medicamento + "\"";
        }
        Cursor c = myDataBase.rawQuery(query,null);
        if (c != null) {
            c.moveToFirst();
        }
        String[]nue=new String[5];
        nue[0]=c.getString(2);
        nue[1]=c.getString(3);
        nue[2]=c.getString(4);
        nue[3]=c.getString(5);
        nue[4]=c.getString(6);
        int cont=0,con2=0,con3=0;
        while (cont<5) {
            int valor=nue[cont].length();
            if (valor>2){
                con2=cont;
            }
            cont++;
        }
        String mios[]=new String[con2+1];
        while (con3<=(con2)){
            mios[con3]=nue[con3];
            con3++;
        }
        int largo=mios.length;
        int [][]dato_var1={{0,1,2,3,4},{1,0,2,3,4},{2,0,1,3,4},{3,0,1,2,4},{4,0,1,2,3}};


        int con1=0,variable=0;

        while(con1<largo){
            int res1=mios[con1].indexOf(comer);
            if(res1!=-1){
                variable=con1;
                break;            }
            con1++;       }
        String[] jarabes_comercial3 = new String[largo];
        String[] jaraDosis=new String[largo];

        int contaArra=0;
        while(contaArra<(largo)){
            jarabes_comercial3[contaArra]=mios[dato_var1[variable][contaArra]];
            jaraDosis[contaArra]=jarabes[dato_var1[variable][contaArra]];

            contaArra++;
        }
        Vector nuevo = new Vector(2);
        nuevo.add(jarabes_comercial3);
        nuevo.add(jaraDosis);
        return nuevo;
    }







    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public String dosis_kilo1(int res_espiner, double resSeeb, double[] dosis_intDou, double[] cada_double, String[] array_jarabes,String medicamen_final, int cada_int) {
        double resultado, resultado1;
        String res;
        double minimoPeso=3; //ESTABLECE PESO MINIMO EN KILOGRAMOS
        double seekBar_minimo= nuevores(resSeeb,minimoPeso);
        double jara_int = Double.parseDouble(array_jarabes[res_espiner]);
        try {
            resultado = ((dosis_intDou[0] * seekBar_minimo) / (24 / cada_double[0])) / ((jara_int) / 5);
            resultado1 = ((dosis_intDou[1] * seekBar_minimo) / (24 / cada_double[0])) / ((jara_int) / 5);
            res=si_gotas2(resultado,resultado1, medicamen_final,cada_int);
            //  txCantidad.setText(res);
        } catch (Exception e) {
            resultado = ((dosis_intDou[0] * seekBar_minimo) / (24 / cada_double[0])) / ((jara_int) / 5);
            resultado1=0;
            res=si_gotas2(resultado,resultado1, medicamen_final,cada_int);
            if(resultado==0){
                res="";
                //   txCantidad.setText("");
            }else {
                //   txCantidad.setText(res);
            }
        }
        return res;
    }

    private double nuevores ( double resSeeb, double minimoPeso){
        double resul;
        if (resSeeb <= minimoPeso) {
            resul = 0;
        } else {
            resul = resSeeb;
        }
        return resul;
    }
    public String si_gotas2 ( double resultado, double re1, String medi, int cada_int){
        boolean cont = true, gotas = false;
        String nue = "";
        int variaGotas = 20;
        double resFinalgotas = resultado, resFinalgotas2 = re1;
        String predni = "PREDNISOLONA", dexgotas = "DEXTROMETORFANO",paragotas = "PARACETAMOL",levogotas = "LEVOCETIRIZINA",ibugotas = "IBUPROFENO",levodrogotas = "LEVODROPROPIZINA";
        if (resultado < 1) {
            if (medi.equals(predni)) {
                gotas = true;
                variaGotas = 40; }
            if (medi.equals(dexgotas)) {
                gotas = true;
                variaGotas = 27; }
            if (medi.equals(paragotas) || medi.equals(levogotas )|| medi.equals(ibugotas)|| medi.equals(levodrogotas)) {
                gotas = true; }
        }
        if (gotas) {
            try {
                if (resultado < 1.5) {
                    resFinalgotas = resultado * variaGotas;
                } else {
                    resFinalgotas = resultado; }
                if (re1 < 2) {
                    resFinalgotas2 = re1 * variaGotas; }
                else {
                    resFinalgotas2 = re1; }
            } catch (Exception e) {
                if (resultado < 1.5) {
                    resFinalgotas = resultado * variaGotas;
                } else {
                    resFinalgotas = resultado;}
            }
        }


        if (re1 == 0) {
            if (gotas) {
                nue = (d.format(resFinalgotas) + " gotas");
            } else {
                nue = (df.format(resFinalgotas) + " cc");
            }
        } else {
            if (gotas) {
                nue = (d.format(resFinalgotas) + " a " + d.format(resFinalgotas2) + " gotas");
            } else {
                nue = (df.format(resFinalgotas) + " a " + df.format(resFinalgotas2) + " cc");
            }
        }
        if (resultado == 0) {
            nue = "";
        }
        return nue;
    }

    public double resul_dosis1 ( double[] dosis, int contador, double sekbar){
        double[] edad_int = new double[10];
        int cont3 = 0;
        double dosis_valor1, dosis_valor2, edad_valor1, edad_valor2, edad_resul, dosis_resul, r_final = 0;
        dosis_valor1 = dosis[contador];
        dosis_valor2 = dosis[contador + 1];
        edad_valor1 = dosis[contador + 2];
        edad_valor2 = dosis[contador + 3];
        dosis_resul = (dosis_valor2 - dosis_valor1) / 10;
        edad_resul = (edad_valor2 - edad_valor1) / 10;
        for (int i = 0; i < 10; i++) {
            edad_int[cont3] = (edad_resul * cont3) + edad_valor1;
            cont3++;
        }
        int cont2 = 0, resul1 = 0;
        while (cont2 < 10) {
            if ((sekbar / 12) >= edad_int[cont2] && (sekbar / 12) <= edad_int[cont2 + 1]) {
                break;
            }
            cont2++;
        }
        r_final = (dosis_resul * (cont2)) + dosis_valor1;
        return r_final;
    }

    //*****SACA DOSIS DE ACUERDO A EDAD VA CON dosis_final_edad  ALERGIA, TOS
    public double[] dosis_final_edad(double[] dosis, double valor_seekb1){
        //<editor-fold desc="SE NECEITA SEEKBARR Y VALORES DE DOSIS EDAD = ultima_dosis_edad ALERGIA, TOS">
        double[] resultado = new double[1];
        double valor_seekb;
        valor_seekb = valor_seekb1 / 12;

        int contador = 0;
        if (dosis[5] <= valor_seekb && dosis[6] >= valor_seekb) {
            contador = 3;
            resultado[0] = resul_dosis1(dosis, contador, valor_seekb1);
        }
        if (dosis[10] <= valor_seekb && dosis[11] >= valor_seekb) {
            contador = 8;
            resultado[0] = resul_dosis1(dosis, contador, valor_seekb1);
        }
        if (dosis[15] <= valor_seekb && dosis[16] >= valor_seekb) {
            contador = 13;
            resultado[0] = resul_dosis1(dosis, contador, valor_seekb1);
        }
        if (valor_seekb < dosis[5]) {
        }
        return resultado;
        //</editor-fold>(ULTIM

    }


}
