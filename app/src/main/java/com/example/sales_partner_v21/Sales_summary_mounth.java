package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.ProductsDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {

    private List<String> meses;
    private List<Integer> countsales;
    private List<String> dates;
    private List<Integer> incomes;




    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtMounth;
        private TextView txtSales;
        private TextView txtincomes;
        private String date;
        private String mes;
        private int Count;
        private int income;
        onNoteListener onNoteListener;

        public ViewHolder(@NonNull final View itemView, onNoteListener onNoteListener) {
            super(itemView);

            txtMounth = itemView.findViewById(R.id.txt_sales_mounth);
            txtincomes = itemView.findViewById(R.id.txt_income);
            txtSales = itemView.findViewById(R.id.txt_number_sales);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }
        public void bind(String Mount, int SaleCount, String Date, int inCome){
            this.mes = Mount;
            this.Count = SaleCount;
            this.date = Date;
            this.income = inCome;
            txtMounth.setText(Mount);
            txtSales.setText(String.valueOf(SaleCount));
            txtincomes.setText(String.valueOf(income));
        }


        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    private onNoteListener onNoteListener;
    public SalesAdapter(List<String> mounths, List<Integer> countSales, List<String> Dates, List<Integer> InComes, onNoteListener onNoteListener){
        this.meses = mounths;
        this.countsales = countSales;
        this.dates = Dates;
        this.incomes = InComes;
        this.onNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_summary_mounth_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new SalesAdapter.ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                viewHolder.bind(meses.get(i), countsales.get(i), dates.get(i), incomes.get(i));

    }

    @Override
    public int getItemCount() {
        return meses.size();
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }

}

public class Sales_summary_mounth extends AppCompatActivity implements SalesAdapter.onNoteListener{

    public static final int SALES_SUMMARY_MOUNTH_REQUEST_CODE = 1;

    public List<String> mounths ;
    public List<Integer> countSales;
    public List<String> assemblyDate;
    public List<Integer> inCome;

    private AppDatabase database;
    private OrdersDao ordersDao;
    private OrdersAssembliesDao ordersAssembliesDao;

    private List<Orders> ordersList;
    private List<String> dates;

    private RecyclerView recycler_sales;

    private String year = "2017";
    private Spinner spinner_year;
    private List<String> years;
    private  List<String> fechas;
    private Button button_search_dates;
    public ArrayList list = new ArrayList();
    private ArrayAdapter<String> arrayAdapterSales;


    static final String SAVED_YEAR = "SAVED_YEAR";
    static final String SAVE_SELECTED_ITEM = "SAVE_SELECTED_ITEM";
    static final String SAVED_CONTROL = "SAVED_CONTROL";

    public String save_year_searched;
    public int save_selected_item =0;
    public boolean control = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary_mounth);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        ordersDao = database.ordersDao();
        ordersAssembliesDao = database.ordersAssembliesDao();

        recycler_sales = findViewById(R.id.recycler_sales_summary_mounth);
        spinner_year = findViewById(R.id.spinner_sales_summary);
        button_search_dates = findViewById(R.id.search_dates);


        if(savedInstanceState != null){

            control = savedInstanceState.getBoolean(SAVED_CONTROL);
            int selected;
            selected = savedInstanceState.getInt(SAVE_SELECTED_ITEM);
            spinner_year.setSelection(selected);
            if(control ==true){
                mounths = new ArrayList<>(list);
                countSales = new ArrayList<>(list);
                assemblyDate = new ArrayList<>(list);
                inCome = new ArrayList<>(list);
               year = savedInstanceState.getString(SAVED_YEAR);
                if(!ordersDao.getordersbydate(year +"0101",year + "0131").isEmpty()){
                    mounths.add("Enero");
                    countSales.add(ordersDao.getordersbydate(year +"0101",year + "0131").size());
                    assemblyDate.add(year+"0101"+year + "0131");
                    inCome.add(ordersDao.getCountbyDate(year +"0101",year + "0131"));
                }
                if(!ordersDao.getordersbydate(year + "0201", year +"0229").isEmpty()){
                    mounths.add("Febrero");
                    countSales.add(ordersDao.getordersbydate(year +"0201",year + "0229").size());
                    assemblyDate.add(year+"0201"+year + "0229");
                    inCome.add(ordersDao.getCountbyDate(year + "0201", year +"0229"));

                }
                if(!ordersDao.getordersbydate(year + "0301", year + "0331").isEmpty()){
                    mounths.add("Marzo");
                    countSales.add(ordersDao.getordersbydate(year +"0301",year + "0331").size());
                    assemblyDate.add(year+"0301"+year + "0331");
                    inCome.add(ordersDao.getCountbyDate(year + "0301", year +"0331"));

                }
                if(!ordersDao.getordersbydate( year + "0401",  year +"0431").isEmpty()){
                    mounths.add("Abril");
                    countSales.add(ordersDao.getordersbydate(year +"0401",year + "0431").size());
                    assemblyDate.add(year+"0401"+year + "0431");
                    inCome.add(ordersDao.getCountbyDate(year + "0401", year +"0431"));

                }
                if(!ordersDao.getordersbydate(year +"0501", year + "0531").isEmpty()){
                    mounths.add("Mayo");
                    countSales.add(ordersDao.getordersbydate(year +"0501",year + "0531").size());
                    assemblyDate.add(year+"0501"+year + "0531");
                    inCome.add(ordersDao.getCountbyDate(year + "0501", year +"0531"));

                }
                if(!ordersDao.getordersbydate(year + "0601", year + "0631").isEmpty()){
                    mounths.add("Junio");
                    countSales.add(ordersDao.getordersbydate(year +"0601",year + "0631").size());
                    assemblyDate.add(year+"0601"+year + "0631");
                    inCome.add(ordersDao.getCountbyDate(year + "0601", year +"0631"));

                }
                if(!ordersDao.getordersbydate(year + "0701", year + "0731").isEmpty()){
                    mounths.add("Julio");
                    countSales.add(ordersDao.getordersbydate(year +"0701",year + "0731").size());
                    assemblyDate.add(year+"0701"+year + "0731");
                    inCome.add(ordersDao.getCountbyDate(year + "0701", year +"0731"));

                }
                if(!ordersDao.getordersbydate(year + "0801", year + "0831").isEmpty()){
                    mounths.add("Agosto");
                    countSales.add(ordersDao.getordersbydate(year +"0801",year + "0831").size());
                    assemblyDate.add(year+"0801"+year + "0831");
                    inCome.add(ordersDao.getCountbyDate(year + "0801", year +"0831"));

                }
                if(!ordersDao.getordersbydate(year + "0901", year + "0931").isEmpty()){
                    mounths.add("Septiembre");
                    countSales.add(ordersDao.getordersbydate(year +"0901",year + "0931").size());
                    assemblyDate.add(year+"0901"+year + "0931");
                    inCome.add(ordersDao.getCountbyDate(year + "0901", year +"0931"));

                }
                if(!ordersDao.getordersbydate(year + "1001", year + "1031").isEmpty()){
                    mounths.add("Octubre");
                    countSales.add(ordersDao.getordersbydate(year +"1001",year + "1031").size());
                    assemblyDate.add(year+"1001"+year + "1031");
                    inCome.add(ordersDao.getCountbyDate(year + "1001", year +"1031"));

                }
                if(!ordersDao.getordersbydate(year + "1101", year + "1131").isEmpty()){
                    mounths.add("Nomviembre");
                    countSales.add(ordersDao.getordersbydate(year +"1101",year + "1131").size());
                    assemblyDate.add(year+"1101"+year + "1131");
                    inCome.add(ordersDao.getCountbyDate(year + "1101", year +"1131"));

                }
                if(!ordersDao.getordersbydate(year + "1201", year + "1231").isEmpty()) {
                    mounths.add("Diciembre");
                    countSales.add(ordersDao.getordersbydate(year +"1201",year + "1231").size());
                    assemblyDate.add(year+"1201"+year + "1231");
                    inCome.add(ordersDao.getCountbyDate(year + "1201", year +"1231"));

                }

                recycler_sales.setLayoutManager(new LinearLayoutManager(Sales_summary_mounth.this));
                recycler_sales.setAdapter(new SalesAdapter(mounths, countSales, assemblyDate, inCome, Sales_summary_mounth.this));

            }

        }

        ordersList = ordersDao.getAllorders();
        fechas = ordersDao.getDates();

        years = new ArrayList<>(list);
        int av = 0;
        int aux= 0;
        for(int i = 0; i< fechas.size();i++){

            String [] a = fechas.get(i).split("");
            String fe = a[1] + a[2] + a[3] + a[4];

            if(av ==0){
                years.add(fe);
                av++;
            }
            if(av >0){
                if(!years.get(aux).contains(fe)){
                    aux++;
                    years.add(fe);
                }
            }
//Obtego una lista de los años para mi spinner
            arrayAdapterSales = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, years);
            arrayAdapterSales.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_year.setAdapter(arrayAdapterSales);

        }


        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                save_selected_item = position;
                save_year_searched = item;
                year = item.toString();
                //control = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = "SeleccionaAlgoWEY";
            }
        });


        button_search_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mounths = new ArrayList<>(list);
                countSales = new ArrayList<>(list);
                assemblyDate = new ArrayList<>(list);
                inCome = new ArrayList<>(list);

                control= true;
                if(year == "SeleccionaAlgoWEY"){

                    Toast.makeText(Sales_summary_mounth.this, "Selecciona un año", Toast.LENGTH_LONG);
                    //este toast es porcualquier cosa XD
                }else {
                    if(!ordersDao.getordersbydate(year +"0101",year + "0131").isEmpty()){
                        mounths.add("Enero");
                        countSales.add(ordersDao.getordersbydate(year +"0101",year + "0131").size());
                        assemblyDate.add(year+"0101"+year + "0131");
                        inCome.add(ordersDao.getCountbyDate(year +"0101",year + "0131"));
                    }
                    if(!ordersDao.getordersbydate(year + "0201", year +"0229").isEmpty()){
                        mounths.add("Febrero");
                        countSales.add(ordersDao.getordersbydate(year +"0201",year + "0229").size());
                        assemblyDate.add(year+"0201"+year + "0229");
                        inCome.add(ordersDao.getCountbyDate(year + "0201", year +"0229"));

                    }
                    if(!ordersDao.getordersbydate(year + "0301", year + "0331").isEmpty()){
                        mounths.add("Marzo");
                        countSales.add(ordersDao.getordersbydate(year +"0301",year + "0331").size());
                        assemblyDate.add(year+"0301"+year + "0331");
                        inCome.add(ordersDao.getCountbyDate(year + "0301", year +"0331"));

                    }
                    if(!ordersDao.getordersbydate( year + "0401",  year +"0431").isEmpty()){
                        mounths.add("Abril");
                        countSales.add(ordersDao.getordersbydate(year +"0401",year + "0431").size());
                        assemblyDate.add(year+"0401"+year + "0431");
                        inCome.add(ordersDao.getCountbyDate(year + "0401", year +"0431"));

                    }
                    if(!ordersDao.getordersbydate(year +"0501", year + "0531").isEmpty()){
                        mounths.add("Mayo");
                        countSales.add(ordersDao.getordersbydate(year +"0501",year + "0531").size());
                        assemblyDate.add(year+"0501"+year + "0531");
                        inCome.add(ordersDao.getCountbyDate(year + "0501", year +"0531"));

                    }
                    if(!ordersDao.getordersbydate(year + "0601", year + "0631").isEmpty()){
                        mounths.add("Junio");
                        countSales.add(ordersDao.getordersbydate(year +"0601",year + "0631").size());
                        assemblyDate.add(year+"0601"+year + "0631");
                        inCome.add(ordersDao.getCountbyDate(year + "0601", year +"0631"));

                    }
                    if(!ordersDao.getordersbydate(year + "0701", year + "0731").isEmpty()){
                        mounths.add("Julio");
                        countSales.add(ordersDao.getordersbydate(year +"0701",year + "0731").size());
                        assemblyDate.add(year+"0701"+year + "0731");
                        inCome.add(ordersDao.getCountbyDate(year + "0701", year +"0731"));

                    }
                    if(!ordersDao.getordersbydate(year + "0801", year + "0831").isEmpty()){
                        mounths.add("Agosto");
                        countSales.add(ordersDao.getordersbydate(year +"0801",year + "0831").size());
                        assemblyDate.add(year+"0801"+year + "0831");
                        inCome.add(ordersDao.getCountbyDate(year + "0801", year +"0831"));

                    }
                    if(!ordersDao.getordersbydate(year + "0901", year + "0931").isEmpty()){
                        mounths.add("Septiembre");
                        countSales.add(ordersDao.getordersbydate(year +"0901",year + "0931").size());
                        assemblyDate.add(year+"0901"+year + "0931");
                        inCome.add(ordersDao.getCountbyDate(year + "0901", year +"0931"));

                    }
                    if(!ordersDao.getordersbydate(year + "1001", year + "1031").isEmpty()){
                        mounths.add("Octubre");
                        countSales.add(ordersDao.getordersbydate(year +"1001",year + "1031").size());
                        assemblyDate.add(year+"1001"+year + "1031");
                        inCome.add(ordersDao.getCountbyDate(year + "1001", year +"1031"));

                    }
                    if(!ordersDao.getordersbydate(year + "1101", year + "1131").isEmpty()){
                        mounths.add("Nomviembre");
                        countSales.add(ordersDao.getordersbydate(year +"1101",year + "1131").size());
                        assemblyDate.add(year+"1101"+year + "1131");
                        inCome.add(ordersDao.getCountbyDate(year + "1101", year +"1131"));

                    }
                    if(!ordersDao.getordersbydate(year + "1201", year + "1231").isEmpty()) {
                        mounths.add("Diciembre");
                        countSales.add(ordersDao.getordersbydate(year +"1201",year + "1231").size());
                        assemblyDate.add(year+"1201"+year + "1231");
                        inCome.add(ordersDao.getCountbyDate(year + "1201", year +"1231"));

                    }

                    recycler_sales.setLayoutManager(new LinearLayoutManager(Sales_summary_mounth.this));
                    recycler_sales.setAdapter(new SalesAdapter(mounths, countSales, assemblyDate, inCome, Sales_summary_mounth.this));
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sales_summary_mounth.this, ReportsActivity.class);
        Sales_summary_mounth.super.finish();
        startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
    }

    public List<OrderAssemblies> ordersAssem;
    private String DATE = "DATE";
    private String DATE_2 = "DATE_2";

    @Override
    public void onNoteClick(int position) {
        String [] aux2 = assemblyDate.get(position).split("");
        String dateN = aux2[1] +aux2[2]+aux2[3]+aux2[4]+aux2[5]+aux2[6]+aux2[7]+aux2[8];
        String dateN2 = aux2[9] +aux2[10]+aux2[11]+aux2[12]+aux2[13]+aux2[14]+aux2[15]+aux2[16];
        Intent intent = new Intent(this,secundary_Sales.class);
        intent.putExtra(DATE,dateN);
        intent.putExtra(DATE_2,dateN2);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SAVED_CONTROL, control);
        outState.putInt(SAVE_SELECTED_ITEM, save_selected_item);
            if(control= true){
                outState.putString(SAVED_YEAR, save_year_searched);
            }
    }
}
