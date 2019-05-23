 package com.example.sales_partner_v21.SellersLogin;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.example.sales_partner_v21.R;
import com.example.sales_partner_v21.ViewModel.SellersModel;

import java.util.List;

 public class SellersLogin extends AppCompatActivity {

    private EditText Getseller;
    private  EditText Getpassword;

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
        test = findViewById(R.id.button_test);
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
                            if (FlagSeller.equals(seller.toString()) && FlagPassword.equals(password.toString())){
                                model.getSellerIdAndName("%"+ seller + "%", "%" + password + "%").observe(SellersLogin.this, new Observer<Sellers>() {
                                    @Override
                                    public void onChanged(@Nullable Sellers sellers) {
                                        int id = sellers.getId();
                                        String name = sellers.getUserName() + " "+ Integer.toString(id);

                                        Toast.makeText(SellersLogin.this, name , Toast.LENGTH_SHORT).show();


                                    }
                                });
                            }else if (FlagPassword.equals(password.toString()) && !FlagSeller.equals(seller.toString())){
                                detail.setText("Quers decir: " + FlagSeller);
                            }else  if (seller.toString().isEmpty() || seller == null || password.toString().isEmpty() || password == null) {
                                Toast.makeText(SellersLogin.this, "Ingrese Usuario o contraseña ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }

    }

