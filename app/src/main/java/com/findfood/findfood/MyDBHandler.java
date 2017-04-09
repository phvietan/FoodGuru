package com.findfood.findfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_LOCATIONS = "locations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "column_category";
    public static final String COLUMN_PRICE = "column_price";
    public static final String COLUMN_LOCATION = "column_location";
    public static final String COLUMN_COMMENT = "column_comment";
    public static final String COLUMN_COOR_LAT = "column_coor_lat";
    public static final String COLUMN_COOR_LONG = "column_coor_long";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_PRICE + " TEXT," + COLUMN_LOCATION + " TEXT," + COLUMN_COMMENT + " TEXT,"
                + COLUMN_COOR_LAT + " REAL," + COLUMN_COOR_LONG + " REAL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void addShop(Location_Item newItem) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, newItem.getCategory());
        values.put(COLUMN_PRICE, newItem.getPrice());
        values.put(COLUMN_LOCATION, newItem.getLocation());
        values.put(COLUMN_COMMENT, newItem.getComment());
        values.put(COLUMN_COOR_LAT, newItem.getCoorlat());
        values.put(COLUMN_COOR_LONG, newItem.getCoorlong());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    public void updateShop(int rowId, Location_Item newItem) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, newItem.getCategory());
        values.put(COLUMN_PRICE, newItem.getPrice());
        values.put(COLUMN_LOCATION, newItem.getLocation());
        values.put(COLUMN_COMMENT, newItem.getComment());
        values.put(COLUMN_COOR_LAT, newItem.getCoorlat());
        values.put(COLUMN_COOR_LONG, newItem.getCoorlong());

// Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rowId) };

        int count = db.update(
                TABLE_LOCATIONS,
                values,
                selection,
                selectionArgs);
    }

    public Location_Item getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { COLUMN_ID, COLUMN_CATEGORY,
                COLUMN_PRICE, COLUMN_LOCATION, COLUMN_COMMENT, COLUMN_COOR_LAT,
                COLUMN_COOR_LONG}, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Location_Item contact = new Location_Item(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getDouble(5), cursor.getDouble(6));

        return contact;
    }

    public List<Location_Item> getAllLocations() {
        List<Location_Item> shopList = new ArrayList<Location_Item>();

        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Location_Item shop = new Location_Item();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setCategory(cursor.getString(1));
                shop.setPrice(cursor.getString(2));
                shop.setLocation(cursor.getString(3));
                shop.setComment(cursor.getString(4));
                shop.setCoorlat(cursor.getDouble(5));
                shop.setCoorlong(cursor.getDouble(6));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public int getLocationsCount() {
        String countQuery = "SELECT * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Deleting a shop
    public void deleteShop(Location_Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }

}
