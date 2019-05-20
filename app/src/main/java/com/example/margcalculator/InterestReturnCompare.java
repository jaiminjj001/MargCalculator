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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterestReturnCompare extends Fragment {
    private View view;
    Bundle Rargs = null;
    Bundle args = null;
    private Button calculate;
    private Button reset;
    private Spinner interestFrequency1;
    private Spinner interestFrequency2;
    private Double DepositAmount;
    private Double RecurringAmount;
    private Double InterestRate1;
    private Double InterestRate2;
    private Integer Tenure1;
    private Integer Tenure2;
    private Integer InterestFrequency1;
    private Integer InterestFrequency2;
    private Double MaturityAmount1=0.0;
    private Double MaturityAmount2=0.0;
    private double scale = Math.pow(10,2);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.interest_return_compare, container, false);
        hideKeyboardFrom(getContext(), view);
        final InterstitialAd mInterstitialAd = EMICalculator.getAd();
        Rargs = new Bundle();
        Rargs = getArguments();
        args = new Bundle();
        EditText amount = view.findViewById(R.id.irc_compare_amount_input);
        TextView amountField = view.findViewById(R.id.irc_compare_amount);
        Toolbar toolbar = getActivity().findViewById(R.id.ir_toolbar);
        if(Rargs.getString("type").equals("Recurring")){
            amount.setHint(R.string.recurring_amount_input);
            amountField.setText(R.string.recurring_amount);
            toolbar.setTitle("Recurring Deposit");
        }
        else if(Rargs.getString("type").equals("Fixed")){
            amount.setHint(R.string.deposit_amount_input);
            amountField.setText(R.string.deposit_amount);
            toolbar.setTitle("Fixed Deposit");
        }
        interestFrequency1 = view.findViewById(R.id.irc_compare_interest_frequency_input1);
        interestFrequency2 = view.findViewById(R.id.irc_compare_interest_frequency_input2);
        final ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.interest_frequency_list,R.layout.spinner_item);
        final ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.interest_frequency_list,R.layout.spinner_item);
        interestFrequency1.setAdapter(adapter1);
        interestFrequency2.setAdapter(adapter2);

        calculate =view.findViewById(R.id.irc_compare_calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
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

                MaturityAmount1=0.0;
                MaturityAmount2=0.0;
                EditText amount = view.findViewById(R.id.irc_compare_amount_input);
                EditText interestRate1 = view.findViewById(R.id.irc_compare_interest_input1);
                EditText interestRate2 = view.findViewById(R.id.irc_compare_interest_input2);
                EditText tenure1 = view.findViewById(R.id.irc_compare_tenure_input1);
                EditText tenure2 = view.findViewById(R.id.irc_compare_tenure_input2);

                if(!amount.getText().toString().isEmpty() && !interestRate1.getText().toString().isEmpty() && !interestRate2.getText().toString().isEmpty()
                    && !tenure1.getText().toString().isEmpty() && !tenure2.getText().toString().isEmpty()) {
                    InterestRate1 = Double.valueOf(interestRate1.getText().toString());
                    InterestRate2 = Double.valueOf(interestRate2.getText().toString());
                    Tenure1 = Integer.valueOf(tenure1.getText().toString());
                    Tenure2 = Integer.valueOf(tenure2.getText().toString());
                    if(Tenure1==0 || Tenure2==0){
                        Toast.makeText(getContext(),"Please enter a valid Tenure",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (interestFrequency1.getSelectedItem().equals("Monthly")) {
                            InterestFrequency1 = 1;
                            args.putString("frequency1", "Monthly");
                        } else if (interestFrequency1.getSelectedItem().equals("Quarterly")) {
                            InterestFrequency1 = 3;
                            args.putString("frequency1", "Quarterly");
                        } else if (interestFrequency1.getSelectedItem().equals("Half-Yearly")) {
                            InterestFrequency1 = 6;
                            args.putString("frequency1", "Half-Yearly");
                        } else if (interestFrequency1.getSelectedItem().equals("Yearly")) {
                            InterestFrequency1 = 12;
                            args.putString("frequency1", "Yearly");
                        }


                        if (interestFrequency2.getSelectedItem().equals("Monthly")) {
                            InterestFrequency2 = 1;
                            args.putString("frequency2", "Monthly");
                        } else if (interestFrequency2.getSelectedItem().equals("Quarterly")) {
                            InterestFrequency2 = 3;
                            args.putString("frequency2", "Quarterly");
                        } else if (interestFrequency2.getSelectedItem().equals("Half-Yearly")) {
                            InterestFrequency2 = 6;
                            args.putString("frequency2", "Half-Yearly");
                        } else if (interestFrequency2.getSelectedItem().equals("Yearly")) {
                            InterestFrequency2 = 12;
                            args.putString("frequency2", "Yearly");
                        }

                        int compoundFreq1 = 12 / InterestFrequency1;
                        int compoundFreq2 = 12 / InterestFrequency2;

                        if (Rargs.getString("type").equals("Recurring")) {
                            RecurringAmount = Double.valueOf(amount.getText().toString());
                            args.putDouble("RecurringAmount", RecurringAmount);
                            for (int i = Tenure1; i > 0; i--) {
                                MaturityAmount1 += RecurringAmount * Math.pow((1 + (InterestRate1 / (100 * compoundFreq1))), ((compoundFreq1 * (double) i) / 12));
                            }
                            for (int i = Tenure1; i > 0; i--) {
                                MaturityAmount2 += RecurringAmount * Math.pow((1 + (InterestRate2 / (100 * compoundFreq2))), ((compoundFreq2 * (double) i) / 12));
                            }
                        } else if (Rargs.getString("type").equals("Fixed")) {
                            DepositAmount = Double.valueOf(amount.getText().toString());
                            args.putDouble("DepositAmount", DepositAmount);
                            int x1 = (Tenure1 / InterestFrequency1);
                            MaturityAmount1 = DepositAmount * Math.pow((1 + (InterestRate1 / (compoundFreq1 * 100))), (x1));
                            x1 *= InterestFrequency1;
                            if (Tenure1 - x1 > 0) {
                                Double extraInterest = MaturityAmount1 * (Tenure1 - (x1)) * (InterestRate1 / (12 * 100));
                                MaturityAmount1 += extraInterest;
                                System.out.println("Maturity Amount: " + MaturityAmount1);
                            }
                            MaturityAmount1 = Math.round(MaturityAmount1 * scale) / scale;

                            int x2 = (Tenure2 / InterestFrequency2);
                            MaturityAmount2 = DepositAmount * Math.pow((1 + (InterestRate2 / (compoundFreq2 * 100))), (x2));
                            x2 *= InterestFrequency2;
                            if (Tenure2 - x2 > 0) {
                                Double extraInterest = MaturityAmount2 * (Tenure2 - (x2)) * (InterestRate2 / (12 * 100));
                                MaturityAmount2 += extraInterest;
                                System.out.println("Maturity Amount: " + MaturityAmount2);
                            }
                            MaturityAmount2 = Math.round(MaturityAmount2 * scale) / scale;
                        }
                        args.putDouble("interest1", InterestRate1);
                        args.putDouble("interest2", InterestRate2);
                        args.putInt("tenure1", Tenure1);
                        args.putInt("tenure2", Tenure2);
                        args.putDouble("maturityAmount1", MaturityAmount1);
                        args.putDouble("maturityAmount2", MaturityAmount2);
                        IRCompareOutput irCompareOutput = new IRCompareOutput();
                        irCompareOutput.setArguments(args);
                        assert getFragmentManager() != null;
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.ir_tab_viewer, irCompareOutput);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else
                    Toast.makeText(getContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();
            }
        });

        reset = view.findViewById(R.id.irc_compare_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.irc_compare_amount_input);
                EditText interestRate1 = view.findViewById(R.id.irc_compare_interest_input1);
                EditText interestRate2 = view.findViewById(R.id.irc_compare_interest_input2);
                EditText tenure1 = view.findViewById(R.id.irc_compare_tenure_input1);
                EditText tenure2 = view.findViewById(R.id.irc_compare_tenure_input2);
                interestFrequency1.setSelection(0);
                interestFrequency2.setSelection(0);
                amount.setText("");
                interestRate1.setText("");
                interestRate2.setText("");
                tenure1.setText("");
                tenure2.setText("");
            }
        });
        return view;
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
