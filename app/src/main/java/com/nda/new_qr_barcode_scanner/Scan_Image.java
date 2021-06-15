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

/* Regarding ads */
import com.google.android.gms.ads.AdView;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;

/*End of regarding ads */
public class Scan_Image extends AppCompatActivity {
    Button btnScan, btnBack;
    ImageView imgScannedImg;
    IntentIntegrator integrator;
    /* Regarding ads */
    private static final String TAG = "Scan";
    private static final String AD_UNIT_ID_interstitalAd = "ca-app-pub-1973973370482992/7728811352";
    private AdView mAdView, mAdView_2;
    private NativeAd nativeAd;

    private InterstitialAd interstitialAd;

    /* Regarding ads */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__image);
        /* Regarding ads */
        mAdView = findViewById(R.id.adView_scan_img_1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAd();


        /* End of Regarding ads */
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
                    if(interstitialAd != null)
                    {
                        interstitialAd.show(Scan_Image.this);
                        scan_img_with_interestial();
                    }
                    else{
                        scan_img_with_interestial();
                    }
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

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                Scan_Image.this,
                AD_UNIT_ID_interstitalAd,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Scan_Image.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback( new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                Scan_Image.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                Scan_Image.this.interstitialAd = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });
    }
}