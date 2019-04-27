package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class orders_simulator extends AppCompatActivity {

    public static final int  ORDERS_SIMULATOR_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_simulator);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(orders_simulator.this, ReportsActivity.class);
        orders_simulator.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
}
