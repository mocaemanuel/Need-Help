package com.brainiac.mocae.needhelp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
        // Include the OnCreate() method here too, as described above.
        private Marker centerMarker;
        private GPSTracker gpsTracker;
        private LatLng location;
        @Override
        public void onMapReady(final GoogleMap googleMap) {
                location = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
                centerMarker = googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    location = googleMap.getCameraPosition().target;
                    if (centerMarker == null) {
                        centerMarker = googleMap.addMarker(new MarkerOptions().position(location));
                    }
                    else
                        centerMarker.setPosition(location);
                }
            });
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            gpsTracker = new GPSTracker(getApplicationContext(),this);
            gpsTracker.getLocation();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        public void onOkClick (View view) {
            Intent intent = new Intent("com.brainiac.mocae.RESULT_ACTION", Uri.parse("content://result_uri"));
            intent.putExtra("LOCATION", location);
            setResult(Activity.RESULT_OK,intent);
            finish();


        }
    }