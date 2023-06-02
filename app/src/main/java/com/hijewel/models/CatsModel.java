package com.hijewel.models;

import java.io.Serializable;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class CatsModel implements Serializable{

    private String CategoryID, MenuID, DisplayOrder, CategoryDisplayName, CategoryImage, CategoryImagePath,
            CategorythumbImagePath, Status, IsDisplayExplore, URL, CreatedDate,ProductCount;

    public CatsModel() {
    }

    public CatsModel(String CategoryDisplayName) {
        this.CategoryDisplayName = CategoryDisplayName;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getDisplayOrder() {
        return DisplayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        DisplayOrder = displayOrder;
    }

    public String getCategoryDisplayName() {
        return CategoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        CategoryDisplayName = categoryDisplayName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryImagePath() {
        return CategoryImagePath;
    }

    public void setCategoryImagePath(String categoryImagePath) {
        CategoryImagePath = categoryImagePath;
    }

    public String getCategorythumbImagePath() {
        return CategorythumbImagePath;
    }

    public void setCategorythumbImagePath(String categorythumbImagePath) {
        CategorythumbImagePath = categorythumbImagePath;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIsDisplayExplore() {
        return IsDisplayExplore;
    }

    public void setIsDisplayExplore(String isDisplayExplore) {
        IsDisplayExplore = isDisplayExplore;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getProductCount() {
        return ProductCount;
    }

    public void setProductCount(String productCount) {
        ProductCount = productCount;
    }
}
