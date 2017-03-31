package com.example.android.biyesed1;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by sdekock on 2017-03-30.
 */

public class ContactModel {
    public String id;
    public String name;
    public String mobileNumber;
    public Bitmap photo;
    public Uri photoURI;

    public ContactModel(String id, String name, String mobileNumber, Bitmap photo, Uri photoURI){
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.photo = photo;
        this.photoURI = photoURI;
    }
    public ContactModel(){

    }
}
