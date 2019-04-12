package com.example.sales_partner_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class EnsamblesActivity extends AppCompatActivity {

    public static final int ENSAMBLES_REQUEST_CODE =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensambles);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnsamblesActivity.this, MainActivity.class);
        EnsamblesActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }
}
