package com.example.aluasistencias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtén las coordenadas de la ubicación (si las tienes) y muestra el mapa
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // Muestra el mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Centra el mapa en las coordenadas si existen, de lo contrario, en un valor predeterminado
        LatLng location = new LatLng(latitude != 0 ? latitude : 19.4326, longitude != 0 ? longitude : -99.1332); // Ciudad de México por defecto
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Agregar marcador en la ubicación
        mMap.addMarker(new MarkerOptions().position(location).title("Ubicación seleccionada"));

        // Permitir al usuario seleccionar un lugar tocando el mapa
        mMap.setOnMapClickListener(latLng -> {
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            mMap.clear();  // Eliminar marcadores anteriores
            mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación"));

            // Opcional: Puedes actualizar el campo de la dirección con la ubicación seleccionada
            Toast.makeText(MapsActivity.this, "Ubicación seleccionada: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        // Al presionar atrás, devolver la ubicación seleccionada
        Intent intent = new Intent();
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
}
