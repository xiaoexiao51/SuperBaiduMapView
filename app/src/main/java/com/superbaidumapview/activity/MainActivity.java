package com.superbaidumapview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.superbaidumapview.R;
import com.superbaidumapview.SuperApplication;
import com.superbaidumapview.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private String mSelectAddress;
    private String mSelectLatLon;
    private static final int LOCATION_CODE = 5010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.tv_location)).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SuperApplication.add)) {
                    ((TextView) findViewById(R.id.tv_location)).setText("定位失败");
                } else {
                    mSelectAddress = SuperApplication.add;
                    mSelectLatLon = SuperApplication.lat + "," + SuperApplication.lon;
                    ((TextView) findViewById(R.id.tv_location)).setText(mSelectAddress + "\n" + mSelectLatLon);
                }
            }
        }, 1000);

        findViewById(R.id.bt_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        findViewById(R.id.bt_location2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mSelectAddress) && TextUtils.isEmpty(mSelectLatLon)) {
                    Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("src_latlon", mSelectLatLon);
                    ActivityUtils.launchActivityForResult(MainActivity.this, LocationActivity2.class, LOCATION_CODE, bundle);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LOCATION_CODE) {
            String address = data.getStringExtra("address");
            String latLon = data.getStringExtra("latLon");
            if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(latLon)) {
                mSelectAddress = address;
                mSelectLatLon = latLon;
                ((TextView) findViewById(R.id.tv_location)).setText(mSelectAddress + "\n" + mSelectLatLon);
            }
        }
    }
}
