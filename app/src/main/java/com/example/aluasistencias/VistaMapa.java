package com.example.aluasistencias;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class VistaMapa extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mapa);

        // Configurar el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar botones (sin funcionalidad adicional)
        Button saveLocationButton = findViewById(R.id.btn_save_location);
        saveLocationButton.setOnClickListener(v -> Toast.makeText(this, "Funcionalidad no implementada.", Toast.LENGTH_SHORT).show());

        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Configurar gestos y tipo de mapa inicial
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Solicitar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            enableUserLocation();
        }

        // Configurar botones de tipo de mapa
        findViewById(R.id.btn_normal).setOnClickListener(v -> map.setMapType(GoogleMap.MAP_TYPE_NORMAL));
        findViewById(R.id.btn_satellite).setOnClickListener(v -> map.setMapType(GoogleMap.MAP_TYPE_SATELLITE));
        findViewById(R.id.btn_terrain).setOnClickListener(v -> map.setMapType(GoogleMap.MAP_TYPE_TERRAIN));
        findViewById(R.id.btn_hybrid).setOnClickListener(v -> map.setMapType(GoogleMap.MAP_TYPE_HYBRID));
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(location -> {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
