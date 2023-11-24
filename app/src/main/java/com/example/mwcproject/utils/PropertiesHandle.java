package com.example.mwcproject.utils;

import android.content.Context;
import java.util.Properties;

public class PropertiesHandle {

    public static String getProperty(String key, Context context) {
        Properties properties = new Properties();
        try{
            properties.load(context.getAssets().open("app.properties"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return properties.getProperty(key, "");
    }

    public static int getPropertyInt(String key, Context context) {
        Properties properties = new Properties();
        try{
            properties.load(context.getAssets().open("app.properties"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(properties.getProperty(key, "3000"));
    }




}
