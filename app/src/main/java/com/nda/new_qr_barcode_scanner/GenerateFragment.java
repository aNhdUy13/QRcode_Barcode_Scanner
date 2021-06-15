package com.nda.new_qr_barcode_scanner;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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
    //StoreDataWithDoublyLinkedList aList = new StoreDataWithDoublyLinkedList();
    /* Regarding ads */
        LinearLayout llbackgroundShowAdGenerateFragment;
        private static final String TAG = "GenerateFragment";
        private static final String AD_UNIT_ID_interstitalAd_generate = "ca-app-pub-1973973370482992/7728811352";
        private static final String AD_UNIT_ID_nativeAd_generate = "000";

    private AdView mAdView, mAdViewPopUpSaveImage_2;
        private NativeAd nativeAd1;
        private InterstitialAd interstitialAd;

    /* Regarding ads */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_fragment, container, false);
        //ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        /* Regarding ads */
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAd();
            }
        });
        llbackgroundShowAdGenerateFragment = (LinearLayout) view.findViewById(R.id.backgroundShowAdGenerateFragment);
        mAdView = view.findViewById(R.id.adView_generate_01);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                llbackgroundShowAdGenerateFragment.setVisibility(View.VISIBLE);
            }
        });


        AdLoader adLoader = new AdLoader.Builder(getContext(), AD_UNIT_ID_nativeAd_generate)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        // Show the ad.
//                        FrameLayout frameLayout = view.findViewById(R.id.id_native_ad);
//                        NativeAdView nativeAdView = (NativeAdView) getLayoutInflater()
//                                .inflate(R.layout.native_ad_layout,null);
//                        //mapUnifiNativetoLayout(NativeAd,nativeAdView);
//                        //MediaView mediaView = nativeAdView.findViewById(R.id.ad_media);
//                        nativeAdView.setMediaView((MediaView) nativeAdView.findViewById(R.id.ad_media));
//
//                        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
//                        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
//                        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
//                        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_icon));
//                        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
//                        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_rating));
//                        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
//                        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
//
//                        ((TextView) nativeAdView.getHeadlineView()).setText(NativeAd.getHeadline());
//                        nativeAdView.getMediaView().setMediaContent(NativeAd.getMediaContent());
//
//                        if (NativeAd.getBody() == null)
//                        {
//                            nativeAdView.getBodyView().setVisibility(View.GONE);
//                        } else {
//                            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
//                            ((TextView)nativeAdView.getBodyView()).setText(NativeAd.getBody());
//                        }
//                        if (NativeAd.getHeadline() == null)
//                        {
//                            nativeAdView.getHeadlineView().setVisibility(View.GONE);
//                        } else {
//                            nativeAdView.getHeadlineView().setVisibility(View.VISIBLE);
//                            ((TextView)nativeAdView.getHeadlineView()).setText(NativeAd.getHeadline());
//                        }
//                        if (NativeAd.getCallToAction() == null)
//                        {
//                            nativeAdView.getCallToActionView().setVisibility(View.GONE);
//                        } else {
//                            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
//                            ((TextView)nativeAdView.getCallToActionView()).setText(NativeAd.getCallToAction());
//                        }
//                        if (NativeAd.getIcon() == null)
//                        {
//                            nativeAdView.getIconView().setVisibility(View.GONE);
//                        } else {
//                            ((ImageView)nativeAdView.getIconView()).setImageDrawable(NativeAd.getIcon().getDrawable());
//                            nativeAdView.getIconView().setVisibility(View.VISIBLE);
//                        }
//                        if (NativeAd.getPrice() == null)
//                        { nativeAdView.getPriceView().setVisibility(View.GONE); }
//                        else {
//                            nativeAdView.getPriceView().setVisibility(View.VISIBLE);
//                            ((TextView)nativeAdView.getPriceView()).setText(NativeAd.getPrice());
//                        }
//                        if (NativeAd.getStarRating() == null)
//                        { nativeAdView.getStarRatingView().setVisibility(View.GONE); }
//                        else {
//                            ((RatingBar)nativeAdView.getStarRatingView())
//                                    .setRating(NativeAd.getStarRating().floatValue());
//                            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
//                        }
//                        if (NativeAd.getStore() == null)
//                        {
//                            nativeAdView.getStoreView().setVisibility(View.GONE);
//                        } else {
//                            nativeAdView.getStoreView().setVisibility(View.VISIBLE);
//                            ((TextView)nativeAdView.getStoreView()).setText(NativeAd.getStore());
//                        }
//                        if (NativeAd.getAdvertiser() == null)
//                        { nativeAdView.getAdvertiserView().setVisibility(View.GONE); }
//                        else {
//                            ((TextView)nativeAdView.getAdvertiserView()).setText(NativeAd.getAdvertiser());
//                            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
//                        }
//                        nativeAdView.setNativeAd(NativeAd);
//
//                        //frameLayout.removeAllViews();
//                        frameLayout.addView(nativeAdView);

                    }
                })

                .build();
        adLoader.loadAd(new AdRequest.Builder().build());


        /* End of Regarding ads */


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
                    if(interstitialAd != null)
                    {
                        save_id(btnStartGenerate.getId());
                        interstitialAd.show(getActivity());
                    }
                    else
                    {
                        GenerateQRcode_Barcode();
                    }
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
            QRGEncoder qrgEncoder = new QRGEncoder(text.trim(),null, QRGContents.Type.TEXT,700);

            try {
                bitmap= qrgEncoder.encodeAsBitmap();
                imgGetImgGenerate.setImageBitmap(bitmap);
                imgDownLoad.setVisibility(View.VISIBLE);

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


        mAdViewPopUpSaveImage_2 = popupDialog.findViewById(R.id.adView_savePopUp_2);
        AdRequest adRequest_SavePopUp_2 = new AdRequest.Builder().build();
        mAdViewPopUpSaveImage_2.loadAd(adRequest_SavePopUp_2);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
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
                loadAd();

                if(interstitialAd != null)
                {
                    save_id(btnSaveCode.getId());
                    interstitialAd.show(getActivity());
                }
                else
                {
                    save_with_interstial();
                }
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
    private void save_id(int id) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SAVING",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mID",id);
        editor.apply();
    }
    private int load_id()
    {
        SharedPreferences sharedPreferences_loaded = getContext().getSharedPreferences("SAVING",Context.MODE_PRIVATE);
        return sharedPreferences_loaded.getInt("mID",0);
    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                getContext(),
                AD_UNIT_ID_interstitalAd_generate,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        GenerateFragment.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback( new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                GenerateFragment.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                                switch(load_id()){
                                    case R.id.generate_start_generate:
                                        GenerateQRcode_Barcode();
                                        break;
                                    case R.id.save_in_popup:
                                        save_with_interstial();
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
                                GenerateFragment.this.interstitialAd = null;
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
