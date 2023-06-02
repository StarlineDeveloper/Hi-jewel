package com.hijewel.models;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class BannerModel {

    private String banner_id, banner_thumb_name, bammer_image;

    public BannerModel() {
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_thumb_name() {
        return banner_thumb_name;
    }

    public void setBanner_thumb_name(String banner_thumb_name) {
        this.banner_thumb_name = banner_thumb_name;
    }

    public String getBammer_image() {
        return bammer_image;
    }

    public void setBammer_image(String bammer_image) {
        this.bammer_image = bammer_image;
    }
}
