package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsCategories;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class tertiaryActivity_assemblies extends AppCompatActivity {

    private int ProductID;
    public String PRODUCT_ID = "PRODUCT_ID";
    TextView Category;
    TextView Description;
    TextView Price;
    TextView Quantity;
    private Products product;
    private String productCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tertiary_assemblies);

        Intent intent = getIntent();
        ProductID = intent.getIntExtra(PRODUCT_ID,0);

        Category = findViewById(R.id.category_text);
        Description = findViewById(R.id.description_text);
        Price = findViewById(R.id.price_text);
        Quantity = findViewById(R.id.qty_text);
        NumberFormat formatter = new DecimalFormat("#,###");

        if (savedInstanceState != null){
            ProductID = savedInstanceState.getInt(PRODUCT_ID);
        }

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        ProductsDao productsDao = database.productsDao();
        ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();

        product = productsDao.getProductByID(ProductID);
        productCategory = productsCategoriesDao.getCategoryByProductCategoryID(ProductID);

        Category.setText(productCategory);
        Description.setText(product.getDescription());
        Price.setText("$ " + formatter.format(product.getPrice()));
        Quantity.setText(String.valueOf(product.getQty()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PRODUCT_ID,ProductID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tertiaryActivity_assemblies.super.finish();
    }
}
