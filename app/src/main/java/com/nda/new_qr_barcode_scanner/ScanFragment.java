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


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;


public class ScanFragment extends Fragment {
    TextView txtScan_with_image, txtResult, txtResult_link, txtShowAds;
    Button btn_scan, btnSearch;
    ImageView imgCopy_result_text, imgCopy_link_text;
    LinearLayout llbackgroundShowAdScanFragment;


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

                                search_with_interestial();

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


    private void scanCode() {
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false);
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan QR code & Barcode");
        IntentIntegrator.forSupportFragment(this).initiateScan();

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

                        txtResult_link.setText(result.getContents());
                        imgCopy_link_text.setVisibility(View.VISIBLE);

                }
                else
                {

                        txtResult.setText(result.getContents());
                        imgCopy_result_text.setVisibility(View.VISIBLE);

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

        dialog.show();

    }
}
