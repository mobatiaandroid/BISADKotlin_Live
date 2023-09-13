package com.mobatia.bisad.activity.canteen;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.mobatia.bisad.manager.AppController;

import java.io.BufferedReader;
import java.io.IOException;

public class PayCanteenRecActivity extends Activity {
    Context context;
    AppController appController;
    String student_name;
    public static void main(String[] args){

    }
    void loadWebViewWithDataPrint(WebView paymentWeb, BufferedReader br,
                                 String parent_name,String email,String id,
                                  String student_id,String user_id,String amount,String bill_no,
                                  String order_reference,String created_on,
                                  String invoice_note,String payment_type,
                                  String student_name,String trn_no) {
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
            fullHtml = fullHtml.replace("###amount###", amount);
            fullHtml = fullHtml.replace("###order_Id###", order_reference);
            fullHtml = fullHtml.replace("###ParentName###", student_name);
            fullHtml = fullHtml.replace("###Date###", created_on);
            fullHtml = fullHtml.replace("###paidBy###", invoice_note);
            fullHtml = fullHtml.replace("###billing_code###", bill_no);
            fullHtml = fullHtml.replace("###trn_no###", trn_no);
            fullHtml = fullHtml.replace("###payment_type###", payment_type);
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", "Title");

            paymentWeb.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");



        }

    }
}
