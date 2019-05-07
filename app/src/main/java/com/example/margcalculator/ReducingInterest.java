package com.example.margcalculator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    Double Period = 0.0;
    Double years;
    Double Interest = 0.0;
    Double Amount = 0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reducing_interest, container, false);
        EMI = 0.0;
        TotalAmount = 0.0;
        TotalInterest = 0.0;
        Processing_fee = 0.0;
        Calculate = view.findViewById(R.id.calculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = view.findViewById(R.id.amount_input);
                EditText interest = view.findViewById(R.id.interest_input);
                final EditText period = view.findViewById(R.id.period_input);
                EditText processing_fee = view.findViewById(R.id.processing_fees_input);
                final RadioGroup radioGroup = view.findViewById(R.id.PeriodSelector);
                EditText emi;

                if (!amount.getText().toString().isEmpty() && !interest.getText().toString().isEmpty()
                        && !period.getText().toString().isEmpty() && !processing_fee.getText().toString().isEmpty()) {
                    Amount = Double.valueOf(amount.getText().toString());
                    Interest = Double.valueOf(interest.getText().toString());
                    Period= Double.valueOf(period.getText().toString());
                    Processing_fee = Double.valueOf(processing_fee.getText().toString());

                    RadioButton selectedPeriod =view.findViewById(radioGroup.getCheckedRadioButtonId());
                    if( selectedPeriod.getText().equals("YR")){
                        years = Period;
                        Period*=12;
                    }
//                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(RadioGroup group, int checkedId) {
//                            RadioButton selectedPeriod =view.findViewById(radioGroup.getCheckedRadioButtonId());
//                            System.out.println("+++++++++++++++++++++++++++++++"+ selectedPeriod.getText());
//                            if( selectedPeriod.getText().equals("YR")){
//                                Period*=12;
//                            }
//                        }
//                    });
                    double scale = Math.pow(10,2);
                    Processing_fee = (Processing_fee * Amount) / 100;
                    Processing_fee = Math.round(Processing_fee * scale) / scale;
                    EMI = ComputeEMI(Amount, Interest, Period);
                    EMI = Math.round(EMI * scale) / scale;
                    TotalAmount = EMI * Period;
                    TotalAmount = Math.round(TotalAmount * scale) / scale;
                    TotalInterest = (TotalAmount - Amount);
                    TotalInterest = Math.round(TotalInterest * scale) / scale;

                    TextView Total_Amount_output = view.findViewById(R.id.total_amount_output);
                    TextView Total_Interest_output = view.findViewById(R.id.total_interest_output);
                    TextView EMI_PerMonth = view.findViewById(R.id.EMI_output);
                    TextView Processing_fees_output = view.findViewById(R.id.processing_fees_output);

                    Total_Amount_output.setText(TotalAmount.toString());
                    Total_Interest_output.setText(TotalInterest.toString());
                    EMI_PerMonth.setText(EMI.toString());
                    Processing_fees_output.setText(Processing_fee.toString());
                    hideKeyboardFrom(getContext(), view);
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
                TextView Oamount = view.findViewById(R.id.total_amount_output);
                TextView Ointerest = view.findViewById(R.id.total_interest_output);
                TextView Oemi = view.findViewById(R.id.EMI_output);
                TextView Oprocessing_fee = view.findViewById(R.id.processing_fees_output);
                RadioButton year = view.findViewById(R.id.radio_year);
                RadioButton month = view.findViewById(R.id.radio_month);
                year.setChecked(false);
                month.setChecked(false);
                Oamount.setText("");
                Ointerest.setText("");
                Oemi.setText("");
                Oprocessing_fee.setText("");
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

        Share = view.findViewById(R.id.share);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndDisplayPdf();
            }
        });

        EMIDetails =view.findViewById(R.id.emi_details);
        EMIDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMITable emiTable = new EMITable();
                Bundle args = new Bundle();
                if(EMI!=0.0 && TotalAmount!=0.0){
                    args.putDouble("EMI",EMI);
                    args.putDouble("TotalAmount",TotalAmount);
                    args.putDouble("Interest",Interest);
                    args.putDouble("Period",Period);
                    args.putDouble("Amount",Amount);
                    args.putString("EMI Type","Reducing");
                    emiTable.setArguments(args);
                    assert getFragmentManager() != null;
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.tab_viewer, emiTable);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"Please Calculate EMI first",Toast.LENGTH_SHORT).show();
                }

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
            amount.setText(Amount.toString());
            interest.setText(Interest.toString());
            RadioGroup radioGroup = view.findViewById(R.id.PeriodSelector);
            RadioButton button = view.findViewById(radioGroup.getCheckedRadioButtonId());
            if(button.getText().toString().equals("YR")){
                Period = years;
            }
            period.setText(Period.toString());
            Double t = (Processing_fee*100)/Amount;
            processing_fee.setText(t.toString());
        }
    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private Double ComputeEMI(Double amount, Double interest, Double period){
        interest = interest/(12*100);
        Double EMI1 = (amount * interest * (pow((1+interest),period)))/((pow((1+interest),period))-1);
        return EMI1;
    }
    private void createAndDisplayPdf() {

        if(EMI==0.0 && TotalAmount == 0.0){
            Toast.makeText(getContext(),"Please Calculate EMI first",Toast.LENGTH_SHORT).show();
            return;
        }
        Font font = null;
        Font paraFont = null;
        try {
            font = new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false), 18, Font.NORMAL);
            paraFont = new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false), 22, Font.UNDERLINE|Font.BOLD);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        font.setColor(BaseColor.RED);
        paraFont.setColor(BaseColor.BLACK);
        PdfPTable ITable = new PdfPTable(3);
        PdfPTable OTable = new PdfPTable(3);
        ITable.getDefaultCell().setBorder(0);

        ITable.addCell(new Phrase("Input",paraFont));
        ITable.addCell(new Phrase("",paraFont));
        ITable.addCell(new Phrase("",paraFont));

        ITable.addCell(new Phrase("\n",paraFont));
        ITable.addCell(new Phrase("\n",paraFont));
        ITable.addCell(new Phrase("\n",paraFont));

        ITable.addCell(new Phrase("Loan Amount",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(Amount.toString(),font));

        ITable.addCell(new Phrase("Interest %(Per Year)",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(Interest.toString(),font));

        ITable.addCell(new Phrase("No of Months",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(Period.toString(),font));

        ITable.addCell(new Phrase("Processing Fees",font));
        ITable.addCell(new Phrase(":",font));
        double t = (Processing_fee*100)/Amount;
        ITable.addCell(new Phrase(Double.toString(t),font));

        OTable.getDefaultCell().setBorder(0);
        OTable.addCell(new Phrase("Output\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("EMI PerMonth",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase(EMI.toString(),font));

        OTable.addCell(new Phrase("Processing Fees",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase(Processing_fee.toString(),font));

        OTable.addCell(new Phrase("Total Interest",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase(TotalInterest.toString(),font));

        OTable.addCell(new Phrase("Total Amount",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase(TotalAmount.toString(),font));

        Document doc = new Document();
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "ReducingEMI_Output.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            doc.add(ITable);
            doc.add(new Paragraph("\n\n\n"));
            doc.add(OTable);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }
        viewPdf("ReducingEMI_Output.pdf");
    }

    // Method for opening a pdf file
    private void viewPdf(String file) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}
