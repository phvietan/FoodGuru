package com.findfood.findfood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class addItemActivity extends AppCompatActivity {

    public Location loc;
    boolean haveget = false;

    private void setSpinner() {
        Spinner spinner_category = (Spinner) findViewById(R.id.add_item_category);
        ArrayAdapter<CharSequence> adapter_category = ArrayAdapter.createFromResource(this,
                R.array.add_item_location_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);

        Spinner spinner_price = (Spinner) findViewById(R.id.add_item_price);
        ArrayAdapter<CharSequence> adapter_price = ArrayAdapter.createFromResource(this,
                R.array.add_item_price, android.R.layout.simple_spinner_item);
        adapter_price.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_price.setAdapter(adapter_price);
    }

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
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpinner();
        boolean s = getLocation();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    public void AddItemToMapClicked(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Latitude",loc.getLatitude());
        intent.putExtra("Longitude",loc.getLongitude());
        intent.putExtra("VISIBILITY",true);
        intent.putExtra("markerstring","Bạn đang ở đây");
        startActivityForResult(intent,1);
        ///////
        TextView textView = (TextView)findViewById(R.id.haveget);
        textView.setText("Bạn đã nhập địa chỉ thành công");
        textView.setTextColor(getResources().getColor(R.color.green));
        haveget = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                double lati = data.getDoubleExtra("Latitude",0.0);
                double logi = data.getDoubleExtra("Longitude",0.0);
                loc.setLatitude(lati); loc.setLongitude(logi);
            }
        }
    }

    private int boolToInt(boolean x) {
        return x ? 1 : 0;
    }

    public void AddItemButtonClicked(View view) {
        if (!haveget) {
            TextView textView = (TextView)findViewById(R.id.haveget);
            textView.setText("Bạn phải chọn địa chỉ");
            return;
        }
        MyDBHandler db = new MyDBHandler(this);
        EditText location_name = (EditText)findViewById(R.id.add_item_location);
        Spinner category_name = (Spinner)findViewById(R.id.add_item_category);
        Spinner price_name = (Spinner)findViewById(R.id.add_item_price);
        EditText comment_name = (EditText)findViewById(R.id.add_item_comment);
        db.addShop(new Location_Item(category_name.getSelectedItem().toString()
                ,price_name.getSelectedItem().toString()
                ,location_name.getText().toString()
                ,comment_name.getText().toString()
                ,loc.getLatitude()
                ,loc.getLongitude()) );
        NavUtils.navigateUpFromSameTask(this);
    }
}
