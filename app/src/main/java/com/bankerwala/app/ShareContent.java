package com.bankerwala.app;

import android.content.Context;
import android.content.Intent;

/**
 * Created by mosenthi on 12-Oct-16.
 */
public class ShareContent {

    private String input;
    private Context context;



    public ShareContent(String input, Context context) {
        this.input = input;
        this.context = context;
    }

    ShareContent( Context context) {
        this.context = context;
    }

    public void shareData(String subject) {

        String CalculatedBy = "\n\nCalculated by https://play.google.com/store/apps/details?id="+this.context.getPackageName();
        context.startActivity(Intent.createChooser(createIntent().putExtra(Intent.EXTRA_TEXT, input+CalculatedBy).putExtra(Intent.EXTRA_SUBJECT, subject), "Share the results via..."));
    }

    public void shareApp() {
        String DownloadApp = "Banker Wala : https://play.google.com/store/apps/details?id="+this.context.getPackageName();
        context.startActivity(Intent.createChooser(createIntent().putExtra(Intent.EXTRA_TEXT,DownloadApp).putExtra(Intent.EXTRA_SUBJECT, "Banker Wala"), "Share the results via..."));
    }

    private Intent createIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        return sendIntent;
    }


}
