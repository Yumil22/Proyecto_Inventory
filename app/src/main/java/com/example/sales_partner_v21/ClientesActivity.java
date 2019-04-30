package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;

 class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txt_first_name;
        private TextView txt_last_name;
        private TextView txt_address;
        private TextView txt_phone;
        private TextView txtx_email;

        private Customers customers;
        private Context context;

        public ViewHolder(@NonNull final View itemView ) {
            super(itemView);
            txt_first_name =itemView.findViewById(R.id.txt_first_name);
            txt_last_name =itemView.findViewById(R.id.txt_last_name);
            txt_address =itemView.findViewById(R.id.txt_address);
            txt_phone =itemView.findViewById(R.id.txt_phone);
            txtx_email =itemView.findViewById(R.id.txt_email);
            final View parent = itemView;
            itemView.setOnCreateContextMenuListener(this);
        }
        public void bind(Customers customers, Context context) {
            this.customers = customers;
            this.context = context;

            txt_first_name.setText(customers.getFirstName());
            txt_last_name.setText(String.valueOf(customers.getLastName()));
            txt_address.setText(customers.getAddress());
            txt_phone.setText(String.valueOf(customers.getPhone1()));
            txtx_email.setText(String.valueOf(customers.getEmail()));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

           MenuItem information =  menu.add(this.getAdapterPosition(), 1, 0, "Información");
            MenuItem edit =menu.add(this.getAdapterPosition(), 2, 1, "Editar");
            MenuItem delete = menu.add(this.getAdapterPosition(), 3, 1, "Eliminar");

            information.setOnMenuItemClickListener(onEditMenu);
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        public String CUSTOMER_ID = "CUSTOMER_ID";
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                            case 1:
                                new Dialog_customers(itemView.getContext(),customers);
                                return true;

                            case 2:
                                //ACTIVITY DESNTRO DEL MENU CONTEXTUAL Y ADEMAS DENTOR DEL VIEW HOLDER
                                Intent intent2 = new Intent(itemView.getContext(), edit_custormer.class);
                                intent2.putExtra(CUSTOMER_ID, customers.getId());

                                //TIENES QUE OBTENER EL CONTEXTO PARA PODER CREAR EL ACTIVITY
                                itemView.getContext().startActivity(intent2);
                                //AQUI ES DONDE CREE EL ACTIVITY DESNTRO DEL VIEW HOLDER
                                return true;

                            case 3:
                                final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                                builder.setMessage("¿Desea eliminar el cliente?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
                                        CustomersDao customersDao = database.customersDao();
                                        OrdersDao ordersDao = database.ordersDao();
                                        OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();

                                        int counter = 0;
                                        int[] order_ids = new int[ordersDao.getOrdersByCustomerID(customers.getId()).size()];
                                        for (Orders order : ordersDao.getOrdersByCustomerID(customers.getId())){
                                            order_ids[counter] = order.getId();
                                            counter++;
                                        }
                                        ordersAssembliesDao.DeleteOrderAssembliesByOrdersID(order_ids);
                                        ordersDao.DeleteOrdersByOrderID(order_ids);
                                        customersDao.Deleteuser(customers);
                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //no realiza nada
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                return true;
                                //Dialogo confirmacion de accion

                            default: return true;
                        }
            }
        };
    }

    private List<Customers> customers;
    private Context context;
    public CustomerAdapter(List<Customers> customers, Context context){
        this.customers = customers;
        this.context = context;
    }

     @Override
     public int getItemViewType(int position) {
         return super.getItemViewType(position);
     }

     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_clientes, viewGroup, false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(customers.get(i),context);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}

public class ClientesActivity extends AppCompatActivity  {
    public static String CLIENTES_FLAG_KEY = "CLIENTES_FLAG_KEY";
    public static final int CLIENTES_REQUEST_CODE = 1;

    private  Toolbar toolbar;
    private Spinner customerSpinner;
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customers> customersAll ;
    private ArrayAdapter<String> arrayAdapterCustomer;
    private EditText searchCustomer;
    private ArrayList<String> customersList = new ArrayList<>();
    private CustomersDao dbCusDao;


    private boolean control_search = false;
    public String searched;
    public int type_search = 0;

    public int itemSearch = 0;

    static final String CONTROL = "SAVED_CONTROL";
    static final String SEARCHED = "SAVED_SEARCHED";
    static final String TYPE_SEARCH = "SAVED_TYPE_SEARCH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        toolbar = findViewById(R.id.toolbar_clients);
        setSupportActionBar(toolbar);

        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        dbCusDao = dbCus.customersDao();
        //customersAll = null;
        customersAll = dbCusDao.getAllCustomers();


        searchCustomer = findViewById(R.id.search_customer);
        customerSpinner = findViewById(R.id.spinner_checkbox);
        recyclerView = findViewById(R.id.recycler_clients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomerAdapter(customersAll,this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        customersList = new ArrayList<>();

        customersList.add("First Name");
        customersList.add("Last Name");
        customersList.add("Address");
        customersList.add("E-mail");
        customersList.add("Phone");

        arrayAdapterCustomer = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, customersList);
        arrayAdapterCustomer.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        customerSpinner.setAdapter(arrayAdapterCustomer);

        if(savedInstanceState != null){

            type_search = savedInstanceState.getInt(TYPE_SEARCH);
            control_search = savedInstanceState.getBoolean(CONTROL);
            searched = savedInstanceState.getString(SEARCHED);

            if(control_search){

                if(type_search == 0){
                    customersAll = dbCusDao.getAllCustomers();
                }
                if(type_search == 1){
                    customersAll = dbCusDao.getCustomersbyFirstname(searched);
                    control_search = true;
                    customerSpinner.setSelection(1);

                } else if(type_search == 2){

                    customersAll = dbCusDao.getCustomersbyLastname(searched);
                    control_search = true;
                    customerSpinner.setSelection(2);

                }else if(type_search == 3)
                {
                    customersAll = dbCusDao.getCustomersbyEmail(searched);
                    control_search = true;
                    customerSpinner.setSelection(3);

                }else if(type_search == 4){

                    customersAll = dbCusDao.getCustomersbyPhone(searched);
                    control_search = true;
                    customerSpinner.setSelection(4);

                }else if(type_search == 5){

                    customersAll = dbCusDao.getCustomersbyAddress(searched);
                    control_search = true;
                    customerSpinner.setSelection(5);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new CustomerAdapter(customersAll,this);
                recyclerView.setAdapter(adapter);
            }
        }

        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if(item == "First Name"){
                            itemSearch =1;
                    type_search =1;
                           // searchCustomer.setInputType();
                } else if(item == "Last Name"){

                    itemSearch = 2;
                    type_search =2;
                }else if(item == "E-mail")
                {
                    itemSearch = 3;
                    type_search=3;

                }else if(item == "Phone"){

                    itemSearch = 4;
                    type_search=4;

                }else if(item == "Address"){

                    itemSearch = 5;
                    type_search=5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                itemSearch = 0;
                Toast.makeText(ClientesActivity.this, "Select an item", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( ClientesActivity.this, MainActivity.class);
        ClientesActivity.super.finish();
        startActivityForResult(intent , MainActivity.PRINCIPAL_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clients_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_button_item:
//comenzamos la funcion de busqueda
                if(itemSearch == 0){
                    customersAll = dbCusDao.getAllCustomers();
                }
                if(itemSearch == 1 && !searchCustomer.getText().toString().isEmpty()){

                    customersAll = dbCusDao.getCustomersbyFirstname(searchCustomer.getText().toString());
                    control_search = true;

                } else if(itemSearch == 2 && !searchCustomer.getText().toString().isEmpty()){

                    customersAll = dbCusDao.getCustomersbyLastname(searchCustomer.getText().toString());
                    control_search = true;

                }else if(itemSearch == 3 && !searchCustomer.getText().toString().isEmpty())
                {
                    customersAll = dbCusDao.getCustomersbyEmail(searchCustomer.getText().toString());
                    control_search = true;

                }else if(itemSearch == 4 && !searchCustomer.getText().toString().isEmpty()){

                    customersAll = dbCusDao.getCustomersbyPhone(searchCustomer.getText().toString());
                    control_search = true;

                }else if(itemSearch == 5 && !searchCustomer.getText().toString().isEmpty()){

                    customersAll = dbCusDao.getCustomersbyAddress(searchCustomer.getText().toString());
                    control_search = true;

                } else {
                    customersAll = dbCusDao.getAllCustomers();

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new CustomerAdapter(customersAll,this);
                recyclerView.setAdapter(adapter);

                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.add_button_item:
//agregamos un activity para anexar a nuevo usuario

                Intent intent2 = new Intent(ClientesActivity.this, new_user.class);
                //ClientesActivity.super.finish();
                startActivityForResult(intent2, new_user.NEW_USER_REQUEST_CODE);


                return true;


                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(control_search == true){

            outState.getBoolean(CONTROL, control_search);
            outState.getString(SEARCHED, searched);
            outState.getInt(TYPE_SEARCH, type_search);

        }

        super.onSaveInstanceState(outState);
    }
}

