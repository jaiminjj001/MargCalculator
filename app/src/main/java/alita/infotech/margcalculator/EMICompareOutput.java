package alita.infotech.margcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.margcalculator.R;

public class EMICompareOutput extends Fragment {
    private Double loanAmount;
    private Double interest1;
    private Double interest2;
    private Integer period1;
    private Integer period2;
    private Double processingFees1;
    private Double processingFees2;
    private Double processingRate1;
    private Double processingRate2;
    private Double EMI1;
    private Double EMI2;
    private Double TotalAmount1;
    private Double TotalAmount2;
    private Double TotalInterest1;
    private Double TotalInterest2;
    private Double years1;
    private Double years2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.emi_compare_ouput, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        Bundle args = getArguments();
        if(args.getString("type").equals("Reducing")){
            toolbar.setTitle("Reducing Compare");
        }
        else if(args.getString("type").equals("Flat")){
            toolbar.setTitle("Flat Compare");
        }
        else if(args.getString("type").equals("RF")){
            toolbar.setTitle("Reducing - Flat Compare");
            TextView bank1 = myView.findViewById(R.id.bank1);
            bank1.setText("Reducing EMI");
            TextView bank2 = myView.findViewById(R.id.bank2);
            bank2.setText("Flat EMI");
        }

        TextView LoanAmount = myView.findViewById(R.id.Cloan_amount_value);
        TextView InterestRate1 = myView.findViewById(R.id.Cinterest_rate_value1);
        TextView InterestRate2 = myView.findViewById(R.id.Cinterest_rate_value2);
        TextView Period1 = myView.findViewById(R.id.Cperiod_value1);
        TextView Period2 = myView.findViewById(R.id.Cperiod_value2);
        TextView ProcessingFees1 = myView.findViewById(R.id.Cprocessing_fees_value1);
        TextView ProcessingFees2 = myView.findViewById(R.id.Cprocessing_fees_value2);

        TextView EMI_perMonth1 = myView.findViewById(R.id.compare_EMI_output1);
        TextView EMI_perMonth2 = myView.findViewById(R.id.compare_EMI_output2);
        TextView Processing_fees_output1 = myView.findViewById(R.id.compare_processing_fees_output1);
        TextView Processing_fees_output2 = myView.findViewById(R.id.compare_processing_fees_output2);
        TextView Total_interest_output1 = myView.findViewById(R.id.compare_total_interest_output1);
        TextView Total_interest_output2 = myView.findViewById(R.id.compare_total_interest_output2);
        TextView Total_amount_output1 = myView.findViewById(R.id.compare_total_amount_output1);
        TextView Total_amount_output2 = myView.findViewById(R.id.compare_total_amount_output2);

        loanAmount = args.getDouble("loan_amount");
        interest1 = args.getDouble("interest1");
        interest2 = args.getDouble("interest2");
        period1 = args.getInt("period1");
        period2 = args.getInt("period2");
        processingFees1 = args.getDouble("processing_fees1");
        processingFees2 = args.getDouble("processing_fees2");
        processingRate1 = (processingFees1 * 100)/loanAmount;
        processingRate2 = (processingFees2*100)/loanAmount;
        EMI1 = args.getDouble("EMI1");
        EMI2 = args.getDouble("EMI2");
        TotalAmount1 = args.getDouble("total_amount1");
        TotalAmount2 = args.getDouble("total_amount2");
        TotalInterest1 = args.getDouble("total_interest1");
        TotalInterest2 = args.getDouble("total_interest2");
        if(args.containsKey("years1")){
            years1 = args.getDouble("years1");
            years2 = args.getDouble("years2");
            Period1.setText(years1.toString()+" years");
            Period2.setText(years2.toString()+" years");
        }
        else{
            Period1.setText(period1.toString()+" months");
            Period2.setText(period2.toString()+" months");
        }

        LoanAmount.setText(loanAmount.toString());
        InterestRate1.setText(interest1.toString()+" %");
        InterestRate2.setText(interest2.toString()+" %");
        ProcessingFees1.setText(processingRate1.toString()+" %");
        ProcessingFees2.setText(processingRate2.toString()+" %");

        EMI_perMonth1.setText(EMI1.toString());
        EMI_perMonth2.setText(EMI2.toString());
        Processing_fees_output1.setText(processingFees1.toString());
        Processing_fees_output2.setText(processingFees2.toString());
        Total_interest_output1.setText(TotalInterest1.toString());
        Total_interest_output2.setText(TotalInterest2.toString());
        Total_amount_output1.setText(TotalAmount1.toString());
        Total_amount_output2.setText(TotalAmount2.toString());

        return  myView;
    }
}
