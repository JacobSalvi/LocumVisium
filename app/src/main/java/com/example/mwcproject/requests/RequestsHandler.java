package com.example.mwcproject.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.mwcproject.utils.PropertiesHandler;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestsHandler {

    // given a path make a request to get the bitmap for that picture
    public static Bitmap getPicture(String path, Context context) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PropertiesHandler.getProperty("scheme", context))
                .host(PropertiesHandler.getProperty("host", context))
                .port(PropertiesHandler.getPropertyInt("port", context))
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

    public static JSONObject getLocationList(LatLng position, int maxDistance, Context context) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PropertiesHandler.getProperty("scheme", context))
                .host(PropertiesHandler.getProperty("host", context))
                .port(PropertiesHandler.getPropertyInt("port", context))
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

    static public Bitmap getImage(Context context, String lat, String longitude) throws IOException, JSONException {
        HttpUrl mySearchUrl = new HttpUrl.Builder()
                .scheme(PropertiesHandler.getProperty("scheme", context))
                .host(PropertiesHandler.getProperty("host", context))
                .port(PropertiesHandler.getPropertyInt("port", context))
                .addPathSegment("location")
                .addQueryParameter("latitude", lat)
                .addQueryParameter("longitude", longitude)
                .build();
        Request request = new Request.Builder()
                .url(mySearchUrl)
                .method("GET", null)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Response response = client.newCall(request).execute();

        String resString = response.body().string();
        JSONObject resBody = new JSONObject(resString);
        String encodedImage = (String) resBody.get("file");
        Bitmap bitmap = StringToBitMap(encodedImage);
        return bitmap;
    }

    static public void sendImage(Bitmap bitmapImage,
                                 String title,
                                 String description,
                                 List<String> tags,
                                 Callback callback, Context context) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();
        String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("picture", encoded)
                .addFormDataPart("title", title)
                .addFormDataPart("descriptionc", description)
                .addFormDataPart("longitude", "10.01")
                .addFormDataPart("latitude", "15")
                .addFormDataPart("tags", new JSONArray(tags).toString())
                .build();
        HttpUrl postUrl= new HttpUrl.Builder()
                .scheme(PropertiesHandler.getProperty("scheme", context))
                .host(PropertiesHandler.getProperty("host", context))
                .port(PropertiesHandler.getPropertyInt("port", context))
                .addPathSegment("upload")
                .build();
        Request request = new Request.Builder()
                .url(postUrl)
                .method("POST", body)
                .build();

       client.newCall(request).enqueue(callback);

    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
