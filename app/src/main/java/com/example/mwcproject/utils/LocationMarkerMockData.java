package com.example.mwcproject.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationMarkerMockData {

    public List<LocationMarker> markers;

    public LocationMarkerMockData() {
        LocationMarker marker1 = new LocationMarker();
        marker1.position(new LatLng(46.0062,8.9519));
        marker1.title("position 1");

        LocationMarker marker2 = new LocationMarker();
        marker2.position(new LatLng(46.0052,8.9505));
        marker2.title("position 2");

        LocationMarker marker3 = new LocationMarker();
        marker3.position(new LatLng(46.0057,8.9538));
        marker3.title("position 3");

        LocationMarker marker4 = new LocationMarker();
        marker4.position(new LatLng(45.9797,8.9304));
        marker4.title("position 4");

        LocationMarker marker5 = new LocationMarker();
        marker5.position(new LatLng(46.0233,8.9579));
        marker5.title("position 5");

        markers = new ArrayList<>(Arrays.asList(marker1, marker2, marker3, marker4, marker5));


    }

}
