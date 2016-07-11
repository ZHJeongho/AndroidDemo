package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jeongho on 16/7/11.
 */
public class WebActivity extends BaseActivity{

    private WebView mWebView;
    private Button mTestHttpBtn;
    @Override
    public void initLayout() {
        setContentView(R.layout.activity_web);
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.wv);
        mTestHttpBtn = (Button) findViewById(R.id.btn_test_http);
        mTestHttpBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_http:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testHttpUrlConnection();
                    }
                }).start();

                break;
        }
    }

    private void testHttpUrlConnection() {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("http://www.baidu.com");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            Log.d("stringBuilder", stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpURLConnection.disconnect();
        }
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        context.startActivity(intent);
    }
}
