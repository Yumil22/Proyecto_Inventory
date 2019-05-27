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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.Sellers;
import com.example.sales_partner_v21.Database.SellersDao;
import com.example.sales_partner_v21.MainActivity;
import com.example.sales_partner_v21.ProductsActivity;
import com.example.sales_partner_v21.R;
import com.example.sales_partner_v21.ViewModel.SellersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

 public class SellersLogin extends AppCompatActivity {

    private EditText Getseller;
    private  EditText Getpassword;
     public static final int SELLERS_REQUEST_CODE =1;

    private Editable seller;
    private Editable password;

    private SellersModel model;

    private Button test;
    private Button refresh;

    private  List<Sellers>  testsellers ;

    private TextView detail;

     public RequestQueue request;

     @Override
     public void onBackPressed() {
         super.onBackPressed();
         Intent intent = new Intent(SellersLogin.this, MainActivity.class);
         SellersLogin.super.finish();
         startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
     }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_login);

        Getseller = findViewById(R.id.seller_login);
        Getpassword = findViewById(R.id.seller_password);
        test = findViewById(R.id.button_log);
        detail = findViewById(R.id.details_sm);
        refresh = findViewById(R.id.refresh_button);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                SellersDao sellersDao = database.sellersDao();
                sellersDao.DeleteSellersTable();
                request = Volley.newRequestQueue(SellersLogin.this);
                cargarWebServise(); //GENERO LA LISTA DE NUEVOS USUARIOS, POR LO TANTO REALIZO EL UPDATE GENERAL DE LA TABLA
            }
        });
//AQUI VOY A REALIZAR LA ACTUALIZACION DE LOS USUARIOS


//AQUI VOY A REALIZAR LA ACTUALIZACION DE LOS USUARIOS


        model = ViewModelProviders.of(this).get(SellersModel.class);

            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    seller = Getseller.getText();
                    password = Getpassword.getText();

                    if (seller.toString().isEmpty() || seller == null || password.toString().isEmpty() || password == null){
                        Toast.makeText(SellersLogin.this, "No se detecta el usuario o contraseña", Toast.LENGTH_SHORT).show();
                    }else if (!seller.toString().isEmpty() || seller != null || !password.toString().isEmpty() || password != null){

                        model.getUserByLoginPassword("%"+ seller + "%", "%" + password + "%").observe(SellersLogin.this, new Observer<Sellers>() {
                            @Override
                            public void onChanged(@Nullable Sellers sellers) {

                                String FlagSeller =sellers.getUserName();

                                String FlagPassword = sellers.getPassword();
                              if   (FlagSeller.equals(seller.toString()) && FlagPassword.equals(password.toString())) {
                                  int SellersId = sellers.getId();
                                    SharedPreferences newassembly = getSharedPreferences("LOG", 0);
                                    SharedPreferences.Editor asemblyEditor = newassembly.edit();
                                    asemblyEditor.putBoolean("CODIGOLOGIN", true);
                                    asemblyEditor.putInt("IDSELLER", SellersId);
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

                }
            });

        }
     private JSONObject jsonObject;
     private JSONArray jsonArray;
     private String first_name_list = "";
     private String id_list = "";
     private String last_name_list = "";
     private String user_name_list = "";
     private String password_list = "";
     private List<Sellers> sellersRemoteDatabase = new ArrayList<>();
     private List<Sellers> sellersRemoteDatabase2 = new ArrayList<>();
     private void cargarWebServise() {
         AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
         final SellersDao sellersDao = database.sellersDao();

         String url = "http://192.168.1.101:3000/sellers/"  ;

         JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                 new Response.Listener<JSONArray>()
                 {
                     @Override
                     public void onResponse(JSONArray response) {
                         // display response
                           Toast.makeText(SellersLogin.this, "WEB SERVICE LOGIN", Toast.LENGTH_LONG).show();
                         jsonArray = response;

                         try {
                                 for(int i =0; i<= jsonArray.length();i++){
                                     jsonObject = jsonArray.getJSONObject(i);
                                     id_list = jsonObject.getString("id");
                                     first_name_list = jsonObject.getString("first_name");
                                     last_name_list = jsonObject.getString("last_name");
                                     user_name_list = jsonObject.getString("user_name");
                                     password_list = jsonObject.getString("password");
                                 sellersRemoteDatabase.add(new Sellers(Integer.valueOf(id_list) ,String.valueOf(first_name_list), String.valueOf(last_name_list),String.valueOf(user_name_list),String.valueOf(password_list) ));
                                 sellersDao.InsertSeller(new Sellers(Integer.valueOf(id_list) ,String.valueOf(first_name_list), String.valueOf(last_name_list),String.valueOf(user_name_list),String.valueOf(password_list)));
                                 Toast.makeText(SellersLogin.this, "ACTUALIZANDO SELLERS...", Toast.LENGTH_LONG).show();
                                 /*SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
                                 SharedPreferences.Editor idEditor= configPreferences.edit();
                                 idEditor.putInt("IDSELLER", -1);
                                 idEditor.apply();*/
                             }
                         } catch (JSONException e) {

                             e.printStackTrace();
                             Toast.makeText(SellersLogin.this, "FALLA 1", Toast.LENGTH_LONG).show();
                         }
                         sellersRemoteDatabase2 = sellersRemoteDatabase;

//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                        /** for(int i=0; i<sellersRemoteDatabase.size();i++){
                             sellersDao.InsertSeller(new Sellers(sellersRemoteDatabase.get().getId()));
                         }*/
                     }
                 },
                 new Response.ErrorListener()
                 {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Toast.makeText(SellersLogin.this, error.toString() + "Error", Toast.LENGTH_LONG).show();


                     }
                 }
         );
         //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
         request.add(getRequest);
     }

    }

