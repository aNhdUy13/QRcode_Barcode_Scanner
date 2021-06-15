package com.nda.new_qr_barcode_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navigationView;
    public ViewPager mviewPager;
    public static db database;


    String db_Name = "QRcode_BarCode_Scanner.sqlite";
    public static String db_table ="CodeTable";
    public static String db_table_history = "HistoryTable";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // [END shared_app_measurement]

        database = new db(this,db_Name,null,1);
        database.QueryData("CREATE TABLE IF NOT EXISTS "+ db_table + "(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "storedTitle VARCHAR(200), storedImage BLOB)");

//        database.QueryData("CREATE TABLE IF NOT EXISTS " + db_table_history + "Id_History PRIMARY KEY AUTOINCREMENT," +
//                "History_Date TEXT, History_Image BLOB");
        //database.QueryData("DROP TABLE IF EXISTS " + db_table);


        selectedBottomNavigation();
        setUpViewPager();
    }

    private void selectedBottomNavigation()
    {
         navigationView = (BottomNavigationView) findViewById(R.id.bottom_navi);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.generate_qr:
                        mviewPager.setCurrentItem(0);
                        break;

                    case R.id.scan_qr:
                        mviewPager.setCurrentItem(1);
                        break;

                    case R.id.Onw_qr:
                        mviewPager.setCurrentItem(2);
                }
                return true;
            }
        });
    }

    private void setUpViewPager()
    {
        mviewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPager_Adapter viewPager_adapter = new ViewPager_Adapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mviewPager.setAdapter(viewPager_adapter);

        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        navigationView.getMenu().findItem(R.id.generate_qr).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.scan_qr).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.Onw_qr).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}