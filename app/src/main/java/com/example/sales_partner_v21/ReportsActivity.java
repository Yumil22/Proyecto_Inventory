package com.example.sales_partner_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class ReportsActivity extends AppCompatActivity {

    public static final int REPORTS_REQUEST_CODE = 1;

    private ImageButton missing_products;
    private ImageButton orders_simulator;
    private ImageButton sales_summary_mounth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        missing_products = findViewById(R.id.missing_products);
        orders_simulator = findViewById(R.id.simulator);
        sales_summary_mounth = findViewById(R.id.sales_summary);



        missing_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportsActivity.this, sales_summary.class);
                ReportsActivity.super.finish();
                startActivityForResult(intent , sales_summary.SALES_SUMMARY_REQUEST_CODE);
            }
        });

        orders_simulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, orders_simulator.class);
                ReportsActivity.super.finish();
                startActivityForResult(intent , com.example.sales_partner_v21.orders_simulator.ORDERS_SIMULATOR_REQUEST_CODE);
            }
        });

        sales_summary_mounth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsActivity.this, Sales_summary_mounth.class);
                ReportsActivity.super.finish();
                startActivityForResult(intent , Sales_summary_mounth.SALES_SUMMARY_MOUNTH_REQUEST_CODE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ReportsActivity.this, MainActivity.class);
        ReportsActivity.super.finish();
        startActivityForResult(intent , MainActivity.PRINCIPAL_REQUEST_CODE);
    }
}
