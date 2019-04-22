package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;

public class edit_custormer extends AppCompatActivity {

    public String CUSTOMER_ID = "CUSTOMER_ID";
    public int Customer_id;
    public static final int edit_REQUEST_CODE = 1;
    private EditText name;
    private EditText last_name;
    private EditText address ;
    private EditText e_mail ;
    private EditText phone1 ;
    private EditText phone2 ;
    private EditText phone3 ;
    private CheckBox check_e_mail;
    private CheckBox check_phone2;
    private CheckBox check_phone3;

    public Customers custumer;
    public Customers customer_edit;
    private CustomersDao dbCusDao;
    public boolean ema = true;
    public boolean pho2 = true;
    public boolean pho3 = true;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custormer);
        Intent intent = getIntent();
            Customer_id = intent.getIntExtra(CUSTOMER_ID, 0);
        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
         dbCusDao = dbCus.customersDao();
        custumer = dbCusDao.getCustomerById(Customer_id);

          name = findViewById(R.id.ed_first_name_dialog);
          last_name = findViewById(R.id.ed_last_name_dialog);
          address = findViewById(R.id.ed_address_dialog);
          e_mail = findViewById(R.id.ed_email_dialog);
          phone1 = findViewById(R.id.ed_phone1_dialog);
          phone2 = findViewById(R.id.ed_phone2_dialog);
          phone3 = findViewById(R.id.ed_phone3_dialog);
          check_e_mail = findViewById(R.id.chk_e_mail);
          check_phone2 = findViewById(R.id.chk_phone2);
          check_phone3 = findViewById(R.id.chk_phone3);

        Button cancel = findViewById(R.id.button_cancel);


        name.setText(custumer.getFirstName());
        last_name.setText(custumer.getLastName());
        address.setText(custumer.getAddress());
        e_mail.setText(custumer.getEmail());
        phone1.setText(custumer.getPhone1());
        phone2.setText(custumer.getPhone2());
        phone3.setText(custumer.getPhone3());
        //poner para tomar



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(edit_custormer.this);

                builder.setMessage("Are you sure you want get out? You will lost your changes").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(edit_custormer.this, ClientesActivity.class);
                        edit_custormer.super.finish();
                        startActivityForResult(intent2, ClientesActivity.CLIENTES_REQUEST_CODE);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });




        check_e_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (ema = true){
                    ema = false;
                }
            }
        });


        check_phone2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (pho2 = true){
                    pho2 = false;
                }
            }
        });

        check_phone3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pho3 = true){
                    pho3 = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_customer_button:

                if(name.getText().toString() == null){
                    customer_edit.setFirstName(custumer.getFirstName().toString());
                }else {
                    customer_edit.setFirstName(name.getText().toString());
                }
                if(last_name.getText().toString() == null){
                    customer_edit.setLastName(custumer.getLastName().toString());
                }else {
                    customer_edit.setLastName(last_name.getText().toString());
                }
                if(address.getText().toString() == null){
                    customer_edit.setAddress(custumer.getAddress().toString());
                }else {
                    customer_edit.setAddress(address.getText().toString());
                }
                if(e_mail.getText().toString() == null){
                    customer_edit.setEmail(custumer.getEmail().toString());
                }else {
                    customer_edit.setEmail(e_mail.getText().toString());
                }
                if(phone1.getText().toString() == null){
                    customer_edit.setPhone1(custumer.getPhone1().toString());
                }else {
                    customer_edit.setPhone1(phone1.getText().toString());
                }
                if(phone2.getText().toString() == null){
                    customer_edit.setPhone2(custumer.getPhone2().toString());
                }else {
                    customer_edit.setPhone2(phone2.getText().toString());
                }
                if(phone3.getText().toString() == null){
                    customer_edit.setPhone3(custumer.getPhone3().toString());
                }else {
                    customer_edit.setPhone3(phone3.getText().toString());
                }
                if(name.getText().toString() == null ||last_name.getText().toString() == null||address.getText().toString() == null
                || e_mail.getText().toString() == null || phone1.getText().toString() == null ||phone2.getText().toString() == null
                ||phone3.getText().toString() == null){
                    Toast.makeText(this, "La informacion en blanco fue puesta como la anterior a lo editado", Toast.LENGTH_LONG);
                }

                customer_edit.setId(custumer.getId());


                //agrego query para actualizar
                return true;



            default:   return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_custormer.this);

        builder.setMessage("Are you sure you want get out? You will lost your changes").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(edit_custormer.this, ClientesActivity.class);
                edit_custormer.super.finish();
                startActivityForResult(intent2, ClientesActivity.CLIENTES_REQUEST_CODE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        super.onBackPressed();
    }
}
