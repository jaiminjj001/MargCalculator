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
import androidx.fragment.app.FragmentTransaction;

import com.example.margcalculator.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static java.lang.Math.pow;

public class RFCompare extends Fragment {
    Button compareCalculate;
    Button compareReset;
    Double Amount;
    Double Interest1;
    Double Interest2;
    Integer Period1;
    Integer Period2;
    Double Processing_fees1;
    Double Processing_fees2;
    Double EMI1=0.0;
    Double EMI2 = 0.0;
    Double TotalAmount1=0.0;
    Double TotalAmount2 = 0.0;
    Double TotalInterest1 = 0.0;
    Double TotalInterest2 = 0.0;
    Double years1 = 0.0;
    Double years2 = 0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View myView = inflater.inflate(R.layout.rf_compare, container, false);
        hideKeyboardFrom(getContext(), myView);
        final InterstitialAd mInterstitialAd = EMICalculator.getAd();
        compareCalculate = myView.findViewById(R.id.RFcompare_calculate);
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
                EditText amount = myView.findViewById(R.id.RFcompare_amount_input);
                EditText interest1 = myView.findViewById(R.id.RFcompare_interest_input1);
                EditText interest2 = myView.findViewById(R.id.RFcompare_interest_input2);
                EditText period1 = myView.findViewById(R.id.RFcompare_period_input1);
                EditText period2 = myView.findViewById(R.id.RFcompare_period_input2);
                EditText processing_fees1 = myView.findViewById(R.id.RFcompare_processing_fees_input1);
                EditText processing_fees2 = myView.findViewById(R.id.RFcompare_processing_fees_input2);
                final RadioGroup radioGroup = myView.findViewById(R.id.RFPeriodSelector);

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
                        Toast.makeText(getContext(), "Please enter a valid Loan Period", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        double scale = Math.pow(10, 2);
                        Processing_fees1 = (Processing_fees1 * Amount) / 100;
                        Processing_fees1 = Math.round(Processing_fees1 * scale) / scale;
                        if(Interest1==0){
                            EMI1 = Amount/Period1;
                        }
                        else {
                            EMI1 = ComputeREMI(Amount, Interest1, Period1);
                        }
                        TotalAmount1 = EMI1 * Period1;
                        TotalInterest1 = (TotalAmount1 - Amount);
                        EMI1 = Math.round(EMI1 * scale) / scale;
                        TotalAmount1 = Math.round(TotalAmount1 * scale) / scale;
                        TotalInterest1 = Math.round(TotalInterest1 * scale) / scale;

                        Processing_fees2 = (Processing_fees2 * Amount) / 100.0;
                        Processing_fees2 = Math.round(Processing_fees2 * scale) / scale;
                        TotalInterest2 = (Interest2 * Amount * (Period2 / 12.00)) / 100.0;
                        TotalInterest2 = Math.round(TotalInterest2 * scale) / scale;
                        EMI2 = ComputeFEMI(Amount, TotalInterest2, Period2);
                        EMI2 = Math.round(EMI2 * scale) / scale;
                        TotalAmount2 = TotalInterest2 + Amount;
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
                        args.putString("type", "RF");
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
                        RadioButton year = myView.findViewById(R.id.RFradio_year);
                        RadioButton month = myView.findViewById(R.id.RFradio_month);
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
            }
        });
        return myView;

    }
    private Double ComputeREMI(Double amount, Double interest, Integer period){
        interest = interest/(12*100);
        Double EMI = (amount * interest * (pow((1+interest),period)))/((pow((1+interest),period))-1);
        return EMI;
    }
    private Double ComputeFEMI(Double amount, Double interest, Integer period){
        Double EMI = (amount+interest)/period;
        return EMI;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
