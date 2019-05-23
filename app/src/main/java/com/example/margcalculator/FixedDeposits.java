package com.example.margcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class FixedDeposits extends Fragment {
    private View view;
    private Spinner interestFrequency;
    private Button calculate;
    private Button reset;
    private Button compare;
    private Double DepositAmount=0.0;
    private Double InterestRate=0.0;
    private Integer InterestFrequency=0;
    private Integer Tenure=0;
    private Double MaturityAmount=0.0;
    private  Bundle args = new Bundle();
    private double scale = Math.pow(10,2);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fixed_deposits, container, false);
        hideKeyboardFrom(getContext(), view);
        final InterstitialAd mInterstitialAd = InterestReturnCalculator.getAd();
        interestFrequency = view.findViewById(R.id.Fdeposit_interest_frequency_input);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.interest_frequency_list,R.layout.spinner_item);
        interestFrequency.setAdapter(adapter);
        calculate = view.findViewById(R.id.Fdeposit_calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getContext(), view);
                MainActivity.count++;
                if(MainActivity.count==MainActivity.MAX_REQ) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        mInterstitialAd.show();
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                            Toast.makeText(getContext(), errorCode + ": Inventory in creation", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            InterestReturnCalculator.setAd(mInterstitialAd);
                        }
                    });
                    MainActivity.count=0;
                }
                EditText depositAmount = view.findViewById(R.id.Fdeposit_amount_input);
                EditText interestRate = view.findViewById(R.id.Fdeposit_interest_input);
                EditText tenure = view.findViewById(R.id.Fdeposit_period_input);

                if(!depositAmount.getText().toString().isEmpty() && !interestRate.getText().toString().isEmpty() && !tenure.getText().toString().isEmpty()){

                    DepositAmount = Double.valueOf(depositAmount.getText().toString());
                    InterestRate = Double.valueOf(interestRate.getText().toString());
                    Tenure = Integer.valueOf(tenure.getText().toString());
                    if(Tenure==0){
                        Toast.makeText(getContext(),"Please enter a valid Tenure",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (interestFrequency.getSelectedItem().equals("Monthly")) {
                            InterestFrequency = 1;
                            args.putString("frequency", "Monthly");
                        } else if (interestFrequency.getSelectedItem().equals("Quarterly")) {
                            InterestFrequency = 3;
                            args.putString("frequency", "Quarterly");
                        } else if (interestFrequency.getSelectedItem().equals("Half-Yearly")) {
                            InterestFrequency = 6;
                            args.putString("frequency", "Half-Yearly");
                        } else if (interestFrequency.getSelectedItem().equals("Yearly")) {
                            InterestFrequency = 12;
                            args.putString("frequency", "Yearly");
                        }
                        int x = (Tenure / InterestFrequency);
                        MaturityAmount = DepositAmount * Math.pow((1 + (InterestRate / ((12 / InterestFrequency) * 100))), (x));
                        x *= InterestFrequency;
                        if (Tenure - x > 0) {
                            Double extraInterest = MaturityAmount * (Tenure - (x)) * (InterestRate / (12 * 100));
                            MaturityAmount += extraInterest;
                            System.out.println("Maturity Amount: " + MaturityAmount);
                        }
                        MaturityAmount = Math.round(MaturityAmount * scale) / scale;
                        args.putDouble("DepositAmount", DepositAmount);
                        args.putDouble("InterestRate", InterestRate);
                        args.putInt("Tenure", Tenure);
                        args.putDouble("MaturityAmount", MaturityAmount);
                        args.putString("type", "Fixed");
                        FDOutput fdOutput = new FDOutput();
                        fdOutput.setArguments(args);
                        assert getFragmentManager() != null;
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.ir_tab_viewer, fdOutput);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else
                    Toast.makeText(getContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();
            }
        });
        reset = view.findViewById(R.id.Fdeposit_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText depositAmount = view.findViewById(R.id.Fdeposit_amount_input);
                EditText interestRate = view.findViewById(R.id.Fdeposit_interest_input);
                EditText tenure = view.findViewById(R.id.Fdeposit_period_input);
                interestFrequency.setSelection(0);
                depositAmount.setText("");
                interestRate.setText("");
                tenure.setText("");
                DepositAmount =0.0;
                InterestRate = 0.0;
                InterestFrequency = 0;
                MaturityAmount=0.0;
            }
        });

        compare = view.findViewById(R.id.Fdeposit_compare);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("type","Fixed");
                InterestReturnCompare interestReturnCompare = new InterestReturnCompare();
                interestReturnCompare.setArguments(args);
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.ir_tab_viewer, interestReturnCompare);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();
                if(manager!=null){
                    onFragmentResume();
                }
            }
        });
        return view;
    }
    private void onFragmentResume() {
        EditText depositAmount = view.findViewById(R.id.Fdeposit_amount_input);
        EditText interestRate = view.findViewById(R.id.Fdeposit_interest_input);
        EditText tenure = view.findViewById(R.id.Fdeposit_period_input);
        if(DepositAmount!=0 && InterestRate!=0.0 && Tenure!=0) {
            depositAmount.setText(DepositAmount.toString());
            interestRate.setText(InterestRate.toString());
            tenure.setText(Tenure.toString());
        }
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
