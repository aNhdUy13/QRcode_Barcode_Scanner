package com.nda.new_qr_barcode_scanner.Fragment;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
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
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nda.new_qr_barcode_scanner.AdapterWithNativeAd;
import com.nda.new_qr_barcode_scanner.BuildConfig;
import com.nda.new_qr_barcode_scanner.MainActivity;
import com.nda.new_qr_barcode_scanner.R;
import com.nda.new_qr_barcode_scanner.Scan_Image;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.ads.banner.banner3d.Banner3D;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ScanFragment extends Fragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    TextView txtScan_with_image, txtShowAds, txtResult_text, txtGetPhoneNumber,txtSMSMessage;
    Button btn_scan, btnCancel_result_text,btnSearchText;
    ImageView  imgCopyText,imgCopyPhone,imgCopyMessage,imgLight;
    boolean hasCameraFlash = false;
    boolean flashOn = false;
    @Nullable
    protected AdapterWithNativeAd adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);

        requireActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        );
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2CC6C6"));
        mapting(view);
        showNativeAds(view);
        initiate();



        return view;
    }

    private void initiate() {
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
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

        /*  Function : Flash Light   */
        hasCameraFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        imgLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraFlash)
                {
                    if (flashOn)
                    {
                        flashOn = false;
                        imgLight.setImageResource(R.drawable.icon_light_off);
                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        flashOn = true;
                        imgLight.setImageResource(R.drawable.icon_light_on);
                        try {
                            flishLightOn();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Flash Found !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*  Function (End) : Flash Light   */
    }

    private void flishLightOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,true);

    }

    private void flashLightOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,false);
    }


    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());

        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false);
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan QR code & Barcode");
        IntentIntegrator.forSupportFragment(this).initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Error : Not Found (000)", Toast.LENGTH_LONG).show();
            } else {
                if (result.getContents().startsWith("http") || result.getContents().startsWith("https"))
                {
                    // check if "link" found

                    dialogShowLink(result);
                }
//                else if(result.getContents().contains("SMSTO:"))
//                {
//                    // check if "SMS" found
//                    dialogShowSMS(result);
//                }
//                else if(result.getContents().contains("WIFI:T"))
//                {
//                    Toast.makeText(getContext(), "Wifi Found", Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    // check if "text" found
                    dialogShowText(result);
                }
            }
        } else {

            Toast.makeText(getContext(), "Error : Not Found (001)", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void dialogShowSMS(IntentResult result) {
        Dialog dialogResult = new Dialog(getContext());
        dialogResult.setContentView(R.layout.dialog_scan_result_sms);
        dialogResult.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtGetPhoneNumber       = (TextView) dialogResult.findViewById(R.id.txtGetPhoneNumber);
        txtSMSMessage           = (TextView) dialogResult.findViewById(R.id.txtSMSMessage);

        btnCancel_result_text   = (Button) dialogResult.findViewById(R.id.btnCancel_result_text);
        imgCopyPhone            = (ImageView) dialogResult.findViewById(R.id.imgCopyPhone);
        imgCopyMessage          = (ImageView) dialogResult.findViewById(R.id.imgCopyMessage);


        String scanResult = result.getContents();

        String[] stringSplited = scanResult.split(":");
        List<String> get = new ArrayList<>();

        for (int i = 0 ; i < stringSplited.length; i ++)
        {
            get.add("" + i);
        }


        imgCopyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ImgView",txtResult_text.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Text Copied",Toast.LENGTH_LONG).show();
            }
        });


        btnCancel_result_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.dismiss();
            }
        });

        dialogResult.show();
    }


    private void dialogShowLink(IntentResult result) {
        Dialog dialogResult = new Dialog(getContext());
        dialogResult.setContentView(R.layout.dialog_scan_result_link);
        dialogResult.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtResult_text  = (TextView) dialogResult.findViewById(R.id.txtResult_text);
        btnCancel_result_text   = (Button) dialogResult.findViewById(R.id.btnCancel_result_text);
        btnSearchText           = (Button) dialogResult.findViewById(R.id.btnSearchText);
        imgCopyText             = (ImageView) dialogResult.findViewById(R.id.imgCopyText);

        txtResult_text.setText(result.getContents());
        imgCopyText.setVisibility(View.VISIBLE);

        imgCopyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ImgView",txtResult_text.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Text Copied",Toast.LENGTH_LONG).show();
            }
        });

        btnSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText(txtResult_text);
            }
        });
        btnCancel_result_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.dismiss();
            }
        });

        dialogResult.show();
    }

    private void dialogShowText(IntentResult result) {
        Dialog dialogResult = new Dialog(getContext());
        dialogResult.setContentView(R.layout.dialog_scan_result_text);
        dialogResult.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtResult_text  = (TextView) dialogResult.findViewById(R.id.txtResult_text);
        btnCancel_result_text   = (Button) dialogResult.findViewById(R.id.btnCancel_result_text);
        btnSearchText           = (Button) dialogResult.findViewById(R.id.btnSearchText);
        imgCopyText             = (ImageView) dialogResult.findViewById(R.id.imgCopyText);

        txtResult_text.setText(result.getContents());
        imgCopyText.setVisibility(View.VISIBLE);

        imgCopyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ImgView",txtResult_text.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Text Copied",Toast.LENGTH_LONG).show();
            }
        });

        btnSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText(txtResult_text);
            }
        });
        btnCancel_result_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResult.dismiss();
            }
        });

        dialogResult.show();
    }
    private void searchText(TextView txtResult_text) {
        Intent intentSearch = new Intent(Intent.ACTION_WEB_SEARCH);
        String term = txtResult_text.getText().toString();
        intentSearch.putExtra(SearchManager.QUERY, term);
        startActivity(intentSearch);
    }
    private void showAd_with_hapy_icon()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_show_ad);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

    }

    private void showNativeAds(View view)
    {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter = new AdapterWithNativeAd(getContext()));
        loadData();
        loadNativeAd();
    }

    private void loadNativeAd() {
        final StartAppNativeAd nativeAd = new StartAppNativeAd(getContext());

        nativeAd.loadAd(new NativeAdPreferences()
                .setAdsNumber(1)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(2), new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                if (adapter != null) {
                    adapter.setNativeAd(nativeAd.getNativeAds());
                }
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                if (BuildConfig.DEBUG) {
                }
            }
        });
    }

    // TODO example of loading JSON array, change this code according to your needs
    @UiThread
    private void loadData() {
        if (adapter != null) {
            adapter.setData(Collections.singletonList("Loading..."));
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            @WorkerThread
            public void run() {
                String url = "https://raw.githubusercontent.com/StartApp-SDK/StartApp_InApp_SDK_Example/master/app/data.json";

                final List<String> data = new ArrayList<>();

                try (InputStream is = new URL(url).openStream()) {
                    if (is != null) {
                        JsonReader reader = new JsonReader(new InputStreamReader(is));
                        reader.beginArray();

                        while (reader.peek() == JsonToken.STRING) {
                            data.add(reader.nextString());
                        }

                        reader.endArray();
                    }
                } catch (RuntimeException | IOException ex) {
                    data.clear();
                    data.add(ex.toString());
                } finally {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (adapter != null) {
//                                adapter.setData(data);
//                            }
//                        }
//                    });
                }
            }
        });
    }
    private void mapting(View view) {
        txtScan_with_image       = view.findViewById(R.id.scan_fragment_scanned_image);
        txtShowAds               = view.findViewById(R.id.show_ads);
        btn_scan                 = view.findViewById(R.id.scan_main);
        imgLight                 = view.findViewById(R.id.imgLight);
    }

}
