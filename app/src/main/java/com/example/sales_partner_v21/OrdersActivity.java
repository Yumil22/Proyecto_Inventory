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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client = itemView.findViewById(R.id.client);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            assembliesQty = itemView.findViewById(R.id.qty_assemblies);
            totalPrice = itemView.findViewById(R.id.total_cost);
            itemView.setOnCreateContextMenuListener(this);
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
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            String return_state = "estadoAnterior";
            String next_state = "estadoSiguiente";

            MenuItem details = menu.add(this.getAdapterPosition(),0,0,"Detalles");
            MenuItem backState = menu.add(this.getAdapterPosition(),1,0,"Regresar estado a " + return_state);
            MenuItem nextState = menu.add(this.getAdapterPosition(),2,0,"Avanzar estado a " + next_state);
            MenuItem editOrder = menu.add(this.getAdapterPosition(),3,0,"Editar orden");

            details.setOnMenuItemClickListener(OrdersListener);
            backState.setOnMenuItemClickListener(OrdersListener);
            nextState.setOnMenuItemClickListener(OrdersListener);
            editOrder.setOnMenuItemClickListener(OrdersListener);
        }

        private final MenuItem.OnMenuItemClickListener OrdersListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0){
                    return true;
                }
                else if (item.getItemId() == 1){
                    return true;
                }
                else if (item.getItemId() == 2){
                    return true;
                }
                else if (item.getItemId() == 3){
                    return true;
                }
                else {
                    return true;
                }
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);
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

public class OrdersActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener{
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
    private String INITIAL_YEAR = "INITIAL_YEAR";
    private String INITIAL_MONTH = "INITIAL_MONTH";
    private String INITIAL_DAY = "INITIAL_DAY";
    private String FINAL_YEAR = "FINAL_YEAR";
    private String FINAL_MONTH = "FINAL_MONTH";
    private String FINAL_DAY = "FINAL_DAY";
    private Calendar calendar;
    private ArrayAdapter<String> arrayAdapterClients;
    private OrderStatusAdapter orderStatusAdapter;
    private ArrayList<String> ordersStatusList;
    private ArrayList<OrderStatusItem> orderStatusItems = new ArrayList<>();
    private ArrayList<String> clientsList;
    private List<Integer> orderStatusesSelected = new ArrayList<>();
    private String SELECTED_STATUSES = "SELECTED_STATUS";
    private String CLIENT_ID = "CLIENT_ID";
    private int ClientID = 0;
    private Customers customer;
    private List<OrderStatus> orderStatuses = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initialDateBtn = findViewById(R.id.button_initialDate);
        finalDateBtn = findViewById(R.id.button_finalDate);
        initialDateCheckbox = findViewById(R.id.checkbox_initialDate);
        finalDateCheckbox = findViewById(R.id.checkbox_finalDate);
        MultiSpinner orderStatusSpinner = findViewById(R.id.orderStatus_spinner);
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
        clientsSpinner.setAdapter(arrayAdapterClients);
        clientsSpinner.setSelection(ClientID);
        orderStatusesSelected = orderStatusDao.getAllOrdersIDs();
        orderStatusSpinner.setItems(ordersStatusList,getString(R.string.for_all),this,orderStatusesSelected);

        calendar = Calendar.getInstance();
        initialYear = calendar.get(Calendar.YEAR);
        initialMonth = calendar.get(Calendar.MONTH);
        initialDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        finalYear = calendar.get(Calendar.YEAR);
        finalMonth = calendar.get(Calendar.MONTH);
        finalDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        initialDateBtn.setEnabled(false);
        finalDateBtn.setEnabled(false);

        if (savedInstanceState != null){
            orderStatusesSelected = savedInstanceState.getIntegerArrayList(SELECTED_STATUSES);
            ClientID = savedInstanceState.getInt(CLIENT_ID,0);
            initialYear = savedInstanceState.getInt(INITIAL_YEAR,2019);
            initialMonth = savedInstanceState.getInt(INITIAL_MONTH,4);
            initialDayOfMonth = savedInstanceState.getInt(INITIAL_DAY,30);
            finalYear = savedInstanceState.getInt(FINAL_YEAR,2019);
            finalMonth = savedInstanceState.getInt(FINAL_MONTH,5);
            finalDayOfMonth = savedInstanceState.getInt(FINAL_DAY,1);

            orderStatusSpinner.setItems(ordersStatusList,getString(R.string.for_all),this,orderStatusesSelected);
            clientsSpinner.setSelection(ClientID);

        }

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

        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClientID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ClientID = 0;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(SELECTED_STATUSES,new ArrayList<>(orderStatusesSelected));
        outState.putInt(CLIENT_ID,ClientID);
        outState.putInt(INITIAL_YEAR,initialYear);
        outState.putInt(INITIAL_MONTH,initialMonth);
        outState.putInt(INITIAL_DAY,initialDayOfMonth);
        outState.putInt(FINAL_YEAR,finalYear);
        outState.putInt(FINAL_MONTH,finalMonth);
        outState.putInt(FINAL_DAY,finalDayOfMonth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (finalDateCheckbox.isChecked()){
            finalDateBtn.setEnabled(true);
        }
        if (initialDateCheckbox.isChecked()){
            initialDateBtn.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_button_item){
            Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
            AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
            CustomersDao customersDao = database.customersDao();
            OrderStatusDao orderStatusDao = database.orderStatusDao();
            OrdersDao ordersDao = database.ordersDao();
            OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();

            customer = customersDao.getCustomerById(ClientID);

            if (orderStatuses.size() != 0){
                orderStatuses.clear();
            }

            for (int status : orderStatusesSelected){
                orderStatuses.add(orderStatusDao.getOrderStatusByID(Integer.valueOf(status)));
            }

            if (initialDateCheckbox.isChecked() && finalDateCheckbox.isChecked()){

            }
            else if (initialDateCheckbox.isChecked()){

            }
            else if (finalDateCheckbox.isChecked()){

            }

        }
        else if(item.getItemId() == R.id.add_button_item){
            Intent intent = new Intent(this,activityAddNewOrder.class);
            startActivity(intent);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
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
    public void onItemsSelected(boolean[] selected) {
        if (selected[0]){
            if (!orderStatusesSelected.contains(0)){
                orderStatusesSelected.add(0);
            }
        }
        else {
            orderStatusesSelected.remove(Integer.valueOf(1));
        }
        if (selected[1]){
            if (!orderStatusesSelected.contains(1)){
                orderStatusesSelected.add(1);
            }
        }
        else{
            orderStatusesSelected.remove(Integer.valueOf(1));
        }
        if (selected[2]){
            if (!orderStatusesSelected.contains(2)){
                orderStatusesSelected.add(2);
            }
        }
        else{
            orderStatusesSelected.remove(Integer.valueOf(2));
        }
        if (selected[3]){
            if (!orderStatusesSelected.contains(3)){
                orderStatusesSelected.add(3);
            }
        }
        else{
            orderStatusesSelected.remove(Integer.valueOf(3));
        }
        if (selected[4]){
            if (!orderStatusesSelected.contains(4)){
                orderStatusesSelected.add(4);
            }
        }
        else{
            orderStatusesSelected.remove(Integer.valueOf(4));
        }
    }
}
