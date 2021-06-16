package com.nda.new_qr_barcode_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;

public class Scan_Image extends AppCompatActivity {
    Button btnScan, btnBack;
    ImageView imgScannedImg;
    IntentIntegrator integrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__image);

        btnScan     = (Button) findViewById(R.id.scan_for_image);
        btnBack     = (Button) findViewById(R.id.back_to_main_scan);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
         integrator = new IntentIntegrator(this);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_img_with_interstial();
            }
        });
    }
    private void scan_img_with_interstial(){
        integrator.setPrompt("Scan Image");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled !", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Picasso.with(this).load(result.getContents()).into(imgScannedImg);
                }catch (Exception e)
                {

                        scan_img_with_interestial();

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void scan_img_with_interestial()
    {
        imgScannedImg = (ImageView) findViewById(R.id.image_scanned);
        Toast.makeText(this, "Cannot Found Image !", Toast.LENGTH_LONG).show();
        imgScannedImg.setImageResource(R.drawable.sad_img);
    }


}