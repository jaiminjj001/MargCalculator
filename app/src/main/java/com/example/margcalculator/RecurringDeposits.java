package com.example.margcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RecurringDeposits extends Fragment {
    private View view;
    private Spinner interestFrequency;
    private Button calculate;
    private Button reset;
    private Button compare;
    private Double RecurringAmount = 0.0;
    private Double InterestRate = 0.0;
    private Integer InterestFrequency;
    private Integer Tenure;
    private Double MaturityAmount=0.0;
    private Bundle args = new Bundle();
    private double scale = Math.pow(10,2);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recurring_deposits, container, false);
        interestFrequency = view.findViewById(R.id.Rdeposit_interest_frequency_input);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.interest_frequency_list,R.layout.spinner_item);
        interestFrequency.setAdapter(adapter);

        calculate = view.findViewById(R.id.Rdeposit_calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaturityAmount=0.0;
                EditText depositAmount = view.findViewById(R.id.Rdeposit_amount_input);
                EditText interestRate = view.findViewById(R.id.Rdeposit_interest_input);
                EditText tenure = view.findViewById(R.id.Rdeposit_period_input);

                if(!depositAmount.getText().toString().isEmpty() && !interestRate.getText().toString().isEmpty() && !tenure.getText().toString().isEmpty()){

                    RecurringAmount = Double.valueOf(depositAmount.getText().toString());
                    InterestRate = Double.valueOf(interestRate.getText().toString());
                    Tenure = Integer.valueOf(tenure.getText().toString());
                    if(interestFrequency.getSelectedItem().equals("Monthly")){
                        InterestFrequency = 1;
                        args.putString("frequency","Monthly");
                    }
                    else if(interestFrequency.getSelectedItem().equals("Quarterly")) {
                        InterestFrequency = 3;
                        args.putString("frequency","Quarterly");
                    }
                    else if(interestFrequency.getSelectedItem().equals("Half-Yearly")) {
                        InterestFrequency = 6;
                        args.putString("frequency","Half-Yearly");
                    }
                    else if(interestFrequency.getSelectedItem().equals("Yearly")) {
                        InterestFrequency = 12;
                        args.putString("frequency","Yearly");
                    }
                    int compoundFreq = 12/InterestFrequency;
                    for(int i =Tenure;i>0;i--){
                        MaturityAmount+= RecurringAmount * Math.pow((1+(InterestRate/(100*compoundFreq))),((compoundFreq*(double)i)/12));
                    }
                    //MaturityAmount = RecurringAmount * Math.pow(( 1+ ( InterestRate/((12/InterestFrequency)*100) ) ),(x));
                    System.out.println("Maturity Amount: "+MaturityAmount);
                    MaturityAmount = Math.round(MaturityAmount*scale)/scale;
                    args.putDouble("RecurringAmount",RecurringAmount);
                    args.putDouble("InterestRate",InterestRate);
                    args.putInt("Tenure",Tenure);
                    args.putDouble("MaturityAmount",MaturityAmount);
                    args.putString("type","Recurring");
                    FDOutput fdOutput = new FDOutput();
                    fdOutput.setArguments(args);
                    assert getFragmentManager() != null;
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.ir_tab_viewer, fdOutput);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset = view.findViewById(R.id.Rdeposit_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText depositAmount = view.findViewById(R.id.Rdeposit_amount_input);
                EditText interestRate = view.findViewById(R.id.Rdeposit_interest_input);
                EditText tenure = view.findViewById(R.id.Rdeposit_period_input);
                interestFrequency.setSelection(0);
                depositAmount.setText("");
                interestRate.setText("");
                tenure.setText("");
                RecurringAmount =0.0;
                InterestRate = 0.0;
                InterestFrequency = 0;
                MaturityAmount=0.0;
            }
        });

        compare = view.findViewById(R.id.Rdeposit_compare);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("type","Recurring");
                InterestReturnCompare interestReturnCompare = new InterestReturnCompare();
                interestReturnCompare.setArguments(args);
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.ir_tab_viewer, interestReturnCompare);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
