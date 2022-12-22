package com.nda.new_qr_barcode_scanner.Fragment.Scanner;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nda.new_qr_barcode_scanner.MainActivity;
import com.nda.new_qr_barcode_scanner.Models.ScannedCode;
import com.nda.new_qr_barcode_scanner.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ScanFragment extends Fragment {
    TextView txtResult_text, txtGetPhoneNumber,txtSMSMessage;
    Button  btnCancel_result_text,btnSearchText;
    ImageView  imgCopyText,imgCopyPhone,imgCopyMessage, img_scan;

    RecyclerView rcv_scannedCode;
    AdapterScannedCode adapterScannedCode;
    List<ScannedCode> scannedCodeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2CC6C6"));

        initUI(view);
        setupRCV();
        initiate();



        return view;
    }

    private void setupRCV() {
        adapterScannedCode = new AdapterScannedCode(this, scannedCodeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_scannedCode.setLayoutManager(linearLayoutManager);
        rcv_scannedCode.setAdapter(adapterScannedCode);
        adapterScannedCode.notifyDataSetChanged();

        displayScannedCode();
    }

    private void initiate() {
        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

    }


    private void scanCode() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Voluming up to flash on !");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(scanOptions);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() != null) {
                    dialogShowText(result);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH-mm-ss", Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());

                    MainActivity.database.INSERT_CODE_SCANNED(
                            result.getContents(),
                            currentDateAndTime
                    );

                    displayScannedCode();
                }

    } );
    private void dialogShowText(ScanIntentResult result) {
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


    private void dialogLinkResult(ScanIntentResult result) {
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



    private void searchText(TextView txtResult_text) {
        Intent intentSearch = new Intent(Intent.ACTION_WEB_SEARCH);
        String term = txtResult_text.getText().toString();
        intentSearch.putExtra(SearchManager.QUERY, term);
        startActivity(intentSearch);
    }


    public void displayScannedCode()
    {
        scannedCodeList.clear();

        Cursor cursor = MainActivity.database.GetData("SELECT * FROM TableCodeScanned");

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String scannedContent = cursor.getString(1);
            String scannedDate = cursor.getString(2);

            scannedCodeList.add(0,new ScannedCode(id, scannedContent, scannedDate));
        }

        adapterScannedCode.notifyDataSetChanged();


    }

    private void initUI(View view) {
        rcv_scannedCode = view.findViewById(R.id.rcv_scannedCode);

        img_scan = view.findViewById(R.id.img_scan);

    }

}
