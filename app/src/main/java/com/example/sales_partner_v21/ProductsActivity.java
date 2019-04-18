package com.example.sales_partner_v21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;

public class ProductsActivity extends AppCompatActivity {

    public static final int PRODUCTS_REQUEST_CODE =1;
    private Spinner categoriesSpinner;
    private EditText searchProduct;
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        categoriesSpinner = findViewById(R.id.categories_spinner);
        searchProduct = findViewById(R.id.search_product);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
        ProductsDao productsDao = database.productsDao();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductsActivity.this, MainActivity.class);
        ProductsActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }
}
