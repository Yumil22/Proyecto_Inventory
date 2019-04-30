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
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;

import java.util.List;
class EstrangeAdapter extends RecyclerView.Adapter<EstrangeAdapter.ViewHolder> {

    public List<OrderAssemblies> orderAssembliesList;
    public List<String> asseblyDescription;
    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tat_assembly;
        private TextView txt_order_id;
        private TextView txt_quantity;
        private TextView txt_income_2;


        private AppDatabase database;
        private AssembliesDao assembliesDao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tat_assembly = itemView.findViewById(R.id.txt_sales_assembly);
            txt_order_id = itemView.findViewById(R.id.txt_order_id);
            txt_quantity = itemView.findViewById(R.id.txt_qty);
            txt_income_2 = itemView.findViewById(R.id.txt_price);

            database = AppDatabase.getAppDatabase(itemView.getContext());

            assembliesDao = database.assembliesDao();

        }
        public void bind(OrderAssemblies orderAssemblies, String s) {

            int aux = assembliesDao.getPriceyAssembled(orderAssemblies.getOrder_id());
            tat_assembly.setText(s);
            txt_income_2.setText(String.valueOf(orderAssemblies.getQty()*aux));
            txt_quantity.setText(String.valueOf(orderAssemblies.getQty()));
            txt_order_id.setText(String.valueOf(orderAssemblies.getOrder_id()));
        }
    }

    public EstrangeAdapter(List<OrderAssemblies> orderAssemblies, List<String> descriptionsAssembly){
        this.orderAssembliesList = orderAssemblies;
        this.asseblyDescription = descriptionsAssembly;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.secundary_sales_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new EstrangeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(orderAssembliesList.get(i), asseblyDescription.get(i));

    }

    @Override
    public int getItemCount() {
        return orderAssembliesList.size();
    }


}


public class secundary_Sales extends AppCompatActivity {

    public String DATE = "DATE";
    public String DATE_2 = "DATE_2";

    private String Date;
    private String Date2;

    public List<OrderAssemblies> orderAssembliesList;
    public AppDatabase database;
    public OrdersAssembliesDao ordersAssembliesDao ;

    public List<OrderAssemblies> ordersAssem;
    public List<String> descriptionAssemblies;

    public RecyclerView recyclersecundarySales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundary__sales);

        recyclersecundarySales = findViewById(R.id.recycler_secundary_sales);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        ordersAssembliesDao = database.ordersAssembliesDao();

        Intent intent = getIntent();
        Date = intent.getStringExtra(DATE);
        Date2 = intent.getStringExtra(DATE_2);

        ordersAssem = ordersAssembliesDao.getordersAssembliesbyDate(Date, Date2);
        descriptionAssemblies = ordersAssembliesDao.getDescriptionbyDate(Date, Date2);

        recyclersecundarySales.setLayoutManager(new LinearLayoutManager(secundary_Sales.this));
        recyclersecundarySales.setAdapter(new EstrangeAdapter(ordersAssem, descriptionAssemblies));



    }
}
