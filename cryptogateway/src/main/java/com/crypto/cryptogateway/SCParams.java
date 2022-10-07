package com.crypto.cryptogateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SCParams {
    String clientName;
    String clientAmount;
    String clientEmail;
    String clientPhone;
    String clientApiKey;
    String clientTransaction;
    String clientHash;
    String clientOs;


    public void setAmount(String amount) {
        this.clientAmount = amount;
    }

    public void setName(String name) {
        this.clientName = name;
    }

    public void setEmail(String email) {
        this.clientEmail = email;
    }

    public void setApiKey(String key) {
        this.clientApiKey = key;
    }

    public void setOrderId(String orderId) {
        this.clientTransaction = orderId;
    }

    public void setPhone(String phone) {
        this.clientPhone = phone;
    }

    public void setClientHash(String clientHash) {
        this.clientHash = clientHash;
    }

    public void setClientOs(String clientOs) {
        this.clientOs = clientOs;
    }


    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientApiKey() {
        return clientApiKey;
    }

    public String getClientTransaction() {
        return clientTransaction;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientAmount() {
        return clientAmount;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getClientHash() {
        return clientHash;
    }

    public String getClientOs() {
        return clientOs;
    }

}
