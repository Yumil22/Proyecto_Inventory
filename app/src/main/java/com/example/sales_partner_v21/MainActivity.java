package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton productsBtn;
    ImageButton ReportsBtn;
    ImageButton OrdersBtn;
    ImageButton ClientsBtn;
    ImageButton AssembliesBtn;
    ImageButton InfoBtn;

    public static final int PRINCIPAL_REQUEST_CODE = 1;


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

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, ProductsActivity.PRODUCTS_REQUEST_CODE);
            }
        });

        ReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
            }
        });

        OrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, OrdersActivity.ORDERS_REQUEST_CODE);
            }
        });

        ClientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(MainActivity.this, ClientesActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent2, ClientesActivity.CLIENTES_REQUEST_CODE);
            }
        });

        AssembliesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EnsamblesActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, EnsamblesActivity.ENSAMBLES_REQUEST_CODE);
            }
        });

        InfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Made in Mexico by Yumil Rueda Flores Trejo and Alejandro Matos")/*.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }) */;
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
}
