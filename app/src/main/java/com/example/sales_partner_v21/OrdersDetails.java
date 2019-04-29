package com.example.sales_partner_v21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderStatus;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

public class OrdersDetails extends AppCompatActivity {
    private int OrderID;
    private TextView clientText;
    private TextView statusText;
    private TextView dateText;
    private RecyclerView assembliesRecycler;
    private RecyclerView changesLogRecycler;

    private Orders orderDetails;
    private Customers customerDetails;
    private OrderStatus orderStatusDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);

        clientText = findViewById(R.id.client_text);
        statusText = findViewById(R.id.status_text);
        dateText = findViewById(R.id.date_text);
        assembliesRecycler = findViewById(R.id.assemblies_RecyclerView);
        changesLogRecycler = findViewById(R.id.recyclerView_changes);

        Intent details = getIntent();
        OrderID = details.getIntExtra("ORDER_ID",0);

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao customersDao = database.customersDao();
        OrderStatusDao orderStatusDao = database.orderStatusDao();
        OrdersDao ordersDao = database.ordersDao();
        OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();

        orderDetails = ordersDao.getOrderByID(OrderID);
        customerDetails = customersDao.getCustomerById(orderDetails.getCustomer_id());
        orderStatusDetails = orderStatusDao.getOrderStatusByID(orderDetails.getStatus_id());


    }
}
