package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

    private TextView txt_ema;
    private TextView txt_pho2;
    private TextView txt_pho3;
    private TextView txt_name;
    private TextView txt_lastname;
    private TextView txt_address;
    private TextView txt_email;
    private TextView txt_phone1;
    private TextView txt_phone2;
    private TextView txt_phone3;

    private Customers custumer;
    private Customers customer_edit;
    private CustomersDao dbCusDao;
    public boolean ema = false;
    public boolean pho2 = false;
    public boolean pho3 = false;
    private boolean control_edit = false;


    static final String NAME = "SAVED_NAME";
    static final String LAST_NAME = "SAVED_LAST_NAME";
    static final String ADDRESS = "SAVED_ADDRESS";
    static final String E_MAIL = "SAVED_E_MAIL";
    static final String PHONE_1 = "SAVED_PHONE_1";
    static final String PHONE_2 = "SAVED_PHONE_2";
    static final String PHONE_3 = "SAVED_PHONE_3";
    static final String BANDERA_EMAIL = "SAVED_BANDERA_EMAIL";
    static final String BANDERA_PHONE2 = "SAVED_BANDERA_PHONE2";
    static final String BANDERA_PHONE3 = "SAVED_BANDERA_PHONE3";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custormer);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        Customer_id = intent.getIntExtra(CUSTOMER_ID, 0);
        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
         dbCusDao = dbCus.customersDao();
        custumer = dbCusDao.getCustomerById(Customer_id);

          name = findViewById(R.id.edit_first_name_dialog);
          last_name = findViewById(R.id.edit_last_name_dialog);
          address = findViewById(R.id.edit_address_dialog);
          e_mail = findViewById(R.id.edit_email_dialog);
          phone1 = findViewById(R.id.edit_phone1_dialog);
          phone2 = findViewById(R.id.edit_phone2_dialog);
          phone3 = findViewById(R.id.edit_phone3_dialog);
          check_e_mail = findViewById(R.id.chk_e_mail_edit_edit);
          check_phone2 = findViewById(R.id.chk_phone2_edit_edit);
          check_phone3 = findViewById(R.id.chk_phone3_edit_edit);

        txt_name = findViewById(R.id.txt_edit_name);
        txt_lastname = findViewById(R.id.txt_edit_lastname);
        txt_address = findViewById(R.id.txt_edit_address);
        txt_phone1 = findViewById(R.id.txt_edit_phone1);


          txt_ema = findViewById(R.id.txt_em);
          txt_pho2 = findViewById(R.id.txt_pho2);
          txt_pho3 = findViewById(R.id.txt_pho3);

        Button cancel = findViewById(R.id.button_cancel);


        name.setText(custumer.getFirstName());
        last_name.setText(custumer.getLastName());
        address.setText(custumer.getAddress());
        e_mail.setText(custumer.getEmail());
        phone1.setText(custumer.getPhone1());
        phone2.setText(custumer.getPhone2());
        phone3.setText(custumer.getPhone3());
        //poner para tomar

        if(savedInstanceState != null){
            name.setText(savedInstanceState.getString(NAME));
            last_name.setText(savedInstanceState.getString(LAST_NAME));
            address.setText(savedInstanceState.getString(ADDRESS));
            e_mail.setText(savedInstanceState.getString(E_MAIL));
            phone1.setText(savedInstanceState.getString(PHONE_1));
            phone2.setText(savedInstanceState.getString(PHONE_2));
            phone3.setText(savedInstanceState.getString(PHONE_3));
            ema = savedInstanceState.getBoolean(BANDERA_EMAIL);
            pho2 = savedInstanceState.getBoolean(BANDERA_PHONE2);
            Customer_id = savedInstanceState.getInt("CUSTOMER_ID");
            pho3 = savedInstanceState.getBoolean(BANDERA_PHONE3);
        }


        if(ema = true){
            check_e_mail.setChecked(true);
            e_mail.setFocusableInTouchMode(true);
        }else{
            check_e_mail.setChecked(false);
            e_mail.setFocusableInTouchMode(false);
        }
        if(pho2){
            check_phone2.setChecked(true);
            phone2.setFocusableInTouchMode(true);

        }else {
            check_phone2.setChecked(false);
            phone2.setFocusableInTouchMode(false);
        }
        if(pho3){
            check_phone3.setChecked(true);
            phone3.setFocusableInTouchMode(true);

        }else {
            check_phone3.setChecked(false);
            phone3.setFocusableInTouchMode(false);
        }



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



        if(custumer.getEmail() != null){
            check_e_mail.setChecked(true);
            e_mail.setFocusableInTouchMode(true);
        }
        if(custumer.getPhone2() != null){
            check_phone2.setChecked(true);
            phone2.setFocusableInTouchMode(true);
        }

        if(custumer.getPhone3() != null){
            check_phone3.setChecked(true);
            phone3.setFocusableInTouchMode(true);
        }

        check_e_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    e_mail.setFocusableInTouchMode(true);
                    ema = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    e_mail.setFocusableInTouchMode(false);
                    ema = false;

                }

            }
        });


        check_phone2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    phone2.setFocusableInTouchMode(true);
                    pho2 = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    phone2.setFocusableInTouchMode(false);
                    pho2 = false;
                }

            }
        });

        check_phone3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    phone3.setFocusableInTouchMode(true);
                    pho3 = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    phone3.setFocusableInTouchMode(false);
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
                String n;
                if(name.getText().length() == 0){
                     n =custumer.getFirstName();
                     control_edit = true;
                     txt_name.setTextColor(Color.RED);

                }else {
                     n = name.getText().toString();
                    txt_name.setTextColor(Color.GREEN);
                }
                String L;
                if(last_name.getText().length() == 0){
                    L = custumer.getLastName();
                    control_edit= true;
                    txt_lastname.setTextColor(Color.RED);

                }else {
                    L = last_name.getText().toString();
                    txt_lastname.setTextColor(Color.GREEN);

                }
                String ad;
                if(address.getText().length() == 0){
                    control_edit = true;
                    txt_address.setTextColor(Color.RED);
                    ad = null;
                }else {
                    ad = address.getText().toString();
                    txt_address.setTextColor(Color.GREEN);
                }
                String em;
                if(e_mail.getText().length() == 0){
                    em = null;

                    if(check_e_mail.isChecked()){
                        txt_ema.setTextColor(Color.RED);
                        control_edit = true;
                    }
                }else {
                    em = e_mail.getText().toString();

                }
                String ph;
                if(phone1.getText().length() == 0){
                    ph = custumer.getPhone1();
                    control_edit = true;
                    txt_phone1.setTextColor(Color.RED);

                }else {
                    ph =phone1.getText().toString();
                    txt_phone1.setTextColor(Color.GREEN);
                }
                String pho2;

                if(phone2.getText().length() == 0){
                    pho2 = null;
                    if(check_phone2.isChecked()){
                        txt_pho2.setTextColor(Color.RED);
                        control_edit = true;
                    }
                }else {
                    pho2 = phone2.getText().toString();
                    txt_pho2.setTextColor(Color.GREEN);
                }
                String pho3;
                if(phone3.getText().length() == 0){
                    pho3 = null;
                    if(check_phone3.isChecked()){
                        txt_pho3.setTextColor(Color.RED);
                        control_edit= true;

                    }
                }else {
                    pho3 = phone3.getText().toString();
                    txt_pho3.setTextColor(Color.GREEN);
                }
                if(control_edit){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(edit_custormer.this);

                    builder.setMessage("Completa los campos obligatorios").setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            return false;
                        }
                    }).setIcon(R.drawable.ic_warning_black_24dp).setTitle("WARNING");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    control_edit = false;
                    return true;
                }else{
                    AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                    CustomersDao customersDao = database.customersDao();

                    customer_edit = new Customers(custumer.getId(), n, L, ad,  ph, pho2, pho3, em );
                    new Dialog_customers(this ,customer_edit);
                    Customers auxCustomer = customersDao.getCustomerById(Customer_id);
                    customersDao.Deleteuser(auxCustomer);
                    customersDao.InsertNewUser(customer_edit);
                    edit_custormer.super.finish();
                    //agrego query para actualizar confirmo actualizacion con un Toast
                    control_edit = false;
                }

                return true;

            default:   return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_custormer.this);

        builder.setMessage("¿Seguro que quiere salir? Se perderan todos los datos").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit_custormer.super.finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.getString(NAME, name.getText().toString());
        savedInstanceState.putString(LAST_NAME, last_name.getText().toString());
        savedInstanceState.putString(ADDRESS, address.getText().toString());
        savedInstanceState.putString(E_MAIL, e_mail.getText().toString());
        savedInstanceState.putString(PHONE_1, phone1.getText().toString());
        savedInstanceState.putString(PHONE_2, phone2.getText().toString());
        savedInstanceState.putInt("CUSTOMER_ID",Customer_id);
        savedInstanceState.putString(PHONE_3, phone3.getText().toString());
        savedInstanceState.putBoolean(BANDERA_EMAIL, ema);
        savedInstanceState.putBoolean(BANDERA_PHONE2, pho2);
        savedInstanceState.putBoolean(BANDERA_PHONE3, pho3);
        super.onSaveInstanceState(savedInstanceState);
    }
}
