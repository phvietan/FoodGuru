package com.findfood.findfood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class EditActivity extends AppCompatActivity {

    int ID;
    int numberchange;
    Location_Item LI;
    Location loc;

    private void setSpinner() {
        Spinner spinner_category = (Spinner) findViewById(R.id.edit_category);
        ArrayAdapter<CharSequence> adapter_category = ArrayAdapter.createFromResource(this,
                R.array.add_item_location_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);
        spinner_category.setSelection(adapter_category.getPosition(LI.getCategory()));

        Spinner spinner_price = (Spinner) findViewById(R.id.edit_price);
        ArrayAdapter<CharSequence> adapter_price = ArrayAdapter.createFromResource(this,
                R.array.add_item_price, android.R.layout.simple_spinner_item);
        adapter_price.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_price.setAdapter(adapter_price);
        spinner_price.setSelection(adapter_price.getPosition(LI.getPrice()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView)findViewById(R.id.numberchange);
        textView.setVisibility(View.INVISIBLE);

        numberchange = 0;
        Intent data = getIntent();
        MyDBHandler db = new MyDBHandler(this);
        ID = data.getIntExtra("IDLocationItemEdit", -1);
        LI = db.getLocation(ID);
        loc = new Location("a");
        loc.setLatitude(LI.getCoorlat());
        loc.setLongitude(LI.getCoorlong());

        setSpinner();
        EditText location = (EditText)findViewById(R.id.edit_location);
        EditText comment = (EditText)findViewById(R.id.edit_comment);
        location.setText(LI.getLocation());
        comment.setText(LI.getComment());
    }

    public void AddItemToMapClicked(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Latitude",loc.getLatitude());
        intent.putExtra("Longitude",loc.getLongitude());
        intent.putExtra("VISIBILITY",true);
        intent.putExtra("markerstring","Quán ở đây");
        startActivityForResult(intent,1);

        numberchange++;
        TextView textView = (TextView)findViewById(R.id.numberchange);
        textView.setText("Đã cập nhật vị trí " + Integer.toString(numberchange) + " lần");
        textView.setVisibility(View.VISIBLE);
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

    public void submitEdit(View v) {
        Spinner category = (Spinner)findViewById(R.id.edit_category);
        Spinner price = (Spinner)findViewById(R.id.edit_price);
        EditText location = (EditText)findViewById(R.id.edit_location);
        EditText comment = (EditText)findViewById(R.id.edit_comment);

        LI.setCategory(category.getSelectedItem().toString());
        LI.setLocation(location.getText().toString());
        LI.setPrice(price.getSelectedItem().toString());
        LI.setComment(comment.getText().toString());
        LI.setCoorlat(loc.getLatitude());
        LI.setCoorlong(loc.getLongitude());

        MyDBHandler db = new MyDBHandler(this);
        db.updateShop(ID,LI);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
