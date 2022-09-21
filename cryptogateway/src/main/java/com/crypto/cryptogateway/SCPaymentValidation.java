package com.crypto.cryptogateway;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SCPaymentValidation {
    private Activity context = null;
    private final HashMap<String, String> params = new LinkedHashMap<>();

    public SCPaymentValidation(SCParams scParams, Activity context) {
        this.context = context;
        if (TextUtils.isEmpty(scParams.getClientName()) && TextUtils.isEmpty(scParams.getClientAmount()) &&
                TextUtils.isEmpty(scParams.getClientEmail()) && TextUtils.isEmpty(scParams.getClientApiKey()) &&
                TextUtils.isEmpty(scParams.getClientTransaction()) && TextUtils.isEmpty(scParams.getClientPhone())
                &&TextUtils.isEmpty(scParams.getClientHash())) {
            throw new RuntimeException("Oops something went wrong");
        } else {
            this.params.put(SCConstant.NAME, scParams.getClientName());
            this.params.put(SCConstant.AMOUNT, scParams.getClientAmount());
            this.params.put(SCConstant.EMAIL, scParams.getClientEmail());
            this.params.put(SCConstant.API_KEY, scParams.getClientApiKey());
            this.params.put(SCConstant.ORDER_ID, scParams.getClientTransaction());
            this.params.put(SCConstant.PHONE, scParams.getClientPhone());
            this.params.put(SCConstant.HASH, scParams.getClientHash());
        }
    }

    public void initialPaymentProcess() {
        Intent startActivity = new Intent(this.context, SCPaymentGateway.class);
        startActivity.putExtra(SCConstant.POST_PARAMS, this.params);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.context.startActivityForResult(startActivity, SCConstant.REQUEST_CODE);

    }
}