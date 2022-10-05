package my.awesome.tooros;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Locale;

public class Signup extends AppCompatActivity {
    EditText name, email, phone, password, confirmpassword, aadharcardno, dlno;
    TextView Dob;
    CheckBox checkBox;
    // TextView aadharSnap,dlSnap;
    Uri imageuri;
    //ImageView aadharpic,dlpic;

    String strname, stremail, strphone, strpassword, strconfirmpassword, straadharcardno, strdlno, strdob;
    Button Register;
    String finalResult;
    //    String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
    String HttpURL = "https://tooros.in/api/api.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    int pay = 0;
    //HashMap<String,String> hashMap = new HashMap<>();
    //HttpParse httpParse = new HttpParse();
    JsonHttpParse jsonhttpParse = new JsonHttpParse();
    Calendar myEndCalendar;
    DatePickerDialog.OnDateSetListener enddatelistener;
    TextView vla;

    LocalDate dob2;

    int dobYear,dobMonth,dobDay;

    String startdateSelected = "";

    int nowyear,nowday,nowmonth;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Register = findViewById(R.id.registerbtn);
        name = findViewById(R.id.namecontainer);
        email = findViewById(R.id.emailcontainer);
        phone = findViewById(R.id.mobilenocontainer);
        password = findViewById(R.id.passwordcontainer);
        confirmpassword = findViewById(R.id.repasswordcontainer);
        aadharcardno = findViewById(R.id.aadharcontainer);
        dlno = findViewById(R.id.dlnocontainer);
        Dob = findViewById(R.id.dobv);
        checkBox = findViewById(R.id.checkBox);
        vla = findViewById(R.id.vla);

        vla.setOnClickListener(view -> vlaClick());

        Intent intent = getIntent();
        pay = intent.getIntExtra("val", 0);


//        aadharSnap=findViewById(R.id.aadharsnapcontainer);
//        dlSnap=findViewById(R.id.dlsnapcontainer);
//        aadharpic=findViewById(R.id.aadharpic);
//        dlpic=findViewById(R.id.dlpic);


        myEndCalendar = Calendar.getInstance();

        Calendar c;
        c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")

        String now_year = new SimpleDateFormat("yyyy").format(c.getTime());
        String now_month = new SimpleDateFormat("MM").format(c.getTime());
        String now_day = new SimpleDateFormat("dd").format(c.getTime());

        nowyear = Integer.parseInt(now_year);
        nowmonth = Integer.parseInt(now_month);
        nowday = Integer.parseInt(now_day);

       // myEndCalendar.set(nowyear-21, 0, 1);


       // myEndCalendar.add(Calendar.YEAR,1 );

        enddatelistener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub

            String getstartdate = startdateSelected;
            String getfrom[] = getstartdate.split("-");







            myEndCalendar.set(Calendar.YEAR, year);
            myEndCalendar.set(Calendar.MONTH, monthOfYear);
            myEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);





            String myFormat = "yyyy-MM-dd"; //In which you need put here

            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dobYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(myEndCalendar.getTime()));
            dobDay = Integer.parseInt(new SimpleDateFormat("dd").format(myEndCalendar.getTime()));
            dobMonth = Integer.parseInt(new SimpleDateFormat("MM").format(myEndCalendar.getTime()));

           // Util.showMessage(getApplicationContext(), dobYear + "");

            String dateSelected = sdf.format(myEndCalendar.getTime());


            Dob.setText(dateSelected);

            //Toast.makeText(CitySelectionActivity.this,sdf.format(myEndCalendar.getTime()) , Toast.LENGTH_SHORT).show();
        };

//        aadharSnap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery=new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(gallery,"select picture"),0);
//            }
//        });
//        dlSnap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery=new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(gallery,"select picture"),1);
//            }
//        });

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==0 && resultCode==RESULT_OK){
//            imageuri=data.getData();
//            String path=imageuri.getPath();
//            aadharSnap.setText(path);
//            try{
//                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
//                aadharpic.setImageBitmap(bitmap);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//
//        }
//        if(requestCode==1 && resultCode==RESULT_OK){
//            imageuri=data.getData();
//            String path=imageuri.getPath();
//            dlSnap.setText(path);
//
//            try{
//                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
//                dlpic.setImageBitmap(bitmap);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickSubmit(View view) {
        //startActivity(new Intent(Custom19.this,MobileVerification.class));

        // Checking whether EditText is Empty or Not
        CheckEditTextIsEmptyOrNot();

        if (!checkBox.isChecked()) {
            Toast.makeText(this, "Please tick the checkbox first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (CheckEditText) {

            SharedPreferences sharedPreferences1 = Signup.this.getSharedPreferences("profie", MODE_PRIVATE);
            final SharedPreferences.Editor edit = sharedPreferences1.edit();
            edit.putString("name", strname);
            edit.putString("email", stremail);
            edit.putString("phone", strphone);
            edit.apply();
            // If EditText is not empty and CheckEditText = True then this block will execute.

            UserRegisterFunction("registerUser", strname, stremail, strphone, strpassword, strdob, straadharcardno, strdlno);

        } else {

            // If EditText is empty then this block will execute .
            Toast.makeText(Signup.this, "Invalid details.. ", Toast.LENGTH_LONG).show();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CheckEditTextIsEmptyOrNot() {

        strname = name.getText().toString();
        stremail = email.getText().toString();
        strphone = phone.getText().toString();
        strpassword = password.getText().toString();
        strconfirmpassword = confirmpassword.getText().toString();
        straadharcardno = aadharcardno.getText().toString();
        strdlno = dlno.getText().toString();
        strdob = Dob.getText().toString().trim();

        if (TextUtils.isEmpty(strname) || TextUtils.isEmpty(stremail) || TextUtils.isEmpty(strphone) || TextUtils.isEmpty(strpassword) || TextUtils.isEmpty(strconfirmpassword) || TextUtils.isEmpty(straadharcardno) || TextUtils.isEmpty(strdlno)) {

            CheckEditText = false;

        } else {
            if (!strpassword.equals(strconfirmpassword)) {
                Toast.makeText(this, "Please confirm correct password !!", Toast.LENGTH_SHORT).show();
                CheckEditText = false;
            } else {

                if (strphone.length() == 10) {


                    //LocalDate dob = LocalDate.parse(input);
                   // dob2 = LocalDate.of(1988, 12, 13);
                   // calculateAge(dob2);
                    if (calculateAge(dobYear) >= 21) {
                        CheckEditText = true;
                    } else {

                        CheckEditText = false;
                        sneekmsg("21+ age only allowed !!",name);
                        //Toast.makeText(this, "", Toast.LENGTH_LONG).show();

                    }


                } else {

                    sneekmsg("Enter valid phone number !!",name);
                 //   Toast.makeText(this, "Enter valid phone number !!", Toast.LENGTH_LONG).show();
                    CheckEditText = false;
                }

            }


        }

    }

    private void sneekmsg(String msg, View view)
    {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("OK", view1 -> {

                })

                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();

    }
    public void UserRegisterFunction(final String method, final String name, final String Email, final String phone, final String password, final String dob, final String aadharno, final String dlno) {

        class UserRegisterFunctionClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Signup.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                boolean msg = httpResponseMsg.contains("200");

                if (msg) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpResponseMsg);
                        //  String messege= jsonObject.getString("otp");
                        //String messege= jsonObject.getString("msg");
                        finish();
                        Intent intent = new Intent(Signup.this, OtpVerification.class);
                        intent.putExtra("phone", strphone);
                        intent.putExtra("val", pay);
                        startActivity(intent);

                        //Toast.makeText(Signup.this, messege, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Signup.this, "Verify OTP", Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpResponseMsg);
                        String messege = jsonObject.getString("msg");
                        Toast.makeText(Signup.this, messege, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {

//                hashMap.put("method",params[0]);
//
//                hashMap.put("customer_name",params[1]);
//
//                hashMap.put("customer_email",params[2]);
//
//                hashMap.put("customer_mobile",params[3]);
//
//                hashMap.put("customer_password",params[4]);
//                hashMap.put("customer_dob",params[5]);
//                hashMap.put("customer_aadharno",params[6]);
//                hashMap.put("customer_dlno",params[7]);
//
//                finalResult = httpParse.postRequest(hashMap, HttpURL);

                String jsonInputString = "{\"method\":\"registerUser\",\"name\":\"" + strname + "\",\"email\":\"" + stremail + "\",\"mobile\":\"" + strphone + "\",\"password\":\"" + strpassword + "\",\"dl_no\":\"" + strdlno + "\",\"aadhar_no\":\"" + straadharcardno + "\",\"dob\":\"" + strdob + "\"}";

//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(method, name, Email, phone, password, dob, aadharno, dlno);
    }

    public void onClickEndDate(View view) {

    /*

      new DatePickerDialog(Signup.this, enddatelistener, myEndCalendar
                .get(Calendar.YEAR), myEndCalendar.get(Calendar.MONTH),
                myEndCalendar.get(Calendar.DAY_OF_MONTH)).show();

    */


        DatePickerDialog datePickerDialog= new DatePickerDialog(Signup.this, enddatelistener, myEndCalendar
                .get(Calendar.YEAR), myEndCalendar.get(Calendar.MONTH),
                myEndCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar c = Calendar.getInstance();
        c.set(nowyear-21, nowmonth,nowday);



        // this will provide the 2001 =  now year(2022) - 21 years
        // it will change by time automatic

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();

    }

    public void onClickAadharsnap(View view) {
    }

    public void onClickDlsnap(View view) {
    }

    public void termsc(View view) {

        Intent intent = new Intent(Signup.this, TermsAndCondition.class);
        startActivity(intent);
    }


    public void vlaClick() {

        startActivity(new Intent(this, LeaseAgreement.class));
    }


    public static int calculateAge(int dobyear) {

        Calendar c;
        c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")

        String now_year = new SimpleDateFormat("yyyy").format(c.getTime());

        int nowyear = Integer.parseInt(now_year);

        return nowyear - dobyear;
    }
}