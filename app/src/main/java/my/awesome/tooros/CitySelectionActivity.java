package my.awesome.tooros;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class CitySelectionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

//    String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
String HttpURL = "https://tooros.in/api/api.php";
    String finalResult ;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;
    ProgressDialog progressDialog3;
    JsonHttpParse jsonhttpParse = new JsonHttpParse();

    TextView username;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    Spinner select_city_spinner;
    Spinner delivery_type_spinner;

    String city;
    Calendar myEndCalendar;
    Calendar myStartCalendar;


    TextView startdate,enddate;
    String startdateSelected="",enddateSelected="";



    DatePickerDialog.OnDateSetListener startdatelistener;
    DatePickerDialog.OnDateSetListener enddatelistener;



   RecyclerView senitization_recycler;
    ArrayList<Guidlines_model> guidlines_models = new ArrayList<Guidlines_model>();
Guidlines_adapter guidlines_adapter;
//
RecyclerView offer_recycler;
    ArrayList<Guidlines_model> offer_model_arraylist = new ArrayList<Guidlines_model>();
    Offer_adapter offer_adapter;

 //
 int hour=0,min,hour1=0,min1,daydif=0,monthdif=0,yeardif=0;


    TextView startime,endtime;


    String st="",et="";


    //this is city adapter
    CityAdapter cityAdapter;
 ArrayList<ModelCity> modelcityss=new ArrayList<>();
    ModelCity modelCity;
   // List<String> list=new ArrayList<String>() ;

    TextView carname,fuel,gear,baggage,totalseat,startdatebooking,enddatebooking;
    ImageView carimagebooking;

    LinearLayout bookinglinearLayout;
    String userid;


    String[] country= {"--Select City--","Bhubenswar","Puri"};

    int dayOfMonth1;
    ////////////////////////////////////////////////////
    /////////////////////////


    private ArrayList<HashMap<String, Object>> map = new ArrayList<>();

    private ArrayList<HashMap<String, Object>> map2 = new ArrayList<>();

    String city_name="";
    String city_id="";
    String dtype = "";

    String st_day;

    private Calendar c = Calendar.getInstance();
    private SharedPreferences sh;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
        startime=findViewById(R.id.startt);
        endtime=findViewById(R.id.endt);
        sh = getSharedPreferences("sh", Activity.MODE_PRIVATE);

        delivery_type_spinner = findViewById(R.id.district);

        HashMap<String, Object> _item2 = new HashMap<>();
        _item2.put("key", "Select Delivery Type..");
        _item2.put("id", 0);
        map2.add(_item2);

        _item2 = new HashMap<>();
        _item2.put("key", "Home Delivery");

        // _item.put("id", ob.getString("id"));

        map2.add(_item2);
        _item2 = new HashMap<>();
        _item2.put("key", "Parking Place");

        // _item.put("id", ob.getString("id"));

        map2.add(_item2);





        carname=findViewById(R.id.carname);
        fuel=findViewById(R.id.fueltype);
        gear=findViewById(R.id.geartype);
        baggage=findViewById(R.id.baggage);
        totalseat=findViewById(R.id.totalseat);
        startdatebooking=findViewById(R.id.startdate);
        enddatebooking=findViewById(R.id.enddate);
        carimagebooking=findViewById(R.id.carimage);
        bookinglinearLayout=findViewById(R.id.bookingstatus);

        offer_recycler=findViewById(R.id.offer_recycler);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        startdate=findViewById(R.id.startDate);
        enddate=findViewById(R.id.endDate);
        select_city_spinner = findViewById(R.id.select_city);

        delivery_type_spinner.setAdapter(new SelectDeliveryAdapter(map2));

        // this listview for spinner

       /* ArrayAdapter<String> adapter =
                new ArrayAdapter (this,
                        android.R.layout.simple_spinner_item,
                        country);

        // set simple layout resource file
        // for each item of spinner

        adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
*/



/*        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //



        boolean online=isOnline();
        if(!online){
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection and reopen the application");

            //            builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    onCreate(savedInstanceState);
//                }
//            });


            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

       /* cityAdapter=new CityAdapter(CitySelectionActivity.this,R.layout.city,modelcityss);
        spinner.setAdapter(cityAdapter);*/


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username= (TextView) headerView.findViewById((R.id.user_name));
        SharedPreferences sharedPreferences2 = CitySelectionActivity.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        Menu menu = navigationView.getMenu();

        if (sharedPreferences2.getString("Name", null) != null) {

            username.setText(sharedPreferences2.getString("Name", null));
            userid=sharedPreferences2.getString("userid",null);

            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_SignUp).setVisible(false);
             // menu.findItem(R.id.nav_logout).setVisible(true);
             // menu.findItem(R.id.nav_profile).setVisible(true);

        }else{

            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_booking).setVisible(false);
        }

        ServicesFunction("getAlllocation");  //add service name
        gettingOffersFunction("getAlloffer");

        if(userid!=null){
            final int res=0;
            upCommingBookingInfo("upCommingBookingInfo",Integer.parseInt(userid),res);
        }else{
            bookinglinearLayout.setVisibility(View.GONE);
        }



        setSupportActionBar(toolbar);

        //hide or show items
//        SharedPreferences shared = getSharedPreferences("loginOrNot", MODE_PRIVATE);
//        String info = (shared.getString("info", ""));
//       // String name = (shared.getString("username", ""));
//        Menu menu2 = navigationView.getMenu();
//        if(info.equals("yes")){
//            //username.setText(name);
//            menu2.findItem(R.id.nav_login).setVisible(false);
//            menu2.findItem(R.id.nav_SignUp).setVisible(false);
//            menu2.findItem(R.id.nav_logout).setVisible(true);
//            menu2.findItem(R.id.nav_profile).setVisible(true);
//
//        }else{
//           // username.setText(name);
//            menu2.findItem(R.id.nav_logout).setVisible(false);
//            menu2.findItem(R.id.nav_profile).setVisible(false);
//            menu2.findItem(R.id.nav_booking).setVisible(false);
//        }


        navigationView.setItemIconTintList(null);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

         navigationView.bringToFront();

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);



         myStartCalendar = Calendar.getInstance();
         myEndCalendar = Calendar.getInstance();


        //endtime output : 02:00 AM  // format hh:00 aaa
        //end_date output :  03-12-2004 // format dd-MM-yyyy
        // this shared preference used for show rate dialog on end date
        //sh.edit().putString("drop_date",enddate.toString()+endt).apply();
        // find rest code on paymentpage,java at line 878









       /*  if(getIntent().hasExtra("rate"))
         {
             if(getIntent().getStringExtra("rate").equals("true")){

                 //Toast.makeText(this, "rate inside", Toast.LENGTH_SHORT).show();

             }
         }


*/



       // cus_rate("https://play.google.com/store/apps/details?id=my.awesome.tooros");


        select_city_spinner .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;

                city_name = map.get((int) _position).get("key").toString();
                city_id = map.get((int) _position).get("id").toString();



                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Date", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("city",""+city_name);
                myEdit.putString("cityid",""+city_id);
                myEdit.apply();






                sneekmsg(city_name, _param2);


                //Toast.makeText (CitySelectionActivity.this,value, Toast.LENGTH_SHORT).show ();

                    }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });

        delivery_type_spinner .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;

                dtype = Objects.requireNonNull(map2.get((int) _position).get("key")).toString();

               sneekmsg(dtype, _param2);
               //Toast.makeText (CitySelectionActivity.this, dtype , Toast.LENGTH_SHORT).show ();

            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });


         startdatelistener = (view, year, monthOfYear, dayOfMonth) -> {

             // TODO Auto-generated method stub
             myStartCalendar.set(Calendar.YEAR, year);
             myStartCalendar.set(Calendar.MONTH, monthOfYear);
             myStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
             String myFormat = "yyyy-MM-dd";//In which you need put here
             String toShowFormat="dd-MM-yyyy";
             SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
             SimpleDateFormat sdf_show = new SimpleDateFormat(toShowFormat, Locale.US);

              startdateSelected=sdf.format(myStartCalendar.getTime());
             String startdateSelected_show=sdf_show.format(myStartCalendar.getTime());

             startdate.setText(startdateSelected_show);

             st_day = startdateSelected_show; // used for if condition by shubhamjit 12/9/2022

            //  Toast.makeText(CitySelectionActivity.this, ""+dateSelected, Toast.LENGTH_SHORT).show();
             SharedPreferences sharedPreferences = CitySelectionActivity.this.getSharedPreferences("Date", MODE_PRIVATE);
             SharedPreferences.Editor myEdit = sharedPreferences.edit();
             myEdit.putString("startdate",""+startdateSelected);
             myEdit.putString("startdate_show",""+startdateSelected_show);

             myEdit.apply();

            // Toast.makeText(CitySelectionActivity.this,sdf.format(myStartCalendar.getTime()) , Toast.LENGTH_SHORT).show();
         };

         enddatelistener = (view, year, monthOfYear, dayOfMonth) -> {

               // TODO Auto-generated method stub

             String getstartdate=startdateSelected;
             String getfrom[]=getstartdate.split("-");
              dayOfMonth1=Integer.parseInt(getfrom[2]);
             int month1=Integer.parseInt(getfrom[1]);
             int year1=Integer.parseInt(getfrom[0]);
             myEndCalendar.set(Calendar.YEAR, year);
             myEndCalendar.set(Calendar.MONTH, monthOfYear);
             myEndCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


             String myFormat = "yyyy-MM-dd"; //In which you need put here
             String myFormat_show = "dd-MM-yyyy"; //In which you need put here
             SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
             SimpleDateFormat sdf_show = new SimpleDateFormat(myFormat_show, Locale.US);


             enddateSelected=sdf.format(myEndCalendar.getTime());
             String enddateSelected_show=sdf_show.format(myEndCalendar.getTime());
             String getenddate=enddateSelected;
             String getfrom2[]=getenddate.split("-");
             int dayOfMonth2=Integer.parseInt(getfrom2[2]);
             int mont2=Integer.parseInt(getfrom2[1]);
             int year2=Integer.parseInt(getfrom2[0]);
         daydif=dayOfMonth2-dayOfMonth1;
         monthdif=mont2-month1;
         yeardif=year2-year1;


           /// Toast.makeText(CitySelectionActivity.this, ""+daydif+"\n\n"+monthdif+"\n\n"+""+yeardif, Toast.LENGTH_SHORT).show();

                 enddate.setText(enddateSelected_show);

             //String dateSelected=sdf.format(myEndCalendar.getTime());

           //  enddate.setText(enddateSelected);
             SharedPreferences sharedPreferences = CitySelectionActivity.this.getSharedPreferences("Date", MODE_PRIVATE);
             SharedPreferences.Editor myEdit = sharedPreferences.edit();
             myEdit.putString("Enddate",""+enddateSelected);
             myEdit.putString("Enddate_show",""+enddateSelected_show);
             myEdit.apply();


             //Toast.makeText(CitySelectionActivity.this,sdf.format(myEndCalendar.getTime()) , Toast.LENGTH_SHORT).show();
         };

        //we have to fetch image here and set on guidline models




        offer_adapter=new Offer_adapter(offer_model_arraylist,CitySelectionActivity.this);
        offer_recycler.setAdapter(offer_adapter);

        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(CitySelectionActivity.this,RecyclerView.HORIZONTAL,false);
        offer_recycler.setLayoutManager(linearLayoutManager1);



        // Guidlines_model offer2=new Guidlines_model(R.drawable.offertooros);
        // offer_model_arraylist.add(offer2);





        //this will show the rate us dialog custom
        cus_rate_dialog_show_logic();

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onClickStartDate(View view) {

        DatePickerDialog datePickerDialog= new DatePickerDialog(CitySelectionActivity.this, startdatelistener, myStartCalendar
                .get(Calendar.YEAR), myStartCalendar.get(Calendar.MONTH),
                myStartCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
       // datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // datePickerDialog.getWindow().setNavigationBarDividerColor(getResources().getColor(R.color.green));
        datePickerDialog.show();




    }

    public void onClickEndDate(View view) {

        DatePickerDialog datePickerDialog= new DatePickerDialog(CitySelectionActivity.this, enddatelistener, myEndCalendar
                .get(Calendar.YEAR), myEndCalendar.get(Calendar.MONTH),
                myEndCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();


    }

    private void upCommingBookingInfo(final String method, final int user_id,  final int res) {

        class UserLoginClass2 extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(CitySelectionActivity.this,"Loading...",null,true,true);
                progressDialog2.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(httpResponseMsg);
                    String status = jsonObject2.getString("status");

                    if(status.equals("200")){

                        JSONObject jsonObject =new JSONObject(jsonObject2.getString("result"));
                        carname.setText(jsonObject.getString("booked_car"));
                        fuel.setText(jsonObject.getString("fuel_type"));
                        gear.setText(jsonObject.getString("gear_type"));
                        baggage.setText(jsonObject.getString("no_of_baggage")+" Baggage");
                        totalseat.setText(jsonObject.getString("no_of_seat")+" Seat");
                        startdatebooking.setText(jsonObject.getString("bookFrom"));
                        enddatebooking.setText(jsonObject.getString("bookTo"));

                        String carurl=jsonObject.getString("car_img");
                        Picasso.with(CitySelectionActivity.this).load(carurl.replace("http","https")).fit().centerInside().into(carimagebooking);

                       // Toast.makeText(CitySelectionActivity.this, "booking status", Toast.LENGTH_SHORT).show();




                    }else{

                        bookinglinearLayout.setVisibility(View.GONE);

//                        String messege = jsonObject2.getString("msg");
//                        Toast.makeText(CitySelectionActivity.this, messege, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Toast.makeText(Signup.this, httpResponseMsg, Toast.LENGTH_SHORT).show();



            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"upCommingBookingInfo\",\"user_id\":\""+user_id+"\"}";




//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass2 userLoginClass2 = new UserLoginClass2();

        userLoginClass2.execute(method);




    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(CitySelectionActivity.this, "No Internet connection!", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

//add service name accordingly
       public void ServicesFunction(String getAlllocation) {
//we have to fetch here
        class UserLoginClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                progressDialog = ProgressDialog.show(CitySelectionActivity.this,"Loading...",null,true,false);
                progressDialog.setCancelable(false);
                //progressDialog = ProgressDialog.show(CitySelectionActivity.this,"Loading Services",null,true,true);


            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                // Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                if(httpResponseMsg.contains("200")){
                    //Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                    try {
                       // modelcityss.add();

                        HashMap<String, Object> _item = new HashMap<>();
                        _item.put("key", "Select a city..");
                        _item.put("id", 0);
                        map.add(_item);


                        JSONObject jsonObject = new JSONObject(httpResponseMsg);
                        JSONArray result = jsonObject.getJSONArray("result");
                        for (int i=0; i<result.length(); i++ ){
                            JSONObject ob=result.getJSONObject(i);
                               //  fetch image here and set to adapter
                              //  Toast.makeText(FirstActivity.this, ob.getString("name"), Toast.LENGTH_SHORT).show();
                             //  homemodel history=new homemodel(R.drawable.promocodecar2,ob.getString("img"),
                            //  ob.getString("service_name"),ob.getString("sch_servie_id"));
                           //  androidFlavors.add(history);
                               //   list.add(ob.getString("location"));

                            _item = new HashMap<>();
                            _item.put("key", ob.getString("location"));
                            _item.put("id", ob.getString("id"));
                            map.add(_item);


                            //modelCity=new ModelCity(ob.getString("location"),ob.getString("id"));

                            //Toast.makeText(CitySelectionActivity.this, ob.getString("location")+"\n\n"+ob.getString("id"), Toast.LENGTH_SHORT).show();

                            //modelcityss.add(modelCity);
                        }

                        select_city_spinner.setAdapter(new SelectCityAdapter(map));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }else{
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(httpResponseMsg);
                        String messege = jsonObject.getString("msg");
                        Toast.makeText(
                                CitySelectionActivity.this,
                                messege,
                                Toast.LENGTH_SHORT)
                                .show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"getAlllocation\"}";

                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(getAlllocation);
    }




    public class SelectCityAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;
        public SelectCityAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }
        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.cus_spinner, null);
            }

            final LinearLayout linear1 = (LinearLayout) _view.findViewById(R.id.linear1);

            final TextView textview1 = (TextView) _view.findViewById(R.id.textview1);

            textview1.setText(map.get((int)_position).get("key").toString());







            return _view;
        }
    }

    private void cus_rate_dialog_show_logic(){

        c = Calendar.getInstance();
try {

    if(!sh.getString("drop_exact_day","").equals("")){

        // explain


        /**

         this rate dialog will show after drop date and if a user not
         provide any rate means not click on rate us button then rate us
         dialog show each time of opening of app here iam used nextday
         and nextmoth after the drop date to find the next date of drop
         date to show the rate dialog dialog again this dialog added
         on v1.1.1 versionCode 13 date 23th sept 2022 by shubhamjit

         **/


        String saved_date = sh.getString("drop_date", "");

        int nextDropDay = Integer.parseInt(sh.getString("drop_exact_day",""));
        int nextDropMonth = Integer.parseInt(sh.getString("drop_exact_month",""));


        @SuppressLint("SimpleDateFormat")
        String  today = new SimpleDateFormat("dd-MM-yyyy hh:00 aaa").format(c.getTime());
        int c_exactDay = Integer.parseInt(new SimpleDateFormat("dd").format(c.getTime()));
        int c_exactMonth = Integer.parseInt(new SimpleDateFormat("MM").format(c.getTime()));

       // Util.showMessage(getApplicationContext(), saved_date);


        if (today.equals(saved_date) || nextDropDay < c_exactDay || nextDropMonth < c_exactMonth ) {
            cus_rate("https://play.google.com/store/apps/details?id=my.awesome.tooros");
           // Util.showMessage(getApplicationContext(), "Show rate....");


        }
        else {
          //  Util.showMessage(getApplicationContext(), "Not show");
        }
    }

}catch (Exception e)
{
    //Util.showMessage(getApplicationContext(),e.toString());
}



    }

    private void cus_rate(String open_link) {
        final AlertDialog dialog3 = new AlertDialog.Builder(this).create();
        View inflate = getLayoutInflater().inflate(R.layout.rating,null);

        dialog3.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog3.setView(inflate);

        final TextView rate = (TextView) inflate.findViewById(R.id.rate);
        final TextView later = (TextView) inflate.findViewById(R.id.later);
				/*TextView done  = (TextView) inflate.findViewById(R.id.done);
TextView close  = (TextView) inflate.findViewById(R.id.close);
final TextView amt = (TextView) inflate.findViewById(R.id.amt);
*/
        final LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);


        dialog3.setCancelable(false);


        later.setOnClickListener(_view -> dialog3.dismiss());
        rate.setOnClickListener(_view -> {
            if(!open_link.equals("")){

                sh.edit().putString("drop_date", "").apply();
                sh.edit().putString("drop_exact_month", "").apply();
                sh.edit().putString("drop_exact_day", "").apply();

                //it will rest the drop date after user click the

                // rate us button

                Intent in = new Intent();
                in.setAction(Intent.ACTION_VIEW);
                in.setData(Uri.parse(open_link));
                startActivity(in);

                dialog3.dismiss();
            }

        });
        dialog3.show();
    }

    public class SelectDeliveryAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;
        public SelectDeliveryAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }
        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.cus_spinner, null);
            }

            final LinearLayout linear1 = (LinearLayout) _view.findViewById(R.id.linear1);

            final TextView textview1 = (TextView) _view.findViewById(R.id.textview1);

            textview1.setText(map2.get((int)_position).get("key").toString());


            String deliveryType=map2.get((int)_position).get("key").toString();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Date", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("deliveryType",""+deliveryType);

            myEdit.apply();


/*

            String id=map.get((int)_position).get("id").toString();
            String city=map.get((int)_position).get("key").toString();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Date", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("city",""+city);
            myEdit.putString("cityid",""+id);
            myEdit.apply();

*/



            return _view;
        }
    }



    public void   gettingOffersFunction(String getAllOffers) {
//we have to fetch here
        class OfferClass  extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog3 = ProgressDialog.show(CitySelectionActivity.this,"Loading..",null,true,false);
          progressDialog3.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog3.dismiss();

                // Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                if(httpResponseMsg.contains("200")){
                    //Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                    try {

                        JSONObject jsonObject = new JSONObject(httpResponseMsg);
                        JSONArray result = jsonObject.getJSONArray("result");
                        int count=0;
                        for (int i=0; i<result.length(); i++ ){
                            JSONObject ob=result.getJSONObject(i);

                            String offerimage=ob.getString("promo_image");

                            if(!TextUtils.isEmpty(offerimage)){

                                Guidlines_model offer=new Guidlines_model(offerimage);
                                offer_model_arraylist.add(offer);
                                count++;

                            }
                      }
                     if(count==0){
                         offer_recycler.setVisibility(View.GONE);
                     }

//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    offer_recycler.setAdapter(offer_adapter);
                    offer_adapter.notifyDataSetChanged();



                }else{

                    offer_recycler.setVisibility(View.GONE);
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(httpResponseMsg);
                        String messege = jsonObject.getString("msg");
                        Toast.makeText(CitySelectionActivity.this, messege, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"getAlloffer\"}";

                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        OfferClass offerClass = new OfferClass();

        offerClass.execute(getAllOffers);

    }



    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        //Toast.makeText(this,sdf.format(myCalendar.getTime()) , Toast.LENGTH_SHORT).show();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickFindCarButton(View view) throws IOException {



        if(startdateSelected.equals("") || enddateSelected.equals("") || st.equals("") || et.equals("") || city_name.equals("") || city_name.equals("Select a city..") || dtype.equals("Select Delivery Type..") || dtype.equals("") )

        {

            dialog("Select required inputs !");
            //
            // Toast.makeText(this, "!", Toast.LENGTH_SHORT).show();

        }else if(daydif<0) {
            if (monthdif <= 0) {
            if(yeardif<=0) {

                dialog("Select valid date to drop !!");

                //dialog("Select valid  date to drop !!"+yeardif+"\n\n"+monthdif+"\n\n"+daydif);

            } else { chackDeliveryType(); }
            } else {
                if (yeardif >= 0) {

                     chackDeliveryType();
                }
            }
        }
        else { chackDeliveryType(); }



     }

private void dialog(String msg)
{
    AlertDialog.Builder d = new AlertDialog.Builder(this);
    d.setMessage(msg);
    d.setPositiveButton("OK", (dialogInterface, i) -> {

    });
    d.create().show();

}

private void sneekmsg(String msg, View view)
{
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setAction("OK", view1 -> {

            })

            .setActionTextColor(getResources().getColor(R.color.colorAccent))
            .show();

}


@SuppressLint("SimpleDateFormat")
private void chackDeliveryType(){
    c = Calendar.getInstance();
    // if home delivery is true


    int dif = Math.abs(hour1 - hour);
    //Toast.makeText(CitySelectionActivity.this, ""+dif, Toast.LENGTH_SHORT).show();

    if (dtype.equals("Home Delivery")) {

       String c_day = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
       
        //  daydif > 0 ||
        //String toShowFormat="dd-MM-yyyy";
        // || hour == 00

        if(!c_day.equals(st_day)  || (Double.parseDouble(new SimpleDateFormat("H").format(c.getTime())) >= 21 || hour >= (Double.parseDouble(new SimpleDateFormat("H").format(c.getTime())) + 3)))
        {
                lauchCarBooking();

            // setText("Success");
            // " hour " is pickup hour and
            // " hour 1" is end hour


        } else {

            // setText("Failed ");
            dialog("Sorry!! Your Pick Up time will be 3 hours more than current time");

        }




    } else {

        // if home delivery is false
        lauchCarBooking();
    }



}

    private void lauchCarBooking()
    {
        int dif = Math.abs(hour1 - hour);
        //  Toast.makeText(CitySelectionActivity.this, ""+dif, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences2 = CitySelectionActivity.this.getSharedPreferences("Date", MODE_PRIVATE);
        final SharedPreferences.Editor myEdit2 = sharedPreferences2.edit();
        myEdit2.putInt("dif", dif);
        myEdit2.apply();
        SharedPreferences sharedPreferencesForLoginOrNot = CitySelectionActivity.this.getSharedPreferences("loginOrNot", MODE_PRIVATE);
        String login = sharedPreferencesForLoginOrNot.getString("info", null);
        //   Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        Intent intent = new Intent(CitySelectionActivity.this, CarBooking.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed(){



        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
           // super.onBackPressed();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to exit?");
            builder1.setCancelable(true);

            builder1.setPositiveButton("YES", (dialogInterface, i) -> {

            });

            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> finishAffinity());

            builder1.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

           builder1.create().show();
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.nav_home:
                break;
            case R.id.nav_help:
                startActivity(new Intent(this,HelpAndSupport.class));
                break;
            case R.id.nav_about_us:
                startActivity(new Intent(this,AboutUs.class));
                break;
            case R.id.nav_booking:
                startActivity(new Intent(this,BookingHistory.class));
                break;
            case R.id.nav_policy:
                startActivity(new Intent(this,PolicyActivity.class));
                break;
            case R.id.nav_terms:
                startActivity(new Intent(this,TermsAndCondition.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this,Profile.class));
                break;
            case R.id.nav_login:
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.nav_SignUp:
                startActivity(new Intent(this,Signup.class));
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Are you sure you want to logout?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(CitySelectionActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
//                                SharedPreferences sharedPreferences = CitySelectionActivity.this.getSharedPreferences("loginOrNot", MODE_PRIVATE);
//                                final SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                                myEdit.putString("info","no");
//                                // myEdit.putString("username","Guest_User");
//                                myEdit.apply();
                                SharedPreferences preferences =getSharedPreferences("MySharedPref2",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();
                                startActivity(new Intent(CitySelectionActivity.this,CitySelectionActivity.class));
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void endTime(View view) {

        TimePickerDialog timePickerDialog=new TimePickerDialog(CitySelectionActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                hour1=hourOfDay;

               // min1=00;
                String time1=hour1+":"+00;
                SimpleDateFormat f24hours1=new SimpleDateFormat("HH:mm");
                try {



                    Date date1=f24hours1.parse(time1);
                   SimpleDateFormat f12hour1=new SimpleDateFormat("hh:mm aa");
                    endtime.setText(f12hour1.format(date1));
                     et=endtime.getText().toString();
                     String endtimee=f24hours1.format(date1);
                    SharedPreferences sharedPreferences1 = CitySelectionActivity.this.getSharedPreferences("Date", MODE_PRIVATE);
                    final SharedPreferences.Editor myEdit = sharedPreferences1.edit();
                    myEdit.putString("endtime",""+et);
                    myEdit.putString("endtimee",""+endtimee);
                    myEdit.apply();

                    //endtime output : 02:00 AM  // format hh:00 aaa

                    // Toast.makeText(getApplicationContext(),""+et, Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    // Toast.makeText(CarBooking.this, "hi", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        },12,0,false
        );
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(hour1,min1);
        timePickerDialog.show();

    }

    public void starttime(View view) {
     final   TimePickerDialog timePickerDialog=new TimePickerDialog(CitySelectionActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                hour=hourOfDay;

               min=minute;
                String time1=hour+":"+00;
                SimpleDateFormat f24hours=new SimpleDateFormat("HH:mm");
                try {
                    Date date=f24hours.parse(time1);
                    SimpleDateFormat f12hour=new SimpleDateFormat("hh:mm aa");

                    startime.setText(f12hour.format(date));
                    String timee=f24hours.format(date);
                     st=startime.getText().toString();
                    SharedPreferences sharedPreferences1 = CitySelectionActivity.this.getSharedPreferences("Date", MODE_PRIVATE);
                    final SharedPreferences.Editor myEdit = sharedPreferences1.edit();
                    myEdit.putString("starttime",""+st);

                    myEdit.putString("startimee",""+timee);

                    //Toast.makeText(CitySelectionActivity.this, timee, Toast.LENGTH_SHORT).show();
                    myEdit.apply();
                    //Toast.makeText(CarBooking.this, ""+startime, Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    // Toast.makeText(CarBooking.this, "hi", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        },12,0,false);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(hour,min);
        timePickerDialog.show();








    }
}