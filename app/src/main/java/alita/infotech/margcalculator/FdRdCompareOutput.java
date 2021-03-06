package alita.infotech.margcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.margcalculator.R;

public class FdRdCompareOutput extends Fragment {
    private Bundle args = null;
    private Double DepositAmount;
    private Double RecurringAmount;
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
        final View myView = inflater.inflate(R.layout.fdrd_compare_output, container, false);
        args = getArguments();
        Toolbar toolbar = getActivity().findViewById(R.id.ir_toolbar);
        toolbar.setTitle("Fixed Deposit vs Recurring Deposit");
        TextView depositAmount = myView.findViewById(R.id.fdrdco_deposit_amount_value);
        TextView recurringAmount = myView.findViewById(R.id.fdrdco_recurring_amount_value);
        TextView interest1 = myView.findViewById(R.id.fdrdco_Cinterest_rate_value1);
        TextView interest2 = myView.findViewById(R.id.fdrdco_Cinterest_rate_value2);
        TextView tenure1 = myView.findViewById(R.id.fdrdco_Ctenure_value1);
        TextView tenure2 = myView.findViewById(R.id.fdrdco_Ctenure_value2);
        TextView interestFrequency1 = myView.findViewById(R.id.fdrdco_Cfrequency_value1);
        TextView interestFrequency2 = myView.findViewById(R.id.fdrdco_Cfrequency_value2);
        TextView maturityAmount1 = myView.findViewById(R.id.fdrdco_maturityAmount_output1);
        TextView maturityAmount2 = myView.findViewById(R.id.fdrdco_maturityAmount_output2);
        TextView interestEarned1 = myView.findViewById(R.id.fdrdco_Interest_earned_output1);
        TextView interestEarned2 = myView.findViewById(R.id.fdrdco_Interest_earned_output2);


        Interest1 = args.getDouble("interest1");
        interest1.setText(Interest1.toString()+" %");
        Interest2 = args.getDouble("interest2");
        interest2.setText(Interest2.toString()+" %");

        Tenure1 = args.getInt("tenure1");
        tenure1.setText(Tenure1.toString()+" months");
        Tenure2 = args.getInt("tenure2");
        tenure2.setText(Tenure2.toString()+" months");

        InterestFrequency1 = args.getString("frequency1");
        interestFrequency1.setText(InterestFrequency1);
        InterestFrequency2 = args.getString("frequency2");
        interestFrequency2.setText(InterestFrequency2);

        MaturityAmount1 = args.getDouble("maturityAmount1");
        MaturityAmount1 = Math.round(MaturityAmount1*scale)/scale;
        maturityAmount1.setText(String.format("%.2f", MaturityAmount1));
        MaturityAmount2 = args.getDouble("maturityAmount2");
        MaturityAmount2 = Math.round(MaturityAmount2*scale)/scale;
        maturityAmount2.setText(String.format("%.2f", MaturityAmount2));

        DepositAmount = args.getDouble("DepositAmount");
        depositAmount.setText(String.format("%.2f", DepositAmount));
        RecurringAmount = args.getDouble("RecurringAmount");
        recurringAmount.setText(String.format("%.2f", RecurringAmount));

        InterestEarned1 = MaturityAmount1 - DepositAmount;
        InterestEarned2 = MaturityAmount2 - (RecurringAmount*Tenure2);
        InterestEarned1 = Math.round(InterestEarned1*scale)/scale;
        InterestEarned2 = Math.round(InterestEarned2*scale)/scale;
        interestEarned1.setText(String.format("%.2f", InterestEarned1));
        interestEarned2.setText(String.format("%.2f", InterestEarned2));
        return myView;
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
