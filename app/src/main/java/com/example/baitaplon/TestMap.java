package com.example.baitaplon;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
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
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestMap.this, MainActivity.class));
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.ggmap = googleMap;

        // Lấy tọa độ từ Intent
        double userLatitude = getIntent().getDoubleExtra("userLatitude", 0);
        double userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        double houseLatitude = getIntent().getDoubleExtra("houseLatitude", 0);
        double houseLongitude = getIntent().getDoubleExtra("houseLongitude", 0);



        // Hiển thị vị trí người dùng
        LatLng userLocation = new LatLng(userLatitude, userLongitude);
        ggmap.addMarker(new MarkerOptions().position(userLocation).title("Vị trí của bạn"));

        // Hiển thị vị trí của trọ
        LatLng houseLocation = new LatLng(houseLatitude, houseLongitude);
        ggmap.addMarker(new MarkerOptions().position(houseLocation).title("Vị trí của trọ"));

        // Zoom vào giữa hai vị trí
        LatLng midPoint = new LatLng((userLatitude + houseLatitude) / 2, (userLongitude + houseLongitude) / 2);
        ggmap.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint, 12));
    }
}