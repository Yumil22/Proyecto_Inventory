 package com.example.sales_partner_v21.SellersLogin;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.Sellers;
import com.example.sales_partner_v21.MainActivity;
import com.example.sales_partner_v21.ProductsActivity;
import com.example.sales_partner_v21.R;
import com.example.sales_partner_v21.ViewModel.SellersModel;

import java.util.List;

 public class SellersLogin extends AppCompatActivity {

    private EditText Getseller;
    private  EditText Getpassword;
     public static final int SELLERS_REQUEST_CODE =1;

    private Editable seller;
    private Editable password;

    private SellersModel model;

    private Button test;

    private  List<Sellers>  testsellers ;

    private TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_login);

        Getseller = findViewById(R.id.seller_login);
        Getpassword = findViewById(R.id.seller_password);
        test = findViewById(R.id.button_log);
        detail = findViewById(R.id.details_sm);

        model = ViewModelProviders.of(this).get(SellersModel.class);

        seller = Getseller.getText();
        password = Getpassword.getText();

            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.getUserByLoginPassword("%"+ seller + "%", "%" + password + "%").observe(SellersLogin.this, new Observer<Sellers>() {
                        @Override
                        public void onChanged(@Nullable Sellers sellers) {
                            String FlagSeller =sellers.getUserName();

                            String FlagPassword = sellers.getPassword();
                           if (seller.toString().isEmpty() || seller == null || password.toString().isEmpty() || password == null){
                               Toast.makeText(SellersLogin.this, "No se detecta el usuario o contraseña", Toast.LENGTH_SHORT).show();
                           }else if (FlagSeller.equals(seller.toString()) && FlagPassword.equals(password.toString())) {
                               SharedPreferences newassembly = getSharedPreferences("LOG", 0);
                               SharedPreferences.Editor asemblyEditor = newassembly.edit();
                               asemblyEditor.putBoolean("CODIGOLOGIN", true);
                               asemblyEditor.apply();
                               Intent intent = new Intent(SellersLogin.this, MainActivity.class);
                               SellersLogin.super.finish();
                               startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);                           
                           }else if (!FlagSeller.equals(seller.toString()) || !FlagPassword.equals(password.toString())){
                               Toast.makeText(SellersLogin.this, "Ingresa un usuario o contraseña correcta", Toast.LENGTH_SHORT).show();
                           }
                        }
                    });


                }
            });

        }

    }

