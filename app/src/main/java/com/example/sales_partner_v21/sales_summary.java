package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Intent;
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

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.AssembliesProducts;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

class ProductsAdapter2 extends RecyclerView.Adapter<ProductsAdapter2.ViewHolder> {

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

    public ProductsAdapter2(List<Products> products, ViewHolder.onProductListener onProductListener){
        this.products = products;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view,onProductListener);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary);


        missing = findViewById(R.id.missing);
        recycler_reports = findViewById(R.id.recycler_reports);

        database = AppDatabase.getAppDatabase(getApplicationContext());

        productsDao = database.productsDao();
        assembliesProductsDao = database.assembliesProductsDao();
        ordersDao = database.ordersDao();
        ordersAssembliesDao = database.ordersAssembliesDao();



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(sales_summary.this, ReportsActivity.class);
        sales_summary.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
}
