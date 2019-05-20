package com.example.margcalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

public class InterestReturnCalculator extends AppCompatActivity {
    private static InterstitialAd mInterstitialAd;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_return_calculator);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Toolbar toolbar = findViewById(R.id.ir_toolbar);
        toolbar.setTitle("Interest/Return Calculator");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(this, "ca-app-pub-4198388995953911~2398672403");
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if(errorCode==3)
                    Toast.makeText(getApplicationContext(), errorCode +": Inventory in creation",Toast.LENGTH_SHORT).show();
            }
        });
        TabLayout tabLayout = findViewById(R.id.ir_tab_layout);
        ViewPager viewPager = findViewById(R.id.ir_view_pager);
        InterestReturnAdapter adapter = new InterestReturnAdapter(getSupportFragmentManager());
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
            Toolbar toolbar = findViewById(R.id.ir_toolbar);
            toolbar.setTitle("Interest/Return Calculator");
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
            Toolbar toolbar = findViewById(R.id.ir_toolbar);
            toolbar.setTitle("Interest/Return Calculator");
        }
        else
            super.onBackPressed();
    }
}
