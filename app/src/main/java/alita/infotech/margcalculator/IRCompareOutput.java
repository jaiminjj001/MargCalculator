package alita.infotech.margcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.margcalculator.R;

public class IRCompareOutput extends Fragment {
    private View view;
    private Bundle args = null;
    private Double Amount;
    private Double Interest1;
    private Double Interest2;
    private Integer Tenure1;
    private Integer Tenure2;
    private String InterestFrequency1;
    private String InterestFrequency2;
    private Double MaturityAmount1;
    private Double MaturityAmount2;
    private Double InterestEarned1;
    private Double InterestEarned2;
    double scale = Math.pow(10,2);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ircompare_output, container, false);
        args = getArguments();
        Toolbar toolbar = getActivity().findViewById(R.id.ir_toolbar);
        TextView amountField = view.findViewById(R.id.irco_amount);
        TextView amount = view.findViewById(R.id.irco_amount_value);
        TextView interest1 = view.findViewById(R.id.irco_Cinterest_rate_value1);
        TextView interest2 = view.findViewById(R.id.irco_Cinterest_rate_value2);
        TextView tenure1 = view.findViewById(R.id.irco_Ctenure_value1);
        TextView tenure2 = view.findViewById(R.id.irco_Ctenure_value2);
        TextView interestFrequency1 = view.findViewById(R.id.irco_Cfrequency_value1);
        TextView interestFrequency2 = view.findViewById(R.id.irco_Cfrequency_value2);
        TextView maturityAmount1 = view.findViewById(R.id.irco_maturityAmount_output1);
        TextView maturityAmount2 = view.findViewById(R.id.irco_maturityAmount_output2);
        TextView interestEarned1 = view.findViewById(R.id.irco_Interest_earned_output1);
        TextView interestEarned2 = view.findViewById(R.id.irco_Interest_earned_output2);

        Interest1 = args.getDouble("interest1");
        interest1.setText(String.format("%.2f",Interest1)+"%");
        Interest2 = args.getDouble("interest2");
        interest2.setText(String.format("%.2f",Interest2)+"%");

        Tenure1 = args.getInt("tenure1");
        tenure1.setText(String.format("%.2f months",Tenure1));
        Tenure2 = args.getInt("tenure2");
        tenure2.setText(String.format("%.2f months",Tenure2));

        InterestFrequency1 = args.getString("frequency1");
        interestFrequency1.setText(InterestFrequency1);
        InterestFrequency2 = args.getString("frequency2");
        interestFrequency2.setText(InterestFrequency2);

        MaturityAmount1 = args.getDouble("maturityAmount1");
        MaturityAmount1 = Math.round(MaturityAmount1*scale)/scale;
        maturityAmount1.setText(String.format("%.2f",MaturityAmount1));
        MaturityAmount2 = args.getDouble("maturityAmount2");
        MaturityAmount2 = Math.round(MaturityAmount2*scale)/scale;
        maturityAmount2.setText(String.format("%.2f",MaturityAmount2));

        if(args.containsKey("DepositAmount")){
            amountField.setText("Deposit Amount");
            Amount = args.getDouble("DepositAmount");
            amount.setText(String.format("%.2f",Amount));
            InterestEarned1 = MaturityAmount1 - Amount;
            InterestEarned2 = MaturityAmount2 - Amount;
            toolbar.setTitle("Fixed Deposit");
        }
        else if(args.containsKey("RecurringAmount")){
            amountField.setText("Recurring Amount");
            Amount = args.getDouble("RecurringAmount");
            amount.setText(String.format("%.2f",Amount));
            InterestEarned1 = MaturityAmount1 - (Amount*Tenure1);
            InterestEarned2 = MaturityAmount2 - (Amount*Tenure2);
            toolbar.setTitle("Recurring Deposit");
        }

        InterestEarned1 = Math.round(InterestEarned1*scale)/scale;
        InterestEarned2 = Math.round(InterestEarned2*scale)/scale;
        interestEarned1.setText(String.format("%.2f",InterestEarned1));
        interestEarned2.setText(String.format("%.2f",InterestEarned2));
        return view;
    }
}
