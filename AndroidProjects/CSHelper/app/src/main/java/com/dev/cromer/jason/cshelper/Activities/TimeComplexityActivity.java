package com.dev.cromer.jason.cshelper.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;

public class TimeComplexityActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView thisWebView;
    private ImageView thisBackButton;

    static String THIS_URL = "https://en.wikipedia.org/wiki/Sorting_algorithm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_complexity);

        thisWebView = (WebView) findViewById(R.id.timeComplexityWebView);
        thisBackButton = (ImageView) findViewById(R.id.timeComplexityBackButton);
        thisBackButton.setOnClickListener(this);

        initializeWebView();
    }



    public void initializeWebView() {
        final Activity thisActivity = this;

        //Set progress meter for webview loading
        thisWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                thisActivity.setProgress(progress * 1000);
            }
        });


        //Handle errors loading webview
        thisWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingURL) {
                Toast.makeText(thisActivity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        thisWebView.loadUrl(THIS_URL);
    }


    @Override
    public void onClick(View v) {
        if(v == thisBackButton) {
            finish();
        }
    }
}