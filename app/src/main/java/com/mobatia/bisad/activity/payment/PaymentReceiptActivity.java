package com.mobatia.bisad.activity.payment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.mobatia.bisad.manager.AppController;
import com.mobatia.bisad.manager.PreferenceData;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentReceiptActivity extends Activity {
    Context context;
    AppController appController;
    String student_name;

    public static void main(String[] args){

    }
    void loadWebViewWithDataPrint(WebView paymentWeb,BufferedReader br,
                                 String student_name,
                                  String account_code,
                                  String pupil_code,String academic_year,String invoice_ref,
                                  String invoice_description,String current_amount,String vat_percentage,
                                  String vat_amount,String total_amount,String due_date,
                                  String paid_date,String payment_type,String paid_by,String title,
                                  String payment_type_print,String order_id,String orderRef,
                                  String thankyou_note,String trn_pay) {
        this.context = context;
        appController= new AppController();
        student_name=student_name;
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            sb = new StringBuffer();
            eachLine = br.readLine();
            Log.e("appcontline",eachLine);
            while (eachLine != null) {
                sb.append(eachLine);
                sb.append("\n");
                eachLine =br.readLine();
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }


       String fullHtml = sb.toString();

        if (fullHtml.length() > 0) {
           // Calendar c = Calendar.getInstance();

            String invoicenote="This payment was done by "+paid_by+" for "+ student_name+" via BIS_AD Mobile App";
            //fullHtml = fullHtml.replace("###amount###", current_amount);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            fullHtml = fullHtml.replace("###amount###", current_amount);
//            fullHtml = fullHtml.replace("###order_Id###", orderId);
            fullHtml = fullHtml.replace("###ParentName###",student_name);
            fullHtml = fullHtml.replace("###Date###",formattedDate );
            fullHtml = fullHtml.replace("###paidBy###", thankyou_note );
            fullHtml = fullHtml.replace("###billing_code###", account_code);
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", invoice_description);
            fullHtml = fullHtml.replace("###trn_no###", trn_pay);
            fullHtml = fullHtml.replace("###order_Id###", invoice_ref);
            fullHtml = fullHtml.replace("###payment_type###", payment_type_print);
            fullHtml = fullHtml.replace("###percentageAmount###", vat_amount);
            fullHtml = fullHtml.replace("###full-amount###", total_amount);
            fullHtml = fullHtml.replace("###percent###", "("+vat_percentage+"%)");

            paymentWeb.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");



        }

    }
}
