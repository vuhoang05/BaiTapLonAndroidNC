package com.example.baitaplon;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class TestMap extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap ggmap;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        this.ggmap = googleMap;
//
//        LatLng mapVN = new LatLng( 21.028511,105.804817);
//        this.ggmap.addMarker(new MarkerOptions().position(mapVN).title("Marker in Vietnam "));
//        this.ggmap.moveCamera(CameraUpdateFactory.newLatLng(mapVN));
        this.ggmap = googleMap;

        // Lấy tọa độ từ Intent
        double userLatitude = getIntent().getDoubleExtra("userLatitude", 0);
        double userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        double trọLatitude = getIntent().getDoubleExtra("trọLatitude", 0);
        double trọLongitude = getIntent().getDoubleExtra("trọLongitude", 0);

        // Hiển thị vị trí người dùng
        LatLng userLocation = new LatLng(userLatitude, userLongitude);
        ggmap.addMarker(new MarkerOptions().position(userLocation).title("Vị trí của bạn"));

        // Hiển thị vị trí của trọ
        LatLng trọLocation = new LatLng(trọLatitude, trọLongitude);
        ggmap.addMarker(new MarkerOptions().position(trọLocation).title("Vị trí của trọ"));

        // Zoom vào giữa hai vị trí
        LatLng midPoint = new LatLng((userLatitude + trọLatitude) / 2, (userLongitude + trọLongitude) / 2);
        ggmap.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint, 12));
    }
}