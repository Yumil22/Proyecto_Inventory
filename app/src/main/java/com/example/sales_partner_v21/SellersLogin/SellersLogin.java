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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_login);

        Getseller = findViewById(R.id.seller_login);
        Getpassword = findViewById(R.id.seller_password);
        test = findViewById(R.id.button_test);

        model = ViewModelProviders.of(this).get(SellersModel.class);

        seller = Getseller.getText();
        password = Getpassword.getText();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getUserByLoginPassword("%"+ seller + "%", "%" + password + "%").observe(SellersLogin.this, new Observer<Sellers>() {
                    @Override
                    public void onChanged(@Nullable Sellers sellers) {
                        String FlagPrincipal =sellers.getUserName();

                        Toast.makeText(SellersLogin.this, name , Toast.LENGTH_SHORT).show();


                        if (FlagPrincipal.equals(seller.toString())){
                            model.getSellerIdAndName("%"+ seller + "%", "%" + password + "%").observe(SellersLogin.this, new Observer<Sellers>() {
                                @Override
                                public void onChanged(@Nullable Sellers sellers) {
                                    int id = sellers.getId();
                                    String name = sellers.getUserName() + " "+ Integer.toString(id);

                                    Toast.makeText(SellersLogin.this, name , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(SellersLogin.this, "No funciono", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
