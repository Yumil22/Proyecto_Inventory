package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;

public class edit_custormer extends AppCompatActivity {

    public String CUSTOMER_ID = "CUSTOMER_ID";
    public int Customer_id;
    public static final int edit_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custormer);
        Intent intent = getIntent();
            Customer_id = intent.getIntExtra(CUSTOMER_ID, 0);
        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao dbCusDao = dbCus.customersDao();
        Customers custumer = dbCusDao.getCustomerById(Customer_id);

        EditText name = findViewById(R.id.ed_first_name_dialog);
        EditText last_name = findViewById(R.id.ed_last_name_dialog);
        EditText address = findViewById(R.id.ed_address_dialog);
        EditText e_mail = findViewById(R.id.ed_email_dialog);
        EditText phone1 = findViewById(R.id.ed_phone1_dialog);
        EditText phone2 = findViewById(R.id.ed_phone2_dialog);
        EditText phone3 = findViewById(R.id.ed_phone3_dialog);
        Button edit = findViewById(R.id.button_edit);
        Button cancel = findViewById(R.id.button_cancel);


        name.setText(custumer.getFirstName());
        last_name.setText(custumer.getLastName());
        address.setText(custumer.getAddress());
        e_mail.setText(custumer.getEmail());
        phone1.setText(custumer.getPhone1());
        phone2.setText(custumer.getPhone2());
        phone3.setText(custumer.getPhone3());
        //poner para tomar

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try and catch para ver si se logro actualizar los datos
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(edit_custormer.this, ClientesActivity.class);
                edit_custormer.super.finish();
                startActivityForResult(intent2, ClientesActivity.CLIENTES_REQUEST_CODE);
            }
        });
    }
}
