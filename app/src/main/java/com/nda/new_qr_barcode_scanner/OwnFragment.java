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


import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;

public class OwnFragment extends Fragment {
    ListView lstOwnCode;
    public static ArrayList<OwnConstructor> OwnArrayList_Main;
    public static OwnCode_Adapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_own_fragment, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );


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

        TextView txtDetail = (TextView) dialog.findViewById(R.id.show_detail_title_own_code);
        ImageView img          = (ImageView) dialog.findViewById(R.id.show_detail_image);

        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh,0,hinhAnh.length);

        txtDetail.setText(title);
        img.setImageBitmap(bitmap);
        dialog.show();
    }
    public void dialog_delete(String title, int Id )
    {
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Are you sure to delete \" " + title + " \"");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    MainActivity.database.QueryData("DELETE FROM CodeTable WHERE Id = '" + Id +"'");

                Toast.makeText(getContext(),"Delete Success \" " + title + " \"",Toast.LENGTH_LONG ).show();
                disPlayOwnCode();
                StartAppAd.showAd(getContext());

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}