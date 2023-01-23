package com.example.hikersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import kotlinx.coroutines.debug.internal.DebugCoroutineInfo;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    private View decorView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView accTextView = findViewById(R.id.accTextView);
        TextView addTextView = findViewById(R.id.addTextView);


        decorView =  getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i == 0 ){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(@NonNull Location location) {

                    latTextView.setText("Latitude: " + location.getLatitude());
                    longTextView.setText("Longitude: " + location.getLongitude());
                    altTextView.setText("Altitude: " + location.getAltitude());
                    accTextView.setText("Accuracy: " + location.getAccuracy());

                try{
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if(addressList != null && addressList.size() >0) {
                        //Log.i("PlaceInfo", addressList.get(0).toString());
                        String address = "";


                        if (addressList.get(0).getThoroughfare() != null) {
                            address += addressList.get(0).getThoroughfare() + ", ";
                        }

                        if (addressList.get(0).getLocale() != null) {
                            address += addressList.get(0).getLocality() + ", ";
                        }


                        if (addressList.get(0).getSubAdminArea() != null) {
                            address += addressList.get(0).getSubAdminArea() + ", ";
                        }

                        if (addressList.get(0).getAdminArea() != null) {
                            address += addressList.get(0).getAdminArea() + ", ";
                        }

                        if (addressList.get(0).getPostalCode() != null) {
                            address += addressList.get(0).getPostalCode();
                        }
                        addTextView.setText("Address: " + address);
                    }


                } catch( Exception e ){
                    e.printStackTrace();
                }
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }


    }
    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
}