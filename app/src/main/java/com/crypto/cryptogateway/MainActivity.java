package com.crypto.cryptogateway;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.cryptogateway.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.gateway_btn);
        button.setOnClickListener(view -> {
            {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://pro.nexdha.com/api/order_id",
                        response -> {
                            try {
                                JSONObject respontran = new JSONObject(response);
                                String n_order_id = respontran.getString("transactionid");
                                System.out.println("this is the trrrrrrrrrrrrrrrrrrrrrransaaaaaaaaaaaaaction id");
                                System.out.println(n_order_id);


                                Toaster toaster = new Toaster();
                                toaster.setName("Deepak");
                                toaster.setAmount("200");
                                toaster.setEmail("deepak@gmail.com");
                                toaster.setPhone("0957605768");
                                toaster.setAmount("200");
                                toaster.setApiKey("4cae0046204c42559d4d164b0f9c1234");
                                toaster.setOrderId(n_order_id);
                                WebViewActivity webViewActivity = new WebViewActivity(toaster, this);
                                webViewActivity.initialPaymentProcess();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";

                        if (error instanceof NetworkError) {
                            message = "cannot connect";
                        } else if (error instanceof AuthFailureError) {
                            message = "";
                        } else if (error instanceof NoConnectionError) {
                            message = "cannot connect";
                        } else if (error instanceof ParseError) {
                            message = "Connection Timedout";
                        }
                        if (!message.isEmpty()) {
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);


            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                String response = data.getStringExtra("PAYMENT_RESPONSE");
                System.out.println("Response: " + response);
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }

        }

    }
}