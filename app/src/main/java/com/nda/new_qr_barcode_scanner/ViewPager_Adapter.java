package com.nda.new_qr_barcode_scanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nda.new_qr_barcode_scanner.Fragment.GenerateFragment;
import com.nda.new_qr_barcode_scanner.Fragment.OwnFragment;
import com.nda.new_qr_barcode_scanner.Fragment.ScanFragment;

public class ViewPager_Adapter extends FragmentStatePagerAdapter {
    public ViewPager_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new GenerateFragment();
            case 1:
                return new ScanFragment();
            case 2:
                return new OwnFragment();
            default:
                return new ScanFragment();

        }
    }

    @Override
    public int getCount() {
        return 3; // Tra ve so luong cua tap
    }
}
