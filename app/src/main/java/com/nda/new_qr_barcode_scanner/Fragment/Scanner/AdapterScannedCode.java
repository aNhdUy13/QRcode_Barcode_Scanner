package com.nda.new_qr_barcode_scanner.Fragment.Scanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.new_qr_barcode_scanner.MainActivity;
import com.nda.new_qr_barcode_scanner.Models.ScannedCode;
import com.nda.new_qr_barcode_scanner.R;

import java.util.List;

public class AdapterScannedCode extends RecyclerView.Adapter<AdapterScannedCode.HolderScannedCode> {
    ScanFragment context;
    List<ScannedCode> scannedCodeList;

    public AdapterScannedCode(ScanFragment context, List<ScannedCode> scannedCodeList) {
        this.context = context;
        this.scannedCodeList = scannedCodeList;
    }

    @NonNull
    @Override
    public AdapterScannedCode.HolderScannedCode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scanned_code, parent, false);
        return new AdapterScannedCode.HolderScannedCode(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterScannedCode.HolderScannedCode holder, int position) {
        ScannedCode scannedCode = scannedCodeList.get(position);

        String scannedDateTime = scannedCode.getScannedTime();
        String[] splitDateTime = scannedDateTime.split("_");

        String date = splitDateTime[0];
        String time = splitDateTime[1];

        String[] splitTime = time.split("-");
        String finalTime = splitTime[0] + "h " + splitTime[1] + "m " + splitTime[2] + "s";

        holder.txt_scannedTime.setText(date + " " + finalTime);
        holder.txt_scannedContent.setText(scannedCode.getScannedContent());

        holder.ll_itemScannedCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete(scannedCode);
            }
        });
    }

    private void dialogDelete(ScannedCode scannedCode) {
        Dialog dialog = new Dialog(context.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete);

        CardView cv_delete = dialog.findViewById(R.id.cv_delete);
        CardView cv_cancel = dialog.findViewById(R.id.cv_cancel);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.database.QueryData("DELETE FROM TableCodeScanned WHERE Id = '" + scannedCode.getId() + "'");

                context.displayScannedCode();
                dialog.dismiss();

            }
        });
        cv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return scannedCodeList.size();
    }

    public class HolderScannedCode extends RecyclerView.ViewHolder {
        TextView txt_scannedTime, txt_scannedContent;
        LinearLayout ll_itemScannedCode;

        public HolderScannedCode(@NonNull View itemView) {
            super(itemView);

            txt_scannedTime = itemView.findViewById(R.id.txt_scannedTime);
            txt_scannedContent = itemView.findViewById(R.id.txt_scannedContent);

            ll_itemScannedCode = itemView.findViewById(R.id.ll_itemScannedCode);

        }
    }
}
