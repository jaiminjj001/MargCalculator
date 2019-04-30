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


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import static java.lang.Math.pow;

public class ReducingInterest extends Fragment {

    View view;
    Button Calculate;
    Button Reset;
    Button Compare;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.reducing_interest, container, false);
        Calculate = view.findViewById(R.id.calculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.amount_input);
                EditText interest = view.findViewById(R.id.interest_input);
                EditText period = view.findViewById(R.id.period_input);
                EditText processing_fee = view.findViewById(R.id.processing_fees_input);
                EditText emi;

                if(!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty() && !period.getText().toString().isEmpty()){
                Double Amount = Double.valueOf(amount.getText().toString());
                Double Interest = Double.valueOf(interest.getText().toString());
                Double Period = Double.valueOf(period.getText().toString());
                Double Processing_fee = Double.valueOf(processing_fee.getText().toString());

                Processing_fee =(Processing_fee * Amount)/100;
                Double EMI = ComputeEMI(Amount,Interest,Period);
                Double TotalAmount = EMI*Period;
                Double TotalInterest = (TotalAmount - Amount);

                TextView Oamount = view.findViewById(R.id.total_amount_output);
                TextView Ointerest = view.findViewById(R.id.total_interest_output);
                TextView Oemi = view.findViewById(R.id.EMI_output);
                TextView Oprocessing_fee = view.findViewById(R.id.processing_fees_output);

                Oamount.setText(String.format("%f",TotalAmount));
                Ointerest.setText(String.format("%f",TotalInterest));
                Oemi.setText(String.format("%f",EMI));
                Oprocessing_fee.setText(String.format("%f",Processing_fee));
                hideKeyboardFrom(getContext(),view);
            }}
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
            }
        });

        Compare = view.findViewById(R.id.compare);
        Compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.view_pager, new CompareEMI());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;

    }
    private void swapFragment(){
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
