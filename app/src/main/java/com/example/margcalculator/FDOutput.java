package com.example.margcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FDOutput extends Fragment {
    private Bundle args;
    private Button reset;
    private Double DepositAmount;
    private Double RecurringAmount;
    private Double InterestRate;
    private String InterestFrequency;
    private Integer Tenure;
    private Double MaturityAmount;
    private Double InterestEarned;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myView = inflater.inflate(R.layout.fd_output, container, false);
        final TextView amountField = myView.findViewById(R.id.Odeposit_amount);
        final TextView depositAmount = myView.findViewById(R.id.Odeposit_amount_value);
        final TextView interestRate  = myView.findViewById(R.id.Odeposit_interest_rate_value);
        final TextView interestFreq  = myView.findViewById(R.id.Odeposit_interest_frequency_value);
        final TextView tenure  = myView.findViewById(R.id.Odeposit_tenure_value);
        final TextView maturityAmount  = myView.findViewById(R.id.Odeposit_MaturityAmountValue);
        final TextView interestEarned  = myView.findViewById(R.id.Odeposit_InterestEarnedValue);
        Toolbar toolbar = getActivity().findViewById(R.id.ir_toolbar);
        args= getArguments();
        InterestRate = args.getDouble("InterestRate");
        InterestFrequency = args.getString("frequency");
        Tenure = args.getInt("Tenure");
        MaturityAmount = args.getDouble("MaturityAmount");
        if(args.getString("type").equals("Recurring")){
            RecurringAmount = args.getDouble("RecurringAmount");
            amountField.setText("Recurring Amount");
            depositAmount.setText(RecurringAmount.toString());
            InterestEarned = MaturityAmount - (RecurringAmount*Tenure);
            toolbar.setTitle("Recurring Deposit");
        }
        else if(args.getString("type").equals("Fixed")){
            DepositAmount = args.getDouble("DepositAmount");
            amountField.setText("Deposit Amount");
            depositAmount.setText(DepositAmount.toString());
            InterestEarned = MaturityAmount - DepositAmount;
            toolbar.setTitle("Fixed Deposit");
        }
        double scale = Math.pow(10,2);
        InterestEarned = Math.round(InterestEarned*scale)/scale;
        interestRate.setText(InterestRate.toString());
        interestFreq.setText(InterestFrequency);
        tenure.setText(Tenure.toString());
        maturityAmount.setText(MaturityAmount.toString());
        interestEarned.setText(InterestEarned.toString());

        reset = myView.findViewById(R.id.Odeposit_share);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndDisplayPdf();
            }
        });
        return myView;
    }
    private void createAndDisplayPdf() {

        Font font = null;
        Font paraFont = null;
        try {
            AssetManager am = getContext().getApplicationContext().getAssets();
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

        if(args.getString("type").equals("Recurring")){
            ITable.addCell(new Phrase("Recurring Amount",font));
            ITable.addCell(new Phrase(":",font));
            ITable.addCell(new Phrase("Rs. "+RecurringAmount.toString(),font));
        }
        else if(args.getString("type").equals("Fixed")){
            ITable.addCell(new Phrase("Deposit Amount",font));
            ITable.addCell(new Phrase(":",font));
            ITable.addCell(new Phrase("Rs. "+DepositAmount.toString(),font));
        }
        ITable.addCell(new Phrase("Interest %(Per Year)",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(InterestRate.toString()+" %",font));

        ITable.addCell(new Phrase("Interest Frequency",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(InterestFrequency,font));

        ITable.addCell(new Phrase("Tenure(in Months)",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(Tenure.toString(),font));

        OTable.getDefaultCell().setBorder(0);
        OTable.addCell(new Phrase("Output\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("Maturity Amount",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+MaturityAmount.toString(),font));

        OTable.addCell(new Phrase("Interest Earned",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+InterestEarned.toString(),font));

        Document doc = new Document();
        String name="";
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();
            File file=null;
            if(args.getString("type").equals("Recurring")) {
                file = new File(dir, "RecurringDeposit_Output.pdf");
                name = "RecurringDeposit_Output.pdf";
            }
            else if(args.getString("type").equals("Fixed")) {
                file = new File(dir, "FixedDeposit_Output.pdf");
                name = "FixedDeposit_Output.pdf";
            }
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
        viewPdf(name);
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
        pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}
