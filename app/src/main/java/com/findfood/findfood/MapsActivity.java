package com.findfood.findfood;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng now;
    Marker marker;
    Location loc;
    boolean show;
    String markerstring;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        loc = new Location("a");
        loc.setLatitude(intent.getDoubleExtra("Latitude",0.0));
        loc.setLongitude(intent.getDoubleExtra("Longitude",0.0));
        markerstring = intent.getStringExtra("markerstring");
        show = intent.getBooleanExtra("VISIBILITY",true);

        if (show==false) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.showornot);
            linearLayout.setVisibility(View.GONE);
        }
    }

    public void OKButtonClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("Latitude", now.latitude);
        intent.putExtra("Longitude", now.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        now = new LatLng(loc.getLatitude(),loc.getLongitude());
        if (marker!=null)
            marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(now).title(markerstring));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(now,(float)15.0));
    }

    public void ChooseButtonClicked(View view) {
        LatLng latLng = mMap.getCameraPosition().target;
        if (marker != null)
            marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Quán ở đây"));
        now = latLng;
    }

    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<android.location.Address> addressList = null;

        if (!location.matches("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }

        if (addressList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_LONG).show();
            return;
        }
        android.location.Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        if (marker != null)
            marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
        now = latLng;
    }
}
