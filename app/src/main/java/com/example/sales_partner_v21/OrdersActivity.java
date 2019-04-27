package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderStatus;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{
    private List<Customers> customers;
    private List<Orders> orders;
    private List<OrderStatus> orderStatuses;
    private List<Integer> qtyAssemblies;
    private List<Integer> totalCosts;
    private ViewHolder.onOrderListener onOrderListener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView client;
        private TextView status;
        private TextView date;
        private TextView assembliesQty;
        private TextView totalPrice;

        private Customers customer;
        private Orders order;
        private OrderStatus orderStatus;
        private int qtyAssemblies;
        private int totalCost;
        NumberFormat formatter = new DecimalFormat("#,###");
        onOrderListener onOrderListener;

        public ViewHolder(@NonNull View itemView, onOrderListener onOrderListener) {
            super(itemView);
            client = itemView.findViewById(R.id.client);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            assembliesQty = itemView.findViewById(R.id.qty_assemblies);
            totalPrice = itemView.findViewById(R.id.total_cost);
            this.onOrderListener = onOrderListener;
            itemView.setOnLongClickListener(this);
        }

        public void bind(Customers customer, Orders order, OrderStatus orderStatus, int qtyAssemblies, int totalCost){
            this.customer = customer;
            this.order = order;
            this.qtyAssemblies = qtyAssemblies;
            this.totalCost = totalCost;

            client.setText(customer.getLastName());
            status.setText(orderStatus.getDescription());
            date.setText(order.getDate());
            assembliesQty.setText(String.valueOf(qtyAssemblies));
            totalPrice.setText("$ " + formatter.format(totalCost));
        }

        @Override
        public boolean onLongClick(View v) {
            onOrderListener.onOrderLongClick(getAdapterPosition());
            return true;
        }

        public interface onOrderListener{
            void onOrderLongClick(int position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view,onOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(customers.get(i),orders.get(i),orderStatuses.get(i),qtyAssemblies.get(i),totalCosts.get(i));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}

public class OrdersActivity extends AppCompatActivity implements OrdersAdapter.ViewHolder.onOrderListener{
    public static final int ORDERS_REQUEST_CODE = 1;
    private Button initialDateBtn;
    private Button finalDateBtn;
    private CheckBox initialDateCheckbox;
    private CheckBox finalDateCheckbox;
    private Spinner orderStatusSpinner;
    private Spinner clientsSpinner;
    private Toolbar ordersToolbar;
    private RecyclerView ordersRecyclerView;
    private DatePickerDialog datePickerInitialDate;
    private DatePickerDialog datePickerFinalDate;
    private int initialYear;
    private int initialMonth;
    private int initialDayOfMonth;
    private int finalYear;
    private int finalMonth;
    private int finalDayOfMonth;
    private Calendar calendar;
    private ArrayAdapter<String> arrayAdapterClients;
    private OrderStatusAdapter orderStatusAdapter;
    private ArrayList<String> ordersStatusList;
    private ArrayList<OrderStatusItem> orderStatusItems = new ArrayList<>();
    private ArrayList<String> clientsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initialDateBtn = findViewById(R.id.button_initialDate);
        finalDateBtn = findViewById(R.id.button_finalDate);
        initialDateCheckbox = findViewById(R.id.checkbox_initialDate);
        finalDateCheckbox = findViewById(R.id.checkbox_finalDate);
        orderStatusSpinner = findViewById(R.id.orderStatus_spinner);
        clientsSpinner = findViewById(R.id.clients_spinner);
        ordersToolbar = findViewById(R.id.toolbar_orders);
        ordersRecyclerView = findViewById(R.id.orders_RecyclerView);

        setSupportActionBar(ordersToolbar);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao customersDao = database.customersDao();
        OrderStatusDao orderStatusDao = database.orderStatusDao();
        OrdersDao ordersDao = database.ordersDao();

        clientsList = new ArrayList<>(customersDao.getAllCustomerLastNames());
        clientsList.add("Todos");
        ordersStatusList = new ArrayList<>(orderStatusDao.getAllOrderStatusDescription());
        arrayAdapterClients = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,clientsList);
        for (String order : ordersStatusList){
            orderStatusItems.add(new OrderStatusItem(order));
        }
        orderStatusAdapter = new OrderStatusAdapter(this,orderStatusItems);
        clientsSpinner.setAdapter(arrayAdapterClients);
        orderStatusSpinner.setAdapter(orderStatusAdapter);

        calendar = Calendar.getInstance();
        initialYear = calendar.get(Calendar.YEAR);
        initialMonth = calendar.get(Calendar.MONTH);
        initialDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        finalYear = calendar.get(Calendar.YEAR);
        finalMonth = calendar.get(Calendar.MONTH);
        finalDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        initialDateBtn.setEnabled(false);
        finalDateBtn.setEnabled(false);

        initialDateCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initialDateCheckbox.isChecked()){
                    initialDateBtn.setEnabled(true);
                }
                else {
                    initialDateBtn.setEnabled(false);
                }
            }
        });

        finalDateCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalDateCheckbox.isChecked()){
                    finalDateBtn.setEnabled(true);
                }
                else {
                    finalDateBtn.setEnabled(false);
                }
            }
        });

        datePickerInitialDate = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        initialYear = year;
                        initialMonth = month;
                        initialDayOfMonth = dayOfMonth;
                    }
                },initialYear,initialMonth,initialDayOfMonth);


        datePickerFinalDate = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        finalYear = year;
                        finalMonth = month;
                        finalDayOfMonth = dayOfMonth;
                    }
                },finalYear,finalMonth,finalDayOfMonth);

        initialDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerInitialDate.show();
            }
        });

        finalDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFinalDate.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_button_item:{
                Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
                AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                CustomersDao customersDao = database.customersDao();
                OrderStatusDao orderStatusDao = database.orderStatusDao();
                OrdersDao ordersDao = database.ordersDao();
                OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
            }
            case R.id.add_button_item: {
                Intent intent = new Intent(this,activityAddNewOrder.class);
                startActivity(intent);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.orders_menu,menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.orders_menu,menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
        OrdersActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }

    @Override
    public void onOrderLongClick(int position) {

    }
}
