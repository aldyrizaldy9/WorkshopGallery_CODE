package com.example.aldy.workshopgallery.Model;

/**
 * Created by DBSS014 on 4/4/2018.
 */

public class GalleryModel {
    String nama;
    String caption;
    String photo;

    public GalleryModel(String nama, String caption, String photo) {
        this.nama = nama;
        this.caption = caption;
        this.photo = photo;
    }

    public String getNama() {
        return nama;
    }

    public String getCaption() {
        return caption;
    }

    public String getPhoto() {
        return photo;
    }
}
