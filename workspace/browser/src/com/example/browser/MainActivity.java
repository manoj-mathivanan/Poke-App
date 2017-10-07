package com.example.browser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends Activity {
	private WebView mWebView;
	boolean loadingFinished = true;
	boolean redirect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
 
        mWebView.loadUrl("https://sprintphotondemo.hana.ondemand.com/login.jsp");     
        mWebView.setWebViewClient(new WebViewClient() {
        	   @Override
        	   public void onPageFinished(WebView view, String url) {
        	          String javas = "javascript:function E(){document.getElementById(\"logOnForm\").j_username.value = 'I074667';document.getElementById(\"logOnForm\").j_password.value = '2MYnameis';document.getElementById(\"logOnForm\").logOnFormSubmit.click()}E()";
        	          mWebView.loadUrl(javas);
        	      
        	    }
        	});
    }

}