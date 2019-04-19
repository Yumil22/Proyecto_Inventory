package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class new_user extends AppCompatActivity {

    public static final int NEW_USER_REQUEST_CODE =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        EditText name = findViewById(R.id.add_first_name);
        EditText last_name = findViewById(R.id.add_last_name);
        EditText address = findViewById(R.id.add_address);
        EditText e_mail = findViewById(R.id.add_email);
        EditText phone1 = findViewById(R.id.add_phone1);
        EditText phone2 = findViewById(R.id.add_phone2);
        EditText phone3 = findViewById(R.id.add_phone3);
        Button add = findViewById(R.id.button_add);
        Button cancel = findViewById(R.id.button_cancel_new);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //query para agregar
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
            }
        });
    }
}
