package com.hijewel.models;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class MenuCatsModel {

    private int icon;
    private String id, title, count;

    public MenuCatsModel(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public MenuCatsModel(String id, String title, String count) {
        this.id = id;
        this.title = title;
        this.count = count;
    }

    public MenuCatsModel() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
