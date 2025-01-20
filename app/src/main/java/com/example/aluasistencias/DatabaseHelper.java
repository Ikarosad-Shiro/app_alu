package com.example.aluasistencias;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    public static final String DATABASE_NAME = "delivery_db";
    // Nombre de la tabla
    public static final String TABLE_NAME = "deliveries";

    // Columnas de la tabla
    public static final String COL_ID = "id";
    public static final String COL_CLIENT_NAME = "client_name";
    public static final String COL_PRODUCT_LIST = "product_list";
    public static final String COL_DELIVERY_TIME = "delivery_time";
    public static final String COL_IS_COMPLETED = "is_completed";
    public static final String COL_LATITUDE = "latitude"; // Columna para latitud
    public static final String COL_LONGITUDE = "longitude"; // Columna para longitud
    public static final String COL_ADDRESS = "address"; // Columna para la dirección

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Cambié la versión a 2 para actualizar la base de datos
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla con las nuevas columnas
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLIENT_NAME + " TEXT, " +
                COL_PRODUCT_LIST + " TEXT, " +
                COL_DELIVERY_TIME + " TEXT, " +
                COL_IS_COMPLETED + " INTEGER DEFAULT 0, " +
                COL_LATITUDE + " REAL, " +
                COL_LONGITUDE + " REAL, " +
                COL_ADDRESS + " TEXT)"; // Nueva columna para la dirección
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si hay una nueva versión de la base de datos, eliminamos la tabla anterior y la creamos nuevamente
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para insertar un nuevo registro
    public boolean insertRecord(String clientName, String productList, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CLIENT_NAME, clientName); // Agregar nombre del cliente
        contentValues.put(COL_PRODUCT_LIST, productList); // Agregar lista de productos
        contentValues.put(COL_LATITUDE, latitude); // Guardar la latitud
        contentValues.put(COL_LONGITUDE, longitude); // Guardar la longitud
        contentValues.put(COL_ADDRESS, address); // Guardar la dirección

        long result = db.insert(TABLE_NAME, null, contentValues); // Insertar el registro
        return result != -1; // Devuelve verdadero si la inserción fue exitosa
    }

    // Método para obtener todos los registros
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // Obtener todos los registros
    }

    // Método para obtener un registro por ID
    public Record getRecordById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int clientNameIndex = cursor.getColumnIndex(COL_CLIENT_NAME);
            int productListIndex = cursor.getColumnIndex(COL_PRODUCT_LIST);
            int isCompletedIndex = cursor.getColumnIndex(COL_IS_COMPLETED);
            int latitudeIndex = cursor.getColumnIndex(COL_LATITUDE); // Obtiene la latitud
            int longitudeIndex = cursor.getColumnIndex(COL_LONGITUDE); // Obtiene la longitud
            int addressIndex = cursor.getColumnIndex(COL_ADDRESS); // Obtiene la dirección

            // Verificar que las columnas existan
            if (clientNameIndex != -1 && productListIndex != -1 && isCompletedIndex != -1 &&
                    latitudeIndex != -1 && longitudeIndex != -1 && addressIndex != -1) {
                String clientName = cursor.getString(clientNameIndex);
                String productList = cursor.getString(productListIndex);
                int isCompleted = cursor.getInt(isCompletedIndex);
                double latitude = cursor.getDouble(latitudeIndex); // Obtener la latitud
                double longitude = cursor.getDouble(longitudeIndex); // Obtener la longitud
                String address = cursor.getString(addressIndex); // Obtener la dirección
                cursor.close();
                return new Record(id, clientName, productList, isCompleted, address, latitude, longitude); // Pasar todos los datos
            }
        }

        if (cursor != null) cursor.close();
        return null; // Si no se encuentra el registro
    }

    // Método para actualizar un registro
    public boolean updateRecord(int id, String clientName, String productList, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CLIENT_NAME, clientName);
        contentValues.put(COL_PRODUCT_LIST, productList);
        contentValues.put(COL_LATITUDE, latitude); // Actualizar latitud
        contentValues.put(COL_LONGITUDE, longitude); // Actualizar longitud
        contentValues.put(COL_ADDRESS, address); // Actualizar dirección

        int result = db.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0; // Si se actualizó correctamente, devuelve verdadero
    }

    // Método para eliminar un registro
    public boolean deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0; // Si se eliminó correctamente, devuelve verdadero
    }

    // Método para marcar un pedido como completado
    public boolean markAsCompleted(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IS_COMPLETED, 1); // 1 indica completado

        int result = db.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0; // Si se actualizó correctamente, devuelve verdadero
    }
}
