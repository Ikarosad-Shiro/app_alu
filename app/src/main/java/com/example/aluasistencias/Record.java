package com.example.aluasistencias;

public class Record {
    private int id; // Identificador único del registro
    private String clientName; // Nombre del cliente
    private String productList; // Lista de productos
    private String deliveryTime; // Hora de entrega
    private int isCompleted; // Estado de completado (1 para completado, 0 para no completado)
    private String address; // Dirección en texto
    private double latitude; // Latitud
    private double longitude; // Longitud

    // Constructor principal, de acuerdo con los id de los provedores y rutas
    public Record(int id, String clientName, String productList, int isCompleted, String address, double latitude, double longitude) {
        this.id = id;
        this.clientName = clientName;
        this.productList = productList;
        this.deliveryTime = "";  // Inicialmente vacío
        this.isCompleted = isCompleted; // Estado de completado
        this.address = address; // Dirección en texto
        this.latitude = latitude; // Latitud
        this.longitude = longitude; // Longitud
    }

    // Constructor alternativo (sin dirección, sólo coordenadas)
    public Record(int id, String clientName, String productList, int isCompleted, double latitude, double longitude) {
        this.id = id;
        this.clientName = clientName;
        this.productList = productList;
        this.deliveryTime = "";  // Inicialmente vacío
        this.isCompleted = isCompleted; // Estado de completado
        this.address = ""; // Dirección inicialmente vacía
        this.latitude = latitude; // Latitud
        this.longitude = longitude; // Longitud
    }


    //Correción de metodos, para el reinicio
    // Getter y Setter para id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter y Setter para el nombre del cliente
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    // Getter y Setter para la lista de productos
    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

    // Getter y Setter para la hora de entrega
    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    // Métodos para manejar el estado de completado
    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    // Método para verificar si el registro está completado
    public boolean isCompleted() {
        return isCompleted == 1;
    }

    // Getter y Setter para la dirección
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter y Setter para la latitud
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter y Setter para la longitud
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
