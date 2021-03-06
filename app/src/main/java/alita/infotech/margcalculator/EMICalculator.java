package alita.infotech.margcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.margcalculator.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class EMICalculator extends AppCompatActivity {
    private static InterstitialAd mInterstitialAd;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculator);
        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-6033789502768800/7093864807");  //live id
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //debug id
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("EMI CALCULATOR");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(this, "ca-app-pub-6033789502768800~5575046806");//alita app id
        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                    Toast.makeText(getApplicationContext(), errorCode,Toast.LENGTH_SHORT).show();
            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        EMIAdapter adapter = new EMIAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static InterstitialAd getAd(){
        return mInterstitialAd;
    }

    public static void setAd(InterstitialAd ad){
        mInterstitialAd = ad;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("EMI CALCULATOR");
//            Intent myIntent = new Intent(getApplicationContext(), EMICalculator.class);
//            startActivityForResult(myIntent, 0);
        }
        else {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("EMI CALCULATOR");
        }
        else
            super.onBackPressed();
    }
}
