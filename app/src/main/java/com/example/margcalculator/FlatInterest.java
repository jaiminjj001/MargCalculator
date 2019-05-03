package com.example.margcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FlatInterest extends Fragment {
    View view;
    Button Calculate;
    Button Reset;
    Button Compare;
    Button EMIDetails;
    Double EMI=0.0;
    Double TotalAmount=0.0;
    Double TotalInterest=0.0;
    Double Processing_fee=0.0;
    Double Period = 0.0;
    Double Interest = 0.0;
    Double Amount = 0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.flat_interest, container, false);
        EMI = 0.0;
        TotalAmount = 0.0;
        TotalInterest = 0.0;
        Processing_fee = 0.0;
        Calculate = view.findViewById(R.id.Fcalculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.Famount_input);
                EditText interest = view.findViewById(R.id.Finterest_input);
                EditText period = view.findViewById(R.id.Fperiod_input);
                EditText processing_fee = view.findViewById(R.id.Fprocessing_fees_input);
                EditText emi;

                if (!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty()
                        && !period.getText().toString().isEmpty() && !processing_fee.getText().toString().isEmpty()) {
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest = Double.valueOf(interest.getText().toString());
                    Period= Double.valueOf(period.getText().toString());
                    Processing_fee = Double.valueOf(processing_fee.getText().toString());

                    Processing_fee = (Processing_fee * Amount) / 100;
                    TotalInterest = (Interest*Amount*(Period/12))/100;
                    EMI = ComputeEMI(Amount, TotalInterest, Period);
                    TotalAmount = TotalInterest + Amount;

                    TextView Total_Amount_output = view.findViewById(R.id.Ftotal_amount_output);
                    TextView Total_Interest_output = view.findViewById(R.id.Ftotal_interest_output);
                    TextView EMI_PerMonth = view.findViewById(R.id.FEMI_output);
                    TextView Processing_fees_output = view.findViewById(R.id.Fprocessing_fees_output);

                    Total_Amount_output.setText(String.format("%f", TotalAmount));
                    Total_Interest_output.setText(String.format("%f", TotalInterest));
                    EMI_PerMonth.setText(String.format("%f", EMI));
                    Processing_fees_output.setText(String.format("%f", Processing_fee));
                    hideKeyboardFrom(getContext(), view);
                }
                else{
                    Toast.makeText(getContext(),"Please Fill All the Details",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Reset = view.findViewById(R.id.Freset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.Famount_input);
                EditText interest = view.findViewById(R.id.Finterest_input);
                EditText period = view.findViewById(R.id.Fperiod_input);
                EditText processing_fee = view.findViewById(R.id.Fprocessing_fees_input);
                TextView Oamount = view.findViewById(R.id.Ftotal_amount_output);
                TextView Ointerest = view.findViewById(R.id.Ftotal_interest_output);
                TextView Oemi = view.findViewById(R.id.FEMI_output);
                TextView Oprocessing_fee = view.findViewById(R.id.Fprocessing_fees_output);
                Oamount.setText("");
                Ointerest.setText("");
                Oemi.setText("");
                Oprocessing_fee.setText("");
                amount.setText("");
                interest.setText("");
                period.setText("");
                processing_fee.setText("");
                EMI = 0.0;
                TotalAmount = 0.0;
                TotalInterest = 0.0;
                Processing_fee = 0.0;

            }
        });

        Compare = view.findViewById(R.id.Fcompare);
        Compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.tab_viewer,new FlatCompare());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        EMIDetails =view.findViewById(R.id.Femi_details);
        EMIDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMITable flatEmiTable = new EMITable();
                Bundle args = new Bundle();
                if(EMI!=0.0 && TotalAmount!=0.0){
                    args.putDouble("EMI",EMI);
                    args.putDouble("TotalAmount",TotalAmount);
                    args.putDouble("Interest",Interest);
                    args.putDouble("Period",Period);
                    args.putDouble("Amount",Amount);
                    args.putString("EMI Type","Flat");
                    flatEmiTable.setArguments(args);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.tab_viewer, flatEmiTable);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"Please Calculate EMI first",Toast.LENGTH_SHORT).show();

                }
            }
        });

        //retain values
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
        if (Amount != 0.0 && Processing_fee != 0.0 && Interest != 0.0 && Period != 0.0) {
            EditText amount = view.findViewById(R.id.Famount_input);
            EditText interest = view.findViewById(R.id.Finterest_input);
            EditText period = view.findViewById(R.id.Fperiod_input);
            EditText processing_fee = view.findViewById(R.id.Fprocessing_fees_input);
            amount.setText(Amount.toString());
            interest.setText(Interest.toString());
            period.setText(Period.toString());
            processing_fee.setText(Processing_fee.toString());
        }
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private Double ComputeEMI(Double amount, Double interest, Double period){
        Double EMI = (amount+interest)/period;
        return EMI;
    }
}
