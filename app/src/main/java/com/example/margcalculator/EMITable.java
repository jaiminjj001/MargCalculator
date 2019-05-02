package com.example.margcalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EMITable extends Fragment {

    double scale = Math.pow(10,2);
    TableLayout tableLayout;
    Button Share;
    PdfPTable table = new PdfPTable(5);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.emi_details, container, false);

        Bundle args = getArguments();
        Double argAmount = args.getDouble("Amount");
        Double argTotalAmount = args.getDouble("TotalAmount");
        Double temp = argTotalAmount;
        Double argEMI = args.getDouble("EMI");
        Double argInterest = args.getDouble("Interest");
        Double argPeriod = args.getDouble("Period");


        TextView LoanAmountValue = myView.findViewById(R.id.loanAmountValue);
        LoanAmountValue.setText(argAmount.toString());

        argTotalAmount = Math.round(argTotalAmount * scale) / scale;
        TextView TotalAmountValue  = myView.findViewById(R.id.TotalAmountValue);
        TotalAmountValue.setText(argTotalAmount.toString());

        argEMI = Math.round(argEMI * scale) / scale;
        TextView EmiPerMonthValue = myView.findViewById(R.id.EmiPerMonthValue);
        EmiPerMonthValue.setText(argEMI.toString());

        TextView MonthsValue = myView.findViewById(R.id.MonthsValue);
        MonthsValue.setText(argPeriod.toString());

        TextView InterestValue = myView.findViewById(R.id.InterestValue);
        InterestValue.setText(argInterest.toString());

        Integer i=0;
        argInterest = argInterest/(12*100);

        tableLayout = (TableLayout)myView.findViewById(R.id.displayEMITable);
        tableLayout.setDrawingCacheEnabled(true);
        Resources resources = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.border,null);
        for(int j =0;j<argPeriod;j++){
            Double interest,principal,balance;
            PdfPCell pCell = new PdfPCell();
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams layoutParams =new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,0);
            row.setLayoutParams(layoutParams);
            row.setBackgroundColor(Color.WHITE);
            row.setPadding(2,0,2,2);

            if(i==0)
            {
                PdfPCell cell = new PdfPCell();
                TableRow row1 = new TableRow(getContext());
                TableRow.LayoutParams layoutParams1 =new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0,0,0,0);
                row1.setLayoutParams(layoutParams);
                row1.setBackgroundColor(Color.WHITE);
                row1.setPadding(2,2,2,5);

                TextView Srno = new TextView(getContext());
                TextView Emi = new TextView(getContext());
                TextView Interest = new TextView(getContext());
                TextView Principal = new TextView(getContext());
                TextView Balance = new TextView(getContext());

                Srno.setText("Sr.No");
                cell.addElement(new Phrase("Sr.NO"));
                Srno.setTextColor(Color.RED);
                Srno.setTextSize(20);
                Srno.setWidth(150);
                Srno.setPadding(5,0,0,0);
                Srno.setBackground(drawable);

                Emi.setText("EMI");
                cell.addElement(new Phrase("EMI"));
                Emi.setTextColor(Color.RED);
                Emi.setTextSize(20);
                Emi.setWidth(200);
                Emi.setBackground(drawable);
                Emi.setPadding(5,0,0,0);
                Interest.setText("Interest");
                cell.addElement(new Phrase("Interest"));
                Interest.setTextColor(Color.RED);
                Interest.setTextSize(20);
                Interest.setWidth(250);
                Interest.setBackground(drawable);
                Interest.setPadding(5,0,0,0);
                Principal.setText("Principle");
                cell.addElement(new Phrase("Principle"));
                Principal.setTextColor(Color.RED);
                Principal.setTextSize(20);
                Principal.setWidth(250);
                Principal.setBackground(drawable);
                Principal.setPadding(5,0,0,0);
                Balance.setText("Balance");
                cell.addElement(new Phrase("Balance"));
                Balance.setTextColor(Color.RED);
                Balance.setTextSize(20);
                Balance.setWidth(250);
                Balance.setBackground(drawable);
                Balance.setPadding(5,0,0,0);

                row1.addView(Srno);
                row1.addView(Emi);
                row1.addView(Interest);
                row1.addView(Principal);
                row1.addView(Balance);
                tableLayout.addView(row1,i);
                table.addCell(cell);
            }
            TextView Srno = new TextView(getContext());
            TextView Emi = new TextView(getContext());
            TextView Interest = new TextView(getContext());
            TextView Principal = new TextView(getContext());
            TextView Balance = new TextView(getContext());


            i++;
            argEMI = Math.round(argEMI * scale) / scale;
            interest = argInterest * temp;
            interest = Math.round(interest * scale) / scale;
            principal = argEMI - interest;
            principal = Math.round(principal * scale) / scale;
            balance = temp - argEMI;
            balance = Math.round(balance * scale) / scale;
            temp = balance;

            Srno.setText(i.toString());
            pCell.addElement(new Phrase(i.toString()));
            Srno.setTextColor(Color.RED);
            Srno.setTextSize(20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Srno.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            Srno.setWidth(80);
            Srno.setBackground(drawable);
            Srno.setPadding(5,0,0,0);
            Emi.setText(argEMI.toString());
            pCell.addElement(new Phrase(argEMI.toString()));
            Emi.setTextColor(Color.RED);
            Emi.setTextSize(20);
            Emi.setWidth(150);
            Emi.setBackground(drawable);
            Emi.setPadding(5,0,0,0);
            Interest.setText(interest.toString());
            pCell.addElement(new Phrase(interest.toString()));
            Interest.setTextColor(Color.RED);
            Interest.setTextSize(20);
            Interest.setWidth(150);
            Interest.setBackground(drawable);
            Interest.setPadding(5,0,0,0);
            Principal.setText(principal.toString());
            pCell.addElement(new Phrase(principal.toString()));
            Principal.setTextColor(Color.RED);
            Principal.setTextSize(20);
            Principal.setWidth(150);
            Principal.setBackground(drawable);
            Principal.setPadding(5,0,0,0);
            Balance.setText(balance.toString());
            pCell.addElement(new Phrase(balance.toString()));
            Balance.setTextColor(Color.RED);
            Balance.setTextSize(20);
            Balance.setWidth(250);
            Balance.setBackground(drawable);
            Balance.setPadding(5,0,0,0);

            row.addView(Srno);
            row.addView(Emi);
            row.addView(Interest);
            row.addView(Principal);
            row.addView(Balance);
            tableLayout.addView(row,i);
            table.addCell(pCell);
        }

        Share = myView.findViewById(R.id.download);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JAIMIN";
                    String fpath = "/sdcard/" + "abcd.pdf";
                    File file = new File(fpath);

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Document document = new Document();
                    FileOutputStream fo = new FileOutputStream(file.getAbsoluteFile());
                    PdfWriter.getInstance(document,fo);
                    document.open();
                    document.add(table);
                    document.close();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (DocumentException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String path =  "/sdcard/";
                File file = new File(path, "abcd.pdf");
                if(file.exists() && file.isFile()){
                    System.out.println("_____________+++++++++++++++___________+++++++++++++)))))))))((((((((((((((((((((((((("+file.getAbsolutePath());

                }
                intent.setDataAndType( Uri.fromFile(file), "application/pdf" );
                startActivity(intent);
//                tableLayout.setDrawingCacheEnabled(true);
//                tableLayout.buildDrawingCache();
//                Bitmap bm = Bitmap.createBitmap(tableLayout.getDrawingCache());
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/jpeg");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                File f = new File(Environment.getExternalStorageDirectory()  + "imagexxxxxxxx.jpg");
//
//                try {
//                    f.createNewFile();
//                    FileOutputStream fo = new FileOutputStream(f);
//                    fo.write(bytes.toByteArray());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        return  myView;
    }
}
