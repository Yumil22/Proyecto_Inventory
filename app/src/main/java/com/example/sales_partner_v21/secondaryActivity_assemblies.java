package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.AssembliesProducts;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


class ProductAssemblyAdapter extends RecyclerView.Adapter<ProductAssemblyAdapter.ViewHolder>{
    private List<Products> products;
    private List<AssembliesProducts> requiredProducts;
    private ViewHolder.onProductListener onProductListener;

    public ProductAssemblyAdapter(List<Products> products, List<AssembliesProducts> requiredProducts, ViewHolder.onProductListener onProductListener) {
        this.products = products;
        this.requiredProducts = requiredProducts;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ProductAssemblyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_assembly_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view,onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAssemblyAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(products.get(i),requiredProducts.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView productDescription;
        private TextView requiredProducts;
        private Products product;
        private AssembliesProducts assembliesProduct;

        onProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, onProductListener onProductListener) {
            super(itemView);
            productDescription = itemView.findViewById(R.id.productDescription);
            requiredProducts = itemView.findViewById(R.id.required_quantity);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Products product,AssembliesProducts assemblyProducts) {
            this.product = product;
            this.assembliesProduct = assemblyProducts;
            productDescription.setText(product.getDescription());
            requiredProducts.setText(String.valueOf(assemblyProducts.getQty()));
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }

        public interface onProductListener{
            void onProductClick(int position);
        }
    }
}

public class secondaryActivity_assemblies extends AppCompatActivity implements ProductAssemblyAdapter.ViewHolder.onProductListener{

    public final String ASSEMBLY_ID = "ASSEMBLY_ID";
    private int AssemblyID;
    public String ENSAMBLE_ID = "ENSAMBLE_ID";
    private TextView ensambleText;
    private RecyclerView productsRecyclerView;
    private TextView totalCostText;
    private Assemblies assembly;
    private List<Products> products;
    private List<AssembliesProducts> assembliesProducts;
    public String PRODUCT_ID = "PRODUCT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_assemblies);

        Intent intent = getIntent();
        AssemblyID = intent.getIntExtra(ENSAMBLE_ID,0);

        NumberFormat formatter = new DecimalFormat("#,###");

        ensambleText = findViewById(R.id.assembly_text);
        productsRecyclerView = findViewById(R.id.recyclerView_products);
        totalCostText = findViewById(R.id.total_cost);

        if (savedInstanceState != null){
            AssemblyID = savedInstanceState.getInt(ASSEMBLY_ID);
        }

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        AssembliesDao assembliesDao = database.assembliesDao();
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        ProductsDao productsDao = database.productsDao();

        assembly = assembliesDao.getAssemblyByID(AssemblyID);

        ensambleText.setText(assembly.getDescription());
        totalCostText.setText("$ " + formatter.format(assembliesProductsDao.getCostByAssemblyID(AssemblyID)));

        products = productsDao.getProductsByIDs(assembliesProductsDao.getProductsIDsByAssemblyID(assembly.getId()));
        assembliesProducts = assembliesProductsDao.getAllAssembliesProducts(AssemblyID);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(new ProductAssemblyAdapter(products,assembliesProducts,this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productsRecyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        productsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ASSEMBLY_ID,AssemblyID);
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(secondaryActivity_assemblies.this,tertiaryActivity_assemblies.class);
        intent.putExtra(PRODUCT_ID,products.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        secondaryActivity_assemblies.super.finish();
    }
}
