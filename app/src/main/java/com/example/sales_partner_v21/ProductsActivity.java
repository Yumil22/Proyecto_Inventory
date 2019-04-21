package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsCategories;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    private List<Products> products;
    private ViewHolder.onProductListener onProductListener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView description;
        private TextView productPrice;
        private TextView qtyStock;

        Products product;
        NumberFormat formatter = new DecimalFormat("#,###");
        onProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, onProductListener onProductListener) {
            super(itemView);
            description = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.price_text);
            qtyStock = itemView.findViewById(R.id.qty_stock);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);

        }

        public void bind(Products product){
            this.product = product;
            description.setText(product.getDescription());
            productPrice.setText("$ " + formatter.format(product.getPrice()));
            qtyStock.setText(String.valueOf(product.getQty()));
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }

        public interface onProductListener{
            void onProductClick(int position);
        }
    }

    public ProductsAdapter(List<Products> products, ViewHolder.onProductListener onProductListener){
        this.products = products;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view,onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(products.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.ViewHolder.onProductListener{

    public static final int PRODUCTS_REQUEST_CODE =1;
    private Spinner categoriesSpinner;
    private EditText searchProduct;
    private RecyclerView productsRecyclerView;
    private Toolbar productsToolbar;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> categories;
    private List<ProductsCategories> productsCategories;

    private List<Products> products;
    private boolean SEARCH_PRESS = false;
    private String SEARCH_TEXT = "SEARCH_TEXT";
    private String CATEGORY_SELECTED = "CATEGORY_SELECTED";
    private String SEARCH_BUTTON_PRESS = "SEARCH_BUTTON_PRESS";
    private String PRODUCT_ID = "PRODUCT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        categoriesSpinner = findViewById(R.id.categories_spinner);
        searchProduct = findViewById(R.id.search_product);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsToolbar = findViewById(R.id.toolbar_products);
        setSupportActionBar(productsToolbar);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
        ProductsDao productsDao = database.productsDao();

        productsCategories = productsCategoriesDao.getAllProductsCategories();
        categories = new ArrayList<String>(productsCategoriesDao.getAllProductsDescriptionCategories());
        categories.add("Todos");
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categories);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(arrayAdapter);

        if (savedInstanceState != null){
            searchProduct.setText(savedInstanceState.getString(SEARCH_TEXT));
            categoriesSpinner.setSelection(savedInstanceState.getInt(CATEGORY_SELECTED));
            SEARCH_PRESS = savedInstanceState.getBoolean(SEARCH_BUTTON_PRESS);
            if (SEARCH_PRESS){
                if (searchProduct.getText().toString().isEmpty()){
                    if (categoriesSpinner.getSelectedItemPosition() < 7){
                        products = productsDao.getProductsByCategoryID(categoriesSpinner.getSelectedItemPosition());
                    }
                    else{
                        products = productsDao.getAllProducts();
                    }
                }
                else {
                    if (categoriesSpinner.getSelectedItemPosition() < 7){
                        products = productsDao.getProductsByCategoryIDAndDescription(categoriesSpinner.getSelectedItemPosition(),searchProduct.getText().toString());
                    }
                    else{
                        products = productsDao.getProductsByDescription(searchProduct.getText().toString());
                    }
                }
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                productsRecyclerView.setAdapter(new ProductsAdapter(products,this));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:{
                Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
                AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
                ProductsDao productsDao = database.productsDao();
                if (searchProduct.getText().toString().isEmpty()){
                    if (categoriesSpinner.getSelectedItemPosition() < 7){
                        products = productsDao.getProductsByCategoryID(categoriesSpinner.getSelectedItemPosition());
                    }
                    else{
                        products = productsDao.getAllProducts();
                    }
                }
                else {
                    if (categoriesSpinner.getSelectedItemPosition() < 7){
                        products = productsDao.getProductsByCategoryIDAndDescription(categoriesSpinner.getSelectedItemPosition(),searchProduct.getText().toString());
                    }
                    else{
                        products = productsDao.getProductsByDescription(searchProduct.getText().toString());
                    }
                }
                if (products == null){
                    Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Se han encontrado " + String.valueOf(products.size()) + " similitudes",Toast.LENGTH_SHORT).show();
                }
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                productsRecyclerView.setAdapter(new ProductsAdapter(products,this));
                SEARCH_PRESS = true;
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products_menu,menu);
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.products_menu,menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductsActivity.this, MainActivity.class);
        ProductsActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SEARCH_BUTTON_PRESS,SEARCH_PRESS);
        outState.putInt(CATEGORY_SELECTED,categoriesSpinner.getSelectedItemPosition());
        outState.putString(SEARCH_TEXT,searchProduct.getText().toString());
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(this,secondaryActivity_product.class);
        intent.putExtra(PRODUCT_ID,products.get(position).getId());
        startActivity(intent);
    }
}
