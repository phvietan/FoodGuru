package com.findfood.findfood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowSuggestActivity extends AppCompatActivity {

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

    private int getIndexShiftLeft(String x, String [] s) {
        int i = 1;
        for (String k : s) {
            if (x.equals(k)) return i;
            i *= 2;
        }
        return -1;
    }

    private boolean ok2(String x, int upperbound, String[] s) {
        return (getIndexShiftLeft(x,s) & upperbound)>0;
    }

    private boolean ok(Location_Item x,  int cat, int pri, int dis) {
        Location a = new Location("PointDestination");
        a.setLatitude(x.getCoorlat());
        a.setLongitude(x.getCoorlong());
        boolean check;
        check = dis*1000 > loc.distanceTo(a);
        return check & ok2(x.getCategory(),cat,getResources().getStringArray(R.array.add_item_location_category)) &
                ok2(x.getPrice(),pri,getResources().getStringArray(R.array.add_item_price));
    }

    private List<Location_Item> getSomeLocation(List<Location_Item> now, int cat, int pri, int dis) {
        List<Location_Item> list = new ArrayList<Location_Item>();
        int cnt = 0;
        for (Location_Item x : now)
            if (ok(x,cat,pri,dis)) {
                list.add(x);
                cnt++;
                if (cnt==8) break;
            }
        return list;
    }

    private void setAdapter() {
        MyDBHandler db = new MyDBHandler(this);
        Intent intent = getIntent();
        final List<Location_Item> tmpArray = db.getAllLocations();
        //////////////
        final List<Location_Item> arrayOfUsers = getSomeLocation(tmpArray
                ,intent.getIntExtra("SuggestCat",1)
                ,intent.getIntExtra("SuggestPri",1)
                ,intent.getIntExtra("SuggestDis",1) );
        //////////////
        LocationAdapter adapter = new LocationAdapter(this, arrayOfUsers);
        adapter.setPlacelat(loc.getLatitude());
        adapter.setPlacelong(loc.getLongitude());
        ListView listView = (ListView) findViewById(R.id.show_suggest_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suggest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean f = getLocation();
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
