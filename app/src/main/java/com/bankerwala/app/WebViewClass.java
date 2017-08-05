package com.bankerwala.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bankerwala.app.R;

/**
 * Created by mosenthi on 21-Sep-16.
 */
public class WebViewClass extends Fragment {


    private final String taglaunch = "TAGLAUNCH";
    private ProgressDialog progressBar;
    WebView myWebView;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(taglaunch, "WebView Fragment Created");

        return inflater.inflate(R.layout.webviewdisplay, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(taglaunch, "Menu Triggered " + menu.toString());
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.webview_display, menu);
        Drawable drawable = menu.findItem(R.id.action_popOut).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(),R.color.colorBGwhite));
        menu.findItem(R.id.action_popOut).setIcon(drawable);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {
            myWebView.loadUrl(url);
            return false;
        }
        else if(id==R.id.action_popOut){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null){
            ((Appdrawer)getActivity()).setNavIcon();
        }

        myWebView = (WebView) getActivity().findViewById(R.id.webViewdisplay);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        Context context = getActivity(); // or getBaseContext(), or getApplicationContext()
        int resId = getResources().getIdentifier(getUrlFromStringPool(),"string", context.getPackageName());
        url = getString(resId);
        Log.d(taglaunch, "onViewCreated - WebViewClass");
        myWebView.loadUrl(url);

    }
    final String fd = "fd";

    public String getTitleFromParent(){

        return getArguments().getString(((Appdrawer) getActivity()).BankName);
    }

    public String getInterestType(){

        return getArguments().getString(((Appdrawer) getActivity()).interestType);
    }

    public String getUrlFromStringPool(){
        String[] split = getTitleFromParent().split("\\s+");
        StringBuffer buffer= new StringBuffer();
        for(String s : split){
            buffer.append(s+"_");
        }
        buffer.append("url_"+getInterestType());
        Log.i(taglaunch,"created url "+buffer.toString());
        return buffer.toString();
    }

    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ((Appdrawer) getActivity())
                .setActionBarTitle(getTitleFromParent());
        ((Appdrawer) getActivity())
                .setNavIcon();
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Appdrawer) getActivity()).onSupportNavigateUp();
            }
        });
    }


    @Override
    public void onPause() {

        super.onPause();
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ((Appdrawer) getActivity()).navigate();
        ((Appdrawer) getActivity())
                .setActionBarTitle(getString(R.string.app_name));
    }
    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar = new ProgressDialog(getActivity());
            progressBar.setIndeterminate(true);
            progressBar.setIndeterminateDrawable(getResources()
                    .getDrawable(R.drawable.progressbar_handler));
            progressBar.setMessage("Loading...");
            progressBar.show();
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        public void onPageFinished(WebView view, String url) {
            Log.i(taglaunch, "Finished loading URL: " + url);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }
}


