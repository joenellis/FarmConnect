package com.buah.farmconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String url = "http://www.facebook.com";

        WebView web = findViewById(R.id.webview);
        web.loadUrl(url);
    }
}
