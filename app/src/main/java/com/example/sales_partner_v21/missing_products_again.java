package com.example.sales_partner_v21;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ProductsAdapter3 extends RecyclerView.Adapter<ProductsAdapter3.ViewHolder> {

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

    public ProductsAdapter3(List<Products> products){
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
    public void onBindViewHolder(@NonNull ProductsAdapter3.ViewHolder viewHolder, int i) {
        viewHolder.bind(products.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

public class missing_products_again extends AppCompatActivity {

    public static final int  MISSING_PRODUCTS_AGAIN = 1;
    private String ID_CUSTOMER = "ID_CUSTOMER";
    private String VERIFICACION = "VERIFICACION";

    private int id_customer;
    private int verificacion;

    private TextView txt_auxiliar;
    private RecyclerView recyclerView_missing;
    public AppDatabase database;
    public ProductsDao productsDao;
    private List<Products> listmissing;
    private List<Products> listmissing2;
    private boolean cheching = false;
    private List<Products> listm;
    int idSeller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_products_again);


        SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
        idSeller = configPreferences.getInt("IDSELLER", -1);

        recyclerView_missing = findViewById(R.id.recycler_missing_products_again);
        txt_auxiliar = findViewById(R.id.txt_auxiliar);
        database = AppDatabase.getAppDatabase(getApplicationContext());
        productsDao = database.productsDao();

        Intent intent = getIntent();
        id_customer =  intent.getIntExtra(ID_CUSTOMER,0);
        verificacion = intent.getIntExtra(VERIFICACION, 0);

        listmissing2 = new ArrayList<>();
        listm = new ArrayList<>();
            listm = productsDao.getproductsNeededbyId(id_customer);


        if( cheching ){

            txt_auxiliar.setText("suficiente");
            txt_auxiliar.setTextColor(Color.GREEN);

        }else {

            listmissing = productsDao.getproductsNeededbyId(id_customer);
            for(int i=0; i <listmissing.size();i++){

                if(listmissing.get(i).getCategoryId() - listmissing.get(i).getQty() > 0||listmissing.get(i).getCategoryId() - listmissing.get(i).getQty() == 0
                        ||listmissing.get(i).getCategoryId() - listmissing.get(i).getQty() == 1){
                    listmissing.remove(i);

                }else {
                    listmissing2.add(listmissing.get(i));
                }
            }
            if(listmissing2.isEmpty()){
                txt_auxiliar.setText("HAY PRODUCTOS SUFICIENTES");
                txt_auxiliar.setTextColor(Color.GREEN);
            }
            recyclerView_missing.setLayoutManager(new LinearLayoutManager(missing_products_again.this));
            recyclerView_missing.setAdapter(new ProductsAdapter3(listmissing2));

        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(missing_products_again.this, orders_simulator.class);
        missing_products_again.super.finish();
        //startActivityForResult(intent, orders_simulator.ORDERS_SIMULATOR_REQUEST_CODE);
    }


}
