package my.awesome.tooros;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity {
    EditText name ,aadharcardno,dlno;
    TextView Dob , email , phone;String userid;
    Button Register;
    Uri imageuri;
    ImageView aadharpic,dlpic;
    TextView aadharSnap,dlSnap;
    Calendar myEndCalendar;
    DatePickerDialog.OnDateSetListener enddatelistener;

//    String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
     String HttpURL = "https://tooros.in/api/api.php";


    Bitmap aadharfile,dlfile;
    File aafile,ddfile;String apath,dpath;
ProgressDialog progressDialog;
    Response response;String responsemsg,responseerrormsg;
    String strname, stremail, strphone, straadharcardno,strdlno,strdob,strdldoc,straadhardoc;
    Boolean CheckEditText;

    int dobYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        Register=findViewById(R.id.registerbtn);
        name=findViewById(R.id.namecontainer);
        email=findViewById(R.id.emailcontainer);
        phone=findViewById(R.id.mobilenocontainer);
        aadharcardno=findViewById(R.id.aadharcontainer);
        dlno=findViewById(R.id.dlnocontainer);
        Dob=findViewById(R.id.dobv);

        aadharSnap=findViewById(R.id.aadharsnapcontainer);
        dlSnap=findViewById(R.id.dlsnapcontainer);
        aadharpic=findViewById(R.id.aadharpic);
        dlpic=findViewById(R.id.dlpic);
        SharedPreferences sharedPreferences2 = Profile.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        if (sharedPreferences2 != null) {
            String name1 = sharedPreferences2.getString("Name", null);
            name.setText(name1);
          String  mobile = sharedPreferences2.getString("Mobile", null);
          phone.setText(mobile);
            String email1 = sharedPreferences2.getString("Email", null);
            email.setText(email1);
            String dob = sharedPreferences2.getString("Dob", null);
            Dob.setText(dob);
            String dlno1 = sharedPreferences2.getString("Dlno", null);
            dlno.setText(dlno1);
           String aadharno = sharedPreferences2.getString("Aadharno", null);
            aadharcardno.setText(aadharno);
            userid=sharedPreferences2.getString("userid",null);
//            strdldoc=sharedPreferences2.getString("Dldoc",null);
//            straadhardoc=sharedPreferences2.getString("Aadhardoc",null);

        }


        myEndCalendar = Calendar.getInstance();

        Calendar c;
        c = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        String now_year = new SimpleDateFormat("yyyy").format(c.getTime());
        int nowyear = Integer.parseInt(now_year);

        myEndCalendar.set(nowyear-21, 0, 1);

        enddatelistener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myEndCalendar.set(Calendar.YEAR, year);
            myEndCalendar.set(Calendar.MONTH, monthOfYear);
            myEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);



            dobYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(myEndCalendar.getTime()));

            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            String dateSelected=sdf.format(myEndCalendar.getTime());

            Dob.setText(dateSelected);

        };

        aadharSnap.setOnClickListener(v -> {
            Intent gallery=new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(gallery,"select picture"),0);
        });
        dlSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"select picture"),1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0 && resultCode==RESULT_OK){
            imageuri=data.getData();
             apath=FilePath.getFilePath(Profile.this, imageuri);

            aafile=new File(apath);

            aadharSnap.setText(aafile.getName());
            try{
                aadharfile= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                aadharpic.setImageBitmap(aadharfile);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(requestCode==1 && resultCode==RESULT_OK){
            imageuri=data.getData();
             dpath=FilePath.getFilePath(Profile.this, imageuri);
            //String path=imageuri.getPath();
            ddfile=new File(dpath);

            dlSnap.setText(ddfile.getName());

            try{
                 dlfile= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                dlpic.setImageBitmap(dlfile);
            }catch (IOException e){
                e.printStackTrace();
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


    public void onClickEndDate(View view) {
        new DatePickerDialog(Profile.this, enddatelistener, myEndCalendar
                .get(Calendar.YEAR), myEndCalendar.get(Calendar.MONTH),
                myEndCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void onClickSubmit(View view) throws IOException {

        CheckEditTextIsEmptyOrNot();

        if(CheckEditText){

            UploadTask uploadTask=new UploadTask();
            uploadTask.execute(new String[]{"updateProfile",userid,strname,strdlno,straadharcardno,strdob,dpath,apath});


        }
        else {

            Toast.makeText(Profile.this, "Please fill all required form fields.", Toast.LENGTH_LONG).show();

        }

    }

    public void CheckEditTextIsEmptyOrNot(){

        strname = name.getText().toString();
        stremail = email.getText().toString();
        strphone = phone.getText().toString();
        straadharcardno= aadharcardno.getText().toString();
        strdlno = dlno.getText().toString();
        strdob=Dob.getText().toString().trim();
        if (TextUtils.isEmpty(strname) || TextUtils.isEmpty(stremail) || TextUtils.isEmpty(strphone) || TextUtils.isEmpty(straadharcardno) || TextUtils.isEmpty(strdlno)) {

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

                    //Toast.makeText(this, "21+ age only allowed !!", Toast.LENGTH_SHORT).show();

                }


            } else {

                sneekmsg("Enter valid phone number !!",name);
               // Toast.makeText(this, "Enter valid phone number !!", Toast.LENGTH_SHORT).show();
                CheckEditText = false;
            }

        }


    }

    public void aadharViewButtonclick(View view) {

        SharedPreferences sharedPreferences2 = Profile.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        String straadhardoc = sharedPreferences2.getString("Aadhardoc", null);

       // Toast.makeText(this, ""+straadhardoc, Toast.LENGTH_SHORT).show();

        if (straadhardoc.contains("http")){
            Intent intent=new Intent(this,ProfiledocActivity.class);
            intent.putExtra("url",straadhardoc);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please upload it first!!", Toast.LENGTH_SHORT).show();
        }



    }

    public void dlViewButtonclick(View view) {

        SharedPreferences sharedPreferences2 = Profile.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        String strdldoc = sharedPreferences2.getString("Dldoc", null);
        if(strdldoc.contains("http")){
            Intent intent=new Intent(this,ProfiledocActivity.class);
            intent.putExtra("url",strdldoc);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please upload it first!!", Toast.LENGTH_SHORT).show();

        }


    }

    public class UploadTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);
            //progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();

            JSONObject jsonObject2 = null;
            try {
                jsonObject2 = new JSONObject(httpResponseMsg);
                String status = jsonObject2.getString("status");
                String messege = jsonObject2.getString("msg");
                Toast.makeText(Profile.this, messege, Toast.LENGTH_SHORT).show();

                if(status.equals("200")){

                    try {

                        JSONArray result = jsonObject2.getJSONArray("result");
                        for (int i=0; i<result.length(); i++ ){
                            JSONObject ob=result.getJSONObject(i);

                            SharedPreferences sharedPreferences2 = Profile.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
                            final SharedPreferences.Editor myEdit = sharedPreferences2.edit();
                            myEdit.putString("Name",ob.getString("name"));
                            myEdit.putString("Mobile",ob.getString("mobile"));
                            myEdit.putString("Email",ob.getString("email"));
                            myEdit.putString("Dob",ob.getString("dob"));
                            myEdit.putString("Dlno",ob.getString("dl_no"));
                            myEdit.putString("Aadharno",ob.getString("aadhar_no"));
                            myEdit.putString("Aadhardoc",ob.getString("aadhar_doc"));
                            myEdit.putString("Dldoc",ob.getString("dl_doc"));
                            myEdit.putString("userid",ob.getString("user_id"));
                            myEdit.apply();

                        }
//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }else{

                    String messege2 = jsonObject2.getString("msg");
                    //Toast.makeText(Profile.this, messege2, Toast.LENGTH_SHORT).show();

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            progressDialog = ProgressDialog.show(Profile.this, "Loading...", null, true, false);

        }

        @Override
        protected String doInBackground(String... strings) {


//            if (uploadFile(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7])=="") {
//                return "true";
//            } else {
//                return "failed";
//            }

            String str=uploadFile(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
            return str;
        }

        private String uploadFile(String method, String userid, String strname, String strdlno, String straadharcardno, String strdob, String dpath, String apath) {

            if (apath == null && dpath != null) {

                File dfile = new File(dpath);
                try {
                    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("method", method)
                            .addFormDataPart("userId", userid)
                            .addFormDataPart("name", strname)
                            .addFormDataPart("dl_no", strdlno)
                            .addFormDataPart("aadhar_no", straadharcardno)
                            .addFormDataPart("dob", strdob)
                            .addFormDataPart("dl_doc", dfile.getName(), RequestBody.create(MediaType.parse("*/*"), dfile))
                            .build();

                    Request request = new Request.Builder()
                            .url(HttpURL)
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                        }
//                    });
//                    return "true";
                    Response response=client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "exception";
                }

            } else if (dpath == null && apath != null) {

                File afile = new File(apath);
                try {
                    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("method", method)
                            .addFormDataPart("userId", userid)
                            .addFormDataPart("name", strname)
                            .addFormDataPart("dl_no", strdlno)
                            .addFormDataPart("aadhar_no", straadharcardno)
                            .addFormDataPart("dob", strdob)
                            .addFormDataPart("aadhar_doc", afile.getName(), RequestBody.create(MediaType.parse("*/*"), afile))
                            .build();

                    Request request = new Request.Builder()
                            .url(HttpURL)
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                        }
//                    });
//                    return "true";
                    Response response=client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "exception";
                }

            } else if (dpath != null && apath != null) {

                File dfile = new File(dpath);
                File afile = new File(apath);
                try {
                    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)

                            .addFormDataPart("method", method)
                            .addFormDataPart("userId", userid)
                            .addFormDataPart("name", strname)
                            .addFormDataPart("dl_no", strdlno)
                            .addFormDataPart("aadhar_no", straadharcardno)
                            .addFormDataPart("dob", strdob)
                            .addFormDataPart("dl_doc", dfile.getName(), RequestBody.create(MediaType.parse("*/*"), dfile))
                            .addFormDataPart("aadhar_doc", afile.getName(), RequestBody.create(MediaType.parse("*/*"), afile))
                            .build();

                    Request request = new Request.Builder()
                            .url(HttpURL)
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                        }
//                    });
//                    return "true";
                    Response response=client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "exception";
                }

            } else {

                try {
                    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)

                            .addFormDataPart("method", method)
                            .addFormDataPart("userId", userid)
                            .addFormDataPart("name", strname)
                            .addFormDataPart("dl_no", strdlno)
                            .addFormDataPart("aadhar_no", straadharcardno)
                            .addFormDataPart("dob", strdob)
                            .build();

                    Request request = new Request.Builder()
                            .url(HttpURL)
                            .post(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();


//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException{
//                            strresponse[0] =response.body().string();
//                        }
//                    });
//                    return strresponse[0];
                    Response response=client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.toString();
                }

            }


        }

    }

    private void okhttpmethod4() {

        OkHttpClient client = new OkHttpClient();

        Log.d("response:-", "method called");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("method", "updateProfile")
                .addFormDataPart("userId", "27")
                .addFormDataPart("name", "amanmishra")
                .addFormDataPart("dl_no", "abc123")
                .addFormDataPart("aadhar_no", "aadhar123")
                .addFormDataPart("dob", "2016-01-21")
                .build();

        Log.d("response:-", "request body called");

        Request request = new Request.Builder()
                .url(HttpURL)
                .post(body)
                .build();

        try {
            response = client.newCall(request).execute();

            Log.d("response:-", response.body().string());

//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(response.toString());
//                String status = jsonObject.getString("status");
//
//                if(status.equals("200")){
//
//                     responsemsg=jsonObject.getString("msg");
//                    Log.d("responsemsg:-", responsemsg);
//
//
//                }else{
//
//                     responseerrormsg = jsonObject.getString("msg");
//
//
//                }
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void okhttpmethod1(File afile) {

        OkHttpClient client = new OkHttpClient();

        Log.d("response:-", "method called");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("method", "updateProfile")
                .addFormDataPart("userId", "27")
                .addFormDataPart("name", "amanmishra")
                .addFormDataPart("dl_no", "abc123")
                .addFormDataPart("aadhar_no", "aadhar123")
                .addFormDataPart("dob", "2016-01-21")
                .addFormDataPart("aadhar_doc", afile.getName(), RequestBody.create(MediaType.parse("*/*"), afile))
                .build();

        Log.d("response:-", "request body called");

        Request request = new Request.Builder()
                .url(HttpURL)
                .post(body)
                .build();

        try {
             response = client.newCall(request).execute();

            Log.d("response:-", response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void okhttpmethod2(File dfile) {



        OkHttpClient client = new OkHttpClient();

        Log.d("response:-", "method called");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("method", "updateProfile")
                .addFormDataPart("userId", "27")
                .addFormDataPart("name", "amanmishra")
                .addFormDataPart("dl_no", "abc123")
                .addFormDataPart("aadhar_no", "aadhar123")
                .addFormDataPart("dob", "2016-01-21")
                .addFormDataPart("dl_doc", dfile.getName(), RequestBody.create(MediaType.parse("*/*"), dfile))
                .build();

        Log.d("response:-", "request body called");

        Request request = new Request.Builder()
                .url(HttpURL)
                .post(body)
                .build();

        try {
            response = client.newCall(request).execute();

            Log.d("response:-", response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void okhttpmethod3(File afile,File dfile) {



        OkHttpClient client = new OkHttpClient();

        Log.d("response:-", "method called");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("method", "updateProfile")
                .addFormDataPart("userId", "27")
                .addFormDataPart("name", "amanmishra")
                .addFormDataPart("dl_no", "abc123")
                .addFormDataPart("aadhar_no", "aadhar123")
                .addFormDataPart("dob", "2016-01-21")
                .addFormDataPart("dl_doc", dfile.getName(), RequestBody.create(MediaType.parse("*/*"), dfile))
                .addFormDataPart("aadhar_doc", afile.getName(), RequestBody.create(MediaType.parse("*/*"), afile))
                .build();

        Log.d("response:-", "request body called");

        Request request = new Request.Builder()
                .url(HttpURL)
                .post(body)
                .build();

        try {
            response = client.newCall(request).execute();

            Log.d("response:-", response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


//    public void onClickSubmit(View view) {
//
//        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        //Disimissing the progress dialog
//                        loading.dismiss();
//                        //Showing toast message of the response
//                        Toast.makeText(Profile.this, s , Toast.LENGTH_LONG).show();
//
//                        SharedPreferences sharedPreferences2 = Profile.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
//                        final SharedPreferences.Editor myEdit = sharedPreferences2.edit();
//                        myEdit.putString("Name",name.getText().toString());
//                        myEdit.putString("Mobile",phone.getText().toString());
//                        myEdit.putString("Email",email.getText().toString());
//                        myEdit.putString("Dob",""+Dob.getText().toString());
//                        myEdit.putString("Dlno",dlno.getText().toString());
//                        myEdit.putString("Aadharno",aadharcardno.getText().toString());
////                        myEdit.putString("Aadhardoc",ob.getString("aadhar_doc"));
////                        myEdit.putString("Dldoc",ob.getString("dl_doc"));
////                        myEdit.putString("userid",ob.getString("user_id"));
//
//                        myEdit.apply();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        //Dismissing the progress dialog
//                        loading.dismiss();
//
//                        //Showing toast
//                        Toast.makeText(Profile.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                //Converting Bitmap to String
////                String aadharimage = BitMapToString(aadharfile);
////                String dlimage = BitMapToString(dlfile);
//
//                //Getting Image Name
//                String username = name.getText().toString();
//                String userphone = phone.getText().toString();
//                String useremail = email.getText().toString();
//                String useredob = Dob.getText().toString();
//                String userdlno = dlno.getText().toString();
//                String useraadharno = aadharcardno.getText().toString();
//
//                //Creating parameters
//                Map<String,String> params = new Hashtable<String, String>();
//
//                //Adding parameters
//                params.put("method", "updateProfile");
//                params.put("userId", userid);
//                params.put("name", username);
//                params.put("dl_no", userdlno);
//                params.put("aadhar_no", useraadharno);
//                params.put("dob", useredob);
//                params.put("dl_doc", String.valueOf(dfile));
//                params.put("aadhar_doc", String.valueOf(afile));
//
//               // name.setText(""+user);
//
//                //returning parameters
//                return params;
//            }
//        };
//
//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//
//
//        //do whatever you want from here
//    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

