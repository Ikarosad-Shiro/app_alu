package com.example.aluasistencias;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de usuarios
    private static final String TABLE_USUARIOS = "usuario";
    private static final String COLUMN_ID_USUARIO = "IdUsuario"; // Clave primaria
    private static final String COLUMN_ROL = "rol";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NICK = "nick";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_ESTADO = "estado";

    // Tabla de repartos
    private static final String TABLE_REPARTOS = "repartos";
    private static final String COLUMN_ID_REPARTO = "IdReparto";
    private static final String COLUMN_ID_USUARIO_REPARTO = "IdUsuario"; // Clave foránea
    private static final String COLUMN_DIRECCION_ENTREGA = "Direccion_entrega";
    private static final String COLUMN_ESTATUS = "Estatus";
    private static final String COLUMN_ANOTACIONES = "Anotaciones";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TABLE_USUARIOS + " ("
                + COLUMN_ID_USUARIO + " TEXT PRIMARY KEY, "
                + COLUMN_ROL + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_NICK + " TEXT NOT NULL, "
                + COLUMN_PASS + " TEXT NOT NULL, "
                + COLUMN_ESTADO + " TEXT DEFAULT 'Deshabilitado'"
                + ")";
        db.execSQL(CREATE_USUARIOS_TABLE);

        // Crear tabla de repartos
        String CREATE_REPARTOS_TABLE = "CREATE TABLE " + TABLE_REPARTOS + " ("
                + COLUMN_ID_REPARTO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID_USUARIO_REPARTO + " TEXT NOT NULL, "
                + COLUMN_DIRECCION_ENTREGA + " TEXT NOT NULL, "
                + COLUMN_ESTATUS + " TEXT NOT NULL, "
                + COLUMN_ANOTACIONES + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_ID_USUARIO_REPARTO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID_USUARIO + ")"
                + ")";
        db.execSQL(CREATE_REPARTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas si existen y volver a crearlas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPARTOS);
        onCreate(db);
    }

    // Método para insertar un usuario
    public void insertarUsuario(String idUsuario, String rol, String email, String nick, String pass, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_USUARIO, idUsuario);
        values.put(COLUMN_ROL, rol);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NICK, nick);
        values.put(COLUMN_PASS, pass);
        values.put(COLUMN_ESTADO, estado);
        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    // Método para insertar un reparto
    public void insertarReparto(String idUsuario, String direccion, String estatus, String anotaciones) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_USUARIO_REPARTO, idUsuario);
        values.put(COLUMN_DIRECCION_ENTREGA, direccion);
        values.put(COLUMN_ESTATUS, estatus);
        values.put(COLUMN_ANOTACIONES, anotaciones);
        db.insert(TABLE_REPARTOS, null, values);
        db.close();
    }
}