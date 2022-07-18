package com.crypto.cryptogateway;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WebViewActivity {
    private Activity context = null;
    private final HashMap<String, String> params = new LinkedHashMap<>();

    public WebViewActivity(Toaster toaster, Activity context) {
        this.context = context;
        if (TextUtils.isEmpty(toaster.getClientName()) && TextUtils.isEmpty(toaster.getClientAmount()) &&
                TextUtils.isEmpty(toaster.getClientEmail()) && TextUtils.isEmpty(toaster.getClientApiKey()) &&
                TextUtils.isEmpty(toaster.getClientTransaction()) & TextUtils.isEmpty(toaster.getClientPhone())) {
            throw new RuntimeException("Oops something went wrong");
        } else {
            this.params.put("Name", toaster.getClientName());
            this.params.put("Amount", toaster.getClientAmount());
            this.params.put("Email", toaster.getClientEmail());
            this.params.put("ApiKey", toaster.getClientApiKey());
            this.params.put("OrderId", toaster.getClientTransaction());
            this.params.put("Phone", toaster.getClientPhone());
            this.params.put("Hash", "ABCD");

        }
    }

    public void initialPaymentProcess() {
        Intent startActivity = new Intent(this.context, PaymentGatewayActivity.class);
        startActivity.putExtra("POST_PARAMS", this.params);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.context.startActivityForResult(startActivity, 11111);

    }
}