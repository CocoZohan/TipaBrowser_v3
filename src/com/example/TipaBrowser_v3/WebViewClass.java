package com.example.TipaBrowser_v3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.app.Activity;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * Created by Dos on 24.06.2014.
 */
public class WebViewClass extends Activity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    DBControlClass dbControlClass;
    MyOnTouchListener touchListener;
    WebView webView;
    boolean firstPage;
    public int sessionNo, urlNo;
    AlertDialogClass alertDialogClass;
    Button btnGoToMain, btnGoToAllPages;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        btnGoToAllPages = (Button)findViewById(R.id.buttonGoToUrls);
        btnGoToMain = (Button)findViewById(R.id.buttonGoToMain);
        btnGoToMain.setOnClickListener(this);
        btnGoToAllPages.setOnClickListener(this);

        firstPage = true;
        dbControlClass = new DBControlClass(this);
        alertDialogClass = new AlertDialogClass();

        webView = (WebView)findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String initialPage = intent.getStringExtra("initialUrl");


        webView.loadUrl(data.toString());

        touchListener = new MyOnTouchListener(this);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setOnTouchListener(touchListener);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonGoToMain:
                onPause();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.buttonGoToUrls:
                onPause();
                alertDialogClass.showDialog(this, dbControlClass.returnUrlInSession(sessionNo));
                if(alertDialogClass.itemClicked){
                    webView.loadUrl(dbControlClass.readUrlFromDB(alertDialogClass.itemNo, sessionNo));
                }
        }
    }

    private class MyWebViewClient extends WebViewClient{

        @Override
        // show the web page in webview but not in web browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            Log.d(LOG_TAG, url);
            if(firstPage) {
                sessionNo = dbControlClass.findEmptySession();
                urlNo = 0;
                dbControlClass.insertSessionIntoDB(sessionNo, url);
                firstPage = false;
            }
            urlNo++;
            dbControlClass.insertUrlIntoDB(url, sessionNo, urlNo);
        }
    }

    private class MyOnTouchListener extends OnSwipeTouchListener {
        public MyOnTouchListener(Context ctx) {
            super(ctx);
        }

        public void onSwipeRight() {
            if(webView.canGoBack()){
                webView.goBack();
            }
        }

        public void onSwipeLeft() {
            if(webView.canGoForward()){
                webView.goForward();
            }
        }

        public void onSwipeBottom() {
            //webView.reload();
        }

    }

}
