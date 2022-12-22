package com.nda.new_qr_barcode_scanner.Fragment.SavedCode;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nda.new_qr_barcode_scanner.MainActivity;
import com.nda.new_qr_barcode_scanner.Models.SavedCode;
import com.nda.new_qr_barcode_scanner.R;

import java.util.ArrayList;

public class FragmentSavedCode extends Fragment {
    ListView lstOwnCode;
    public static ArrayList<SavedCode> OwnArrayList_Main;
    public static AdapterSavedCode adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_saved_code, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );


        lstOwnCode = (ListView) view.findViewById(R.id.List_In_Own_Fragment);
        OwnArrayList_Main = new ArrayList<>();
        adapter = new AdapterSavedCode(this,R.layout.item_saved_code,OwnArrayList_Main);
        lstOwnCode.setAdapter(adapter);
        disPlayOwnCode();
        return view;
    }


    public void disPlayOwnCode()
    {
        Cursor cursor = MainActivity.database.GetData("SELECT * FROM TableSavedCode");
        OwnArrayList_Main.clear();
        while (cursor.moveToNext())
        {
            int id =cursor.getInt(0);
            String title = cursor.getString(1);
            byte[] stored_img = cursor.getBlob(2);
            OwnArrayList_Main.add(new SavedCode(id,title,stored_img));
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

                    MainActivity.database.QueryData("DELETE FROM TableSavedCode WHERE Id = '" + Id +"'");

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
}