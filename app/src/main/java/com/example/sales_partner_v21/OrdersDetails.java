package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderStatus;
import com.example.sales_partner_v21.Database.OrderStatusChanges;
import com.example.sales_partner_v21.Database.OrderStatusChangesDao;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

class ChangeLogAdapter extends RecyclerView.Adapter<ChangeLogAdapter.ViewHolder>{
    public List<OrderStatusChanges> orderStatusChanges;
    public Context context;

    @NonNull
    @Override
    public ChangeLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.change_log_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ChangeLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeLogAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(orderStatusChanges.get(i),context);
    }

    @Override
    public int getItemCount() {
        return orderStatusChanges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView status;
        private TextView comment;

        private OrderStatusChanges orderStatusChanges;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            comment = itemView.findViewById(R.id.comment);
        }

        public void bind(OrderStatusChanges orderStatusChanges, Context context){
            this.orderStatusChanges = orderStatusChanges;
            this.context = context;
            AppDatabase database = AppDatabase.getAppDatabase(context);
            OrderStatusDao orderStatusDao = database.orderStatusDao();
            date.setText(orderStatusChanges.getDate());
            status.setText(orderStatusDao.getOrderStatusByID(orderStatusChanges.getStatus_id()).getDescription());
            comment.setText(orderStatusChanges.getComment());
        }
    }

    public ChangeLogAdapter(List<OrderStatusChanges> orderStatusChanges, Context context) {
        this.orderStatusChanges = orderStatusChanges;
        this.context = context;
    }
}

class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>{
    public List<Assemblies> assembliesOrder;
    public List<Integer> assembliesTotalCost;
    public List<Integer> quantities;
    public Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView TxtEnsamble;
        private TextView TxtQuantity;
        private TextView TxtTotalCost;

        private Assemblies assembly;
        private int totalCost;
        private int quantity;
        private Context context;
        NumberFormat formatter = new DecimalFormat("#,###");

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            TxtEnsamble = itemView.findViewById(R.id.assembly);
            TxtQuantity = itemView.findViewById(R.id.qty_assemblies);
            TxtTotalCost = itemView.findViewById(R.id.total_cost);
        }

        public void bind(Assemblies assembly,int totalCost, int quantity, Context context) {
            this.assembly = assembly;
            this.totalCost = totalCost;
            this.context = context;
            this.quantity = quantity;
            TxtEnsamble.setText(assembly.getDescription());
            TxtQuantity.setText(String.valueOf(quantity));
            TxtTotalCost.setText("$ " + formatter.format(totalCost));
        }
    }

    public OrderDetailsAdapter(List<Assemblies> assembliesOrder, List<Integer> assembliesTotalCost, List<Integer> quantities, Context context) {
        this.assembliesOrder = assembliesOrder;
        this.assembliesTotalCost = assembliesTotalCost;
        this.quantities = quantities;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_details_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(assembliesOrder.get(i),assembliesTotalCost.get(i),quantities.get(i),context);
    }

    @Override
    public int getItemCount() {
        return assembliesOrder.size();
    }
}

public class OrdersDetails extends AppCompatActivity {
    private int OrderID;
    private TextView clientText;
    private TextView statusText;
    private TextView dateText;
    private TextView totalCostText;
    private RecyclerView assembliesRecycler;
    private RecyclerView changesLogRecycler;
    NumberFormat formatter = new DecimalFormat("#,###");

    private Orders orderDetails;
    private Customers customerDetails;
    private OrderStatus orderStatusDetails;
    private int totalCost;

    private List<Assemblies> assemblies;
    private List<Integer> assembliesTotalCost;
    private List<Integer> quantities;
    private List<OrderStatusChanges> changeLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);

        clientText = findViewById(R.id.client_text);
        statusText = findViewById(R.id.status_text);
        dateText = findViewById(R.id.date_text);
        totalCostText = findViewById(R.id.total_cost);
        assembliesRecycler = findViewById(R.id.recyclerView_assemblies);
        changesLogRecycler = findViewById(R.id.recyclerView_changes);

        Intent details = getIntent();
        OrderID = details.getIntExtra("ORDER_ID",0);

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao customersDao = database.customersDao();
        OrderStatusDao orderStatusDao = database.orderStatusDao();
        OrdersDao ordersDao = database.ordersDao();
        OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
        OrderStatusChangesDao orderStatusChangesDao = database.orderStatusChangesDao();

        if (savedInstanceState != null){
            OrderID = savedInstanceState.getInt("ORDER_ID",0);
        }

        orderDetails = ordersDao.getOrderByID(OrderID);
        customerDetails = customersDao.getCustomerById(orderDetails.getCustomer_id());
        orderStatusDetails = orderStatusDao.getOrderStatusByID(orderDetails.getStatus_id());
        totalCost = ordersAssembliesDao.getTotalCostOrdersAssemblies(orderDetails.getId());

        assemblies = ordersAssembliesDao.getAllAssembliesByOrderID(orderDetails.getId());
        quantities = ordersAssembliesDao.getQtyAssembliesByOrderID(orderDetails.getId());
        assembliesTotalCost = ordersAssembliesDao.getTotalCostOrderAssembly(OrderID);
        changeLog = orderStatusChangesDao.getOrderStatusChangesByOrderID(orderDetails.getId());

        clientText.setText(customerDetails.getFirstName() + " " + customerDetails.getLastName());
        statusText.setText(orderStatusDetails.getDescription());
        dateText.setText(orderDetails.getDate().substring(6,8)+ "-" + orderDetails.getDate().substring(4,6)+ "-" + orderDetails.getDate().subSequence(0,4));
        totalCostText.setText(formatter.format(totalCost));

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            assembliesRecycler.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            assembliesRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        assembliesRecycler.setAdapter(new OrderDetailsAdapter(assemblies,assembliesTotalCost,quantities,this));

        changesLogRecycler.setLayoutManager(new LinearLayoutManager(this));
        changesLogRecycler.setAdapter(new ChangeLogAdapter(changeLog,this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ORDER_ID",OrderID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OrdersDetails.super.finish();
    }
}
