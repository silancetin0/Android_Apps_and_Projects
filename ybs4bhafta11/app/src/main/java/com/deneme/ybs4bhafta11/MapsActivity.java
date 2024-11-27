package com.deneme.ybs4bhafta11;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.deneme.ybs4bhafta11.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    
    LocationListener locationListener;
    LocationManager locationManager;
    ActivityResultLauncher<String> permissionlauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        registerLauncher();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("ServiceCast")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
        locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                System.out.println("Mevcut Lokasyonunuz ="+location.toString());
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(),"Haritaları görüntülemek için izniniz gerekli",Snackbar.LENGTH_INDEFINITE).setAction("izin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionlauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }).show();
            }
            else {
                permissionlauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION); 
            }
        }
         else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

        // italya için enlem ve boylam 42.1375953,10.4244117.....41.1782431,7.4292689
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(42.1375953, 10.4244117);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in ıtaly"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }
    private void registerLauncher()
    {
        permissionlauncher =registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if(result){
                    if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    }
                }
                else {
                    Toast.makeText(MapsActivity.this, "izin gerekli", Toast.LENGTH_SHORT).show();
                }

            }
        });
    {

    }
    }
}