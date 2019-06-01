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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class FlatInterest extends Fragment {
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
    Double years = 0.0;
    Double Interest = 0.0;
    Double Amount = 0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.flat_interest, container, false);
        hideKeyboardFrom(getContext(), view);
        EMI = 0.0;
        TotalAmount = 0.0;
        TotalInterest = 0.0;
        Processing_fee = 0.0;
        final InterstitialAd mInterstitialAd = EMICalculator.getAd();
        Calculate = view.findViewById(R.id.Fcalculate);
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
                EditText amount = view.findViewById(R.id.Famount_input);
                EditText interest = view.findViewById(R.id.Finterest_input);
                EditText period = view.findViewById(R.id.Fperiod_input);
                EditText processing_fee = view.findViewById(R.id.Fprocessing_fees_input);
                RadioGroup radioGroup = view.findViewById(R.id.FPeriodSelector);

                EditText emi;

                if (!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty()
                        && !period.getText().toString().isEmpty() && !processing_fee.getText().toString().isEmpty()
                         && (radioGroup.getCheckedRadioButtonId() != -1)) {
                    RadioButton selectedPeriod =view.findViewById(radioGroup.getCheckedRadioButtonId());
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest = Double.valueOf(interest.getText().toString());
                    double temp = Double.valueOf(period.getText().toString());
                    if (selectedPeriod.getText().equals("YR")) {
                        temp*=12;
                        Period = (int)temp;
                        years = Double.valueOf(Period)/12.0;
                    }
                    else{
                        years=0.0;
                        Period = (int)temp;
                    }
                    Processing_fee = Double.valueOf(processing_fee.getText().toString());

                    if (Period == 0) {
                        Toast.makeText(getContext(), "Period cannot be 0", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        double scale = Math.pow(10, 2);
                        Processing_fee = (Processing_fee * Amount) / 100.0;
                        Processing_fee = Math.round(Processing_fee * scale) / scale;
                        TotalInterest = (Interest * Amount * (Period / 12.0)) / 100.0;
                        EMI = ComputeEMI(Amount, TotalInterest, Period);
                        TotalAmount = TotalInterest + Amount;
                        EMI = Math.round(EMI * scale) / scale;
                        TotalInterest = Math.round(TotalInterest * scale) / scale;
                        TotalAmount = Math.round(TotalAmount * scale) / scale;

                        Bundle args = new Bundle();
                        FlatEMIOutput flatEMIOutput = new FlatEMIOutput();
                        args.putDouble("loan_amount", Amount);
                        args.putDouble("interest_rate", Interest);
                        args.putInt("period", Period);
                        args.putDouble("processing_fees", Processing_fee);
                        args.putDouble("loan_emi", EMI);
                        args.putDouble("total_interest", TotalInterest);
                        args.putDouble("total_amount", TotalAmount);
                        if (years != 0)
                            args.putDouble("years", years);
                        flatEMIOutput.setArguments(args);
                        assert getFragmentManager() != null;
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.tab_viewer, flatEMIOutput);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        hideKeyboardFrom(getContext(), view);
                    }
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
                RadioButton year = view.findViewById(R.id.Fradio_year);
                RadioButton month = view.findViewById(R.id.Fradio_month);
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
                Period = 0;
                years = 0.0;

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
            amount.setText(String.format("%.2f",Amount));
            interest.setText(Interest.toString());
            RadioGroup radioGroup = view.findViewById(R.id.FPeriodSelector);
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
        Double EMI = (amount+interest)/period;
        return EMI;
    }
}
