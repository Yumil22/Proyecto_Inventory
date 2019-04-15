package com.example.sales_partner_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;

import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public static String CLIENTES_FLAG_KEY = "CLIENTES_FLAG_KEY";
    public static final int CLIENTES_REQUEST_CODE = 1;

    private RecyclerView recyclerView;

   // private List<Customers> customers;
   // public CustomerAdapter (List<Customers> customers){
   //     this.customers = customers;
   // }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Toolbar toolbar = findViewById(R.id.toolbar_clients);
        setSupportActionBar(toolbar);

        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao dbCusDao = dbCus.customersDao();
        List<Customers> customers = dbCusDao.getAllCustomers();

        recyclerView = findViewById(R.id.recycler_clients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setAdapter(new CustomerAdapter(customers));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( ClientesActivity.this, MainActivity.class);
        ClientesActivity.super.finish();
        startActivityForResult(intent , MainActivity.PRINCIPAL_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clients_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

  // @Override
  // public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
  //     getMenuInflater().inflate(R.menu.clients_menu, menu);
  //     super.onCreateContextMenu(menu, v, menuInfo);
  // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_button_item:
//comenzamos la funcion de busqueda
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.add_button_item:
//agregamos un activity para anexar a nuevo usuario
                Toast.makeText(this, "New Activity...", Toast.LENGTH_SHORT).show();

                return true;


                default:
                    return super.onOptionsItemSelected(item);


        }

    }
}
