package alita.infotech.margcalculator;

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
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.margcalculator.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.AdRequest;
import static java.lang.Math.pow;

public class ReducingInterest extends Fragment {

    View view;
    Button Calculate;
    Button Reset;
    Button Compare;
    Button EMIDetails;
    Button Share;
    Double EMI=0.0;
    Double TotalAmount=0.0;
    Double TotalInterest=0.0;
    Double Processing_fee=0.0;
    Integer Period = 0;
    Double years=0.0;
    Double Interest = 0.0;
    Double Amount = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reducing_interest, container, false);
        hideKeyboardFrom(getContext(), view);
        EMI = 0.0;
        TotalAmount = 0.0;
        TotalInterest = 0.0;
        Processing_fee = 0.0;
        final InterstitialAd mInterstitialAd = EMICalculator.getAd();
        Calculate = view.findViewById(R.id.calculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
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
                hideKeyboardFrom(getContext(), view);
                EditText amount = view.findViewById(R.id.amount_input);
                EditText interest = view.findViewById(R.id.interest_input);
                final EditText period = view.findViewById(R.id.period_input);
                EditText processing_fee = view.findViewById(R.id.processing_fees_input);
                final RadioGroup radioGroup = view.findViewById(R.id.PeriodSelector);
                EditText emi;

                if (!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty()
                        && !period.getText().toString().isEmpty() && !processing_fee.getText().toString().isEmpty() && (radioGroup.getCheckedRadioButtonId() != -1)) {
                    Amount = Double.valueOf(amount.getText().toString());
                    double temp;
                    temp = Double.valueOf(period.getText().toString());
                    RadioButton selectedPeriod = view.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selectedPeriod.getText().equals("YR")) {
                        temp*=12;
                        Period = (int)temp;
                        years = Double.valueOf(Period)/12;
                    }
                    else{
                        years=0.0;
                        Period = (int)temp;
                    }
                    Interest = Double.valueOf(interest.getText().toString());
                    Processing_fee = Double.valueOf(processing_fee.getText().toString());
                    if(Period==0){
                        Toast.makeText(getContext(),"Please enter a valid Loan Period",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        double scale = Math.pow(10, 2);
                        Processing_fee = (Processing_fee * Amount) / 100;
                        Processing_fee = Math.round(Processing_fee * scale) / scale;
                        if(Interest==0){
                            EMI = Amount/Period;
                        }else{
                            EMI = ComputeEMI(Amount, Interest, Period);
                        }
                        TotalAmount = EMI * Period;
                        TotalInterest = (TotalAmount - Amount);
                        EMI = Math.round(EMI * scale) / scale;
                        TotalAmount = Math.round(TotalAmount * scale) / scale;
                        TotalInterest = Math.round(TotalInterest * scale) / scale;


                        Bundle args = new Bundle();
                        ReducingEMIOutput reducingEMIOutput = new ReducingEMIOutput();
                        args.putDouble("loan_amount", Amount);
                        args.putDouble("interest_rate", Interest);
                        args.putInt("period", Period);
                        args.putDouble("processing_fees", Processing_fee);
                        args.putDouble("loan_emi", EMI);
                        args.putDouble("total_interest", TotalInterest);
                        args.putDouble("total_amount", TotalAmount);
                        if (years != 0)
                            args.putDouble("years", years);
                        reducingEMIOutput.setArguments(args);
                        assert getFragmentManager() != null;
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.tab_viewer, reducingEMIOutput);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

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
                RadioButton year = view.findViewById(R.id.radio_year);
                RadioButton month = view.findViewById(R.id.radio_month);
                year.setChecked(true);
                month.setChecked(false);
                amount.setText("");
                interest.setText("");
                period.setText("");
                processing_fee.setText("");
                EMI = 0.0;
                TotalAmount = 0.0;
                TotalInterest = 0.0;
                Processing_fee = 0.0;
                years = 0.0;
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

        //code to retain values
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
            EditText amount = view.findViewById(R.id.amount_input);
            EditText interest = view.findViewById(R.id.interest_input);
            EditText period = view.findViewById(R.id.period_input);
            EditText processing_fee = view.findViewById(R.id.processing_fees_input);
            amount.setText(String.format("%.2f",Amount));
            interest.setText(Interest.toString());
            RadioGroup radioGroup = view.findViewById(R.id.PeriodSelector);
            RadioButton button = view.findViewById(radioGroup.getCheckedRadioButtonId());
            if(button.getText().toString().equals("YR")){
                period.setText(years.toString());
            }
            else
                period.setText(Period.toString());
            Double t = (Processing_fee*100)/Amount;
            processing_fee.setText(t.toString());
        }
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private Double ComputeEMI(Double amount, Double interest, Integer period){
        interest = interest/(12*100);
        Double EMI1 = (amount * interest * (pow((1+interest),period)))/((pow((1+interest),period))-1);
        return EMI1;
    }

}
