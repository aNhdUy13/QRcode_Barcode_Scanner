package com.nda.new_qr_barcode_scanner.Fragment.SavedCode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nda.new_qr_barcode_scanner.Models.SavedCode;
import com.nda.new_qr_barcode_scanner.R;

import java.util.ArrayList;

public class AdapterSavedCode extends BaseAdapter {
    private FragmentSavedCode context;
    private  int layout;
    private ArrayList<SavedCode> OwnCode_ArrayList;

    public AdapterSavedCode(FragmentSavedCode context, int layout, ArrayList<SavedCode> ownCode_ArrayList) {
        this.context = context;
        this.layout = layout;
        OwnCode_ArrayList = ownCode_ArrayList;
    }

    @Override
    public int getCount() {
        return OwnCode_ArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder
    {
        ImageView imgDisplayImage, img_delete;
        TextView txtDisplayTitle;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(layout,null);

            holder.imgDisplayImage      =  convertView.findViewById(R.id.display_image_dongcongviec_own_code);
            holder.txtDisplayTitle      =  convertView.findViewById(R.id.display_title_dongcongviec_own_code);
            holder.img_delete     =  convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        SavedCode ownConstructor = OwnCode_ArrayList.get(position);
        /* Convert image */
        byte[] hinhanh =ownConstructor.getImg_own();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
        /* End of Convert image */

        holder.txtDisplayTitle.setText(ownConstructor.getTitle_own());
        holder.imgDisplayImage.setImageBitmap(bitmap);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.dialog_delete(ownConstructor.getTitle_own(), ownConstructor.getId_own());
            }
        });

        /* Show detail when click OWN_CODE */
        holder.imgDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showDetailOwnCode(ownConstructor.getTitle_own(), ownConstructor.getImg_own());
            }
        });
        /* ENd of Show detail when click OWN_CODE */

        return convertView;
    }
}
