package com.crypto.cryptogateway;

import androidx.appcompat.app.AppCompatActivity;
import com.crypto.cryptogateway.R;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toaster toaster=new Toaster();
        toaster.toast(this,"Hai");
    }
}