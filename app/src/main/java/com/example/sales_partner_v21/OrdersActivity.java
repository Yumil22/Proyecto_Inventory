package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{
    private List<Customers> customers;
    private List<Orders> orders;
    private List<OrderStatus> orderStatuses;
    private List<Integer> qtyAssemblies;
    private Context context;
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
        private Context context;

        NumberFormat formatter = new DecimalFormat("#,###");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",Locale.CANADA);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client = itemView.findViewById(R.id.client);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            // DARLE FORMATO A LA FECHA
            assembliesQty = itemView.findViewById(R.id.qty_assemblies);
            totalPrice = itemView.findViewById(R.id.total_cost);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Customers customer, Orders order, OrderStatus orderStatus, int qtyAssemblies, int totalCost, Context context){
            this.customer = customer;
            this.order = order;
            this.orderStatus = orderStatus;
            this.qtyAssemblies = qtyAssemblies;
            this.totalCost = totalCost;
            this.context = context;

            client.setText(customer.getLastName());
            status.setText(orderStatus.getDescription());
            date.setText(order.getDate().substring(6,8)+ "-" + order.getDate().substring(4,6)+ "-" + order.getDate().subSequence(0,4)  );
            assembliesQty.setText(String.valueOf(qtyAssemblies));
            totalPrice.setText("$" + formatter.format(totalCost));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Detalles
            MenuItem details = menu.add(this.getAdapterPosition(),0,0,"Detalles");
            details.setOnMenuItemClickListener(OrdersListener);

            // Estado anterior
            if (orderStatus.getPrevious().contains("0")){
                MenuItem backState = menu.add(this.getAdapterPosition(),1,0,"Regresar estado a pendiente");
                backState.setOnMenuItemClickListener(OrdersListener);
            }

            // Siguiente estado
            if (!orderStatus.getNext().contains("-")){
                if (orderStatus.getNext().contains("1,2")){
                    MenuItem nextState1 = menu.add(this.getAdapterPosition(),2,0,"Avanzar estado a cancelado");
                    MenuItem nextState2 = menu.add(this.getAdapterPosition(),3,0,"Avanzar estado a confirmado");
                    nextState1.setOnMenuItemClickListener(OrdersListener);
                    nextState2.setOnMenuItemClickListener(OrdersListener);
                }
                else if (orderStatus.getNext().contains("3")){
                    MenuItem nextState1 = menu.add(this.getAdapterPosition(),2,0,"Avanzar estado a en tránsito");
                    nextState1.setOnMenuItemClickListener(OrdersListener);
                }
                else if (orderStatus.getNext().contains("4")){
                    MenuItem nextState1 = menu.add(this.getAdapterPosition(),2,0,"Avanzar estado a finalizado");
                    nextState1.setOnMenuItemClickListener(OrdersListener);
                }
            }

            // Editable
            if (orderStatus.getEditable() == 1){
                MenuItem editOrder = menu.add(this.getAdapterPosition(),4,0,"Editar orden");
                editOrder.setOnMenuItemClickListener(OrdersListener);
            }
        }

        private final MenuItem.OnMenuItemClickListener OrdersListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0){
                    Intent detailsIntent = new Intent(context,OrdersDetails.class);
                    detailsIntent.putExtra("ORDER_ID",order.getId());
                    ((OrdersActivity)context).startActivity(detailsIntent);
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

    public OrdersAdapter(List<Customers> customers, List<Orders> orders, List<OrderStatus> orderStatuses, List<Integer> qtyAssemblies, List<Integer> totalCosts,Context context) {
        this.customers = customers;
        this.orders = orders;
        this.orderStatuses = orderStatuses;
        this.qtyAssemblies = qtyAssemblies;
        this.totalCosts = totalCosts;
        this.context = context;
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
        viewHolder.bind(customers.get(i),orders.get(i),orderStatuses.get(i),qtyAssemblies.get(i),totalCosts.get(i),context);
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
    private ArrayList<String> ordersStatusList;
    private ArrayList<OrderStatusItem> orderStatusItems = new ArrayList<>();
    private ArrayList<String> clientsList;
    private List<Integer> orderStatusesSelected = new ArrayList<>();
    private String SELECTED_STATUSES = "SELECTED_STATUS";
    private String CLIENT_ID = "CLIENT_ID";
    private int ClientID = 0;
    private Boolean SEARCH_PRESS = false;

    private List<Customers> customers = new ArrayList<>();
    private List<Orders> orders = new ArrayList<>();
    private List<OrderStatus> orderStatuses = new ArrayList<>();
    private List<Integer> qtyAssemblies = new ArrayList<>();
    private List<Integer> totalCosts = new ArrayList<>();

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
        OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();

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

            // Clientes
            customers.clear();
            if (ClientID == customersDao.getLastID() + 1){
                customers.addAll(customersDao.getAllCustomers());
            }
            else {
                if (!customers.contains(customersDao.getCustomerById(ClientID))){
                    customers.add(customersDao.getCustomerById(ClientID));
                }
            }
            int[] clientsIDs = new int[customers.size()];
            int counter = 0;
            for (Customers customer : customers){
                clientsIDs[counter] = customer.getId();
                counter++;
            }

            // Estados de las ordenes
            orderStatuses.clear();
            for (int status : orderStatusesSelected){
                orderStatuses.add(orderStatusDao.getOrderStatusByID(status));
            }

            if (orderStatuses.size() != 0){
                int[] statuses = new int[orderStatusesSelected.size()];
                int count = 0;
                for (OrderStatus orderStatus : orderStatuses){
                    statuses[count] = orderStatus.getId();
                    count++;
                }
                // Ordenes
                if (initialDateCheckbox.isChecked() && finalDateCheckbox.isChecked()){
                    String monthInicial;
                    if ((initialMonth + 1) < 10){
                        monthInicial = "0" + (initialMonth + 1);
                    }
                    else {
                        monthInicial = String.valueOf(initialMonth + 1);
                    }
                    String dayInicial;
                    if (initialDayOfMonth < 10){
                        dayInicial = "0" + initialDayOfMonth;
                    }
                    else {
                        dayInicial = String.valueOf(initialDayOfMonth);
                    }
                    String initialFilterDate = initialYear + monthInicial + dayInicial;

                    String monthFinal;
                    if ((finalMonth + 1) <  10){
                        monthFinal = "0" + (finalMonth + 1);
                    }
                    else {
                        monthFinal = String.valueOf(finalMonth + 1);
                    }
                    String dayFinal;
                    if (finalDayOfMonth < 10){
                        dayFinal = "0" + finalDayOfMonth;
                    }
                    else {
                        dayFinal = String.valueOf(finalDayOfMonth);
                    }
                    String finalFilterDate = finalYear + monthFinal + dayFinal;

                    // FILTRADO POR FECHA, CLIENTES Y ESTADOS
                    orders.clear();
                    orders.addAll(ordersDao.getFilterOrders(initialFilterDate,finalFilterDate,clientsIDs, statuses));

                    customers.clear();
                    orderStatuses.clear();
                    qtyAssemblies.clear();
                    totalCosts.clear();

                    if (orders != null){
                        for (Orders order: orders){
                            customers.add(ordersDao.getCustomerFromOrderID(order.getId()));
                            orderStatuses.add(ordersDao.getOrderStatusFromOrderID(order.getId()));
                            qtyAssemblies.add(ordersAssembliesDao.getQtyAssemblies(order.getId()));
                            totalCosts.add(ordersAssembliesDao.getTotalCostOrdersAssemblies(order.getId()));
                        }
                        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        ordersRecyclerView.setAdapter(new OrdersAdapter(customers,orders,orderStatuses,qtyAssemblies,totalCosts,this));
                    }
                }
                else {
                    // FILTRADO POR CLIENTES Y ESTADOS
                    orders.clear();
                    orders.addAll(ordersDao.getFilterOrdersByIDAndStatus(clientsIDs,statuses));

                    customers.clear();
                    orderStatuses.clear();
                    if (orders != null){
                        for (Orders order: orders){
                            customers.add(ordersDao.getCustomerFromOrderID(order.getId()));
                            orderStatuses.add(ordersDao.getOrderStatusFromOrderID(order.getId()));
                            qtyAssemblies.add(ordersAssembliesDao.getQtyAssemblies(order.getId()));
                            totalCosts.add(ordersAssembliesDao.getTotalCostOrdersAssemblies(order.getId()));
                        }
                        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        ordersRecyclerView.setAdapter(new OrdersAdapter(customers,orders,orderStatuses,qtyAssemblies,totalCosts,this));
                    }
                }
            }
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
            AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
            CustomersDao customersDao = database.customersDao();
            OrderStatusDao orderStatusDao = database.orderStatusDao();
            OrdersDao ordersDao = database.ordersDao();
            OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();

            // Clientes
            customers.clear();
            if (ClientID == customersDao.getLastID() + 1){
                customers.addAll(customersDao.getAllCustomers());
            }
            else {
                if (!customers.contains(customersDao.getCustomerById(ClientID))){
                    customers.add(customersDao.getCustomerById(ClientID));
                }
            }
            int[] clientsIDs = new int[customers.size()];
            int counter = 0;
            for (Customers customer : customers){
                clientsIDs[counter] = customer.getId();
                counter++;
            }

            // Estados de las ordenes
            orderStatuses.clear();
            for (int status : orderStatusesSelected){
                orderStatuses.add(orderStatusDao.getOrderStatusByID(status));
            }

            if (orderStatuses.size() != 0){
                int[] statuses = new int[orderStatusesSelected.size()];
                int count = 0;
                for (OrderStatus orderStatus : orderStatuses){
                    statuses[count] = orderStatus.getId();
                    count++;
                }
                // Ordenes
                if (initialDateCheckbox.isChecked() && finalDateCheckbox.isChecked()){
                    Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
                    String monthInicial;
                    if ((initialMonth + 1) < 10){
                        monthInicial = "0" + (initialMonth + 1);
                    }
                    else {
                        monthInicial = String.valueOf(initialMonth + 1);
                    }
                    String dayInicial;
                    if (initialDayOfMonth < 10){
                        dayInicial = "0" + initialDayOfMonth;
                    }
                    else {
                        dayInicial = String.valueOf(initialDayOfMonth);
                    }
                    String initialFilterDate = initialYear + monthInicial + dayInicial;

                    String monthFinal;
                    if ((finalMonth + 1) <  10){
                        monthFinal = "0" + (finalMonth + 1);
                    }
                    else {
                        monthFinal = String.valueOf(finalMonth + 1);
                    }
                    String dayFinal;
                    if (finalDayOfMonth < 10){
                        dayFinal = "0" + finalDayOfMonth;
                    }
                    else {
                        dayFinal = String.valueOf(finalDayOfMonth);
                    }
                    String finalFilterDate = finalYear + monthFinal + dayFinal;

                    // FILTRADO POR FECHA, CLIENTES Y ESTADOS
                    orders.clear();
                    orders.addAll(ordersDao.getFilterOrders(initialFilterDate,finalFilterDate,clientsIDs, statuses));

                    customers.clear();
                    orderStatuses.clear();
                    qtyAssemblies.clear();
                    totalCosts.clear();

                    if (orders != null){
                        for (Orders order: orders){
                            customers.add(ordersDao.getCustomerFromOrderID(order.getId()));
                            orderStatuses.add(ordersDao.getOrderStatusFromOrderID(order.getId()));
                            qtyAssemblies.add(ordersAssembliesDao.getQtyAssemblies(order.getId()));
                            totalCosts.add(ordersAssembliesDao.getTotalCostOrdersAssemblies(order.getId()));
                        }
                        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        ordersRecyclerView.setAdapter(new OrdersAdapter(customers,orders,orderStatuses,qtyAssemblies,totalCosts,this));
                        Toast.makeText(this,"Se han encontrado " + orders.size() + " similitudes",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (initialDateCheckbox.isChecked()){
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                    alertdialog.setTitle("Error");
                    alertdialog.setMessage("Es necesario especificar una fecha final de filtrado");

                    alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = alertdialog.create();
                    alertdialog.show();
                }
                else if (finalDateCheckbox.isChecked()){
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                    alertdialog.setTitle("Error");
                    alertdialog.setMessage("Es necesario especificar una fecha inicial de filtrado");

                    alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = alertdialog.create();
                    alertdialog.show();
                }
                else {
                    Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
                    // FILTRADO POR CLIENTES Y ESTADOS
                    orders.clear();
                    orders.addAll(ordersDao.getFilterOrdersByIDAndStatus(clientsIDs,statuses));

                    customers.clear();
                    orderStatuses.clear();
                    qtyAssemblies.clear();
                    totalCosts.clear();

                    if (orders != null){
                        for (Orders order: orders){
                            customers.add(ordersDao.getCustomerFromOrderID(order.getId()));
                            orderStatuses.add(ordersDao.getOrderStatusFromOrderID(order.getId()));
                            qtyAssemblies.add(ordersAssembliesDao.getQtyAssemblies(order.getId()));
                            totalCosts.add(ordersAssembliesDao.getTotalCostOrdersAssemblies(order.getId()));
                        }
                        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        ordersRecyclerView.setAdapter(new OrdersAdapter(customers,orders,orderStatuses,qtyAssemblies,totalCosts,this));
                        Toast.makeText(this,"Se han encontrado " + orders.size() + " similitudes",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setTitle("Error");
                alertdialog.setMessage("No se ha seleccionado ningun estado de filtrado");

                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertdialog.create();
                alertdialog.show();
            }
        }
        else if(item.getItemId() == R.id.add_button_item){
            Intent intent = new Intent(this,activityAddNewOrder.class);
            startActivity(intent);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        SEARCH_PRESS = true;
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
            orderStatusesSelected.remove(Integer.valueOf(0));
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
