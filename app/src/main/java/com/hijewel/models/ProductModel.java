package com.hijewel.models;

import java.io.Serializable;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class ProductModel implements Serializable {

    private String product_id, product_title, ProductCode, Description, ShortDescription, product_image,
            MrpPrice, OurPrice,Weight, Specification, Source, SubSource;

    public ProductModel() {
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

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
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

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
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
}
