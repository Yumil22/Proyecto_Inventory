package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    int idSeller;
    public RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        request = Volley.newRequestQueue(activityAddNewOrder.this);

        SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
        idSeller = configPreferences.getInt("IDSELLER", -1);

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
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    assembliesRecycler.setLayoutManager(new GridLayoutManager(this,2));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecycler.getContext(),
                            new GridLayoutManager(this,2).getOrientation());
                    assembliesRecycler.addItemDecoration(dividerItemDecoration);
                } else {
                    assembliesRecycler.setLayoutManager(new LinearLayoutManager(this));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecycler.getContext(),
                            new LinearLayoutManager(this).getOrientation());
                    assembliesRecycler.addItemDecoration(dividerItemDecoration);
                }
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
                        String day;
                        if (Month < 10){
                            month = "0" + Month;
                        }
                        else {
                            month = String.valueOf(Month);
                        }
                        if (DayOfMonth < 10){
                            day = "0" + DayOfMonth;
                        }
                        else {
                            day = String.valueOf(DayOfMonth);
                        }
                        final String date = Year  + month  + day;
                        //AGREGA LA ORDEN A "ORDERS"
                       // Orders NewOrder = new Orders(ordersDao.getMaxID() + 1, 0,ClientID,date,null,0);
                        AddNewOrder(ordersDao.getMaxID() + 1, 0,ClientID,date,idSeller);
                       // ordersDao.InsertNewOrder(NewOrder);
                        int counter = 0;
                        for (Assemblies assembly : assembliesOrder){
                            //agrega el assembly
                            //ordersAssembliesDao.InsertNewOrdersAssembly(new OrderAssemblies(ordersDao.getMaxID(),assembly.getId(),adapter.quantities.get(counter)));


                                AddNewOrderAssemblies(ordersDao.getMaxID() + 1, assembly.getId(), adapter.quantities.get(counter));


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
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        assembliesRecycler.setLayoutManager(new GridLayoutManager(this,2));
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecycler.getContext(),
                                new GridLayoutManager(this,2).getOrientation());
                        assembliesRecycler.addItemDecoration(dividerItemDecoration);
                    } else {
                        assembliesRecycler.setLayoutManager(new LinearLayoutManager(this));
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecycler.getContext(),
                                new LinearLayoutManager(this).getOrientation());
                        assembliesRecycler.addItemDecoration(dividerItemDecoration);
                    }
                    adapter = new AddNewOrderAdapter(assembliesOrder,assembliesTotalProducts,assembliesTotalCost,assembliesQuantities,this);
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

    public JSONArray jsonArray2;
    public JSONObject jsonObject;
    public String id_max_orders;
<<<<<<< HEAD
    public String id_max_orders_final;
    private void AddNewOrder(final int status_id, final int customerId, final String date_new, final int idSeller ){
=======
    private void AddNewOrder(final int id_order, final int status_id, final int customerId, final String date_new, final int idSeller ){
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138
        //Orders NewOrder = new Orders(ordersDao.getMaxID() + 1, 0,ClientID,date,null,0);
        String url = "http://192.168.1.101:3000/add_new_order"  ;
        String url2 = "http://192.168.1.101:3000/id_new_order";

<<<<<<< HEAD
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray2 = response;
                        jsonObject = null;
                        try {

                            jsonObject = jsonArray2.getJSONObject(0);
                            id_max_orders = jsonObject.getString("id");
                            Log.d("Error",""+id_max_orders);

                            id_max_orders_final = String.valueOf(Integer.valueOf(id_max_orders)+1);
                            Log.d("Error2",""+id_max_orders_final);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activityAddNewOrder.this, "Fallo la coneccion",  Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activityAddNewOrder.this, "Lost connect", Toast.LENGTH_LONG).show();
=======
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138


//agregando nueva orden
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(activityAddNewOrder.this, "Funcionó", Toast.LENGTH_SHORT).show();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(activityAddNewOrder.this, "Lost connect", Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            public final String ab =    String.valueOf(id_order) ;
            public final String bc =    String.valueOf(status_id);
            public final String ce =    String.valueOf(customerId);
            public final String ed =    date_new;
            public final String df =    String.valueOf(idSeller);
            @Override
            protected Map<String, String> getParams()
            {

<<<<<<< HEAD
                String newid = id_max_orders_final;
                String newstatus = String.valueOf(status_id);
                String newcustomer = String.valueOf(customerId);
                String newseller = String.valueOf(idSeller);

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", newid);
                params.put("status_id", newstatus);
                params.put("customer_id", newcustomer);
                params.put("date", date_new);
                params.put("change_log", "NULL");
                params.put("seller_id", newseller);
=======
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ab);
                params.put("status_id", bc);
                params.put("customer_id", ce);
                params.put("date", ed);
                params.put("change_log", "NULL");
                params.put("seller_id", df);
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138
                params.put("domain", "192.168.1.101:3000");
//CAMBIAR DOMINIO URL
                return params;
            }
        };
        request.add(postRequest);

        CargarDatosOrder();

    }
//Variables para order_assemblies
    private String order_id_oa = "";
    private String assembly_id_oa = "";
    private String qty_oa = "";

    private List<OrderAssemblies> ordersAssembliesRemoteDatabase = new ArrayList<>();
    private List<OrderAssemblies> ordersAssembliesRemoteDatabase2 = new ArrayList<>();
    private JSONArray jsonArray3;
    private JSONObject jsonObject2;



    //Variables de orders
    //Strings de orders
    private String id_orders_db = "";
    private String status_id_orders_db = "";
    private String customer_id_orders_db = "";
    private String date_orders_db = "";
    private String change_log_db = "";
    private String seller_id_orders_db = "";

    private String id_need;
    private List<Orders> ordersRemoteDatabase = new ArrayList<>();
    private List<Orders> ordersRemoteDatabase2 = new ArrayList<>();

<<<<<<< HEAD
    private void AddNewOrderAssemblies(final int assembly_id, final int qty_2) {
        String url = "http://192.168.1.101:3000/add_new_order_assembly";
        String url8 = "http://192.168.1.101:3000/order/assemblies/";
        String url4 = "http://192.168.1.101:3000/order/";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
=======
    private void AddNewOrderAssemblies(final int id_order,final int assembly_id, final int qty_2)  {
        String url5 = "http://192.168.1.101:3000/add_new_order_assembly";
        String url8 = "http://192.168.43.101:3000/order/assemblies/"  ;
        String url4 = "http://192.168.43.101:3000/order/"  ;



        StringRequest postRequest = new StringRequest(Request.Method.POST, url5,
                new Response.Listener<String>()
                {
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(activityAddNewOrder.this, "Funcionó", Toast.LENGTH_SHORT).show();
                        Log.d("Response", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(activityAddNewOrder.this, "Lost connect", Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            public final String a = String.valueOf(id_order);
            public final String b = String.valueOf(assembly_id);
            public final String c = String.valueOf(qty_2);
            @Override
<<<<<<< HEAD
            protected Map<String, String> getParams() {

                String newid = id_max_orders_final;
                String newassembly = String.valueOf(assembly_id);
                String newqty = String.valueOf(qty_2);

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", newid);
                params.put("assembly_id", newassembly);
                params.put("qty", newqty);
=======
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", a);
                params.put("assembly_id", b);
                params.put("qty", c);
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138
                params.put("domain", "192.168.1.101:3000");
//CAMBIAR DOMINIO URL
                return params;
            }
        };
        request.add(postRequest);


        //Ya para concluir, reiniciamos todas las tablas, como un rerfresh de


    }


    //private JSONObject jsonObject;
    private JSONArray jsonArray;


    private void CargarDatosOrder() {

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        final OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
        final OrdersDao ordersDao = database.ordersDao();

        String url4 = "http://192.168.1.101:3000/order/";
        String url8 = "http://192.168.1.101:3000/order/assemblies/";


        request = Volley.newRequestQueue(activityAddNewOrder.this);

        JsonArrayRequest getRequest4 = new JsonArrayRequest(Request.Method.GET, url4, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            ordersDao.DeleteOrdersTable();
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                id_orders_db = jsonObject.getString("id");
                                status_id_orders_db = jsonObject.getString("status_id");
                                customer_id_orders_db = jsonObject.getString("customer_id");
                                date_orders_db = jsonObject.getString("date");
                                change_log_db = jsonObject.getString("change_log");
                                seller_id_orders_db = jsonObject.getString("seller_id");


                                //ACTUALIZACION DE LA TABLA
                                ordersDao.InsertOrders(new Orders(Integer.parseInt(id_orders_db), Integer.parseInt(status_id_orders_db), Integer.parseInt(customer_id_orders_db),
                                        date_orders_db, change_log_db, Integer.parseInt(seller_id_orders_db)));
                            }
                            Toast.makeText(activityAddNewOrder.this, "ORDERS", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ordersRemoteDatabase2 = ordersRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activityAddNewOrder.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest4);


        //ACCTIALIZAMOS order_assemblies


        JsonArrayRequest getRequest8 = new JsonArrayRequest(Request.Method.GET, url8, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            ordersAssembliesDao.DeleteOrderAssembliesTable();
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                order_id_oa = jsonObject.getString("order_id");
                                assembly_id_oa = jsonObject.getString("assembly_id");
                                qty_oa = jsonObject.getString("qty");

                                ordersAssembliesRemoteDatabase.add(new OrderAssemblies(Integer.parseInt(order_id_oa), Integer.parseInt(assembly_id_oa), Integer.parseInt(qty_oa)));
                                //ACTUALIZACION DE LA TABLA
                                ordersAssembliesDao.InsertOrderAssemblies(new OrderAssemblies(Integer.parseInt(order_id_oa), Integer.parseInt(assembly_id_oa), Integer.parseInt(qty_oa)));
                            }
                            Toast.makeText(activityAddNewOrder.this, "ORDERASSEMBLIES", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ordersAssembliesRemoteDatabase2 = ordersAssembliesRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activityAddNewOrder.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
<<<<<<< HEAD
        request.add(getRequest4);

=======
        request.add(getRequest8);
>>>>>>> b053b62446efca5fc448cabc036f103bbca2d138
    }
}
