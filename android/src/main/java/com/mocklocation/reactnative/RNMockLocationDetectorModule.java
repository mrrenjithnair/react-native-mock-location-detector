
package com.mocklocation.reactnative;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

//for callback

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
// import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat;

//import androidx.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.googlandroid.app.AppCompatActivitye.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class RNMockLocationDetectorModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNMockLocationDetectorModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNMockLocationDetector";
  }

  
  /** Java code for checkLocationProvide */
  @ReactMethod
  public void checkMockLocationProvider(final String dialogTitle, final String dialogMessage,
      final String dialogButtonText) {
    if (ActivityCompat.checkSelfPermission(getCurrentActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(getCurrentActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    FusedLocationProviderClient mFusedLocationClient;
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getCurrentActivity());
    mFusedLocationClient.getLastLocation().addOnSuccessListener(getCurrentActivity(),
        new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
              // Logic to handle location object
              if (isLocationFromMockProvider(getCurrentActivity(), location)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity());
                builder.setTitle(dialogTitle);
                builder.setMessage(dialogMessage);
                builder.setCancelable(false);
                builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    getCurrentActivity().finish();
                  }
                });
                builder.show();

              } 
            }
          }

        });
  }

  public boolean isLocationFromMockProvider(Context context, Location location) {
    boolean isMock = false;
    if (android.os.Build.VERSION.SDK_INT >= 18) {
      isMock = location.isFromMockProvider();
    } else {
      if (Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
        return false;
      else {
        return true;
      }
    }
    return isMock;
  }
}