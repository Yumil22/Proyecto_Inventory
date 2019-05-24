package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.sales_partner_v21.SellersLogin.SellersLogin;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    ImageButton productsBtn;
    ImageButton ReportsBtn;
    ImageButton OrdersBtn;
    ImageButton ClientsBtn;
    ImageButton AssembliesBtn;
    ImageButton InfoBtn;

    Button Login, Logout;

    Boolean log;
    private RequestQueue queue;

    public static final int PRINCIPAL_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
        log = configPreferences.getBoolean("CODIGOLOGIN", false);

        productsBtn = findViewById(R.id.productsBtn);
        ReportsBtn = findViewById(R.id.ReportsBtn);
        OrdersBtn = findViewById(R.id.OrdersBtn);
        ClientsBtn = findViewById(R.id.ClientsBtn);
        AssembliesBtn = findViewById(R.id.AssembliesBtn);
        InfoBtn = findViewById(R.id.InfoBtn);
        Login = findViewById(R.id.log_in_button);
        Logout = findViewById(R.id.log_out_button);

        queue = Volley.newRequestQueue(this);

        if(log==true){
            Logout.setEnabled(true);
            Login.setEnabled(false);
            productsBtn.setEnabled(true);
            ReportsBtn.setEnabled(true);
            OrdersBtn.setEnabled(true);
            ClientsBtn.setEnabled(true);
            AssembliesBtn.setEnabled(true);
        }else if (log==false){

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Inicion de sesion");
            builder.setMessage("Es necesario iniciar sesion para acceder a las opciones");
            builder.setNeutralButton("Ok",null);
            Dialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);


            Logout.setEnabled(false);
            Login.setEnabled(true);
            productsBtn.setEnabled(false);
            ReportsBtn.setEnabled(false);
            OrdersBtn.setEnabled(false);
            ClientsBtn.setEnabled(false);
            AssembliesBtn.setEnabled(false);
        }

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

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellersLogin.class);
                MainActivity.super.finish();
                startActivityForResult(intent, SellersLogin.SELLERS_REQUEST_CODE);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Cerrar Sesion");
                builder.setMessage("No podras acceder a ninguna opción");
                builder.setPositiveButton("Cerrar sesion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences newassembly = getSharedPreferences("LOG", 0);
                        SharedPreferences.Editor asemblyEditor = newassembly.edit();
                        asemblyEditor.putBoolean("CODIGOLOGIN", false);
                        asemblyEditor.apply();
                        Logout.setEnabled(false);
                        Login.setEnabled(true);
                        productsBtn.setEnabled(false);
                        ReportsBtn.setEnabled(false);
                        OrdersBtn.setEnabled(false);
                        ClientsBtn.setEnabled(false);
                        AssembliesBtn.setEnabled(false);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel,null);
                Dialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
            }
        });
    }

    private void ObtenerDatosVolley(){
        String url = "https:";
    }
}
