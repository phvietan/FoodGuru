package com.findfood.findfood;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location_Item> {

    private double placelat;
    private double placelong;

    public void setPlacelat(double placelat) {this.placelat = placelat;}
    public void setPlacelong(double placelong) {this.placelong = placelong;}

    public LocationAdapter(Context context, List<Location_Item> users) {
        super(context, 0, users);
    }

    private int getId(String a) {
        if (a.equals("Buffet")) return R.mipmap.buffet;
        else if (a.equals("Nhà hàng")) return R.mipmap.nhahang;
        else if (a.equals("Vỉa hè")) return R.mipmap.viahe;
        else if (a.equals("Quán ăn")) return R.mipmap.quanan;
        else if (a.equals("Tiệm bánh")) return R.mipmap.tiembanh;
        else if (a.equals("Quán nước")) return R.mipmap.quannuoc;
        else if (a.equals("Cafe")) return R.mipmap.coffe;
        else if (a.equals("Bar/pub")) return R.mipmap.barpub;
        return R.mipmap.quannhau;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Location_Item user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView category = (TextView) convertView.findViewById(R.id.layout_list_item_category);
        TextView location = (TextView) convertView.findViewById(R.id.layout_list_item_location);
        TextView dist = (TextView) convertView.findViewById(R.id.layout_list_item_distance);
        ImageView im = (ImageView) convertView.findViewById(R.id.layout_image);
        // Populate the data into the template view using the data object
        im.setImageResource(getId(user.getCategory()));
        category.setText(user.getCategory());
        location.setText(user.getLocation());

        Location A = new Location("point A");
        A.setLatitude(placelat);
        A.setLongitude(placelong);
        Location B = new Location("point B");
        B.setLatitude(user.getCoorlat());
        B.setLongitude(user.getCoorlong());
            //////
        float f = A.distanceTo(B) / 1000.0f;
        DecimalFormat df = new DecimalFormat("#.00");
        dist.setText(Float.toString(Float.parseFloat(df.format(f))) + "km");
        // Return the completed view to render on screen
        return convertView;
    }
}
