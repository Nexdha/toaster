package com.crypto.cryptogateway;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crypto.cryptogateway.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.gateway_btn);
        button.setOnClickListener(view -> {

            Toaster toaster = new Toaster();
            toaster.setName("Deepak");
            toaster.setAmount("200");
            WebViewActivity webViewActivity = new WebViewActivity(toaster, this);
            webViewActivity.initialPaymentProcess();
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