package com.nda.new_qr_barcode_scanner.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.nda.new_qr_barcode_scanner.MainActivity;
import com.nda.new_qr_barcode_scanner.R;
import com.startapp.sdk.adsbase.StartAppAd;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class GenerateFragment extends Fragment {
    EditText edtInputValue, edtGetStoreTitle;
    ImageView imgGetImgGenerate, imgDownLoad, btnCloseDialog;
    Button btnStartGenerate, btnSelector, btnSaveCode;
    Boolean qrCodeSelected = true;
    Bitmap bitmap;
    Dialog popupDialog;
    String getStoreTitle, text;
    Pattern pattern;
    Matcher matcher;
    boolean check_special_char;
    byte[]hinhAnh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_fragment, container, false);
        //ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        requireActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        );

        edtInputValue       = (EditText) view.findViewById(R.id.gemerate_value_entered);
        btnSelector         = (Button) view.findViewById(R.id.selector_generate);
        btnStartGenerate    = (Button) view.findViewById(R.id.generate_start_generate);
        imgGetImgGenerate   = (ImageView) view.findViewById(R.id.get_image_generate);
        imgDownLoad         = (ImageView) view.findViewById(R.id.download);

        btnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectValue();
            }
        });

        btnStartGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 text = edtInputValue.getText().toString();
                 pattern = Pattern.compile("[^a-zA-Z0-9 ]");

                 matcher = pattern.matcher(text);
                 check_special_char = matcher.find();

                if(text.trim().length() == 0 )
                {
                    Toast.makeText(getContext(), "Enter Text ! ", Toast.LENGTH_LONG).show();
                }
                else{

                        GenerateQRcode_Barcode();

                }
            }
        });
        return view;
    }




    private void GenerateQRcode_Barcode()
    {
        if (!qrCodeSelected)
        {
            Toast.makeText(getContext(),"Generate Barcode",Toast.LENGTH_LONG).show();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            if (text.length() > 80 || check_special_char)
            {
                Toast.makeText(getContext(),"Barcode cannot Exceed 80 Characters & Contain Special Characters !",Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Mã vạch không được vượt quá 80 ký tự & chứa ký tự đặc biệt  !",Toast.LENGTH_SHORT).show();

            }
            else{
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text,BarcodeFormat.CODE_128,500,
                            170,null);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imgGetImgGenerate.setImageBitmap(bitmap);
                    imgDownLoad.setVisibility(View.VISIBLE);
                    StartAppAd.showAd(getContext());

                    imgDownLoad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SaveToGallery();
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }

        }
        else
        {
            Toast.makeText(getContext(),"Generate QR Code",Toast.LENGTH_LONG).show();
            MultiFormatWriter writer  = new MultiFormatWriter();

            try {
                BitMatrix matrix = writer.encode(text,BarcodeFormat.QR_CODE,500,500);
                BarcodeEncoder encoder = new BarcodeEncoder();
                bitmap= encoder.createBitmap(matrix);
                imgGetImgGenerate.setImageBitmap(bitmap);

/*                InputMethodManager input = (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );*/


                imgDownLoad.setVisibility(View.VISIBLE);
                StartAppAd.showAd(getContext());

                imgDownLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveToGallery();
                    }
                });
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
    }

    private void SaveToGallery() {
        popupDialog = new Dialog(getActivity());
        popupDialog.setContentView(R.layout.dialog_save_image);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgGetImgGenerate.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap resized =Bitmap.createScaledBitmap(bitmap,612,512,true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        hinhAnh =byteArrayOutputStream.toByteArray();

        btnCloseDialog = (ImageView) popupDialog.findViewById(R.id.close_dialog);
        btnSaveCode    = (Button) popupDialog.findViewById(R.id.save_in_popup);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });
        btnSaveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGetStoreTitle = (EditText) popupDialog.findViewById(R.id.getStoreTitle);
                 getStoreTitle = edtGetStoreTitle.getText().toString();

                    save_with_interstial();


            }
        });
        popupDialog.show();


    }
    private void save_with_interstial()
    {
        MainActivity.database.INSERT_OwnCode(
            getStoreTitle,
            hinhAnh
         );
        Toast.makeText(getContext(),"Image saved !",Toast.LENGTH_SHORT).show();
        popupDialog.dismiss();
        StartAppAd.showAd(getContext());


    }
    private void SelectValue() {
        PopupMenu popupMenu = new PopupMenu(getContext(),btnSelector);

        popupMenu.getMenuInflater().inflate(R.menu.selector_generate, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.selecter_QR_code:
                        btnSelector.setText(R.string.fragment_generate_title_ScanWithQRCode);
                        qrCodeSelected = true;
                        break;
                    case R.id.selecter_Barcode:
                        btnSelector.setText(R.string.fragment_generate_title_ScanWithBarcodeCode);
                        qrCodeSelected = false;
                        break;
                    default:
                        qrCodeSelected = true;
                        break;

                }
                return false;
            }
        });

        popupMenu.show();

    }


}
