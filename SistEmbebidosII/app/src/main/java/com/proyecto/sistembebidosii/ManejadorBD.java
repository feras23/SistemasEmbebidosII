package com.proyecto.sistembebidosii;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ManejadorBD extends SQLiteOpenHelper {
    // Version de la BD
    private static final int DATABASE_VERSION = 3;

    // Nombre BD
    private static final String DATABASE_NAME = "WakeMeUp";

    // Tabla de Ubicaciones
    private static final String TABLE_LOCATIONS = "Ubicaciones";

    // Columnas
    private static final String KEY_ID = "id";
    private static final String KEY_TITULO = "title";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_LATITUD = "latitude";
    private static final String KEY_LONGITUD = "longitude";
    private static final String KEY_ACTIVO = "activo";

    public ManejadorBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creando la Tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITULO + " TEXT,"
                + KEY_DIRECCION + " TEXT,"
                + KEY_LATITUD + " REAL,"
                + KEY_LONGITUD + " REAL,"
                + KEY_ACTIVO + " INTEGER"+ ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    // Actualizando BD
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    //Operaciones
    public boolean agregarUbicacion(Ubicacion ubicacion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, ubicacion.getTitulo());
        values.put(KEY_DIRECCION, ubicacion.getDireccion());
        values.put(KEY_LATITUD, ubicacion.getLatitud());
        values.put(KEY_LONGITUD, ubicacion.getLongitud());
        values.put(KEY_ACTIVO, ubicacion.getActivo());
        try {
            if (db.insert(TABLE_LOCATIONS, null, values) == -1) {
                return false;
            } else {
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            db.close();
        }
        return false;
    }

    public List<Ubicacion> obtenerUbicaciones(){
        List<Ubicacion> listaUbicaciones = new ArrayList<Ubicacion>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setID(Integer.parseInt(cursor.getString(0)));
                ubicacion.setTitulo(cursor.getString(1));
                ubicacion.setDireccion(cursor.getString(2));
                ubicacion.setLatitud(cursor.getDouble(3));
                ubicacion.setLongitud(cursor.getDouble(4));
                ubicacion.setActivo(cursor.getInt(5));
                listaUbicaciones.add(ubicacion);
            } while (cursor.moveToNext());
        }

        return listaUbicaciones;
    }

    public List<Ubicacion> obtenerUbicacionesActivas(){
        List<Ubicacion> listaUbicaciones = new ArrayList<Ubicacion>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " WHERE activo = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setID(Integer.parseInt(cursor.getString(0)));
                ubicacion.setTitulo(cursor.getString(1));
                ubicacion.setDireccion(cursor.getString(2));
                ubicacion.setLatitud(cursor.getDouble(3));
                ubicacion.setLongitud(cursor.getDouble(4));
                ubicacion.setActivo(cursor.getInt(5));
                listaUbicaciones.add(ubicacion);
            } while (cursor.moveToNext());
        }

        return listaUbicaciones;
    }

    public int cuentaUbicaciones(){
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int cuentaUbicacionesActivas(){
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " WHERE activo = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void actualizarUbicacion(Ubicacion ubicacion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, ubicacion.getTitulo());
        values.put(KEY_DIRECCION, ubicacion.getDireccion());
        values.put(KEY_LATITUD, ubicacion.getLatitud());
        values.put(KEY_LONGITUD, ubicacion.getLongitud());
        values.put(KEY_ACTIVO, ubicacion.getActivo());

        db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(ubicacion.getID())});
    }

    public void actualizarEstado(Ubicacion ub, int estado){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVO, estado);

        db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(ub.getID())});
    }

    public void eliminarUbicacion(Ubicacion ubicacion){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(ubicacion.getID()) });
        db.close();
    }


}