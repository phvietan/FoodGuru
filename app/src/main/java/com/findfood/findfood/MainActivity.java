package com.findfood.findfood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String PREFS_NAME = "MyPrefsFile";
    public Location loc;

    private boolean getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 10 );
                return true;
            }
        }
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                loc = location;
                locationManager.removeUpdates(this);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        return true;
    }

    void setAdapter() {
        MyDBHandler db = new MyDBHandler(this);
        final List<Location_Item> arrayOfUsers = db.getAllLocations();
        LocationAdapter adapter = new LocationAdapter(this, arrayOfUsers);
        adapter.setPlacelat(loc.getLatitude());
        adapter.setPlacelong(loc.getLongitude());
        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int ID = arrayOfUsers.get(position).getId();
                Intent data = new Intent(MainActivity.this, DetailActivity.class);
                data.putExtra("IDLocationItem",ID);
                startActivity(data);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.actionbarchef);

        boolean s = getLocation();
        if (s==false) return;
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent data = new Intent(this, addItemActivity.class);
            startActivity(data);
            return true;
        }

        if (id == R.id.action_suggest) {
            Intent data = new Intent(this, SuggestActivity.class);
            startActivity(data);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
