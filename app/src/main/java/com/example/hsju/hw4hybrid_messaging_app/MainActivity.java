package com.example.hsju.hw4hybrid_messaging_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new JavaScriptInterface(this), "Android"); //javascriptInterface 연결
// if the html file is in the app's memory space use:
        browser.loadUrl("file:///android_asset/webView.html"); //url load

    }//onCreate
}//class

