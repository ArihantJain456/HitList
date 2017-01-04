package com.example.arihantjain.hitlist;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Arihant Jain on 12/28/2016.
 */

public class AryaPermission {

    Activity activity;
    public AryaPermission(Activity activity){
        this.activity = activity;
    }

    public boolean checkPermissionFor(String permission){
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public void requestPermissionFor(String permission,int code){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){

        }
        else {
            ActivityCompat.requestPermissions(activity,new String[]{permission},code);
        }
    }
}
