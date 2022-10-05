package my.awesome.tooros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarBooking extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView startdate,enddate,city,startime1,endtime1;
    ArrayList<CarBookingModel> carBookingModels = new ArrayList<CarBookingModel>();
    CarBookingAdapter carBookingAdapter;
    ProgressDialog progressDialog1;
    //String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
    String HttpURL = "https://tooros.in/api/api.php";
    String finalResult1 ;
    JsonHttpParse jsonhttpParse = new JsonHttpParse();
    TextView cityname,start,end;
String cityid;

String dtype; // delivery type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_booking);
        recyclerView=findViewById(R.id.bookingrecycler);
//  <item name="colorAccent">@color/colorAccent</item>
        // <color name="colorAccent">#D81B60</color>
        recyclerView.setHasFixedSize(true);
       startime1=findViewById(R.id.startt);
       endtime1=findViewById(R.id.endt);
        startdate=findViewById(R.id.textView4);
        enddate=findViewById(R.id.textView7);
        city=findViewById(R.id.cityname);
      //  recyclerView.setAdapter(carBookingAdapter);
        SharedPreferences sharedPreferences = CarBooking.this.getSharedPreferences("Date", MODE_PRIVATE);
        String stdate= sharedPreferences.getString("startdate",null);
        String stdate_show= sharedPreferences.getString("startdate_show",null);
        String end= sharedPreferences.getString("Enddate",null);
        String end_show= sharedPreferences.getString("Enddate_show",null);
        String City= sharedPreferences.getString("city",null);
        cityid=sharedPreferences.getString("cityid",null);

        dtype = sharedPreferences.getString("deliveryType",null);

        // Toast.makeText(CarBooking.this, ""+cityid, Toast.LENGTH_SHORT).show();
        final String startime=sharedPreferences.getString("starttime",null);
        final String endtime=sharedPreferences.getString("endtime",null);
        final String startimee=sharedPreferences.getString("startimee",null);
        final String endtimee=sharedPreferences.getString("endtimee",null);

       // Toast.makeText(CarBooking.this, startimee, Toast.LENGTH_SHORT).show();

        if(stdate!=" "&& end !=" "&& City!=" "&& startime!=" " && endtime!=" "){
            startdate.setText(stdate_show);
            enddate.setText(end_show);
            city.setText(City);
            startime1.setText(startime);
            endtime1.setText(endtime);
        }







        carBookingAdapter=new CarBookingAdapter(carBookingModels,CarBooking.this);
        recyclerView.setAdapter(carBookingAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CarBooking.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


       if(dtype.equals("Home Delivery"))
       {
        // Toast.makeText(getApplicationContext(),"Home Delivery",Toast.LENGTH_SHORT).show();
         // getDeliveryFee("deliveryfee","Home% Delivery");
       }else {
         // Toast.makeText(getApplicationContext(),"Parking Place",Toast.LENGTH_SHORT).show();

       }

        getSubSerivices("getAllavailCabs",City,stdate,end,startimee,endtimee);





    }












    private void getSubSerivices(final String method, String city, final String startdate, final String enddate, final String starttime, final String endtime) {

        class OfferClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = ProgressDialog.show(CarBooking.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog1.dismiss();

               // Toast.makeText(getApplicationContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                if(httpResponseMsg.contains("200")){
                    //Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject jsonObject = new JSONObject(httpResponseMsg);

                      //  Toast.makeText(getApplicationContext(),httpResponseMsg,Toast.LENGTH_SHORT).show();

                        JSONArray result = jsonObject.getJSONArray("result");
                        for (int i=0; i<result.length(); i++ ){
                            JSONObject ob=result.getJSONObject(i);

                            CarBookingModel carBookingModel=new CarBookingModel(ob.getString("car_image"),
                                    ob.getString("car_nme")
                                    ,ob.getString("fuelType"),
                                    ob.getString("cost"),
                                    ob.getString("no_of_seat"),
                                    ob.getString("gearType"),
                                    ob.getString("no_of_baggage"),
                                    ob.getString("status"),
                                    ob.getString("weekendcost"),
                                    ob.getString("security"),
                                    ob.getString("id"),
                                    ob.getString("bookCount"),
                                    ob.getString("mtCount"),
                                    ob.getString("note"));
                            carBookingModels.add(carBookingModel);
                        }
//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    recyclerView.setAdapter(carBookingAdapter);
                    carBookingAdapter.notifyDataSetChanged();



                }else{
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(httpResponseMsg);

                        Toast.makeText(CarBooking.this,
                                "No Car Found, Please check other date !",
                                Toast.LENGTH_LONG).show();
                        finish();
                        //startActivity(new Intent(CarBooking.this,CitySelectionActivity.class));

//                        String messege = jsonObject.getString("msg");
//                        Toast.makeText(CarBooking.this, messege, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {

                String concatPdate=""+startdate+" "+starttime;
                String concatDdate=""+enddate+" "+endtime;

                String jsonInputString="{\"method\":\"getAllavailCabs\"," +
                        "\"concatPdate\":\""+concatPdate+"\",\"concatDdate\":\""+concatDdate+"\",\"city\":"+cityid+"}";
               // String jsonInputString={"method":"getAllavailCabs","concatPdate":"2021-01-15 09:00","concatDdate":"2021-01-16 21:00","city":1};



                finalResult1 = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult1;
            }
        }

        OfferClass offerClass = new OfferClass();

        offerClass.execute(method);


       // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    }






}
