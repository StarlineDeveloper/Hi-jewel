package com.hijewel.models;

import java.io.Serializable;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class CartModel implements Serializable {

    private int id, qty;
    private String product_id, product_title, ProductCode, Description, product_image,
            MrpPrice, OurPrice, Weight, Source, SubSource, carat, total_price, total_weight;

    public CartModel() {
    }

    public CartModel(ProductModel pm, String carat) {
        product_id = pm.getProduct_id();
        product_title = pm.getProduct_title();
        ProductCode = pm.getProductCode();
        Description = pm.getDescription();
        product_image = pm.getProduct_image();
        MrpPrice = pm.getMrpPrice();
        OurPrice = pm.getOurPrice();
        Weight = pm.getWeight();
        Source = pm.getSource();
        SubSource = pm.getSubSource();
        this.carat = carat;
        total_price = pm.getOurPrice();
        total_weight = pm.getWeight();
        qty = 1;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getMrpPrice() {
        return MrpPrice;
    }

    public void setMrpPrice(String mrpPrice) {
        MrpPrice = mrpPrice;
    }

    public String getOurPrice() {
        return OurPrice;
    }

    public void setOurPrice(String ourPrice) {
        OurPrice = ourPrice;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getSubSource() {
        return SubSource;
    }

    public void setSubSource(String subSource) {
        SubSource = subSource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
