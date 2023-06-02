package com.hijewel.utils;

import android.content.Context;
import android.util.Log;

import com.hijewel.models.BannerModel;
import com.hijewel.models.CatsModel;
import com.hijewel.models.ProductModel;
import com.hijewel.models.SymbolModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.BANNERS;
import static com.hijewel.utils.Constatnts.CATEGORIES;
import static com.hijewel.utils.Constatnts.KEY_ASK;
import static com.hijewel.utils.Constatnts.KEY_BID;
import static com.hijewel.utils.Constatnts.KEY_GSYMBPLE;
import static com.hijewel.utils.Constatnts.KEY_HIGH;
import static com.hijewel.utils.Constatnts.KEY_ISDISPLAY;
import static com.hijewel.utils.Constatnts.KEY_LOW;
import static com.hijewel.utils.Constatnts.KEY_PRODUCTTYPE;
import static com.hijewel.utils.Constatnts.KEY_SOURCE;
import static com.hijewel.utils.Constatnts.KEY_STATUS;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.KEY_STOCK;
import static com.hijewel.utils.Constatnts.KEY_SYMBOLID;
import static com.hijewel.utils.Functions.checkIT;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class GetData {

    public static String[] IMAGESSSS = {"productimages/96/6B2A0094_1.jpg", "productimages/97/6B2A9729 _1.jpg", "productimages/98/6B2A9768_1.jpg"};
    private static String[] ids = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static String[] size = {"1.01", "10.254", "2112", "1.5", "1.01", "10.254", "2112", "1.5", "1.01", "10.254"};
    private static String[] price = {"0", " 5000", "0", " 50000", "0", " 5000", "0", " 50000", "0", " 5000"};
    private static String[] sku = {"100", "d01", " abc", "111", "100", "d01", " abc", "111", "100", "d01"};
    private static String[] title = {"Ring male", "dimond ring", "ringaa female", "dimond ring female",
            "Ring male", "dimond ring", "ringaa female", "dimond ring female", "Ring male", "dimond ring"};
    private static String[] types = {"Gold", "Diamond", "Gold", "Diamond", "Gold", "Diamond", "Gold", "Diamond", "Gold", "Diamond"};

    private PreferenceUtils utils;

    public GetData(Context context) {
        utils = new PreferenceUtils(context);
    }

    public ArrayList<CatsModel> getCats1(String type) {
        ArrayList<CatsModel> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(utils.getPrefrence(CATEGORIES, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.optJSONObject(i);
                if (j.optString("MenuID").equals(type)) {
                    CatsModel sm = new CatsModel();
                    sm.setCategoryID(j.optString("CategoryID"));
                    sm.setMenuID(j.optString("MenuID"));
                    sm.setDisplayOrder(j.optString("DisplayOrder"));
                    sm.setCategoryDisplayName(j.optString("CategoryDisplayName"));
                    sm.setCategoryImage(j.optString("CategoryImage"));
                    sm.setCategoryImagePath(j.optString("CategoryImagePath"));
                    sm.setCategorythumbImagePath(j.optString("CategorythumbImagePath"));
                    sm.setStatus(j.optString("Status"));
                    sm.setIsDisplayExplore(j.optString("IsDisplayExplore"));
                    sm.setURL(j.optString("URL"));
                    sm.setCreatedDate(j.optString("CreatedDate"));
                    data.add(sm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public ArrayList<CatsModel> getCats(String type) {
        ArrayList<CatsModel> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(utils.getPrefrence(CATEGORIES, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.optJSONObject(i);
                if (j.optString("MenuID").equals(type)) {
                    CatsModel sm = new CatsModel();
                    sm.setCategoryID(j.optString("CategoryID"));
                    sm.setMenuID(j.optString("MenuID"));
                    sm.setDisplayOrder(j.optString("DisplayOrder"));
                    sm.setCategoryDisplayName(j.optString("CategoryDisplayName"));
                    sm.setCategoryImage(j.optString("CategoryImage"));
                    sm.setCategoryImagePath(j.optString("CategoryImagePath"));
                    sm.setCategorythumbImagePath(j.optString("CategorythumbImagePath"));
                    sm.setStatus(j.optString("Status"));
                    sm.setIsDisplayExplore(j.optString("IsDisplayExplore"));
                    sm.setURL(j.optString("URL"));
                    sm.setCreatedDate(j.optString("CreatedDate"));
                    data.add(sm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<CatsModel> getSubCats(String data) {
        ArrayList<CatsModel> kek = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("Table");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.optJSONObject(i);
                CatsModel sm = new CatsModel();
                sm.setCategoryID(j.optString("CategoryID"));
                sm.setMenuID(j.optString("MenuID"));
                sm.setDisplayOrder(j.optString("DisplayOrder"));
                sm.setCategoryDisplayName(j.optString("CategoryDisplayName"));
                sm.setCategoryImage(j.optString("CategoryImage"));
                sm.setCategoryImagePath(j.optString("CategoryImagePath"));
                sm.setCategorythumbImagePath(j.optString("CategorythumbImagePath"));
                sm.setStatus(j.optString("Status"));
                sm.setIsDisplayExplore(j.optString("IsDisplayExplore"));
                sm.setURL(j.optString("URL"));
                sm.setCreatedDate(j.optString("CreatedDate"));
                sm.setProductCount(j.optString("ProductCount"));
                kek.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kek;
    }

    public ArrayList<ProductModel> getHomeProducts(String Source) {


        String tte=utils.getPrefrence(Source, "");
        Log.e("tee",tte);
        ArrayList<ProductModel> data = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(utils.getPrefrence(Source, ""));
            JSONArray jsonArray = jsonObject.optJSONArray("Table");
            for (int i = 0; i < jsonArray.length(); i++) {
                ProductModel pm = new ProductModel();
                JSONObject j = jsonArray.optJSONObject(i);
                pm.setProduct_id(j.optString("ProductID"));
                pm.setProduct_title(j.optString("ProductName"));
                pm.setProductCode(j.optString("ProductCode"));
                pm.setDescription(j.optString("Description"));
                pm.setShortDescription(j.optString("ShortDescription"));
                String image = j.optString("ProductImage1") + ",";
                if (!j.optString("ProductImage2").isEmpty())
                    image = j.optString("ProductImage2") + ",";
                if (!j.optString("ProductImage3").isEmpty())
                    image = j.optString("ProductImage3") + ",";
                if (!j.optString("ProductImage4").isEmpty())
                    image = j.optString("ProductImage4") + ",";
                if (!j.optString("ProductImage5").isEmpty())
                    image = j.optString("ProductImage5") + ",";
                pm.setProduct_image(image.substring(0, image.length() - 1));
                pm.setMrpPrice(j.optString("MrpPrice"));
                pm.setOurPrice(j.optString("OurPrice"));
                pm.setWeight(j.optString("Weight"));
                pm.setSpecification(j.optString("Specification"));
                pm.setSource(j.optString("Source"));
                pm.setSubSource(j.optString("SubSource"));
                data.add(pm);
            }
        } catch (Exception e) {
            Log.e("ERRROR", e.toString());
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<ProductModel> getProducts(String Source, String table) {
        ArrayList<ProductModel> data = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(Source);
            JSONArray jsonArray = jsonObject.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                ProductModel pm = new ProductModel();
                JSONObject j = jsonArray.optJSONObject(i);
                pm.setProduct_id(j.optString("ProductID"));
                pm.setProduct_title(j.optString("ProductName"));
                pm.setProductCode(j.optString("ProductCode"));
                pm.setDescription(j.optString("Description"));
                pm.setShortDescription(j.optString("ShortDescription"));
                String image = j.optString("ProductImage1") + ",";
                if (!j.optString("ProductImage2").isEmpty())
                    image = j.optString("ProductImage2") + ",";
                if (!j.optString("ProductImage3").isEmpty())
                    image = j.optString("ProductImage3") + ",";
                if (!j.optString("ProductImage4").isEmpty())
                    image = j.optString("ProductImage4") + ",";
                if (!j.optString("ProductImage5").isEmpty())
                    image = j.optString("ProductImage5") + ",";
                pm.setProduct_image(image.substring(0, image.length() - 1));
                pm.setMrpPrice(j.optString("MrpPrice"));
                pm.setOurPrice(j.optString("OurPrice"));
                pm.setWeight(j.optString("Weight"));
                pm.setSpecification(j.optString("Specification"));
                pm.setSource(j.optString("Source"));
                pm.setSubSource(j.optString("SubSource"));
                data.add(pm);
            }
        } catch (Exception e) {
            Log.e("hhhhahaha", e.toString());
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<BannerModel> getBanners() {
        ArrayList<BannerModel> kek = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(utils.getPrefrence(BANNERS, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.optJSONObject(i);
                BannerModel bm = new BannerModel();
                bm.setBanner_id(j.optString("SliderID"));
                bm.setBanner_thumb_name(j.optString("SliderImageThubPath"));
                bm.setBammer_image(j.optString("SliderImagePath"));
                kek.add(bm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return kek;
    }

    public SymbolModel getLiveRates(PreferenceUtils utils, String msg) {
        SymbolModel sm = null;
        try {
            JSONObject jsonObject = new JSONObject(msg);
            JSONObject main = jsonObject.optJSONObject("NewDataSet");
            utils.setPrefrences(KEY_STATUS_DISPLAY, main.optJSONObject("DisplayRate").optString(KEY_STATUS_DISPLAY));
            JSONArray jsonArray = main.optJSONArray("GeneralPremium");
            JSONObject j = jsonArray.optJSONObject(0);
            if (j.optString(KEY_PRODUCTTYPE).equals("0")) {
                sm = new SymbolModel();
                sm.setSymbolId(Integer.valueOf(j.optString(KEY_SYMBOLID)));
                sm.setSymbol(j.optString(KEY_GSYMBPLE));
                sm.setBid(j.optString(KEY_BID));
                sm.setAsk(j.optString(KEY_ASK));
                sm.setHigh(j.optString(KEY_HIGH));
                sm.setLow(j.optString(KEY_LOW));
                sm.setSource(j.optString(KEY_SOURCE));
                sm.setStock(j.optString(KEY_STOCK));
                sm.setProductType(j.optString(KEY_PRODUCTTYPE));
                sm.setStatus(j.optString(KEY_STATUS));
                sm.setBidStatus(checkIT(utils.getPrefrence("last_bid" + sm.getSymbolId(), ""), sm.getBid()));
                sm.setAskStatus(checkIT(utils.getPrefrence("last_ask" + sm.getSymbolId(), ""), sm.getAsk()));
                sm.setIsDisplay("1");
                sm.setW_display(j.optString(KEY_ISDISPLAY));
                utils.setPrefrences("last_bid" + sm.getSymbolId(), sm.getBid());
                utils.setPrefrences("last_ask" + sm.getSymbolId(), sm.getAsk());
            }
        } catch (Exception e) {
            Log.e("RIRIRI", e.toString());
            e.printStackTrace();
        }
        return sm;
    }
}
