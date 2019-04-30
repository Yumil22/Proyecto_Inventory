package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;


class SimulatorAdapter extends RecyclerView.Adapter<SimulatorAdapter.ViewHolder>  {

    private List<Customers> customers;
    private List<Integer> verificacion;
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_name;
        private TextView txt_order_id;
        private LinearLayout linear;
        private AppDatabase database;
        private OrdersDao ordersDao;
        onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_customer_name_simulator);
            txt_order_id = itemView.findViewById(R.id.txt_order_simulator);
            linear = itemView.findViewById(R.id.layout_simulator_customer);
            database = AppDatabase.getAppDatabase(itemView.getContext());
            ordersDao = database.ordersDao();
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }


        public void bind(Customers customers, Integer integer) {
            txt_name.setText(customers.getFirstName());

            txt_order_id.setText(String.valueOf(ordersDao.getOrdercountbyid(customers.getId())));
            if(integer ==0){
                linear.setBackgroundColor(CYAN);
            } else if(integer ==1){
                linear.setBackgroundColor(GREEN);

            } else if(integer == 3){
                linear.setBackgroundColor(Color.RED);
            }
        }
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    private onNoteListener onNoteListener;

    public SimulatorAdapter(List<Customers> customersList, List<Integer> Verificacion, onNoteListener onNoteListener ){
        this.customers = customersList;
        this.verificacion = Verificacion;
        this.onNoteListener = onNoteListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simulator_customer_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new SimulatorAdapter.ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(customers.get(i), verificacion.get(i));
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
    public interface onNoteListener{
        void onNoteClick(int position);
    }


}

public class orders_simulator extends AppCompatActivity implements SimulatorAdapter.onNoteListener{

    public static final int  ORDERS_SIMULATOR_REQUEST_CODE = 1;

    public List<String> options;
    public List<String> amount_options;
    public List<Integer> verificacion;
    private ArrayAdapter<String> arrayAdapterFinal;
    public Spinner spinner_final;
    public Spinner spinner_optional;

    private List<Customers> customerList;
    private List<String> customerNameList;
    private List<String> customerNameList2;
    private List<String> customerNameList3;


    List<Products> checkProducts;

    private AppDatabase database;
    private CustomersDao customersDao;
    private ProductsDao productsDao;
    private OrdersDao ordersDao;
    public int Control_customer;

    private RecyclerView recyclerView_last;
    private List<Products> Allproducts;
    public boolean Control_verificacion = false;
    public String auxiliar;
    public int controlcount =0;
    public List<Integer> orders;

    public boolean CONTROL_DATE = false;
    public boolean CONTROL_AMOUNT = false;
    public boolean CONTROL_CUSTOMER= false;

    public int aux;

    public List<Integer> id_orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_simulator);
        spinner_final = findViewById(R.id.spinner_last);
        spinner_optional = findViewById(R.id.spinner_optional);
        recyclerView_last = findViewById(R.id.recycler_last);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        customersDao = database.customersDao();
        productsDao = database.productsDao();
        ordersDao = database.ordersDao();

        Allproducts = productsDao.getAllProducts();
        options = new ArrayList<>();
        customerNameList2 = new ArrayList<>();
        options.add("Customer");
        options.add("Date");
        options.add("Amount of sale");


        arrayAdapterFinal = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, options);
        arrayAdapterFinal.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_final.setAdapter(arrayAdapterFinal);

        if(savedInstanceState != null){

            auxiliar = savedInstanceState.getString(ITEMS);
            controlcount = savedInstanceState.getInt(CONTROLAUX);
            CONTROL_DATE = savedInstanceState.getBoolean(CONTROLDATE, false);
            CONTROL_AMOUNT = savedInstanceState.getBoolean(CONTROLAMOUNT, false);
            CONTROL_CUSTOMER = savedInstanceState.getBoolean(CONTROLCUSTOMER, false);

        }
        if(CONTROL_CUSTOMER == true){

            customerNameList3 = savedInstanceState.getStringArrayList(LISTCUSTOMER);
            if(customerNameList3 != null){

                customerList = new ArrayList<>();
                if( customerNameList3 != null){

                    for(int i = 0; i< customerNameList3.size(); i++) {
                        customerList.add(customersDao.getCustomersbyFirstname(customerNameList3.get(i)).get(0));
                    }


                    verificacion = new ArrayList<>();
                    for(int j=0; j< customerList.size();j++){
                        checkProducts = productsDao.getproductsNeeded(customerList.get(j).getId());
                        for(int a =0; a<checkProducts.size(); a++){
                            for(int b=0; b< Allproducts.size(); b++){
                                if(checkProducts.get(a).getId() == Allproducts.get(b).getId()){
                                    if(Allproducts.get(b).getQty()- checkProducts.get(a).getQty() < 0){
                                        Control_verificacion = true;
                                        Allproducts.get(b).setQty(0);
                                        aux++;
                                    }else{
                                        Allproducts.get(b).setQty(Allproducts.get(b).getQty()- checkProducts.get(a).getQty());

                                    }
                                }
                            }

                        }
                        if(Control_verificacion == true){
                            verificacion.add(0);
                            if(aux == checkProducts.size()){
                                verificacion.add(3);
                            }
                            Control_verificacion = false;
                        }else{
                            verificacion.add(1);
                            Control_verificacion = false;
                        }
                    }
                    CONTROL_CUSTOMER = true;
                    recyclerView_last.setLayoutManager(new LinearLayoutManager(orders_simulator.this));
                    recyclerView_last.setAdapter(new SimulatorAdapter(customerList, verificacion, orders_simulator.this));
                    //Allproducts = productsDao.getAllProducts();
                    //no lo actualizo para demostrar el funcionamiento correcto, de que se gastan los productos necesarios

                }
            }

        }
        if(CONTROL_AMOUNT == true){
            spinner_final.setSelection(2);

        }
        if(CONTROL_DATE == true){
            spinner_final.setSelection(1);
        }
        spinner_final.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(item =="Customer"){

                    CONTROL_DATE = false;
                    CONTROL_AMOUNT= false;
                    //CONTROL_CUSTOMER = false;

                    customerNameList = new ArrayList<>();
                    customerList = new ArrayList<>();
                    customerNameList = ordersDao.getcustomerForComfirm();
                    customerNameList.add(0, "Choose one");
                    Control_customer = customerNameList.size();

                    arrayAdapterFinal = new ArrayAdapter<>(orders_simulator.this, R.layout.support_simple_spinner_dropdown_item, customerNameList);
                    arrayAdapterFinal.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner_optional.setAdapter(arrayAdapterFinal);
                    spinner_optional.setFocusableInTouchMode(true);

                }else if (item =="Date"){
                    CONTROL_DATE= true;
                    CONTROL_AMOUNT = false;
                    CONTROL_CUSTOMER= false;
                    Allproducts = productsDao.getAllProducts();
                    orders = ordersDao.getCountsOrders();//LISTA CON LS ORDER_ID

                    customerList = new ArrayList<>();
                    checkProducts = new ArrayList<>();

                        int aux =0;
                        if(ordersDao.getCountsOrders() != null) {

                            verificacion = new ArrayList<>();
                            for (int j = 0; j < orders.size(); j++) {
                                checkProducts = productsDao.getproductsNeededbyOrderId(orders.get(j));
                                for (int a = 0; a < checkProducts.size(); a++) {
                                    for (int b = 0; b < Allproducts.size(); b++) {
                                        if (checkProducts.get(a).getId() == Allproducts.get(b).getId()) {
                                            if (Allproducts.get(b).getQty() - checkProducts.get(a).getQty() < 0) {
                                                Control_verificacion = true;
                                                Allproducts.get(b).setQty(0);
                                                aux++;
                                            } else {
                                                Allproducts.get(b).setQty(Allproducts.get(b).getQty() - checkProducts.get(a).getQty());

                                            }
                                        }
                                    }

                                }
                                customerList.add(customersDao.getcustomerbyOrderId(orders.get(j)));
                                if (Control_verificacion == true) {
                                    verificacion.add(0);
                                    if (aux == checkProducts.size()) {
                                        verificacion.add(3);
                                    }
                                    Control_verificacion = false;
                                } else {
                                    verificacion.add(1);
                                    Control_verificacion = false;

                                }
                            }

                            Control_verificacion = false;
                            recyclerView_last.setLayoutManager(new LinearLayoutManager(orders_simulator.this));
                            recyclerView_last.setAdapter(new SimulatorAdapter(customerList, verificacion, orders_simulator.this));
                            //Allproducts = productsDao.getAllProducts();
                            //no lo actualizo para demostrar el funcionamiento correcto, de que se gastan los productos necesarios

                        }else
                        {
                            if(customerList.size() == 0){
                                Toast.makeText(orders_simulator.this, "No se puede realizar esta accion", Toast.LENGTH_LONG);
                            }
                        }


                    spinner_optional.setFocusableInTouchMode(false);
                    spinner_optional.setFocusable(false);

                }else if(item == "Amount of sale"){
                    CONTROL_DATE = false;
                    CONTROL_AMOUNT = true;
                    CONTROL_CUSTOMER = false;

                    amount_options = new ArrayList<>();
                    amount_options.add("Higher");
                    amount_options.add("Less");
                    arrayAdapterFinal = new ArrayAdapter<>(orders_simulator.this, R.layout.support_simple_spinner_dropdown_item, amount_options);
                    arrayAdapterFinal.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner_optional.setAdapter(arrayAdapterFinal);
                    spinner_optional.setFocusableInTouchMode(true);
                    if(auxiliar.contains("Higher")){
                        spinner_optional.setSelection(0);
                    }else if(auxiliar.contains("Less")){
                        spinner_optional.setSelection(1);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //spinner_optional.setFocusableInTouchMode(false);

            }
        });



        spinner_optional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                auxiliar = item;
                if(item.contains("Higher") ){
                    Allproducts = productsDao.getAllProducts();
                    id_orders = customersDao.getIdHighercount();//LISTA CON LS ORDER_ID

                    customerList = new ArrayList<>();
                    checkProducts = new ArrayList<>();

                    int aux =0;
                    if(customersDao.getIdHighercount() != null) {

                        verificacion = new ArrayList<>();
                        for (int j = 0; j < id_orders.size(); j++) {
                            checkProducts = productsDao.getproductsNeededbyOrderId(id_orders.get(j));
                            for (int a = 0; a < checkProducts.size(); a++) {
                                for (int b = 0; b < Allproducts.size(); b++) {
                                    if (checkProducts.get(a).getId() == Allproducts.get(b).getId()) {
                                        if (Allproducts.get(b).getQty() - checkProducts.get(a).getQty() < 0) {
                                            Control_verificacion = true;
                                            Allproducts.get(b).setQty(0);
                                            aux++;
                                        } else {
                                            Allproducts.get(b).setQty(Allproducts.get(b).getQty() - checkProducts.get(a).getQty());

                                        }
                                    }
                                }

                            }
                            customerList.add(customersDao.getcustomerbyOrderId(id_orders.get(j)));
                            if (Control_verificacion == true) {
                                verificacion.add(0);
                                if (aux == checkProducts.size()) {
                                    verificacion.add(3);
                                }
                                Control_verificacion = false;
                            } else {
                                verificacion.add(1);
                                Control_verificacion = false;

                            }
                        }
                        Control_verificacion = false;
                        CONTROL_CUSTOMER = true;
                        recyclerView_last.setLayoutManager(new LinearLayoutManager(orders_simulator.this));
                        recyclerView_last.setAdapter(new SimulatorAdapter(customerList, verificacion, orders_simulator.this));
                        //Allproducts = productsDao.getAllProducts();
                        //no lo actualizo para demostrar el funcionamiento correcto, de que se gastan los productos necesarios

                    }else
                    {
                        if(customerList.size() == 0){
                            Toast.makeText(orders_simulator.this, "No se puede realizar esta accion", Toast.LENGTH_LONG);
                        }
                    }

                }else if(item.contains("Less")){
                    Allproducts = productsDao.getAllProducts();
                    id_orders = customersDao.getIdLesscount();//LISTA CON LS ORDER_ID

                    customerList = new ArrayList<>();
                    checkProducts = new ArrayList<>();

                    int aux =0;
                    if(customersDao.getIdLesscount() != null) {

                        verificacion = new ArrayList<>();
                        for (int j = 0; j < id_orders.size(); j++) {
                            checkProducts = productsDao.getproductsNeededbyOrderId(id_orders.get(j));
                            for (int a = 0; a < checkProducts.size(); a++) {
                                for (int b = 0; b < Allproducts.size(); b++) {
                                    if (checkProducts.get(a).getId() == Allproducts.get(b).getId()) {
                                        if (Allproducts.get(b).getQty() - checkProducts.get(a).getQty() < 0) {
                                            Control_verificacion = true;
                                            Allproducts.get(b).setQty(0);
                                            aux++;
                                        } else {
                                            Allproducts.get(b).setQty(Allproducts.get(b).getQty() - checkProducts.get(a).getQty());

                                        }
                                    }
                                }

                            }
                            customerList.add(customersDao.getcustomerbyOrderId(id_orders.get(j)));
                            if (Control_verificacion == true) {
                                verificacion.add(0);
                                if (aux == checkProducts.size()) {
                                    verificacion.add(3);
                                }
                                Control_verificacion = false;
                            } else {
                                verificacion.add(1);
                                Control_verificacion = false;

                            }
                        }

                        recyclerView_last.setLayoutManager(new LinearLayoutManager(orders_simulator.this));
                        recyclerView_last.setAdapter(new SimulatorAdapter(customerList, verificacion, orders_simulator.this));
                        //Allproducts = productsDao.getAllProducts();
                        //no lo actualizo para demostrar el funcionamiento correcto, de que se gastan los productos necesarios

                    }else
                    {
                        if(customerList.size() == 0){
                            Toast.makeText(orders_simulator.this, "No se puede realizar esta accion", Toast.LENGTH_LONG);
                        }
                    }

                }else {

                    //POR LO TANTO ES UN CUSTOMER
                    if(Control_customer > 0 && item != customerNameList.get(0).toString()){
                        Control_customer--;
                        customerNameList.remove(item);
                        customerNameList2.add(item);
                        arrayAdapterFinal = new ArrayAdapter<>(orders_simulator.this, R.layout.support_simple_spinner_dropdown_item, customerNameList);
                        arrayAdapterFinal.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinner_optional.setAdapter(arrayAdapterFinal);
                        //Suponiendo que no se repiten los nombre, por eso se agrega una condicion para evitarlo
                        customerList.add(customersDao.getCustomersbyFirstname(item).get(0));


                        controlcount++;
                        int aux =0;
                        if(Control_customer == 1 && customerList != null){

                            verificacion = new ArrayList<>();
                            for(int j=0; j< customerList.size();j++){
                                checkProducts = productsDao.getproductsNeeded(customerList.get(j).getId());
                                for(int a =0; a<checkProducts.size(); a++){
                                    for(int b=0; b< Allproducts.size(); b++){
                                        if(checkProducts.get(a).getId() == Allproducts.get(b).getId()){
                                            if(Allproducts.get(b).getQty()- checkProducts.get(a).getQty() < 0){
                                                Control_verificacion = true;
                                                Allproducts.get(b).setQty(0);
                                                    aux++;
                                            }else{
                                                Allproducts.get(b).setQty(Allproducts.get(b).getQty()- checkProducts.get(a).getQty());

                                            }
                                        }
                                    }

                                }
                                if(Control_verificacion == true){
                                    verificacion.add(0);
                                    if(aux == checkProducts.size()){
                                        verificacion.add(3);
                                    }
                                    Control_verificacion = false;
                                }else{
                                    verificacion.add(1);
                                    Control_verificacion = false;
                                }
                            }
                            CONTROL_CUSTOMER = true;
                            recyclerView_last.setLayoutManager(new LinearLayoutManager(orders_simulator.this));
                            recyclerView_last.setAdapter(new SimulatorAdapter(customerList, verificacion, orders_simulator.this));
                            //Allproducts = productsDao.getAllProducts();
                            //no lo actualizo para demostrar el funcionamiento correcto, de que se gastan los productos necesarios

                        }else
                        {
                            if(customerList.size() == 0){
                                Toast.makeText(orders_simulator.this, "No se puede realizar esta accion", Toast.LENGTH_LONG);
                            }
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(orders_simulator.this, ReportsActivity.class);
        orders_simulator.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }
    private String ID_CUSTOMER = "ID_CUSTOMER";
    private String VERIFICACION = "VERIFICACION";


    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(orders_simulator.this, missing_products_again.class);
        orders_simulator.super.finish();
        intent.putExtra(ID_CUSTOMER, customerList.get(position).getId());
        intent.putExtra(VERIFICACION, verificacion.get(position));
        startActivityForResult(intent, missing_products_again.MISSING_PRODUCTS_AGAIN);
    }

    private String CONTROLAUX = "CONTROLAUX";
    private String ITEMS = "ITEMS";
    private String CONTROLDATE = "CONTROLDATE";
    private String CONTROLAMOUNT = "CONTROLAMOUNT";
    private String LISTCUSTOMER = "LIST_CUSTOMER";
    private String CONTROLCUSTOMER = "CONTROL_CUSTOMER";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CONTROLAUX, controlcount);
        outState.putString(ITEMS, auxiliar);
        outState.putBoolean(CONTROLDATE, CONTROL_DATE);
        outState.putBoolean(CONTROLAMOUNT, CONTROL_AMOUNT);
        outState.putStringArrayList(LISTCUSTOMER, new ArrayList<String>(customerNameList2));
        outState.putBoolean(CONTROLCUSTOMER, CONTROL_CUSTOMER);

    }
}
