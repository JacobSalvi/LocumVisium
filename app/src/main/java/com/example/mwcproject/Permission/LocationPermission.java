package com.example.mwcproject.Permission;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.mwcproject.services.Localisation.LocationService;

public class LocationPermission extends AbstractPermission  {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private static volatile LocationPermission instance;


    public static LocationPermission getInstance(Context context, AppCompatActivity activity) {
        if (instance == null) {
            synchronized(LocationPermission.class) {
                if (instance == null) {
                    instance = new LocationPermission(context, activity);
                }
            }
        }
        return instance;
    }

    private LocationPermission(Context context, AppCompatActivity activity) {
        super(context, activity);
    }

    @Override
    public boolean isEnabled() {
        return  (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void queryForPermission() {
        if(!isEnabled()) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationService();
        }
    }

    @Override
    public void checkForEnabledPermission(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForPermissionChange(true);
               startLocationService();
            } else {
                Toast.makeText(context, "Localization ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(context, LocationService.class);
        context.startService(serviceIntent);



    }

}
