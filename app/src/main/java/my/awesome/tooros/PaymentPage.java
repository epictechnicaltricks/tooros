package my.awesome.tooros;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;



public class   PaymentPage<online> extends AppCompatActivity implements PaymentResultListener {
    TextView gst,total,basefair,coupondiscount,securitycharges,picupcharges,weekdaychages,weekendcharges,
            startdate,enddate,timeduration,carname,startt,endt,geartype,fuel;
    ImageView carimage;
    EditText Couponcode;
    Button book,apply;
    String book_id,booking_id,payment_id,transaction_id;
    String name,email,mobile,dob,aadharno,dlno,userid;
    ProgressDialog progressDialog;

    JsonHttpParse jsonhttpParse = new JsonHttpParse();
    String finalResult ;
//    String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
String HttpURL = "https://tooros.in/api/api.php";

    View view;

    TextView dfees;

    String dtype23="";

    HashMap<String, Object> map2 = new HashMap<>();

    private RequestNetwork rq,weekend_status;
    private RequestNetwork.RequestListener _rq_request_listener,
            weekend_status_listener;



    LinearLayout dfees_layout;
    String df = "";
    int cost=0;
    int df_=0;
    String weekend_status_value="";
    float totalhr;
   Float M_weekdays_hr , M_weekend_hr;


    private SharedPreferences sh;
    private Calendar c = Calendar.getInstance();

    String city_id="";

    private static final String TAG = PaymentPage.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        view = findViewById(android.R.id.content);

        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
        carimage=findViewById(R.id.carimage);
        weekdaychages=findViewById(R.id.textView21);
        weekendcharges=findViewById(R.id.textView22);
        timeduration=findViewById(R.id.textView12);
        basefair=findViewById(R.id.basefare);
        startt=findViewById(R.id.starttime);
        endt=findViewById(R.id.endtime);
       // picupcharges=findViewById(R.id.delivery);
        coupondiscount=findViewById(R.id.discount);
        gst=findViewById(R.id.gst);
        total=findViewById(R.id.totalamount);
        Couponcode=findViewById(R.id.couponcode);
        carname=findViewById(R.id.carname);
        book=findViewById(R.id.book);
        geartype=findViewById(R.id.textView9);
        fuel=findViewById(R.id.textView10);
        apply=findViewById(R.id.apply);
        securitycharges=findViewById(R.id.security);

        dfees = findViewById(R.id.dfees);
        dfees_layout = findViewById(R.id.dfees_layout);

        sh = getSharedPreferences("sh", Activity.MODE_PRIVATE);

        SharedPreferences sharedPreferences3 = PaymentPage.this.getSharedPreferences("sharedpref3", MODE_PRIVATE);
        String url=sharedPreferences3.getString("carimage",null);
        Picasso.with(this).load(url.replace("http","https")).fit().centerInside().into(carimage);
        String carn=sharedPreferences3.getString("carname",null);
        String  geart=sharedPreferences3.getString("geartype",null);
        String fuelt=sharedPreferences3.getString("fuel",null);

        final String car_id=sharedPreferences3.getString("car_id",null);

//        Intent intent=getIntent();
//       Bundle bundle=getIntent().getExtras();
//       if(bundle!=null){
////           int resid=bundle.getInt("carimage");
////           carimage.setImageResource(resid);
//           String url=bundle.getString("carimage");
//           Picasso.with(this).load(url.replace("http","https")).fit().centerInside().into(carimage);
//       }
//       String carn=intent.getExtras().getString("carname");
//       String  geart=intent.getExtras().getString("geartype");
//       String fuelt=intent.getExtras().getString("fuel");
//       String base_fare=intent.getExtras().getString("cost");
//       String weekendcost=intent.getExtras().getString("weekendcost");
//       final String car_id=intent.getExtras().getString("car_id");

       carname.setText(""+carn);
       geartype.setText(""+geart);
       fuel.setText(""+fuelt);

//       basefair.setText(""+base_fare);
//       weekdaychages.append(""+weekendcost);
//       dfees_layout.setVisibility(View.GONE);









        SharedPreferences sharedPreferences = PaymentPage.this.getSharedPreferences("Date", MODE_PRIVATE);

        final String stdate= sharedPreferences.getString("startdate",null);

        String stdate_show= sharedPreferences.getString("startdate_show",null);
        final String end= sharedPreferences.getString("Enddate",null);
        String end_show= sharedPreferences.getString("Enddate_show",null);
        final String startime=sharedPreferences.getString("starttime",null);
        final String endtime=sharedPreferences.getString("endtime",null);
        int duration=sharedPreferences.getInt("dif",0);

        String dur=String.valueOf(duration);

        city_id=sharedPreferences.getString("cityid",null);

       // Toast.makeText(this, city_id, Toast.LENGTH_SHORT).show();





        dtype23 = sharedPreferences.getString("deliveryType",null);


           // dtype = sharedPreferences.getString("deliveryType",null);



               // Util.showMessage(PaymentPage.this,"map2 ");







        // timeduration.append(" hrs");
//        if(stdate!=" "&& end !=" "&&startime!=null&&endtime!=null&&dur!=null){
//            startdate.setText(stdate);
//            enddate.setText(end);
//            startt.setText(startime);
//            endt.setText(endtime);
//           timeduration.setText(""+dur);
//           timeduration.append(" hrs");
//        }else{
//
//        }


        SharedPreferences sharedPreferences2 = PaymentPage.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        if (sharedPreferences2 != null) {
            name = sharedPreferences2.getString("Name", null);
            mobile = sharedPreferences2.getString("Mobile", null);
            email = sharedPreferences2.getString("Email", null);
            dob = sharedPreferences2.getString("Dob", null);
            dlno = sharedPreferences2.getString("Dlno", null);
            aadharno = sharedPreferences2.getString("Aadharno", null);
            userid=sharedPreferences2.getString("userid",null);

        }
        if(stdate!=" "&& end !=" "){
            startdate.setText(stdate_show);
            enddate.setText(end_show);
            startt.setText(startime);
            endt.setText(endtime);

        }else{

        }

        final String concatpdate=stdate+" "+startime;
        final String concatDdate=end+" "+endtime;

        getPriceDetails("getPriceDetails",car_id,concatpdate,concatDdate,"","Online");
//        Toast.makeText(this, "car_id"+car_id, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "startdate"+concatpdate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "enddate"+concatDdate, Toast.LENGTH_SHORT).show();


        apply.setOnClickListener(v -> {

          String coupon=Couponcode.getText().toString();
          String secutity="Online";

         getPriceDetails("getPriceDetails",car_id,concatpdate,concatDdate,coupon,secutity);


        });


        book.setOnClickListener(v -> {

           // Toast.makeText(PaymentPage.this, ""+userid, Toast.LENGTH_SHORT).show();

            //take all input send to api and proceed for payment

            SharedPreferences sharedPreferences21 = PaymentPage.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
            if (sharedPreferences21.getString("Name", null) == null) {
                Toast.makeText(PaymentPage.this, "Please Login first !!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PaymentPage.this,Login.class);
                int pay=1;
                intent.putExtra("val",pay);
                startActivity(intent);
            }else{



                if(dtype23.equals("Home Delivery")) {

                    bookCab("bookCab", userid, stdate, end, startime, endtime, car_id, total.getText().toString(),"Home Delivery");

                } else {

                    bookCab("bookCab", userid, stdate, end, startime, endtime, car_id, total.getText().toString(),"Parking Place");
                    //Toast.makeText(this,userid+"\n"+ stdate+"\n"+end+"\n"+ startime+"\n"+endtime+"\n"+car_id+"\n"+total.getText().toString()+"\n", Toast.LENGTH_SHORT).show();
                }


                startPayment(Float.parseFloat(total.getText().toString()));
            }


//                SharedPreferences sharedPreferences = PaymentPage.this.getSharedPreferences("loginOrNot", MODE_PRIVATE);
//
//                if(sharedPreferences!=null){
//                    String check=sharedPreferences.getString("info",null);
//                    if(check=="no"||check==null) {
//                        Toast.makeText(PaymentPage.this, "Please Login to continue..", Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(PaymentPage.this,Login.class);
//                        int pay=1;
//                        intent.putExtra("val",pay);
//                        startActivity(intent);
//                    }else {
//                        bookCab("bookCab",userid,stdate,end,startime,endtime,car_id,total.getText().toString());
//
//                        startPayment(Float.parseFloat(total.getText().toString()));
//                    }
//                }



        });

            rq = new RequestNetwork(PaymentPage.this);
            weekend_status = new RequestNetwork(PaymentPage.this);

            //after request the data

            _rq_request_listener = new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
                    final String _tag = _param1;
                    final String _response = _param2;
                    final HashMap<String, Object> _responseHeaders = _param3;

                    try{

                        if(progressDialog != null)
                        {
                            progressDialog.dismiss();
                        }


                        HashMap<String, Object> new_map;
                        new_map = new Gson().fromJson(_response, new TypeToken<HashMap<String,Object>>(){}.getType());
                        df = Objects.requireNonNull(new_map.get("result")).toString();

                        df_  = (int) Float.parseFloat(df);
                        int total_ = cost + (int)df_ ; // df is delivery fees
                        total.setText(total_+"");
                        dfees.setText((int)df_+"");

                       /*if(cost != 0 && df_ != 0)
                       {
                           int total_ = cost + (int)df_ ; // df is delivery fees
                           total.setText(total_+"");
                       }*/

                        //Toast.makeText(PaymentPage.this, df+"", Toast.LENGTH_SHORT).show();

                    }catch (Exception e)
                    {
                        Util.showMessage(PaymentPage.this,e.toString());
                    }


                }

                @Override
                public void onErrorResponse(String _param1, String _param2) {
                    final String _tag = _param1;
                    final String _message = _param2;

                }
            };


            weekend_status_listener = new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {

                    HashMap<String, Object> new_map2;
                    new_map2 = new Gson().fromJson( response, new TypeToken<HashMap<String,Object>>(){}.getType());
                    weekend_status_value = Objects.requireNonNull(new_map2.get("result")).toString();

                   // Toast.makeText(PaymentPage.this, weekend_status_value, Toast.LENGTH_SHORT).show();


                    if(weekend_status_value.equals("1")) {

                        totalhr = M_weekdays_hr + M_weekend_hr ;
                    }
                    else{
                        totalhr =  M_weekdays_hr;
                    }

                    timeduration.setText(totalhr + " hrs");


                }

                @Override
                public void onErrorResponse(String tag, String message) {

                    Toast.makeText(PaymentPage.this, "No Connection!0.", Toast.LENGTH_SHORT).show();

                }
            };





        //  delivery response initialize

        // isOnline();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init()
    {

        // weekend_status call


        //http://cakeweb.com/api/dropDate=2022-09-18





        map2 = new HashMap<>();
        map2.put("method", "weekendstatus");

        rq.setParams(map2, RequestNetworkController.REQUEST_PARAM);
        rq.startRequestNetwork(
                RequestNetworkController.POST,
                HttpURL,
                "tag", weekend_status_listener);



        if(dtype23.equals("Home Delivery")) {


            map2 = new HashMap<>();
            map2.put("method", "deliveryfee");
            map2.put("deliverytype", "Home Delivery");

            rq.setParams(map2, RequestNetworkController.REQUEST_PARAM);
            rq.startRequestNetwork(
                    RequestNetworkController.POST,
                    HttpURL,
                    "tag", _rq_request_listener);


        } else
        {
            if(progressDialog != null)  progressDialog.dismiss();


        }

    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(PaymentPage.this, "No Internet connection!", Toast.LENGTH_LONG).show();

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

            return false;
        }
        return true;
    }








    public void getPriceDetails(final String method, final String car_id, final String concatpdate, final String concatDdate, final String coupon, final String security) {

        class BookCabClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                progressDialog = ProgressDialog.show(PaymentPage.this, "Loading Data", null, true, false);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);




                boolean msg = httpResponseMsg.contains("200");

                init();

                  //Toast.makeText(PaymentPage.this, ""+httpResponseMsg, Toast.LENGTH_SHORT).show();


                if (msg) {

                    // Toast.makeText(PaymentPage.this, "helloooooo", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(PaymentPage.this, ""+httpResponseMsg, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = null;
                    try {



                        jsonObject = new JSONObject(httpResponseMsg);


                      /*  android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text",httpResponseMsg );
                        clipboard.setPrimaryClip(clip);*/

                       // Toast.makeText(PaymentPage.this, ""+jsonObject.getString("result"), Toast.LENGTH_SHORT).show();

                        JSONObject ob = new JSONObject(jsonObject.getString("result"));
                        String weekdays_hr =ob.getString("weekdays_hr");
                        String weekend_hr = ob.getString("weekend_hr");
                        String weekdays_rs = ob.getString("weekdays_rs");
                        String weekend_rs = ob.getString("weekend_rs");
                        String security_rs = ob.getString("security_rs");
                        String total_rs = ob.getString("total_rs");
                        String discountrs = ob.getString("discount_rs");
                        String discountpercentage = ob.getString("discount");
                        String couponstatus = ob.getString("coupon_status");

                       // Toast.makeText(PaymentPage.this, ""+weekdays_hr+"--"+weekdays_rs, Toast.LENGTH_SHORT).show();


                        if (couponstatus.equals("Invalid Coupon Code")) {
                            Snackbar.make(view, "Invalid Coupon Code !!", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                                    .show();
                        } else if(Float.parseFloat(discountpercentage)>0) {
                            Snackbar.make(view, "" + discountpercentage + "% discount added..", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                                    .show();
                        }else if(couponstatus.equals("")){

                        }
                        else{
                            Snackbar.make(view, couponstatus, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                                    .show();
                        }

                        coupondiscount.setText("" + (int) Float.parseFloat(discountrs));

                        String weekendprice;

                        weekendprice = "" + (int) Float.parseFloat(weekend_rs);
                        basefair.setText("" + (int) (Float.parseFloat(weekdays_rs) + Float.parseFloat(weekend_rs)));

                      M_weekdays_hr = Float.parseFloat(weekdays_hr) ;
                      M_weekend_hr =  Float.parseFloat(weekend_hr) ;

                          if(weekend_status_value.equals("1")) {
                            totalhr = M_weekdays_hr + M_weekend_hr ;
                        }
                        else{
                            totalhr =  M_weekdays_hr;
                        }




                        timeduration.setText(totalhr + " hrs");
                        weekdaychages.setText("₹" + (int) Float.parseFloat(weekdays_rs));
                        weekendcharges.setText("₹" + weekendprice);
                        securitycharges.setText((int) Float.parseFloat(security_rs)+"");


                        //total.setText(total_rs);


                        float gstpercentage = 5.0f;

                        cost = (int) Float.parseFloat(total_rs);

                        if(!df.equals(""))
                        {
                            df_  = (int) Float.parseFloat(df);
                        }


                        int gstcost = (int) (cost * (gstpercentage / 100));
                        gst.setText("" + gstcost);


                        total.setText(""+(cost));
                        if(dtype23.equals("Home Delivery")){

                            int total_ = cost + (int)df_ ; // df is delivery fees

                            total.setText(total_+"");
                    }




                    } catch (JSONException e) {
                                           e.printStackTrace();
                    }
                } else {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpResponseMsg);
                        String messege = jsonObject.getString("msg");
                        Toast.makeText(PaymentPage.this, messege, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {


                //String jsonInputString="{\"method\":\"registerUser\",\"name\":\""+strname+"\",\"email\":\""+stremail+"\",\"mobile\":\""+strphone+"\",\"password\":\""+strpassword+"\",\"dl_no\":\""+strdlno+"\",\"aadhar_no\":\""+straadharcardno+"\",\"dob\":\""+strdob+"\"}";


                String jsonInputString = "{\"method\":\"getPriceDetails\",\"car_id\":\"" + car_id + "\",\"concatPdate\":\"" + concatpdate + "\",\"concatDdate\":\"" + concatDdate + "\",\"coupon\":\"" + coupon + "\",\"security\":\"Online\"}";

            /*

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text",jsonInputString );
                clipboard.setPrimaryClip(clip);

                */


                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        BookCabClass bookCabClass = new BookCabClass();
        bookCabClass.execute(method);

    }



    public void bookCab(String method, final String userid, final String stdate, final String end, final String startime, final String endtime, final String car_id, final String price,final String d_type){

        class BookCabClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentPage.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                boolean msg=httpResponseMsg.contains("200");

                //Toast.makeText(PaymentPage.this, ""+httpResponseMsg, Toast.LENGTH_SHORT).show();

                if(msg){

                   // Toast.makeText(PaymentPage.this, "helloooooo", Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpResponseMsg);
                        //  String messege= jsonObject.getString("otp");
                         book_id= ""+jsonObject.getString("book_id");
                         booking_id= jsonObject.getString("booking_id");
                         payment_id= jsonObject.getString("payment_id");

                        beforePayment("beforePayment",book_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpResponseMsg);
                        String messege = jsonObject.getString("msg");
                        Toast.makeText(PaymentPage.this,"bookcab:---"+ messege, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                
                //String jsonInputString="{\"method\":\"registerUser\",\"name\":\""+strname+"\",\"email\":\""+stremail+"\",\"mobile\":\""+strphone+"\",\"password\":\""+strpassword+"\",\"dl_no\":\""+strdlno+"\",\"aadhar_no\":\""+straadharcardno+"\",\"dob\":\""+strdob+"\"}";

                    String jsonInputString =
                                    "{\"method\":\"bookCab\"," +
                                    "\"user_id\":\"" + userid + "\"," +
                                    "\"car_id\":\"" + car_id +
                                    "\",\"city\":\""+ city_id + "\"," +
                                    "\"pickup_date\":\"" + stdate +
                                    "\",\"pickup_time\":\""+startime+"\"," +
                                    "\"dropup_date\":\"" + end + "\"," +
                                    "\"dropup_time\":\""+endtime+"\"," +
                                            "\"dtype\":\""+d_type+"\"," +
                                    "\"booking_amount\":\"" + price + "\"," +
                                    "\"name\":\"" + name +
                                    "\",\"mobile\":\"" +mobile+
                                    "\",\"email\":\"" +email+"\"," +
                                    "\"message\":\"api testing\",\"coupon\":\"\"," +
                                    "\"dlno\":\"" +dlno+
                                    "\",\"dob\":\"" +dob+
                                    "\",\"security\":\" \"}";
//

//
//               finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                    finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                    return finalResult;
                }

        }

        BookCabClass bookCabClass = new BookCabClass();

        bookCabClass.execute(method);
    }




    public void beforePayment(final String method,final String book_id){

        class UserLoginClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentPage.this,"Loading...",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(httpResponseMsg);
                    String status = jsonObject2.getString("status");

                    if(status.equals("200")){

                        transaction_id=jsonObject2.getString("transaction_id");

                    }else{

                        String messege = jsonObject2.getString("msg");
                        Toast.makeText(PaymentPage.this, "before payment:---"+messege, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"beforePayment\",\"book_id\":\""+book_id+"\"}";

              //  Toast.makeText(PaymentPage.this, ""+book_id, Toast.LENGTH_SHORT).show();

//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(method,book_id);
    }

    private void afterPayment(final String method, final String booking_id, final String transaction_id, final String payment_id, String status, final String amount, String response_raw_data) {

        class UserLoginClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentPage.this,"Loading...",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(httpResponseMsg);
                    String status = jsonObject2.getString("status");

                    if(status.equals("200")){



                    }else{

                        String messege = jsonObject2.getString("msg");
                        Toast.makeText(PaymentPage.this, messege, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Toast.makeText(Signup.this, httpResponseMsg, Toast.LENGTH_SHORT).show();



            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"afterPayment\"," +
                        "\"booking_id\":\""+booking_id+"\"," +
                        "\"transaction_id\":\""+transaction_id+"\"," +
                        "\"payment_id\":\""+payment_id+"\"," +
                        "\"status\":1," +
                        "\"amount\":\""+amount+"\"," +
                        "\"response_raw_data\":\"\"}";

                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);


       //    finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);


                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(method);
    }


    public void startPayment(Float total) {

        if(name==null || email==null || mobile==null){
            Toast.makeText(this, "Please login first !!", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Instantiate Checkout
         */
        Checkout.preload(getApplicationContext());
        Checkout checkout = new Checkout();
        //checkout.setKeyID("rzp_test_YZ0rZv8DFWccCl");

        /**
         * Set your logo here
         */

        checkout.setImage(R.drawable.tooroslogo);
       // checkout.setFullScreenDisable(true);

        /**
         * Reference to current activity
         */
        final PaymentPage activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name",""+name);
            options.put("description", "Confirm Booking..");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#98C119");
            options.put("currency", "INR");
            options.put("amount", ""+total*100);//pass amount in currency subunits
            options.put("prefill.email",""+email);
            options.put("prefill.contact",""+mobile);
            checkout.open(activity, options);
            
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        progressDialog.dismiss();

        String status="1";

        String amount=total.getText().toString();


        afterPayment("afterPayment",booking_id,transaction_id,payment_id,status,amount,"");




        //endtime output : 02:00 AM  // format hh:00 aaa
        //end_date output :  03-12-2004 // format dd-MM-yyyy
        // this shared preference used for show rate dialog on end date


        String specificDay = enddate.toString().substring(0,2);
        // before substring = 03-12-2004
        // after substring  = 03
        String specificMonth = enddate.toString().substring(3,5);
        // before substring = 03-12-2004
        // after substring  = 12


        sh.edit().putString("drop_exact_day",specificDay).apply();
        sh.edit().putString("drop_exact_month",specificMonth).apply();

        sh.edit().putString("drop_date",enddate.toString()+" "+endt).apply();

        // format of shared preference date :  dd-MM-yyyy hh:00 aaa


        Intent intent=new Intent(this,ThankYou.class);
        intent.putExtra("invoice",s);
        intent.putExtra("booking_id",booking_id);
        intent.putExtra("startdate",startdate.getText());
        intent.putExtra("enddate",enddate.getText());
        intent.putExtra("starttime",startt.getText());
        intent.putExtra("endtime",endt.getText());





        startActivity(intent);




    }

    @Override
    public void onPaymentError(int i, String s) {

        progressDialog.dismiss();

        // Toast.makeText(this, i+"-"+s, Toast.LENGTH_SHORT).show();
        try {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
           // Toast.makeText(this, "Exception is coming--"+e, Toast.LENGTH_SHORT).show();
        }

    }

    public void termsAndConditionClick(View view) {
        startActivity(new Intent(this,TermsAndCondition.class));
    }

    public void privacyPolicyClick(View view) {

        startActivity(new Intent(this,CancellationPolicy.class));

    }

    public void vla(View view) {

        startActivity(new Intent(this,LeaseAgreement.class));

    }
}
