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

public class ClientesActivity extends AppCompatActivity  implements MultiSpinner.MultiSpinnerListener{
    public static String CLIENTES_FLAG_KEY = "CLIENTES_FLAG_KEY";
    public static final int CLIENTES_REQUEST_CODE = 1;

    private  Toolbar toolbar;
    private Spinner customerSpinner;
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customers> customersAll = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterCustomer;
    private EditText searchCustomer;
    private ArrayList<String> customersList = new ArrayList<>();
    private CustomersDao dbCusDao;
    private List<Integer> optionsSelected = new ArrayList<>();


    private boolean control_search = false;
    public String searched;


    static final String CONTROL = "SAVED_CONTROL";
    static final String SEARCHED = "SAVED_SEARCHED";


    public AppDatabase database;
    public  CustomersDao customersDao;
    public   List<Customers> check_customer= new ArrayList<>();
    public String text_edittext;

    @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        toolbar = findViewById(R.id.toolbar_clients);
        setSupportActionBar(toolbar);

        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        dbCusDao = dbCus.customersDao();

        customersAll = dbCusDao.getAllCustomers();


        searchCustomer = findViewById(R.id.search_customer);

        MultiSpinner customerSpinner = findViewById(R.id.spinner_checkbox);
        recyclerView = findViewById(R.id.recycler_clients);





        customersList.add("First Name");
        customersList.add("Last Name");
        customersList.add("Address");
        customersList.add("E-mail");
        customersList.add("Phone");

        int counter = 0;
        for (int i = 0; i < 5; i++){
            optionsSelected.add(counter);
            counter++;
        }

        customerSpinner.setItems(customersList,getString(R.string.for_all),this,optionsSelected);

        if(savedInstanceState != null){

            control_search = savedInstanceState.getBoolean(CONTROL);
            text_edittext = savedInstanceState.getString(SEARCHED);
            optionsSelected = savedInstanceState.getIntegerArrayList("SELECTED_OPTIONS");

            customerSpinner.setItems(customersList,getString(R.string.for_all),this,optionsSelected);


            if( control_search == true  && !text_edittext.contains("")){

                customersAll = new ArrayList<>();
                check_customer = new ArrayList<>();

                if (optionsSelected.contains(0)){// STRING NAME
                    customersAll.addAll(dbCusDao.getCustomersbyFirstname(text_edittext));
                }
                if (optionsSelected.contains(1)){ // APELLIDO
                    check_customer = customersDao.getCustomersbyLastname(text_edittext);
                    for(int i =0; i<check_customer.size(); i++){

                        for(int a=0; a< customersAll.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer.size() != 0){
                        customersAll.addAll(check_customer);
                    }

                }
                if (optionsSelected.contains(2)) { // DIRECCION

                    check_customer = customersDao.getCustomersbyAddress(text_edittext);
                    if(check_customer.size() != 0){
                        for (int i = 0; i < check_customer.size(); i++) {

                            for (int a = 0; a < customersAll.size(); a++) {
                                if (customersAll.get(i).getId() == check_customer.get(a).getId()) {
                                    check_customer.remove( check_customer.get(a));
                                }

                            }
                        }
                    }
                    if(check_customer != null){
                        customersAll.addAll(check_customer);
                    }
                }
                if (optionsSelected.contains(3)){ // EMAIL
                    check_customer = customersDao.getCustomersbyEmail(text_edittext);
                    for(int i =0; i<check_customer.size(); i++){

                        for(int a=0; a< customersAll.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer != null){
                        customersAll.addAll(check_customer);
                    }
                    //customersAll.addAll(customersDao.getCustomersbyLastname(text_edittext));
                }
                if (optionsSelected.contains(4)){ // TELEFONO
                    check_customer =customersDao.getCustomersbyPhone(text_edittext);
                    for(int i =0; i<check_customer.size(); i++){

                        for(int a=0; a< customersAll.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer != null){
                        customersAll.addAll(check_customer);
                    }

                }}
         //   else {
         //       customersAll = dbCusDao.getAllCustomers();
         //   }


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CustomerAdapter(customersAll,this);
            recyclerView.setAdapter(adapter);



        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(customersAll,this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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
        if (item.getItemId() == R.id.search_button_item){
            Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show();


            customersAll.clear();
            database = AppDatabase.getAppDatabase(getApplicationContext());
            customersDao = database.customersDao();

            control_search = true;

            customersAll = new ArrayList<>();
            check_customer = new ArrayList<>();

            text_edittext = searchCustomer.getText().toString();

            searched = searchCustomer.getText().toString();
            if(!text_edittext.isEmpty()){


            if (optionsSelected.contains(0)){// STRING NAME
                customersAll.addAll(customersDao.getCustomersbyFirstname(text_edittext));
            }
            if (optionsSelected.contains(1)){ // APELLIDO
                check_customer = customersDao.getCustomersbyLastname(text_edittext);
                for(int i =0; i<check_customer.size(); i++){

                    for(int a=0; a< customersAll.size(); a++){
                        if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                            check_customer.remove(check_customer.get(a));
                        }

                    }
                }
                if(check_customer.size() != 0){
                    customersAll.addAll(check_customer);
                }

            }
            if (optionsSelected.contains(2)) { // DIRECCION

                check_customer = customersDao.getCustomersbyAddress(text_edittext);
                if(check_customer.size() != 0){
                for (int i = 0; i < check_customer.size(); i++) {

                    for (int a = 0; a < customersAll.size(); a++) {
                        if (customersAll.get(i).getId() == check_customer.get(a).getId()) {
                            check_customer.remove( check_customer.get(a));
                        }

                    }
                }
            }
                if(check_customer.size() != 0){
                    customersAll.addAll(check_customer);
                }
            }
            if (optionsSelected.contains(3)){ // EMAIL
                check_customer = customersDao.getCustomersbyEmail(text_edittext);
                for(int i =0; i<check_customer.size(); i++){

                    for(int a=0; a< customersAll.size(); a++){
                        if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                            check_customer.remove(check_customer.get(a));
                        }

                    }
                }
                if(check_customer.size() != 0){
                    customersAll.addAll(check_customer);
                }
                //customersAll.addAll(customersDao.getCustomersbyLastname(text_edittext));
            }
            if (optionsSelected.contains(4)){ // TELEFONO
                check_customer =customersDao.getCustomersbyPhone(text_edittext);
                for(int i =0; i<check_customer.size(); i++){

                    for(int a=0; a< customersAll.size(); a++){
                        if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                            check_customer.remove(check_customer.get(a));
                        }

                    }
                }
                if(check_customer.size() != 0){
                    customersAll.addAll(check_customer);
                }

            }}
            else {
                customersAll = customersDao.getAllCustomers();
            }


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CustomerAdapter(customersAll,this);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    new LinearLayoutManager(this).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            return true;
        }
        else if (item.getItemId() == R.id.add_button_item){
            //agregamos un activity para anexar a nuevo usuario
            Intent intent2 = new Intent(ClientesActivity.this, new_user.class);
            //ClientesActivity.super.finish();
            startActivityForResult(intent2, new_user.NEW_USER_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

            outState.putBoolean(CONTROL, control_search);
            outState.putString(SEARCHED, text_edittext);

        outState.putIntegerArrayList("SELECTED_OPTIONS",new ArrayList<>(optionsSelected));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        if (selected[0]){
            if (!optionsSelected.contains(0)){
                optionsSelected.add(0);
            }
        }
        else {
            optionsSelected.remove(Integer.valueOf(0));
        }
        if (selected[1]){
            if (!optionsSelected.contains(1)){
                optionsSelected.add(1);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(1));
        }
        if (selected[2]){
            if (!optionsSelected.contains(2)){
                optionsSelected.add(2);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(2));
        }
        if (selected[3]){
            if (!optionsSelected.contains(3)){
                optionsSelected.add(3);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(3));
        }
        if (selected[4]){
            if (!optionsSelected.contains(4)){
                optionsSelected.add(4);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(4));
        }
    }
}

