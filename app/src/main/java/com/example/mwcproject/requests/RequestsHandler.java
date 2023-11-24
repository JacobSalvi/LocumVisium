package com.example.mwcproject.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.mwcproject.utils.PropertiesHandle;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestsHandler {

    // given a path make a request to get the bitmap for that picture
    public static Bitmap getPicture(String path, Context context) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PropertiesHandle.getProperty("scheme", context))
                .host(PropertiesHandle.getProperty("host", context))
                .port(PropertiesHandle.getPropertyInt("port", context))
                .addPathSegment("picture")
                .addQueryParameter("path", path)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .method("GET", null)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String resString = response.body().string();
            JSONObject resBody = new JSONObject(resString);
            String encodedImage = (String) resBody.get("file");
            return StringToBitMap(encodedImage);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject getLocationList(LatLng position, int maxDistance, Context context ) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PropertiesHandle.getProperty("scheme", context))
                .host(PropertiesHandle.getProperty("host", context))
                .port(PropertiesHandle.getPropertyInt("port", context))
                .addPathSegment("information")
                .addQueryParameter("latitude", String.valueOf(position.latitude))
                .addQueryParameter("longitude", String.valueOf(position.longitude))
                .addQueryParameter("max", String.valueOf(maxDistance))
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .method("GET", null)
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String resString = response.body().string();
            return new JSONObject(resString);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
