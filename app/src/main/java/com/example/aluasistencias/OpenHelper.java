package com.example.aluasistencias;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Locations.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "ubicacion";

    // Columnas de la tabla ubicacion
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CALLE = "calle";
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LONGITUD = "longitud";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_HORA = "hora";

    // Sentencia SQL para crear la tabla ubicacion
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CALLE + " TEXT NOT NULL, "
            + COLUMN_LATITUD + " REAL NOT NULL, "
            + COLUMN_LONGITUD + " REAL NOT NULL, "
            + COLUMN_FECHA + " TEXT NOT NULL, "
            + COLUMN_HORA + " TEXT NOT NULL);";

    private Context context;

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Log.d("DatabaseOpenHelper", "Tabla creada exitosamente.");
        } catch (Exception e) {
            Log.e("DatabaseOpenHelper", "Error al crear la tabla: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    private String getStreetNameFromCoordinates(Geocoder geocoder, double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Devuelve la primera línea de la dirección (esto generalmente incluye el nombre de la calle)
                return address.getAddressLine(0);  // Retorna la calle obtenida
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se puede obtener la calle, retorna null
    }

}