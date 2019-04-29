package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.util.List;

class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtMounth;
        private TextView txtSales;
        private TextView txtincomes;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);


            txtMounth = itemView.findViewById(R.id.txt_sales_mounth);
            txtincomes = itemView.findViewById(R.id.txt_income);
            txtSales = itemView.findViewById(R.id.txt_number_sales);

            itemView.setOnClickListener((View.OnClickListener) this);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}

public class Sales_summary_mounth extends AppCompatActivity {

    public static final int SALES_SUMMARY_MOUNTH_REQUEST_CODE = 1;

    public List<String> mounths;
    private AppDatabase database;
    private OrdersDao ordersDao;
    private OrdersAssembliesDao ordersAssembliesDao;

    private List<Orders> ordersList;
    private List<String> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary_mounth);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        ordersDao = database.ordersDao();
        ordersAssembliesDao = database.ordersAssembliesDao();

        ordersList = ordersDao.getAllorders();



        mounths.add("Enero");
        mounths.add("Febrero");
        mounths.add("Marzo");
        mounths.add("Abril");
        mounths.add("Mayo");
        mounths.add("Junio");
        mounths.add("Julio");
        mounths.add("Agosto");
        mounths.add("Septiembre");
        mounths.add("Octubre");
        mounths.add("Nomviembre");
        mounths.add("Diciembre");

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sales_summary_mounth.this, ReportsActivity.class);
        Sales_summary_mounth.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
}
