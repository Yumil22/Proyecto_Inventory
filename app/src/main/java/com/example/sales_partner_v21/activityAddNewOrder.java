package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.NumberPicker;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class AddNewOrderAdapter extends RecyclerView.Adapter<AddNewOrderAdapter.ViewHolder>{
    public List<Assemblies> assembliesOrder;
    public List<Integer> assembliesTotalProducts;
    public List<Integer> assembliesTotalCost;
    public List<Integer> quantities;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView TxtDescription;
        private TextView TxtTotalProducts;
        private TextView TxtTotalCost;
        private NumberPicker PickerQuantity;

        private Assemblies assembly;
        private int totalProducts;
        private int totalCost;
        private int quantity;
        private Context context;
        NumberFormat formatter = new DecimalFormat("#,###");

        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            TxtDescription = itemView.findViewById(R.id.description);
            TxtTotalProducts = itemView.findViewById(R.id.number_products);
            TxtTotalCost = itemView.findViewById(R.id.total_cost);
            PickerQuantity = itemView.findViewById(R.id.assembly_numberPicker);
            PickerQuantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    quantities.set(getAdapterPosition(),newVal);
                }
            });
            PickerQuantity.setValue(quantity);
        }

        public void bind(Assemblies assembly, int totalProducts, int totalCost, int quantity, Context context) {
            this.assembly = assembly;
            this.totalProducts = totalProducts;
            this.totalCost = totalCost;
            this.context = context;
            this.quantity = quantity;
            itemView.setOnCreateContextMenuListener(this);
            TxtDescription.setText(assembly.getDescription());
            TxtTotalProducts.setText(String.valueOf(totalProducts));
            TxtTotalCost.setText("$ " + formatter.format(totalCost));
            PickerQuantity.setMinValue(1);
            PickerQuantity.setMaxValue(9);
            PickerQuantity.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            PickerQuantity.setWrapSelectorWheel(true);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(this.getAdapterPosition(),0,0,"Eliminar");
            delete.setOnMenuItemClickListener(onOptionsMenu);
        }

        private final MenuItem.OnMenuItemClickListener onOptionsMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:{
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                        alertdialog.setMessage("¿Desea eliminar el ensamble?");

                        alertdialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((activityAddNewOrder)context).assembliesOrder.remove(getAdapterPosition());
                                ((activityAddNewOrder)context).assembliesTotalProducts.remove(getAdapterPosition());
                                ((activityAddNewOrder)context).assembliesTotalCost.remove(getAdapterPosition());
                                ((activityAddNewOrder)context).assembliesQuantities.remove(getAdapterPosition());
                                ((activityAddNewOrder)context).assembliesIDsList.remove(Integer.valueOf(assembly.getId()));
                                ((activityAddNewOrder)context).recreate();
                            }
                        });

                        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = alertdialog.create();
                        alertdialog.show();
                    }
                    default: return true;
                }
            }
        };
    }

    public AddNewOrderAdapter(List<Assemblies> assembliesOrder, List<Integer> assembliesTotalProducts, List<Integer> assembliesTotalCost, List<Integer> quantities, Context context) {
        this.assembliesOrder = assembliesOrder;
        this.assembliesTotalProducts = assembliesTotalProducts;
        this.assembliesTotalCost = assembliesTotalCost;
        this.quantities = quantities;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_order_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(assembliesOrder.get(i),assembliesTotalProducts.get(i),assembliesTotalCost.get(i),quantities.get(i),context);
        viewHolder.PickerQuantity.setValue(quantities.get(i));
    }

    @Override
    public int getItemCount() {
        return assembliesOrder.size();
    }
}

public class activityAddNewOrder extends AppCompatActivity {
    private Spinner clientsSpinner;
    private RecyclerView assembliesRecycler;
    private Toolbar addNewOrder_toolbar;
    private ArrayAdapter<String> clientsAdapter;
    private ArrayList<String> clientsList;
    public static final int ENSAMBLES_REQUEST_CODE = 1;

    public List<Assemblies> assembliesOrderAUX = new ArrayList<>();
    public List<Assemblies> assembliesOrder;
    public List<Orders> orders;
    public List<OrderAssemblies> orderAssemblies;
    public List<Integer> assembliesTotalProducts = new ArrayList<>();
    public List<Integer> assembliesTotalCost = new ArrayList<>();
    public List<Integer> assembliesQuantities = new ArrayList<>();
    public AddNewOrderAdapter adapter;
    public ArrayList<Integer> assembliesIDsList = new ArrayList<>();
    private int ClientID;

    private String CLIENT_ID = "CLIENT_ID";
    private int AssemblyID = -1;
    private String ASSEMBLIES_IDS = "ASSEMBLIES_IDS";
    public String ENSAMBLE_ID = "ENSAMBLE_ID";
    public String QTYS = "QTYS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        clientsSpinner = findViewById(R.id.clients_spinner);
        assembliesRecycler = findViewById(R.id.assemblies_RecyclerView);
        addNewOrder_toolbar = findViewById(R.id.toolbar_newOrder);
        setSupportActionBar(addNewOrder_toolbar);

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao customersDao = database.customersDao();
        AssembliesDao assembliesDao = database.assembliesDao();
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();

        if (savedInstanceState != null){
            assembliesIDsList = savedInstanceState.getIntegerArrayList(ASSEMBLIES_IDS);
            ClientID = savedInstanceState.getInt(CLIENT_ID,0);
            assembliesQuantities = savedInstanceState.getIntegerArrayList(QTYS);
            for (int id : assembliesIDsList){
                if (assembliesDao.getAllAssembliesIDs().contains(id)){
                    assembliesOrderAUX.add(assembliesDao.getAssemblyByID(id));
                    int size = assembliesOrderAUX.size();
                    int counter = 0;
                    int[] Assemblies_IDs = new int[size];
                    for (Assemblies assembly : assembliesOrderAUX){
                        Assemblies_IDs[counter] = assembly.getId();
                        counter++;
                    }
                    assembliesOrder = assembliesDao.getAssembliesAlphabetically(Assemblies_IDs);
                }
            }
            if (assembliesOrder != null){
                for(Assemblies assembly: assembliesOrder){
                    assembliesTotalProducts.add(assembliesProductsDao.getNumberProductsById(assembly.getId()));
                    assembliesTotalCost.add(assembliesProductsDao.getCostByAssemblyID(assembly.getId()));
                }
                assembliesRecycler.setLayoutManager(new LinearLayoutManager(this));
                adapter = new AddNewOrderAdapter(assembliesOrder,assembliesTotalProducts,assembliesTotalCost,assembliesQuantities,this);
                assembliesRecycler.setAdapter(adapter);
            }
        }

        clientsList = new ArrayList<>(customersDao.getAllCustomerLastNames());
        clientsAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,clientsList);
        clientsSpinner.setAdapter(clientsAdapter);
        clientsSpinner.setSelection(ClientID);

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
        outState.putInt(CLIENT_ID,ClientID);
        outState.putIntegerArrayList(ASSEMBLIES_IDS,assembliesIDsList);
        if (assembliesOrder != null){
            outState.putIntegerArrayList(QTYS,new ArrayList<>(adapter.quantities));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_new_order_menu,menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.add_new_order_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_button_item){
            Intent intent = new Intent(activityAddNewOrder.this,activityAddNewAssembly.class);
            startActivityForResult(intent,ENSAMBLES_REQUEST_CODE);
        }
        if (item.getItemId() == R.id.save_action){
            if (assembliesOrder == null){
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setTitle("Aviso");
                alertdialog.setMessage("Es necesario agregar minimo un ensamble");
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
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setTitle("Aviso");
                alertdialog.setMessage("¿Confirma guardar la orden?");

                alertdialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                        OrdersDao ordersDao = database.ordersDao();
                        OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
                        Calendar calendar = Calendar.getInstance();
                        calendar = Calendar.getInstance();
                        int Year = calendar.get(Calendar.YEAR);
                        int Month = calendar.get(Calendar.MONTH);
                        int DayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        String month;
                        if (Month < 10){
                            month = "0" + Month;
                        }
                        else {
                            month = String.valueOf(Month);
                        }
                        final String date = Year  + month  + DayOfMonth;
                        Orders NewOrder = new Orders(ordersDao.getMaxID() + 1, 0,ClientID,date,null);
                        ordersDao.InsertNewOrder(NewOrder);
                        int counter = 0;
                        for (Assemblies assembly : assembliesOrder){
                            ordersAssembliesDao.InserNewOrdersAssembly(new OrderAssemblies(ordersDao.getMaxID(),assembly.getId(),adapter.quantities.get(counter)));
                            counter++;
                        }
                        activityAddNewOrder.this.finish();
                    }
                });

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertdialog.create();
                alertdialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        AssembliesDao assembliesDao = database.assembliesDao();
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        if (requestCode == ENSAMBLES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                AssemblyID = data.getIntExtra(ENSAMBLE_ID, 0);
                if (!assembliesIDsList.contains(AssemblyID)) {
                    assembliesIDsList.add(AssemblyID);
                    Toast.makeText(this, "Se ha agregado el ensamble a la orden", Toast.LENGTH_SHORT).show(); }
                else { Toast.makeText(this, "El ensamble ya se encuentra en la orden", Toast.LENGTH_SHORT).show(); }

                if (assembliesDao.getAllAssembliesIDs().contains(AssemblyID)) {
                    assembliesOrderAUX.add(assembliesDao.getAssemblyByID(AssemblyID));
                    int size = assembliesOrderAUX.size();
                    int counter = 0;
                    int[] Assemblies_IDs = new int[size];
                    for (Assemblies assembly : assembliesOrderAUX) {
                        Assemblies_IDs[counter] = assembly.getId();
                        counter++;
                    }
                    assembliesOrder = assembliesDao.getAssembliesAlphabetically(Assemblies_IDs);
                    assembliesQuantities.add(1);
                    assembliesTotalProducts.clear();
                    assembliesTotalCost.clear();
                    for (Assemblies assembly : assembliesOrder) {
                        assembliesTotalProducts.add(assembliesProductsDao.getNumberProductsById(assembly.getId()));
                        assembliesTotalCost.add(assembliesProductsDao.getCostByAssemblyID(assembly.getId()));
                    }
                    assembliesRecycler.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new AddNewOrderAdapter(assembliesOrder, assembliesTotalProducts, assembliesTotalCost, assembliesQuantities, this);
                    assembliesRecycler.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("Aviso");
        alertdialog.setMessage("¿Desea salir? Los datos no se guardaran.");

        alertdialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activityAddNewOrder.this.finish();
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertdialog.create();
        alertdialog.show();
    }
}
