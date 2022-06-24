package com.assadh.tryfirebase_forimage;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Postdata implements Serializable {
    String username;
    String uri;
    String description;
    Timestamp date;
    String uriimageprofile="https://imgv3.fotor.com/images/side/Free-photo-editor-for-PC-mobile.jpg";


    public String getUriimageprofile() {
        return uriimageprofile;
    }

    public void setUriimageprofile(String uriimageprofile) {
        this.uriimageprofile = uriimageprofile;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


    public Postdata(String username, String uri, String description, Timestamp date) {
        this.username = username;
        this.uri = uri;
        this.description = description;
        this.date = date;

    }
    public Postdata(String username, String uri, String description, Timestamp date,String uri_image_profile) {
        this.username = username;
        this.uri = uri;
        this.description = description;
        this.date = date;
     this.uriimageprofile=uri_image_profile;
    }

}
