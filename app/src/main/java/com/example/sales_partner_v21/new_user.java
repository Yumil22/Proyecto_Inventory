package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;

public class new_user extends AppCompatActivity {

    public static final int NEW_USER_REQUEST_CODE =1;

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
    private Button add;
    private Button cancel;
    private TextView txt_name;
    private TextView txt_lastname;
    private TextView txt_address;
    private TextView txt_email;
    private TextView txt_phone1;
    private TextView txt_phone2;
    private TextView txt_phone3;
    private boolean control = false;

    public String em;
    public String n;
    public String L;
    public String ad;
    public String ph;
    public String pho2;
    public String pho3;


    private CustomersDao dbCusDao;
    private Customers new_customer;

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

    private boolean bandera_email = false;
    private boolean bandera_phone2 = false;
    private boolean bandera_phone3 =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);



        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        dbCusDao = dbCus.customersDao();

         name = findViewById(R.id.add_first_name);
         last_name = findViewById(R.id.add_last_name);
         address = findViewById(R.id.add_address);
         e_mail = findViewById(R.id.add_email);
         phone1 = findViewById(R.id.add_phone1);
         phone2 = findViewById(R.id.add_phone2);
         phone3 = findViewById(R.id.add_phone3);
         add = findViewById(R.id.button_add);
         cancel = findViewById(R.id.button_cancel_new);
         check_e_mail = findViewById(R.id.chk_e_mail_new);
         check_phone2 = findViewById(R.id.chk_phone2_new);
         check_phone3 = findViewById(R.id.chk_phone3_new);
         txt_name = findViewById(R.id.txt_name_new);
         txt_lastname = findViewById(R.id.txt_last_name_new);
         txt_address = findViewById(R.id.txt_address_new);
         txt_email = findViewById(R.id.txt_email_new);
         txt_phone1 = findViewById(R.id.txt_phone1_new);
         txt_phone2 = findViewById(R.id.txt_phone2_new);
         txt_phone3 = findViewById(R.id.txt_phone3_new);

        if(savedInstanceState != null){
            name.setText(savedInstanceState.getString(NAME));
            last_name.setText(savedInstanceState.getString(LAST_NAME));
            address.setText(savedInstanceState.getString(ADDRESS));
            e_mail.setText(savedInstanceState.getString(E_MAIL));
            phone1.setText(savedInstanceState.getString(PHONE_1));
            phone2.setText(savedInstanceState.getString(PHONE_2));
            phone3.setText(savedInstanceState.getString(PHONE_3));
            bandera_email = savedInstanceState.getBoolean(BANDERA_EMAIL);
            bandera_phone2 = savedInstanceState.getBoolean(BANDERA_PHONE2);
            bandera_phone3 = savedInstanceState.getBoolean(BANDERA_PHONE3);
        }

         if(bandera_email){
             check_e_mail.setChecked(true);
             e_mail.setFocusableInTouchMode(true);
         }else{
             check_e_mail.setChecked(false);
             e_mail.setFocusableInTouchMode(false);
         }
         if(bandera_phone2){
            check_phone2.setChecked(true);
             phone2.setFocusableInTouchMode(true);

         }else {
             check_phone2.setChecked(false);
             phone2.setFocusableInTouchMode(false);
         }
         if(bandera_phone3){
             check_phone3.setChecked(true);
             phone3.setFocusableInTouchMode(true);

         }else {
             check_phone3.setChecked(false);
             phone3.setFocusableInTouchMode(false);
         }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length() == 0){
                        txt_name.setTextColor(Color.RED);
                        control = true;

                }else {
                    n = name.getText().toString();
                    txt_name.setTextColor(Color.GREEN);
                }
                if(last_name.getText().length() == 0){
                    txt_lastname.setTextColor(Color.RED);
                    control = true;

                }else {
                    L = last_name.getText().toString();
                    txt_lastname.setTextColor(Color.GREEN);

                }
                if(address.getText().length() == 0){
                        txt_address.setTextColor(Color.RED);
                        control = true;
                }else {
                    ad = address.getText().toString();
                    txt_address.setTextColor(Color.GREEN);
                }
                if(e_mail.getText().length() == 0){
                    if(check_e_mail.isChecked()){
                        txt_email.setTextColor(Color.RED);
                        control = true;
                    }else {
                        em = null;
                    }

                }else {
                    em = e_mail.getText().toString();
                    txt_email.setTextColor(Color.GREEN);

                }
                if(phone1.getText().length() == 0){
                    txt_phone1.setTextColor(Color.RED);
                    control = true;
                }else {
                    ph =phone1.getText().toString();
                    txt_phone1.setTextColor(Color.GREEN);

                }

                if(phone2.getText().toString().length() == 0){
                    if(check_phone2.isChecked()){
                        txt_phone2.setTextColor(Color.RED);
                        control = true;
                    }else{
                        pho2 = null;
                    }
                }else {
                    pho2 = phone2.getText().toString();
                    txt_phone2.setTextColor(Color.GREEN);
                }

                if(phone3.getText().toString().length() == 0){
                    if(check_phone3.isChecked()){
                        txt_phone3.setTextColor(Color.RED);
                        control = true;
                    }else{
                        pho3 = null;
                    }
                }else {
                    pho3 = phone3.getText().toString();
                    txt_phone3.setTextColor(Color.GREEN);
                }

                //se puede implementar una manera de checar los id que faltan en la base de datos
                // para usarlos, se podria decr rellenar si es que se elimino un usuario

                if(control = false){
                    new_customer = new Customers(dbCusDao.getMaxId()+1, n, L, ad,  ph, pho2, pho3, em );
                    new Dialog_customers(new_user.this ,new_customer);
                    new_user.super.finish();
                    //agregar el query de insert
                }else{
                    control = false;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new_user.this);

                    builder.setMessage("Put all the information").setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            return false;
                        }
                    }).setIcon(R.drawable.ic_warning_black_24dp).setTitle("WARNING");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(new_user.this);

                builder.setMessage("Are you sure you want back? You will lose the information").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_user.super.finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //no realiza nada
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        check_e_mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    e_mail.setFocusableInTouchMode(true);
                    bandera_email = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    e_mail.setFocusableInTouchMode(false);
                    bandera_email = false;
                }

            }
        });
        check_phone2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    phone2.setFocusableInTouchMode(true);
                    bandera_phone2 = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    phone2.setFocusableInTouchMode(false);
                    bandera_phone3 = false;
                }

            }
        });

        check_phone3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(edit_custormer.this, "Selected", Toast.LENGTH_SHORT).show();
                    phone3.setFocusableInTouchMode(true);
                    bandera_phone3 = true;
                }else if(!isChecked){
                    //Toast.makeText(edit_custormer.this, "DON't selected", Toast.LENGTH_SHORT).show();
                    phone3.setFocusableInTouchMode(false);
                    bandera_phone3 = false;
                }


            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(NAME, name.getText().toString());
        savedInstanceState.putString(LAST_NAME, last_name.getText().toString());
      savedInstanceState.putString(ADDRESS, address.getText().toString());
      savedInstanceState.putString(E_MAIL, e_mail.getText().toString());
      savedInstanceState.putString(PHONE_1, phone1.getText().toString());
      savedInstanceState.putString(PHONE_2, phone2.getText().toString());
      savedInstanceState.putString(PHONE_3, phone3.getText().toString());
      savedInstanceState.putBoolean(BANDERA_EMAIL, bandera_email);
      savedInstanceState.putBoolean(BANDERA_PHONE2, bandera_phone2);
       savedInstanceState.putBoolean(BANDERA_PHONE3, bandera_phone3);
//

        super.onSaveInstanceState(savedInstanceState);


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(new_user.this);

        builder.setMessage("Are you sure you want back? You will lose the information").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new_user.super.finish();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //no realiza nada
            }
        }).setTitle("WARNING");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}
