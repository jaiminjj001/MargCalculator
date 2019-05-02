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
import androidx.fragment.app.FragmentTransaction;

import static java.lang.Math.pow;

public class ReducingInterest extends Fragment {

    View view;
    Button Calculate;
    Button Reset;
    Button Compare;
    Button EMIDetails;
    Double EMI=0.0;
    Double TotalAmount=0.0;
    Double TotalInterest=0.0;
    Double Processing_fee=0.0;
    Double Period;
    Double Interest;
    Double Amount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reducing_interest, container, false);

        EMI = 0.0;
        TotalAmount = 0.0;
        TotalInterest = 0.0;
        Processing_fee = 0.0;
        Calculate = view.findViewById(R.id.calculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.amount_input);
                EditText interest = view.findViewById(R.id.interest_input);
                EditText period = view.findViewById(R.id.period_input);
                EditText processing_fee = view.findViewById(R.id.processing_fees_input);
                EditText emi;

                if (!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty()
                        && !period.getText().toString().isEmpty() && !processing_fee.getText().toString().isEmpty()) {
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest = Double.valueOf(interest.getText().toString());
                    Period= Double.valueOf(period.getText().toString());
                    Processing_fee = Double.valueOf(processing_fee.getText().toString());

                    Processing_fee = (Processing_fee * Amount) / 100;
                    EMI = ComputeEMI(Amount, Interest, Period);
                    TotalAmount = EMI * Period;
                    TotalInterest = (TotalAmount - Amount);

                    TextView Total_Amount_output = view.findViewById(R.id.total_amount_output);
                    TextView Total_Interest_output = view.findViewById(R.id.total_interest_output);
                    TextView EMI_PerMonth = view.findViewById(R.id.EMI_output);
                    TextView Processing_fees_output = view.findViewById(R.id.processing_fees_output);

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

        Reset = view.findViewById(R.id.reset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.amount_input);
                EditText interest = view.findViewById(R.id.interest_input);
                EditText period = view.findViewById(R.id.period_input);
                EditText processing_fee = view.findViewById(R.id.processing_fees_input);
                TextView Oamount = view.findViewById(R.id.total_amount_output);
                TextView Ointerest = view.findViewById(R.id.total_interest_output);
                TextView Oemi = view.findViewById(R.id.EMI_output);
                TextView Oprocessing_fee = view.findViewById(R.id.processing_fees_output);
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

        Compare = view.findViewById(R.id.compare);
        Compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.tab_viewer,new ReducingCompare());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        EMIDetails =view.findViewById(R.id.emi_details);
        EMIDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMITable emiTable = new EMITable();
                Bundle args = new Bundle();
                if(EMI!=0.0 && TotalAmount!=0.0){
                    args.putDouble("EMI",EMI);
                    args.putDouble("TotalAmount",TotalAmount);
                    args.putDouble("Interest",Interest);
                    args.putDouble("Period",Period);
                    args.putDouble("Amount",Amount);
                    emiTable.setArguments(args);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.tab_viewer, emiTable);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"Please Calculate EMI first",Toast.LENGTH_SHORT).show();

                }

            }
        });
        return view;

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public Double ComputeEMI(Double amount, Double interest, Double period){
        interest = interest/(12*100);
        Double EMI = (amount * interest * (pow((1+interest),period)))/((pow((1+interest),period))-1);
        return EMI;
    }
}
