package com.example.sales_partner_v21;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button productsBtn;
    Button ReportsBtn;
    Button OrdersBtn;
    Button ClientsBtn;
    Button AssembliesBtn;
    Button InfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsBtn = findViewById(R.id.productsBtn);
        ReportsBtn = findViewById(R.id.ReportsBtn);
        OrdersBtn = findViewById(R.id.OrdersBtn);
        ClientsBtn = findViewById(R.id.ClientsBtn);
        AssembliesBtn = findViewById(R.id.AssembliesBtn);
        InfoBtn = findViewById(R.id.InfoBtn);


    }
}
