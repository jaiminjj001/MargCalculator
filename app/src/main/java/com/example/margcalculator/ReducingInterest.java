package com.example.margcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.fragment.app.Fragment;

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

                int Amount = Integer.parseInt(amount.getText().toString());
                int Interest = Integer.parseInt(interest.getText().toString());;
                int Period = Integer.parseInt(period.getText().toString());;
                int Processing_fee = Integer.parseInt(processing_fee.getText().toString());
            }
        });
        return view;

    }
}
