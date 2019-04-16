package com.example.sales_partner_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;
import com.facebook.stetho.Stetho;


public class EnsamblesActivity extends AppCompatActivity {

    public static final int ENSAMBLES_REQUEST_CODE =1;
    private RecyclerView assembliesRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensambles);

        Toolbar assembliesToolbar = findViewById(R.id.toolbar_assemblies);
        setSupportActionBar(assembliesToolbar);

        Stetho.initializeWithDefaults(this);

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        ProductsDao productsDao = database.productsDao();
        ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        AssembliesDao assembliesDao = database.assembliesDao();

        assembliesRecyclerView = findViewById(R.id.assemblies_RecyclerView);
        assembliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assemblies_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnsamblesActivity.this, MainActivity.class);
        EnsamblesActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }
}
