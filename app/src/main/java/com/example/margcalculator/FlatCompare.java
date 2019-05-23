package com.example.margcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static java.lang.Math.pow;

public class FlatCompare extends Fragment {
    private Button compareCalculate;
    private Button compareReset;
    private Double Amount;
    private Double Interest1;
    private Double Interest2;
    private Integer Period1;
    private Integer Period2;
    private Double EMI1;
    private Double EMI2;
    private Double TotalAmount1;
    private Double TotalAmount2;
    private Double TotalInterest1;
    private Double TotalInterest2;
    private Double Processing_fees1;
    private Double Processing_fees2;
    private Double years1=0.0;
    private Double years2=0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View myView = inflater.inflate(R.layout.flat_compare_emi, container, false);
        hideKeyboardFrom(getContext(), myView);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Flat Interest");
        final InterstitialAd mInterstitialAd = EMICalculator.getAd();
        compareCalculate = myView.findViewById(R.id.Fcompare_calculate);
        compareCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            EMICalculator.setAd(mInterstitialAd);
                        }
                    });
                    MainActivity.count=0;
                }
                EditText amount = myView.findViewById(R.id.Fcompare_amount_input);
                EditText interest1 = myView.findViewById(R.id.Fcompare_interest_input1);
                EditText interest2 = myView.findViewById(R.id.Fcompare_interest_input2);
                EditText period1 = myView.findViewById(R.id.Fcompare_period_input1);
                EditText period2 = myView.findViewById(R.id.Fcompare_period_input2);
                EditText processing_fees1 = myView.findViewById(R.id.Fcompare_processing_fees_input1);
                EditText processing_fees2 = myView.findViewById(R.id.Fcompare_processing_fees_input2);
                final RadioGroup radioGroup = myView.findViewById(R.id.FPeriodSelector);

                if(!amount.getText().toString().isEmpty() && !interest1.getText().toString().isEmpty() && !interest2.getText().toString().isEmpty()
                        && !period1.getText().toString().isEmpty() && !period2.getText().toString().isEmpty()
                        && !processing_fees1.getText().toString().isEmpty() && !processing_fees2.getText().toString().isEmpty()
                        && radioGroup.getCheckedRadioButtonId()!=-1) {
                    RadioButton selectedPeriod = myView.findViewById(radioGroup.getCheckedRadioButtonId());
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest1 = Double.valueOf(interest1.getText().toString());
                    Interest2 = Double.valueOf(interest2.getText().toString());
                    double temp1 = Double.valueOf(period1.getText().toString());
                    double temp2 = Double.valueOf(period2.getText().toString());
                    if (selectedPeriod.getText().equals("YR")) {
                        temp1*=12;
                        temp2*=12;
                        Period1 = (int)temp1;
                        Period2 = (int)temp2;
                        years1 = (double) (Period1 / 12);
                        years2 = (double) (Period2 / 12);
                    }
                    else{
                        Period1 = (int)temp1;
                        Period2 = (int)temp2;
                    }
                    Processing_fees1 = Double.valueOf(processing_fees1.getText().toString());
                    Processing_fees2 = Double.valueOf(processing_fees2.getText().toString());

                    if (Period1 == 0 || Period2 == 0) {
                        Toast.makeText(getContext(), "Period cannot be 0", Toast.LENGTH_SHORT).show();
                    } else {

                        Double scale = Math.pow(10, 2);
                        Processing_fees1 = (Processing_fees1 * Amount) / 100.0;
                        Processing_fees2 = (Processing_fees2 * Amount) / 100.0;
                        TotalInterest1 = (Interest1 * Amount * (Period1 / 12.00)) / 100.0;
                        TotalInterest2 = (Interest2 * Amount * (Period2 / 12.00)) / 100.0;
                        EMI1 = ComputeEMI(Amount, TotalInterest1, Period1);
                        EMI2 = ComputeEMI(Amount, TotalInterest2, Period2);
                        TotalAmount1 = EMI1 * Period1;
                        TotalAmount2 = EMI2 * Period2;

                        Processing_fees1 = Math.round(Processing_fees1 * scale) / scale;
                        Processing_fees2 = Math.round(Processing_fees2 * scale) / scale;
                        TotalInterest1 = Math.round(TotalInterest1 * scale) / scale;
                        TotalInterest2 = Math.round(TotalInterest2 * scale) / scale;
                        EMI1 = Math.round(EMI1 * scale) / scale;
                        EMI2 = Math.round(EMI2 * scale) / scale;
                        TotalAmount1 = Math.round(TotalAmount1 * scale) / scale;
                        TotalAmount2 = Math.round(TotalAmount2 * scale) / scale;

                        Bundle args = new Bundle();
                        args.putDouble("loan_amount", Amount);
                        args.putDouble("interest1", Interest1);
                        args.putDouble("interest2", Interest2);
                        args.putInt("period1", Period1);
                        args.putInt("period2", Period2);
                        args.putDouble("processing_fees1", Processing_fees1);
                        args.putDouble("processing_fees2", Processing_fees2);
                        args.putDouble("EMI1", EMI1);
                        args.putDouble("EMI2", EMI2);
                        args.putDouble("total_interest1", TotalInterest1);
                        args.putDouble("total_interest2", TotalInterest2);
                        args.putDouble("total_amount1", TotalAmount1);
                        args.putDouble("total_amount2", TotalAmount2);
                        args.putString("type", "Reducing");
                        if (years1 != 0 && years2 != 0) {
                            args.putDouble("years1", years1);
                            args.putDouble("years2", years2);
                        }
                        EMICompareOutput emiCompareOutput = new EMICompareOutput();
                        emiCompareOutput.setArguments(args);
                        assert getFragmentManager() != null;
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.tab_viewer, emiCompareOutput);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        hideKeyboardFrom(getContext(), myView);
                    }
                }
                else{
                    Toast.makeText(getContext(),"Please fill all the Details",Toast.LENGTH_SHORT).show();

                }
            }
        });
        compareReset  = myView.findViewById(R.id.Fcompare_reset);
        compareReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = myView.findViewById(R.id.Fcompare_amount_input);
                EditText interest1 = myView.findViewById(R.id.Fcompare_interest_input1);
                EditText interest2 = myView.findViewById(R.id.Fcompare_interest_input2);
                EditText period1 = myView.findViewById(R.id.Fcompare_period_input1);
                EditText period2 = myView.findViewById(R.id.Fcompare_period_input2);
                EditText processing_fees1 = myView.findViewById(R.id.Fcompare_processing_fees_input1);
                EditText processing_fees2 = myView.findViewById(R.id.Fcompare_processing_fees_input2);
                RadioButton year = myView.findViewById(R.id.Fradio_year);
                RadioButton month = myView.findViewById(R.id.Fradio_month);
                year.setChecked(true);
                month.setChecked(false);
                amount.setText("");
                interest1.setText("");
                interest2.setText("");
                processing_fees1.setText("");
                processing_fees2.setText("");
                period1.setText("");
                period2.setText("");

                EMI1 = 0.0;
                EMI2 = 0.0;
                TotalAmount1 = 0.0;
                TotalAmount2 = 0.0;
                TotalInterest1 = 0.0;
                TotalInterest2 =0.0;
                Processing_fees1 = 0.0;
                Processing_fees2 = 0.0;
                
            }
        });
        return myView;
    }
    private Double ComputeEMI(Double amount, Double interest, Integer period){
        Double EMI = (amount + interest)/period;
        return EMI;
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
