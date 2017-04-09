package com.findfood.findfood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    int ID = -1;
    Location_Item LI;
    Location loc;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        MyDBHandler db = new MyDBHandler(this);
        ID = data.getIntExtra("IDLocationItem", -1);
        LI = db.getLocation(ID);

        boolean s = getLocation();

        setTextForActivity();
    }

    private void setTextForActivity() {
        TextView catname = (TextView) findViewById(R.id.detail_category);
        TextView priname = (TextView) findViewById(R.id.detail_price);
        TextView locname = (TextView) findViewById(R.id.detail_location);
        TextView comname = (TextView) findViewById(R.id.detail_comment);
        TextView distname = (TextView) findViewById(R.id.detail_distance);

        Location coorA = new Location("Point A");
        coorA.setLatitude(LI.getCoorlat());
        coorA.setLongitude(LI.getCoorlong());

        catname.setText(LI.getCategory());
        priname.setText(LI.getPrice());
        locname.setText(LI.getLocation());
        comname.setText(LI.getComment());

        float f = loc.distanceTo(coorA) / 1000.0f;
        DecimalFormat df = new DecimalFormat("#.00");
        distname.setText(Float.toString(Float.parseFloat(df.format(f))) + "km");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.detail_delete) {
            MyDBHandler db = new MyDBHandler(this);
            db.deleteShop(db.getLocation(ID));
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.detail_edit) {
            Intent data = new Intent(this, EditActivity.class);
            data.putExtra("IDLocationItemEdit",ID);
            startActivityForResult(data, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toMapButtonClicked(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Latitude",LI.getCoorlat());
        intent.putExtra("Longitude",LI.getCoorlong());
        intent.putExtra("VISIBILITY",false);
        intent.putExtra("markerstring","Quán ở đây");
        startActivity(intent);
        ///////
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                MyDBHandler db = new MyDBHandler(this);
                boolean s = getLocation();
                LI = db.getLocation(ID);
                setTextForActivity();
            }
        }
    }
}