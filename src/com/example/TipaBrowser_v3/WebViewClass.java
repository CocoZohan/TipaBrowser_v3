package com.example.TipaBrowser_v3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.app.Activity;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Dos on 24.06.2014.
 */
public class WebViewClass extends Activity {

    final String LOG_TAG = "myLogs";
    DBControlClass dbControlClass;
    WebView webView;
    boolean firstPage;
    int sessionNo, urlNo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        firstPage = true;
        dbControlClass = new DBControlClass(this);

        webView = (WebView)findViewById(R.id.webView);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String initialPage = intent.getStringExtra("initialUrl");


        webView.loadUrl(data.toString());

        webView.setWebViewClient(new MyWebViewClient());
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
                dbControlClass.insertSessionIntoDB(sessionNo, "session 1");
                firstPage = false;
            }
            urlNo++;
            dbControlClass.insertUrlIntoDB(url, sessionNo, urlNo);

            dbControlClass.readAllSessionsFromDB();
            dbControlClass.readAllUrlsFromDB();
        }
    }

}
