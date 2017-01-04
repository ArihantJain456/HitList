package com.example.arihantjain.hitlist;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Arihant Jain on 12/27/2016.
 */

public class EnemyModel implements Serializable{
    private String ID;
    private String name;
    private String reason;
    private String house;
    private String alive;
    private String PlaceToFind;
    private byte[] image;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPlaceToFind() {
        return PlaceToFind;
    }

    public void setPlaceToFind(String placeToFind) {
        PlaceToFind = placeToFind;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
