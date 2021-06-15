package com.nda.new_qr_barcode_scanner;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/* Regarding ads */
import com.google.android.gms.ads.AdView;
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

public class ScanFragment extends Fragment {
    TextView txtScan_with_image, txtResult, txtResult_link, txtShowAds;
    Button btn_scan, btnSearch;
    ImageView imgCopy_result_text, imgCopy_link_text;
    LinearLayout llbackgroundShowAdScanFragment;

    /* Regarding ads */
    private static final String TAG = "ScanFragment";
    private static final String AD_UNIT_ID_interstitalAd = "ca-app-pub-1973973370482992/7728811352";
    private AdView mAdView, mAdView_dialog_2;
    private NativeAd nativeAd;

    private InterstitialAd interstitialAd;

    /* Regarding ads */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        /* Regarding ads */
        llbackgroundShowAdScanFragment = (LinearLayout) view.findViewById(R.id.backgroundShowAdScanFragment);
        mAdView = view.findViewById(R.id.adView_scan_fragment);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadAd();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                llbackgroundShowAdScanFragment.setVisibility(View.VISIBLE);
            }
        });

        /* End of Regarding ads */
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2CC6C6"));


        txtScan_with_image       = view.findViewById(R.id.scan_fragment_scanned_image);
        txtResult                = view.findViewById(R.id.result_text);
        txtResult_link           = view.findViewById(R.id.result_link);
        //txtResult.setMovementMethod(new ScrollingMovementMethod());
        btnSearch                = view.findViewById(R.id.search);
        txtShowAds               = view.findViewById(R.id.show_ads);
        // = view.findViewById(R.id.camera_main);
        btn_scan                 = view.findViewById(R.id.scan_main);
        imgCopy_result_text      = view.findViewById(R.id.copy_result_text);
        imgCopy_link_text        = view.findViewById(R.id.copy_Link_text);

        copy_function();
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    scanCode();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        try{
                            if(interstitialAd != null)
                            {
                                save_id(btnSearch.getId());
                                interstitialAd.show(getActivity());
                            }
                            else
                            {
                                search_with_interestial();
                            }
                        }catch (Exception e)
                        {
                            Toast.makeText(getContext(),"No Link Found !",Toast.LENGTH_LONG).show();
                        }
            }

        });
        txtShowAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showAd_with_hapy_icon();
            }
        });
        txtScan_with_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScanImg = new Intent( getContext() , Scan_Image.class);
                startActivity(intentScanImg);
            }
        });

        return view;
    }
    private void copy_function()
    {
        imgCopy_result_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ImgView",txtResult.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Text Copied",Toast.LENGTH_LONG).show();
            }
        });
        imgCopy_link_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ImgView",txtResult_link.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Link Copied",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void search_with_interestial() {
        Intent intentSearch = new Intent(Intent.ACTION_WEB_SEARCH);
        String term = txtResult_link.getText().toString();
        intentSearch.putExtra(SearchManager.QUERY, term);
        startActivity(intentSearch);
    }

    private void save_id(int id) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SAVING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mID",id);
        editor.apply();
    }
    private int load_id()
    {
        SharedPreferences sharedPreferences_loaded = getContext().getSharedPreferences("SAVING",Context.MODE_PRIVATE);
        return sharedPreferences_loaded.getInt("mID",0);
    }
    private void scanCode() {
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false);
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan QR code & Barcode");
        IntentIntegrator.forSupportFragment(this).initiateScan();


//        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//        intentIntegrator.setCaptureActivity(CaptureAct.class);
//        intentIntegrator.setOrientationLocked(false);
//        intentIntegrator.setBeepEnabled(false);
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        intentIntegrator.setPrompt("Scanning Code");
//        intentIntegrator.initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Not Found (000)", Toast.LENGTH_LONG).show();
            } else {
                if (result.getContents().startsWith("http") || result.getContents().startsWith("https"))
                {
                    if (interstitialAd != null)
                    {
                        interstitialAd.show(getActivity());
                        txtResult_link.setText(result.getContents());
                        imgCopy_link_text.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txtResult_link.setText(result.getContents());
                        imgCopy_link_text.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if (interstitialAd != null)
                    {
                        interstitialAd.show(getActivity());
                        txtResult.setText(result.getContents());
                        imgCopy_result_text.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        txtResult.setText(result.getContents());
                        imgCopy_result_text.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {

            Toast.makeText(getContext(), "Not Found (001)", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void showAd_with_hapy_icon()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_show_ad);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mAdView_dialog_2 = dialog.findViewById(R.id.adView_dialog_show_ads_02);
        AdRequest adRequest_2 = new AdRequest.Builder().build();
        mAdView_dialog_2.loadAd(adRequest_2);
        dialog.show();

    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                getContext(),
                AD_UNIT_ID_interstitalAd,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        ScanFragment.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback( new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                ScanFragment.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                                switch(load_id())
                                {
                                    case R.id.search:
                                        search_with_interestial();
                                        break;
                                    default:
                                        break;

                                }
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                ScanFragment.this.interstitialAd = null;
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
