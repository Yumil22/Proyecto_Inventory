package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.AssembliesProducts;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class ProductsAdapter2 extends RecyclerView.Adapter<ProductsAdapter2.ViewHolder> {

    private List<Products> products;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView description;

        private TextView qtyStock;
        private int qty_final;

        Products product;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.productDescription_missing);
            qtyStock = itemView.findViewById(R.id.qty_stock_missing);

        }

        public void bind(Products product){
            this.product = product;
            description.setText(product.getDescription());

            //Lo que almacene en category_id es la cantidad en STOCK
            qty_final = product.getCategoryId() - product.getQty();
            if( qty_final < 0){
                qtyStock.setTextColor(Color.RED);
            }else{
                qtyStock.setTextColor(Color.RED);
            }
            qtyStock.setText(String.valueOf(qty_final).toString());
        }


    }

    public ProductsAdapter2(List<Products> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_missing,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter2.ViewHolder viewHolder, int i) {
        viewHolder.bind(products.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}


public class sales_summary extends AppCompatActivity {

    //ERROR EN EL NOMBRE JEJEJ REALMENTE ES EL DE PRODUCOTS FALTANTES

    public static final int SALES_SUMMARY_REQUEST_CODE = 1;

    private TextView missing;
    private RecyclerView recycler_reports;
    private Adapter adapter;
    private List<Orders> orders;
    private AppDatabase database;
    private ProductsDao productsDao;
    private AssembliesProductsDao assembliesProductsDao;
    private OrdersDao ordersDao;
    private OrdersAssembliesDao ordersAssembliesDao;


    private List<Products> productsInventory;
    private List<Products> poductsNeeded;

    private List<Products> productsList;
    private List<OrderAssemblies> orderAssembliesList;
    private List<Orders> ordersList;
    private List<AssembliesProducts> assembliesProductsList;

    private List<OrderAssemblies> orderAssembliesList2 ;
    private List<AssembliesProducts> assembliesProductsList2;
    private List<Products> productsList2;
    public ArrayList list = new ArrayList();

    int idSeller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary);

        SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
        idSeller = configPreferences.getInt("IDSELLER", -1);

        missing = findViewById(R.id.missing);
        recycler_reports = findViewById(R.id.recycler_reports);

        database = AppDatabase.getAppDatabase(getApplicationContext());

       productsDao = database.productsDao();
       assembliesProductsDao = database.assembliesProductsDao();
       ordersDao = database.ordersDao();
       ordersAssembliesDao = database.ordersAssembliesDao();

       //productsList = productsDao.getAllProducts();
       ordersList = ordersDao.getordersforComfirm(idSeller);
       assembliesProductsList = assembliesProductsDao.getAllAssemblies();
       orderAssembliesList = ordersAssembliesDao.getAllorderAssemblies();

        productsList= productsDao.getProductsMissing2(idSeller);
        //ESTA LISTA DE PRODUCTOS ESTA CON UN FORMATO DIFERENTE A LO NORMAL PARA EVITAR LA NECESIDAD DE UN NUEVO OBJETO
        // EN VES DE category_id ES LA CANTIDAD EN STOCK y el qty ES LA CANTIDAD NECESARIA PARA PODER CONFIRMAR LA SOSLICITUDES



        productsList2 = new ArrayList<>(list);

        if(productsList != null){

            for(int i=0; i <productsList.size();i++){

                if(productsList.get(i).getCategoryId() - productsList.get(i).getQty() > 0||productsList.get(i).getCategoryId() - productsList.get(i).getQty() == 0
                ||productsList.get(i).getCategoryId() - productsList.get(i).getQty() == 1){
                    productsList.remove(i);

                   // productsList.remove(productsList.get(i));
                }else {
                    productsList2.add(productsList.get(i));
                }
            }
            recycler_reports.setLayoutManager(new LinearLayoutManager(sales_summary.this));
            recycler_reports.setAdapter(new ProductsAdapter2(productsList2));

        }
        else{
            recycler_reports.setLayoutManager(new LinearLayoutManager(sales_summary.this));
            recycler_reports.setAdapter(new ProductsAdapter2(productsList2));
            Toast.makeText(this, "DONT", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(sales_summary.this, ReportsActivity.class);
        sales_summary.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
}
