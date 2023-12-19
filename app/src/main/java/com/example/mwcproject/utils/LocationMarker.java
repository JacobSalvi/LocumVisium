package com.example.mwcproject.utils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMarker extends MarkerOptions {
    private String path;
   public LocationMarker() {
       super();
       this.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
   }

   public void addPath(String path) {
       this.path = path;
   }

   public String getPath() {
       return this.path;
   }

}
