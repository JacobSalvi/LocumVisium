package com.example.mwcproject.utils;

import com.google.android.gms.maps.model.MarkerOptions;

public class LocationInfo extends MarkerOptions {
    private String path;
   public LocationInfo() {
       super();
   }

   public void addPath(String path) {
       this.path = path;
   }

   public String getPath() {
       return this.path;
   }

}
