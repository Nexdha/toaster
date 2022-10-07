package com.crypto.cryptogateway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SCPaymentGateway extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    String name, amount, email, apiKey, orderId, hash, phone, os;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        webView = this.findViewById(R.id.web_view);
        progressBar = this.findViewById(R.id.progress_bar);
        HashMap hashMap = (HashMap) this.getIntent().getSerializableExtra(SCConstant.POST_PARAMS);
        name = (String) hashMap.get(SCConstant.NAME);
        amount = (String) hashMap.get(SCConstant.AMOUNT);
        email = (String) hashMap.get(SCConstant.EMAIL);
        apiKey = (String) hashMap.get(SCConstant.API_KEY);
        orderId = (String) hashMap.get(SCConstant.ORDER_ID);
        hash = (String) hashMap.get(SCConstant.HASH);
        phone = (String) hashMap.get(SCConstant.PHONE);
        os = (String) hashMap.get(SCConstant.OS);
        loadUrl();


        try {
            WebView.setWebContentsDebuggingEnabled(false);
            this.webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    SCPaymentGateway.this.progressBar.setVisibility(View.GONE);

                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    SCPaymentGateway.this.progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    SCPaymentGateway.this.progressBar.setVisibility(View.GONE);
                    return super.shouldOverrideUrlLoading(view, request);

                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    SCPaymentGateway.this.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    progressBar.setVisibility(View.GONE);
                }

            });

            WebSettings webSettings = this.webView.getSettings();
            this.webView.addJavascriptInterface(new MyJavascriptInterface(this), "Android");
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);


        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String str = stringWriter.toString();
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SCPaymentGateway.this);
                builder.setMessage(SCConstant.ALERT_MESSAGE);
                builder.setTitle(SCConstant.ALERT_TITLE);
                builder.setPositiveButton(SCConstant.OK, (dialogInterface, i) -> {
                    Intent data = new Intent();
                    data.putExtra(SCConstant.PAYMENT_RESPONSE, SCConstant.CANCELLED);
                    SCPaymentGateway.this.setResult(-1, data);
                    SCPaymentGateway.this.finish();
                    super.onBackPressed();
                });
                builder.setNegativeButton(SCConstant.CANCEL, (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public class MyJavascriptInterface {
        Context context;

        MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void paymentResponse(String response) {
            Intent data = new Intent();
            data.putExtra(SCConstant.PAYMENT_RESPONSE, response);
            SCPaymentGateway.this.setResult(-1, data);
            SCPaymentGateway.this.finish();

        }
    }

    private void loadUrl() {
        StringRequest request = new StringRequest(Request.Method.POST, SCConstant.POST_URL, response -> {
            try {
                JSONObject json = new JSONObject(response);
                String url = json.getString(SCConstant.POST_RESPONSE);
                webView.loadUrl(url);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error instanceof ClientError);
            String message = "";
            if (error instanceof NetworkError) {
                message = SCConstant.ERROR;
            } else if (error instanceof ServerError) {
                message = SCConstant.ERROR;
            } else if (error instanceof AuthFailureError) {
                message = SCConstant.ERROR;
            } else if (error instanceof ParseError) {
                message = SCConstant.ERROR;
            } else if (error instanceof TimeoutError) {
                message = SCConstant.ERROR;
            }
            if (!message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SCConstant.POST_NAME, name);
                params.put(SCConstant.POST_EMAIL, email);
                params.put(SCConstant.POST_PHONE, phone);
                params.put(SCConstant.POST_AMOUNT, amount);
                params.put(SCConstant.POST_API_KEY, apiKey);
                params.put(SCConstant.POST_ORDER_ID, orderId);
                params.put(SCConstant.POST_HASH, hash);
                params.put(SCConstant.POST_OS, os);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setShouldCache(false);
        queue.add(request);


    }
}