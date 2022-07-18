package com.crypto.cryptogateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Toaster {
    String clientName, clientAmount;

    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setName(String name) {
        this.clientName = name;
    }

    public void setAmount(String amount) {
        this.clientAmount = amount;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientAmount() {
        return clientAmount;
    }


}
