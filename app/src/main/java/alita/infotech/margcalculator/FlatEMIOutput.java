package alita.infotech.margcalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.margcalculator.R;
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

public class FlatEMIOutput extends Fragment {
    private View view;
    private Double loanAmount=0.0;
    private Double interestRate=0.0;
    private Integer period=0;
    private Double processingFees=0.0;
    private Double EMI=0.0;
    private Double totalInterest=0.0;
    private Double totalAmount=0.0;
    private Double processingRate=0.0;
    private Button Share;
    private Button EMIDetails;
    private String periodType;
    private Double years=0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emi_output, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Flat Interest");

        TextView Loan_Amount = view.findViewById(R.id.Oloan_amount_value);
        TextView Interest_Rate = view.findViewById(R.id.Ointerest_rate_value);
        TextView Period = view.findViewById(R.id.Operiod_value);
        TextView Processing_Rate = view.findViewById(R.id.Oprocessing_fees_value);

        TextView Total_Amount_output = view.findViewById(R.id.total_amount_output);
        TextView Total_Interest_output = view.findViewById(R.id.total_interest_output);
        TextView EMI_PerMonth = view.findViewById(R.id.EMI_output);
        TextView Processing_fees_output = view.findViewById(R.id.processing_fees_output);

        Bundle args = getArguments();

        loanAmount = args.getDouble("loan_amount");
        interestRate = args.getDouble("interest_rate");
        period = args.getInt("period");
        processingFees = args.getDouble("processing_fees");
        EMI = args.getDouble("loan_emi");
        totalInterest = args.getDouble("total_interest");
        totalAmount = args.getDouble("total_amount");
        processingRate = (processingFees*100)/loanAmount;

        if(args.containsKey("years")) {
            periodType = "YR";
            years = args.getDouble("years");
            Period.setText(years.toString()+" years");
        }
        else
            Period.setText(period.toString() + "months");
        Loan_Amount.setText(loanAmount.toString());
        Interest_Rate.setText(interestRate.toString()+" %");
        Processing_Rate.setText(processingRate.toString()+" %");
        Total_Amount_output.setText(totalAmount.toString());
        Total_Interest_output.setText(totalInterest.toString());
        EMI_PerMonth.setText(EMI.toString());
        Processing_fees_output.setText(processingFees.toString());

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
                if(EMI!=0.0 && totalAmount!=0.0){
                    args.putDouble("EMI",EMI);
                    args.putDouble("TotalAmount",totalAmount);
                    args.putDouble("Interest",interestRate);
                    args.putInt("Period",period);
                    args.putDouble("Amount",loanAmount);
                    args.putString("EMI Type","Flat");
                    if(years!=0.0) {
                        args.putDouble("years", years);
                    }
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

        return view;
    }
    private void createAndDisplayPdf() {

        if(EMI==0.0 && totalAmount == 0.0){
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

        ITable.addCell(new Phrase("Loan Details",paraFont));
        ITable.addCell(new Phrase("",paraFont));
        ITable.addCell(new Phrase("",paraFont));

        ITable.addCell(new Phrase("\n",paraFont));
        ITable.addCell(new Phrase("\n",paraFont));
        ITable.addCell(new Phrase("\n",paraFont));

        ITable.addCell(new Phrase("Loan Amount",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase("Rs. "+loanAmount.toString(),font));

        ITable.addCell(new Phrase("Interest %(Per Year)",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(interestRate.toString()+" %",font));

        ITable.addCell(new Phrase("No of Months",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(period.toString(),font));

        ITable.addCell(new Phrase("Processing Fees",font));
        ITable.addCell(new Phrase(":",font));
        ITable.addCell(new Phrase(processingRate.toString()+" %",font));

        OTable.getDefaultCell().setBorder(0);
        OTable.addCell(new Phrase("Repayment Calculation\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));
        OTable.addCell(new Phrase("\n",paraFont));

        OTable.addCell(new Phrase("EMI PerMonth",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+EMI.toString(),font));

        OTable.addCell(new Phrase("Processing Fees",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+processingFees.toString(),font));

        OTable.addCell(new Phrase("Total Interest",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+totalInterest.toString(),font));

        OTable.addCell(new Phrase("Total Amount",font));
        OTable.addCell(new Phrase(":",font));
        OTable.addCell(new Phrase("Rs. "+totalAmount.toString(),font));

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
        pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}
