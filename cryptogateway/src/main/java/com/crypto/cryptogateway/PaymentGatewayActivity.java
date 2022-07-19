package com.crypto.cryptogateway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class PaymentGatewayActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    String name, amount, email, apiKey, orderId, hash, phone;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        webView = this.findViewById(R.id.web_view);
        progressBar = this.findViewById(R.id.progress_bar);
        HashMap hashMap = (HashMap) this.getIntent().getSerializableExtra("POST_PARAMS");
        name = (String) hashMap.get("Name");
        amount = (String) hashMap.get("Amount");
        email = (String) hashMap.get("Email");
        apiKey = (String) hashMap.get("ApiKey");
        orderId = (String) hashMap.get("OrderId");
        hash = (String) hashMap.get("Hash");
        phone = (String) hashMap.get("Phone");
        loadUrl();


        try {
            WebView.setWebContentsDebuggingEnabled(false);
            this.webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    PaymentGatewayActivity.this.progressBar.setVisibility(View.GONE);

                }


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    PaymentGatewayActivity.this.progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    PaymentGatewayActivity.this.progressBar.setVisibility(View.GONE);
                    return super.shouldOverrideUrlLoading(view, request);

                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    PaymentGatewayActivity.this.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    progressBar.setVisibility(View.GONE);
                }

            });


            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    android.util.Log.d("WebView", consoleMessage.message());
                    System.out.println(consoleMessage.message());
                    String hash = consoleMessage.message();
                    if (hash.contains("transaction_hash")) {
                        Intent data = new Intent();
                        data.putExtra("PAYMENT_RESPONSE", hash);
                        PaymentGatewayActivity.this.setResult(-1, data);
                        PaymentGatewayActivity.this.finish();
                    }
                    return true;
                }
            });

            WebSettings webSettings = this.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            this.webView.addJavascriptInterface(new PaymentGatewayActivity.MyJavaScriptInterface(this), "Android");
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);


        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String str = stringWriter.toString();
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        }
    }


    private void loadUrl() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://pred.nbicoin.com/api/sdk_transfer", response -> {
            try {
                JSONObject json = new JSONObject(response);
                String url = json.getString("url");
                webView.loadUrl(url);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error instanceof ClientError);
            String message = "";
            if (error instanceof NetworkError) {
                message = "Cannot connect to internet......please check your internet connection";
            } else if (error instanceof ServerError) {
                message = "";
            } else if (error instanceof AuthFailureError) {
                message = "Cannot connect to internet......please check your internet connection";
            } else if (error instanceof ParseError) {
                message = "Cannot connect to internet......please check your internet connection";
            } else if (error instanceof TimeoutError) {
                message = "Connection Timedout......please check your internet connection";
            }
            if (!message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("amount", amount);
                params.put("api_key", apiKey);
                params.put("transaction_id", orderId);
                params.put("hash", hash);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setShouldCache(false);
        queue.add(request);


    }

    public class MyJavaScriptInterface {
        Context context;

        MyJavaScriptInterface(Context context) {
            this.context = context;

        }

        @JavascriptInterface
        public void showHTML(String html, String url) {
            Log.i("log", "showHTML: " + url + " : " + html);
        }

        @JavascriptInterface
        public void paymentResponse(String response) {
            try {

                Log.d("ResponseJSON: ", response);
                if (response.equals("null")) {
                    Intent data = new Intent();
                    data.putExtra("PAYMENT_RESPONSE", "hai");
                    PaymentGatewayActivity.this.setResult(-1, data);
                    PaymentGatewayActivity.this.finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}