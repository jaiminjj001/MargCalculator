package alita.infotech.margcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import com.example.margcalculator.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EMITable extends Fragment {

    private double scale = Math.pow(10,2);
    private TableLayout tableLayout;
    private Button Share;
    Double argAmount;
    Double argTotalAmount;
    Double argEMI;
    Double argInterest;
    Integer argPeriod;
    String EMIType;
    Double argYears;

    private PdfPTable table = new PdfPTable(5);
    Font font = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.emi_details, container, false);
        try {
            table.setTotalWidth(460f);
            table.setLockedWidth(true);
            table.setWidths(new float[]{60f,100f,100f,100f,100f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        final Bundle args = getArguments();
        argAmount = args.getDouble("Amount");
        argTotalAmount = args.getDouble("TotalAmount");
        Double temp = argAmount;
        argEMI = args.getDouble("EMI");
        argInterest = args.getDouble("Interest");
        argPeriod = args.getInt("Period");
        EMIType = args.getString("EMI Type");


        TextView LoanAmountValue = myView.findViewById(R.id.loanAmountValue);
        LoanAmountValue.setText(String.format("%.2f", argAmount));

        argTotalAmount = Math.round(argTotalAmount * scale) / scale;
        TextView TotalAmountValue  = myView.findViewById(R.id.TotalAmountValue);
        TotalAmountValue.setText(String.format("%.2f", argTotalAmount));

        argEMI = Math.round(argEMI * scale) / scale;
        TextView EmiPerMonthValue = myView.findViewById(R.id.EmiPerMonthValue);
        EmiPerMonthValue.setText(String.format("%.2f", argEMI));

        TextView MonthsValue = myView.findViewById(R.id.MonthsValue);
        if(args.containsKey("years")) {
            argYears = args.getDouble("years");
            MonthsValue.setText(String.format("%.2f years", argYears));
        }
        else
            MonthsValue.setText(String.format("%d months", argPeriod));

        TextView InterestValue = myView.findViewById(R.id.InterestValue);
        InterestValue.setText(String.format("%.2f", argInterest)+"%");


        Integer i=0;
        argInterest = argInterest/(12*100);
        tableLayout = myView.findViewById(R.id.displayEMITable);
        tableLayout.setDrawingCacheEnabled(true);
        Resources resources = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.table_border,null);



        for(int j =0;j<argPeriod;j++){
            Double interest = 0.0,principal = 0.0,balance = 0.0;
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams layoutParams =new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,0);
            row.setLayoutParams(layoutParams);
            row.setMinimumWidth(1100);
            if(j%2==0){
                row.setBackgroundColor(getResources().getColor(R.color.row_even));
            }
            else{
                row.setBackgroundColor(Color.WHITE);
            }

            row.setPadding(2,0,2,2);
            try {
                font = new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false), 16, Font.NORMAL);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            font.setColor(BaseColor.BLACK);
            PdfPCell cell3;
            PdfPCell cell1;
            PdfPCell cell2;
            PdfPCell cell4;
            PdfPCell cell5;
            if(i==0)
            {
                cell1 = new PdfPCell();
                cell2 = new PdfPCell();
                cell3 = new PdfPCell();
                cell4 = new PdfPCell();
                cell5 = new PdfPCell();

                TableRow.LayoutParams layoutParams1 =new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0,0,0,0);
                TableRow row1 = new TableRow(getContext());
                row1.setLayoutParams(layoutParams);
                row1.setBackgroundColor(Color.WHITE);
                row1.setPadding(2,2,2,5);

                TextView Srno = new TextView(getContext());
                TextView Emi = new TextView(getContext());
                TextView Interest = new TextView(getContext());
                TextView Principal = new TextView(getContext());
                TextView Balance = new TextView(getContext());

                Srno.setText("Sr.No");
                Srno.setTextColor(Color.BLACK);
                Srno.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Srno.setTextSize(20);
                Srno.setWidth(150);
                Srno.setPadding(5,0,0,0);
//                Srno.setBackground(drawable);

                Emi.setText("EMI\\mo");
                Emi.setTextColor(Color.BLACK);
                Emi.setTextSize(20);
                Emi.setWidth(300);
                Emi.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                Emi.setBackground(drawable);
                Emi.setPadding(5,0,0,0);

                Interest.setText("Interest");
                Interest.setTextColor(Color.BLACK);
                Interest.setTextSize(20);
                Interest.setWidth(300);
                Interest.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                Interest.setBackground(drawable);
                Interest.setPadding(5,0,0,0);

                Principal.setText("Principle");
                Principal.setTextColor(Color.BLACK);
                Principal.setTextSize(20);
                Principal.setWidth(300);
//                Principal.setBackground(drawable);
                Principal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Principal.setPadding(5,0,0,0);

                Balance.setText("Balance");
                Balance.setTextColor(Color.BLACK);
                Balance.setTextSize(20);
                Balance.setWidth(300);
                //Balance.setBackground(drawable);
                Balance.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Balance.setPadding(5,0,0,0);

                row1.addView(Srno);
                row1.addView(Emi);
                row1.addView(Interest);
                row1.addView(Principal);
                row1.addView(Balance);
                row1.setBackgroundColor(getResources().getColor(R.color.toolbar));
                tableLayout.addView(row1,i);

                font.setColor(BaseColor.BLACK);
                cell1.addElement(new Phrase("Sr.NO",font));
                cell2.addElement(new Phrase("EMI",font));
                cell3.addElement(new Phrase("Interest",font));
                cell4.addElement(new Phrase("Principle",font));
                cell5.addElement(new Phrase("Balance",font));
                font.setColor(BaseColor.BLACK);

                cell1.setBackgroundColor(BaseColor.WHITE);
                cell2.setBackgroundColor(BaseColor.WHITE);
                cell3.setBackgroundColor(BaseColor.WHITE);
                cell4.setBackgroundColor(BaseColor.WHITE);
                cell5.setBackgroundColor(BaseColor.WHITE);

                cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell2.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell5.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);

                cell1.setBorderColor(BaseColor.BLACK);
                cell2.setBorderColor(BaseColor.BLACK);
                cell3.setBorderColor(BaseColor.BLACK);
                cell4.setBorderColor(BaseColor.BLACK);
                cell5.setBorderColor(BaseColor.BLACK);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);

            }
            TextView Srno = new TextView(getContext());
            TextView Emi = new TextView(getContext());
            TextView Interest = new TextView(getContext());
            TextView Principal = new TextView(getContext());
            TextView Balance = new TextView(getContext());

            cell1 = new PdfPCell();
            cell2 = new PdfPCell();
            cell3 = new PdfPCell();
            cell4 = new PdfPCell();
            cell5 = new PdfPCell();

            i++;
            if(EMIType.equals("Reducing")) {
                argEMI = Math.round(argEMI * scale) / scale;
                interest = argInterest * temp;
                interest = Math.round(interest * scale) / scale;
                principal = argEMI - interest;
                principal = Math.round(principal * scale) / scale;
                balance = temp - principal;
                balance = Math.round(balance * scale) / scale;
                temp = balance;
            }
            else if(EMIType.equals("Flat")){
                argEMI = Math.round(argEMI * scale) / scale;
                interest = argInterest * argAmount;
                interest = Math.round(interest * scale) / scale;
                principal = argEMI - interest;
                principal = Math.round(principal * scale) / scale;
                balance = temp - principal;
                balance = Math.round(balance * scale) / scale;
                temp = balance;
            }
            Srno.setText(String.format("%d",i));
            Srno.setTextColor(Color.BLACK);
            Srno.setTextSize(20);
            Srno.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            Srno.setWidth(150);
//            Srno.setBackground(drawable);
            Srno.setPadding(5,0,0,0);

            Emi.setText(String.format("%.2f",argEMI));
            Emi.setTextColor(Color.BLACK);
            Emi.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            Emi.setTextSize(20);
            Emi.setWidth(300);
//            Emi.setBackground(drawable);
            Emi.setPadding(5,0,0,0);

            Interest.setText(String.format("%.2f",interest));
            Interest.setTextColor(Color.BLACK);
            Interest.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            Interest.setTextSize(20);
            Interest.setWidth(300);
//            Interest.setBackground(drawable);
            Interest.setPadding(5,0,0,0);

            Principal.setText(String.format("%.2f",principal));
            Principal.setTextColor(Color.BLACK);
            Principal.setTextSize(20);
            Principal.setWidth(300);
            Principal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            Principal.setBackground(drawable);
            Principal.setPadding(5,0,0,0);

            Balance.setText(String.format("%.2f",balance));
            Balance.setTextColor(Color.BLACK);
            Balance.setTextSize(20);
            Balance.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            Balance.setWidth(300);
//            Balance.setBackground(drawable);
            Balance.setPadding(5,0,0,0);

            row.addView(Srno);
            row.addView(Emi);
            row.addView(Interest);
            row.addView(Principal);
            row.addView(Balance);
            tableLayout.addView(row,i);

            cell1.addElement(new Phrase(i.toString(),font));
            cell2.addElement(new Phrase(argEMI.toString(),font));
            cell3.addElement(new Phrase(interest.toString(),font));
            cell4.addElement(new Phrase(principal.toString(),font));
            cell5.addElement(new Phrase(balance.toString(),font));

            cell1.setBackgroundColor(BaseColor.WHITE);
            cell2.setBackgroundColor(BaseColor.WHITE);
            cell3.setBackgroundColor(BaseColor.WHITE);
            cell4.setBackgroundColor(BaseColor.WHITE);
            cell5.setBackgroundColor(BaseColor.WHITE);

            cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);


            cell1.setBorderColor(BaseColor.BLACK);
            cell2.setBorderColor(BaseColor.BLACK);
            cell3.setBorderColor(BaseColor.BLACK);
            cell4.setBorderColor(BaseColor.BLACK);
            cell5.setBorderColor(BaseColor.BLACK);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
        }

        Share = myView.findViewById(R.id.download);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region extra
               /*try{
                    createandDisplayPdf("HELLO THIS IS JAIMIN - ANDROID");
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

                tableLayout.setDrawingCacheEnabled(true);
                tableLayout.buildDrawingCache();
                Bitmap bm = Bitmap.createBitmap(tableLayout.getDrawingCache());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File f = new File(Environment.getExternalStorageDirectory()  + "imagexxxxxxxx.jpg");

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
               }*/
               //endregion
                Font dfont = new Font();
                try {
                    dfont = new Font(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false), 16, Font.BOLD);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dfont.setColor(BaseColor.RED);
                PdfPTable pDTable = new PdfPTable(3);
                pDTable.getDefaultCell().setBorder(0);
                pDTable.addCell(new Phrase("Loan Amount",dfont));
                pDTable.addCell(new Phrase(":",font));
                pDTable.addCell(new Phrase("Rs. "+argAmount.toString(),dfont));

                pDTable.addCell(new Phrase("Total Amount",dfont));
                pDTable.addCell(new Phrase(":",font));
                pDTable.addCell(new Phrase("Rs. "+argTotalAmount.toString(),dfont));

                pDTable.addCell(new Phrase("EMI PerMonth",dfont));
                pDTable.addCell(new Phrase(":",font));
                pDTable.addCell(new Phrase("Rs. "+argEMI.toString(),dfont));

                pDTable.addCell(new Phrase("No of Months",dfont));
                pDTable.addCell(new Phrase(":",font));
                if(args.containsKey("years")) {
                    pDTable.addCell(new Phrase(argYears.toString()+" years",dfont));
                }
                else
                    pDTable.addCell(new Phrase(argPeriod.toString()+" months",dfont));


                pDTable.addCell(new Phrase("Interest %(PerYear)",dfont));
                pDTable.addCell(new Phrase(":",font));
                double t = argInterest*12*100;
                pDTable.addCell(new Phrase(Double.toString(t),dfont));

                createAndDisplayPdf(table,pDTable);
            }
        });
        return  myView;
    }
    private void createAndDisplayPdf(PdfPTable pTable,PdfPTable pDTable) {

        Document doc = new Document();
        String name = null;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();
            if(EMIType.equals("Reducing")){
                name = "ReducingEMI_Table.pdf";
            }
            else if (EMIType.equals("Flat")){
                name = "FlatEMI_Table.pdf";
            }
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            doc.add(pDTable);
            doc.add(new Paragraph("\n"));
            doc.add(pTable);

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
