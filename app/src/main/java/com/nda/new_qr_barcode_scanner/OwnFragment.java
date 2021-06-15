package com.nda.new_qr_barcode_scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/* Regarding ads */
import com.google.android.gms.ads.AdListener;
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

import java.util.ArrayList;

public class OwnFragment extends Fragment {
    ListView lstOwnCode;
    public static ArrayList<OwnConstructor> OwnArrayList_Main;
    public static OwnCode_Adapter adapter;

    /* Regarding ads */
    LinearLayout llbackgroundShowAdOwnFragment;
    private static final String TAG = "OwnFragment";
    private static final String AD_UNIT_ID_interstitalAd = "ca-app-pub-1973973370482992/7728811352";
    private AdView mAdView, mAdView_Detail, mAdView_Detail_2;
    private NativeAd nativeAd;

    private InterstitialAd interstitialAd;

    /* Regarding ads */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_own_fragment, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
        /* Regarding ads */
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        llbackgroundShowAdOwnFragment = (LinearLayout) view.findViewById(R.id.backgroundShowAdOwnFragment);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                llbackgroundShowAdOwnFragment.setVisibility(View.VISIBLE);
            }
        });

        loadAd();
        /* End of Regarding ads */

        lstOwnCode = (ListView) view.findViewById(R.id.List_In_Own_Fragment);
        OwnArrayList_Main = new ArrayList<>();
        adapter = new OwnCode_Adapter(this,R.layout.dongcongviec_own_code,OwnArrayList_Main);
        lstOwnCode.setAdapter(adapter);
        disPlayOwnCode();
        return view;
    }


    public void disPlayOwnCode()
    {
        Cursor cursor = MainActivity.database.GetData("SELECT * FROM CodeTable");
        OwnArrayList_Main.clear();
        while (cursor.moveToNext())
        {
            int id =cursor.getInt(0);
            String title = cursor.getString(1);
            byte[] stored_img = cursor.getBlob(2);
            OwnArrayList_Main.add(new OwnConstructor(id,title,stored_img));
        }
        adapter.notifyDataSetChanged();
    }
    public void showDetailOwnCode(String title, byte[] hinhAnh)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_own_code);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /* Regarding ads */

        mAdView_Detail_2 = dialog.findViewById(R.id.adView_detail_2);
        AdRequest adRequest_Detail_2 = new AdRequest.Builder().build();
        mAdView_Detail_2.loadAd(adRequest_Detail_2);


        /* End of Regarding ads */
        TextView txtDetail = (TextView) dialog.findViewById(R.id.show_detail_title_own_code);
        ImageView img          = (ImageView) dialog.findViewById(R.id.show_detail_image);

        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh,0,hinhAnh.length);

        txtDetail.setText(title);
        img.setImageBitmap(bitmap);
        dialog.show();
    }
    public void dialog_delete(String title, int Id )
    {
        loadAd();
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Are you sure to delete \" " + title + " \"");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (interstitialAd != null)
                {
                    interstitialAd.show(getActivity());
                    MainActivity.database.QueryData("DELETE FROM CodeTable WHERE Id = '" + Id +"'");
                }
                else
                {
                    MainActivity.database.QueryData("DELETE FROM CodeTable WHERE Id = '" + Id +"'");
                }
                Toast.makeText(getContext(),"Delete Success \" " + title + " \"",Toast.LENGTH_LONG ).show();
                disPlayOwnCode();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
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
                        OwnFragment.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback( new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                OwnFragment.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                OwnFragment.this.interstitialAd = null;
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