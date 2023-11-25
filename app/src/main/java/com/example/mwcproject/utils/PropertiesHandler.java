package com.example.mwcproject.utils;

import android.content.Context;

import com.example.mwcproject.R;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    public static String getProperty(String key, Context context) {
        Properties properties = new Properties();
        InputStream rawResource = context.getResources().openRawResource(R.raw.config);
        try{
            properties.load(rawResource);
        }catch(Exception e){
            e.printStackTrace();
        }
        return properties.getProperty(key, "");
    }

    public static int getPropertyInt(String key, Context context) {
        Properties properties = new Properties();
        try{
            InputStream rawResource = context.getResources().openRawResource(R.raw.config);
            properties.load(rawResource);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(properties.getProperty(key, "3000"));
    }




}
