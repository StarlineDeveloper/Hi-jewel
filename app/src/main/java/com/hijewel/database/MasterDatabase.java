package com.hijewel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hijewel.models.CartFooterModel;
import com.hijewel.models.CartModel;

import java.util.ArrayList;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class MasterDatabase {

    public static SQLiteDatabase mDatabase;

    private static String CART_TABLE = "cart_table";

    public static void createCartTable() {
        String CREATE_CART_TABLE = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "product_id TEXT,"
                + "product_title TEXT,"
                + "ProductCode TEXT,"
                + "Description TEXT,"
                + "product_image TEXT,"
                + "MrpPrice TEXT,"
                + "OurPrice TEXT,"
                + "Weight TEXT,"
                + "Source TEXT,"
                + "SubSource TEXT,"
                + "qty INTEGER,"
                + "carat TEXT,"
                + "total_price TEXT,"
                + "total_weight TEXT);";
        mDatabase.execSQL(CREATE_CART_TABLE);
    }

    public static ArrayList<CartModel> getAllProducts() {
        ArrayList<CartModel> kek = new ArrayList<>();
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + CART_TABLE + " order by id desc", null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            CartModel symbolData = new CartModel();
            symbolData.setId(c.getInt(0));
            symbolData.setProduct_id(c.getString(1));
            symbolData.setProduct_title(c.getString(2));
            symbolData.setProductCode(c.getString(3));
            symbolData.setDescription(c.getString(4));
            symbolData.setProduct_image(c.getString(5));
            symbolData.setMrpPrice(c.getString(6));
            symbolData.setOurPrice(c.getString(7));
            symbolData.setWeight(c.getString(8));
            symbolData.setSource(c.getString(9));
            symbolData.setSubSource(c.getString(10));
            symbolData.setQty(c.getInt(11));
            symbolData.setCarat(c.getString(12));
            symbolData.setTotal_price(c.getString(13));
            symbolData.setTotal_weight(c.getString(14));
            kek.add(symbolData);
            c.moveToNext();
        }
        c.close();
        return kek;
    }

    public static CartFooterModel getFooterData() {
        CartFooterModel cfm = new CartFooterModel();
        float jewl_total_weight = 0, utsav_total_weight = 0, plat_total_weight = 0;
        int jewl_total_qty = 0, utsav_total_qty = 0, plat_total_qty = 0;
        float jewl_total_price = 0, utsav_total_price = 0, plat_total_price = 0;
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + CART_TABLE, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("Source")).toLowerCase().equals("jewellery")) {
                jewl_total_weight = jewl_total_weight + Float.parseFloat(c.getString(c.getColumnIndex("total_weight")));
                jewl_total_qty = jewl_total_qty + c.getInt(c.getColumnIndex("qty"));
                jewl_total_price = jewl_total_price + Float.parseFloat(c.getString(c.getColumnIndex("total_price")));
            } else if (c.getString(c.getColumnIndex("Source")).toLowerCase().equals("utsav")) {
                utsav_total_weight = utsav_total_weight + Float.parseFloat(c.getString(c.getColumnIndex("total_weight")));
                utsav_total_qty = utsav_total_qty + c.getInt(c.getColumnIndex("qty"));
                utsav_total_price = utsav_total_price + Float.parseFloat(c.getString(c.getColumnIndex("total_price")));
            } else if (c.getString(c.getColumnIndex("Source")).toLowerCase().equals("platinum")) {
                plat_total_weight = plat_total_weight + Float.parseFloat(c.getString(c.getColumnIndex("total_weight")));
                plat_total_qty = plat_total_qty + c.getInt(c.getColumnIndex("qty"));
                plat_total_price = plat_total_price + Float.parseFloat(c.getString(c.getColumnIndex("total_price")));
            }
            c.moveToNext();
        }
        c.close();
        cfm.setJewl_total_weight(String.valueOf(jewl_total_weight));
        cfm.setJewl_total_qty(String.valueOf(jewl_total_qty));
        cfm.setJewl_total_price(String.valueOf(jewl_total_price));
        cfm.setUtsav_total_weight(String.valueOf(utsav_total_weight));
        cfm.setUtsav_total_qty(String.valueOf(utsav_total_qty));
        cfm.setUtsav_total_price(String.valueOf(jewl_total_price));
        cfm.setPlat_total_weight(String.valueOf(plat_total_weight));
        cfm.setPlat_total_qty(String.valueOf(plat_total_qty));
        cfm.setPlat_total_price(String.valueOf(plat_total_price));
        return cfm;
    }

    public static void insertCartData(CartModel sm) {
        ContentValues cv = new ContentValues();
        cv.put("product_id", sm.getProduct_id());
        cv.put("product_title", sm.getProduct_title());
        cv.put("ProductCode", sm.getProductCode());
        cv.put("Description", sm.getDescription());
        cv.put("product_image", sm.getProduct_image());
        cv.put("MrpPrice", sm.getMrpPrice());
        cv.put("OurPrice", sm.getOurPrice());
        cv.put("Weight", sm.getWeight());
        cv.put("Source", sm.getSource());
        cv.put("SubSource", sm.getSubSource());
        cv.put("qty", sm.getQty());
        cv.put("carat", sm.getCarat());
        cv.put("total_price", sm.getTotal_price());
        cv.put("total_weight", sm.getTotal_weight());
        mDatabase.insert(CART_TABLE, null, cv);
    }

    public static void updateCartData(CartModel sm) {
        ContentValues cv = new ContentValues();
        cv.put("product_id", sm.getProduct_id());
        cv.put("product_title", sm.getProduct_title());
        cv.put("ProductCode", sm.getProductCode());
        cv.put("Description", sm.getDescription());
        cv.put("product_image", sm.getProduct_image());
        cv.put("MrpPrice", sm.getMrpPrice());
        cv.put("OurPrice", sm.getOurPrice());
        cv.put("Weight", sm.getWeight());
        cv.put("Source", sm.getSource());
        cv.put("SubSource", sm.getSubSource());
        cv.put("qty", sm.getQty());
        cv.put("carat", sm.getCarat());
        cv.put("total_price", sm.getTotal_price());
        cv.put("total_weight", sm.getTotal_weight());
        mDatabase.update(CART_TABLE, cv, "product_id= " + sm.getProduct_id(), null);
    }

    public static void deleteItem(String product_id) {
        mDatabase.delete(CART_TABLE, "product_id=" + product_id, null);
    }

    public static boolean checkInCart(String product_id) {
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + CART_TABLE + " WHERE product_id =" + product_id, null);
        int count = c.getCount();
        c.close();
        return count > 0;
    }

    public static int getCartCount() {
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + CART_TABLE, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public static void clearCart() {
        mDatabase.delete(CART_TABLE, null, null);
    }
}
