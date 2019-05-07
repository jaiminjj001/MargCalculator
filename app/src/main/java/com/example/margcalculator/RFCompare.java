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

import static java.lang.Math.pow;

public class RFCompare extends Fragment {
    Button compareCalculate;
    Button compareReset;
    Double Amount;
    Double Interest1;
    Double Interest2;
    Double Period1;
    Double Period2;
    Double Processing_fees1;
    Double Processing_fees2;
    Double EMI1=0.0;
    Double EMI2 = 0.0;
    Double TotalAmount1=0.0;
    Double TotalAmount2 = 0.0;
    Double TotalInterest1 = 0.0;
    Double TotalInterest2 = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View myView = inflater.inflate(R.layout.rf_compare, container, false);
        compareCalculate = myView.findViewById(R.id.RFcompare_calculate);
        compareCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = myView.findViewById(R.id.RFcompare_amount_input);
                EditText interest1 = myView.findViewById(R.id.RFcompare_interest_input1);
                EditText interest2 = myView.findViewById(R.id.RFcompare_interest_input2);
                EditText period1 = myView.findViewById(R.id.RFcompare_period_input1);
                EditText period2 = myView.findViewById(R.id.RFcompare_period_input2);
                EditText processing_fees1 = myView.findViewById(R.id.RFcompare_processing_fees_input1);
                EditText processing_fees2 = myView.findViewById(R.id.RFcompare_processing_fees_input2);
                if(!amount.getText().toString().isEmpty() && !interest1.getText().toString().isEmpty() && !interest2.getText().toString().isEmpty()
                        && !period1.getText().toString().isEmpty() && !period2.getText().toString().isEmpty()
                        && !processing_fees1.getText().toString().isEmpty() && !processing_fees2.getText().toString().isEmpty())
                {
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest1= Double.valueOf(interest1.getText().toString());
                    Interest2 = Double.valueOf(interest2.getText().toString());
                    Period1 = Double.valueOf(period1.getText().toString());
                    Period2 = Double.valueOf(period2.getText().toString());
                    Processing_fees1 = Double.valueOf(processing_fees1.getText().toString());
                    Processing_fees2 = Double.valueOf(processing_fees2.getText().toString());

                    double scale = Math.pow(10,2);
                    Processing_fees1 = (Processing_fees1 * Amount) / 100;
                    Processing_fees1 = Math.round(Processing_fees1 * scale) / scale;
                    EMI1= ComputeREMI(Amount,Interest1,Period1);
                    EMI1 = Math.round(EMI1 * scale) / scale;
                    TotalAmount1 = EMI1 * Period1;
                    TotalAmount1 = Math.round(TotalAmount1 * scale) / scale;
                    TotalInterest1 = (TotalAmount1 - Amount);
                    TotalInterest1 = Math.round(TotalInterest1 * scale) / scale;

                    Processing_fees2 = (Processing_fees2 * Amount) / 100;
                    Processing_fees2 = Math.round(Processing_fees2 * scale) / scale;
                    TotalInterest2 = (Interest2*Amount*(Period2/12))/100;
                    TotalInterest2 = Math.round(TotalInterest2 * scale) / scale;
                    EMI2 = ComputeFEMI(Amount, TotalInterest2, Period2);
                    EMI2 = Math.round(EMI2 * scale) / scale;
                    TotalAmount2 = TotalInterest2 + Amount;
                    TotalAmount2 = Math.round(TotalAmount2 * scale) / scale;


                    TextView EMI_perMonth1 = myView.findViewById(R.id.RFcompare_EMI_output1);
                    TextView EMI_perMonth2 = myView.findViewById(R.id.RFcompare_EMI_output2);
                    TextView Processing_fees_output1 = myView.findViewById(R.id.RFcompare_processing_fees_output1);
                    TextView Processing_fees_output2 = myView.findViewById(R.id.RFcompare_processing_fees_output2);
                    TextView Total_interest_output1 = myView.findViewById(R.id.RFcompare_total_interest_output1);
                    TextView Total_interest_output2 = myView.findViewById(R.id.RFcompare_total_interest_output2);
                    TextView Total_amount_output1 = myView.findViewById(R.id.RFcompare_total_amount_output1);
                    TextView Total_amount_output2 = myView.findViewById(R.id.RFcompare_total_amount_output2);

                    EMI_perMonth1.setText(EMI1.toString());
                    EMI_perMonth2.setText(EMI2.toString());
                    Processing_fees_output1.setText(Processing_fees1.toString());
                    Processing_fees_output2.setText(Processing_fees2.toString());
                    Total_interest_output1.setText(TotalInterest1.toString());
                    Total_interest_output2.setText(TotalInterest2.toString());
                    Total_amount_output1.setText(TotalAmount1.toString());
                    Total_amount_output2.setText(TotalAmount2.toString());
                    hideKeyboardFrom(getContext(), myView);

                }
                else{
                    Toast.makeText(getContext(),"Please fill all the Details",Toast.LENGTH_SHORT).show();
                }
                compareReset = myView.findViewById(R.id.RFcompare_reset);
                compareReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText amount = myView.findViewById(R.id.RFcompare_amount_input);
                        EditText interest1 = myView.findViewById(R.id.RFcompare_interest_input1);
                        EditText interest2 = myView.findViewById(R.id.RFcompare_interest_input2);
                        EditText period1 = myView.findViewById(R.id.RFcompare_period_input1);
                        EditText period2 = myView.findViewById(R.id.RFcompare_period_input2);
                        EditText processing_fees1 = myView.findViewById(R.id.RFcompare_processing_fees_input1);
                        EditText processing_fees2 = myView.findViewById(R.id.RFcompare_processing_fees_input2);

                        TextView EMI_perMonth1 = myView.findViewById(R.id.RFcompare_EMI_output1);
                        TextView EMI_perMonth2 = myView.findViewById(R.id.RFcompare_EMI_output2);
                        TextView Processing_fees_output1 = myView.findViewById(R.id.RFcompare_processing_fees_output1);
                        TextView Processing_fees_output2 = myView.findViewById(R.id.RFcompare_processing_fees_output2);
                        TextView Total_interest_output1 = myView.findViewById(R.id.RFcompare_total_interest_output1);
                        TextView Total_interest_output2 = myView.findViewById(R.id.RFcompare_total_interest_output2);
                        TextView Total_amount_output1 = myView.findViewById(R.id.RFcompare_total_amount_output1);
                        TextView Total_amount_output2 = myView.findViewById(R.id.RFcompare_total_amount_output2);

                        amount.setText("");
                        interest1.setText("");
                        interest2.setText("");
                        processing_fees1.setText("");
                        processing_fees2.setText("");
                        period1.setText("");
                        period2.setText("");

                        EMI_perMonth1.setText("");
                        EMI_perMonth2.setText("");
                        Processing_fees_output1.setText("");
                        Processing_fees_output2.setText("");
                        Total_interest_output1.setText("");
                        Total_interest_output2.setText("");
                        Total_amount_output1.setText("");
                        Total_amount_output2.setText("");

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
            }
        });
        return myView;

    }
    private Double ComputeREMI(Double amount, Double interest, Double period){
        interest = interest/(12*100);
        Double EMI = (amount * interest * (pow((1+interest),period)))/((pow((1+interest),period))-1);
        return EMI;
    }
    private Double ComputeFEMI(Double amount, Double interest, Double period){
        Double EMI = (amount+interest)/period;
        return EMI;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
