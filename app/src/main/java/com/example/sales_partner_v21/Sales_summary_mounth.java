package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sales_summary_mounth extends AppCompatActivity {

    public static final int SALES_SUMMARY_MOUNTH_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary_mounth);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sales_summary_mounth.this, ReportsActivity.class);
        Sales_summary_mounth.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
}
