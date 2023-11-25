package com.example.mwcproject.Permission;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPermission {
    AppCompatActivity activity;
    Context context;
    private boolean enabled;
    private List<PermissionListener> permissionListeners;

    public void AddListener(PermissionListener itself) {
        permissionListeners.add(itself);
    }

    public AbstractPermission(Context context, AppCompatActivity activity) {
        this.permissionListeners = new ArrayList<>();
        this.context = context;
        this.activity = activity;
        this.enabled = isEnabled();
    }


    public interface PermissionListener {
        void onPermissionChange();
    }

    protected void checkForPermissionChange(boolean newValue) {
        if (enabled != newValue) {
            onPermissionChange();
            enabled = newValue;
        }
    }
    private void onPermissionChange() {
        if (permissionListeners != null && !permissionListeners.isEmpty()) {
            permissionListeners.forEach(PermissionListener::onPermissionChange);
        }

    }
    public abstract boolean isEnabled();
    public abstract void queryForPermission();

    public abstract void checkForEnabledPermission(int requestCode, @NonNull int[] grantResults);


}
