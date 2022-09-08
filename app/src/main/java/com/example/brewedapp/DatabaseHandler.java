package com.example.brewedapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cartManager";
    private static final String TABLE_NAME = "tbl_cart";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_tbl_cart = "Create Table IF NOT Exists " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, p_imageURL TEXT, p_title TEXT, p_origin TEXT, p_price NUMERIC, p_quantity INTEGER DEFAULT 1)";

        db.execSQL(create_tbl_cart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to upgrade my database if needed
        //executed when DATABASE_VERSION differs from the one stored on your application
    }

    //check if item already exists, before adding
    public boolean hasRow(Product p) {
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE p_title = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectString, new String[] {p.getP_title()});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }

        cursor.close();
        db.close(); //Close the database
        return hasObject; //return value.
    }

    //a function to add a memo to the database
    public void addItemToCart (Product p){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("p_imageURL", p.getP_imageURL());
        values.put("p_title", p.getP_title());
        values.put("p_origin", p.getP_origin());
        values.put("p_price", p.getP_price());
        // quantity is 1 by default

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //a function to retrieve all items from our cart
    public List<ProductSqlite> getAllItemsFromCart(){
        List<ProductSqlite> items = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                ProductSqlite product = new ProductSqlite();
                product.setP_imageURL(cursor.getString(1));
                product.setP_title(cursor.getString(2));
                product.setP_origin(cursor.getString(3));
                product.setP_price(cursor.getFloat(4));
                product.setP_quantity(cursor.getInt(5));

                //add item to the list
                items.add(product);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    //a function to retrieve all items from our cart
    public float getSubTotal(){
        float total = 0.0f;
        List<ProductSqlite> items = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                total += cursor.getFloat(4) * cursor.getInt(5);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return total;
    }


    //delete an item from cart
    public void removeItemFromCart (ProductSqlite p){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "p_title = ?", new String[]{p.getP_title()});
        db.close();
    }

    // delete all items from cart
    public void removeAllItemsFromCart (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    // increment item quantity by 1
    public void updateItemQuantity (String p_title, String action){

        SQLiteDatabase db = this.getWritableDatabase();
        String q;

        if (action.equals("increase")){
            q = "UPDATE " + TABLE_NAME + " SET p_quantity = p_quantity + 1 WHERE p_title = " + "'" + p_title + "'";
        } else {
            //decrease
            q = "UPDATE " + TABLE_NAME + " SET p_quantity = p_quantity - 1 WHERE p_title = " + "'" + p_title + "'";
        }


        db.execSQL(q);

        db.close();

    }

}







