package my.awesome.tooros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText email,password;
//    String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
String HttpURL = "https://tooros.in/api/api.php";
    Button signin;
    String stremail, strpassword;
    String finalResult ;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    int pay=0;
    JsonHttpParse jsonhttpParse = new JsonHttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.emailcontainer);
        password=findViewById(R.id.passwordcontainer);
        signin=findViewById(R.id.loginbtn);

        Intent intent=getIntent();
         pay = intent.getIntExtra("val",0);



        }

        public void SignUpPageClick(View view) {

            Intent intent=new Intent(Login.this,Signup.class);
            intent.putExtra("val",pay);
            startActivity(intent);


        }
    public void forgetPasswordClick(View view) {

        Intent intent=new Intent(Login.this,ForgetPassword.class);
        intent.putExtra("val",pay);
        startActivity(intent);

    }

        public void onClickSignInBtn2(View view){



        }

        public void onClickSignInBtn(View view) {
            CheckEditTextIsEmptyOrNot();

            if(CheckEditText){

            UserLoginFunction("login",stremail,strpassword);


            }
            else {

                Toast.makeText(Login.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            }

        }


        public void CheckEditTextIsEmptyOrNot(){

            stremail = email.getText().toString().trim();
            strpassword = password.getText().toString().trim();

            if(TextUtils.isEmpty(stremail) || TextUtils.isEmpty(strpassword))
            {
                CheckEditText = false;
            }
            else {

                CheckEditText = true ;
            }
        }

        public void UserLoginFunction(final String method,final String Email, final String Password){

            class UserLoginClass extends AsyncTask<String,Void,String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(Login.this,"Authenticating User...",null,true,true);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {

                    super.onPostExecute(httpResponseMsg);

                    progressDialog.dismiss();

                    // Toast.makeText(Signup.this, httpResponseMsg, Toast.LENGTH_SHORT).show();

                    if(httpResponseMsg.contains("200")){
                        try {
                            JSONObject jsonObject = new JSONObject(httpResponseMsg);
                            JSONArray result = jsonObject.getJSONArray("result");
                            for (int i=0; i<result.length(); i++ ){
                                JSONObject ob=result.getJSONObject(i);

                                SharedPreferences sharedPreferences2 = Login.this.getSharedPreferences("MySharedPref2", MODE_PRIVATE);
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
                                Toast.makeText(Login.this, "Logged in successfully !", Toast.LENGTH_SHORT).show();



//                                //to check if user is already login or not
//                                SharedPreferences sharedPreferencesForLoginOrNot = Login.this.getSharedPreferences("loginOrNot", MODE_PRIVATE);
//                                final SharedPreferences.Editor loginedit = sharedPreferencesForLoginOrNot.edit();
//                                loginedit.putString("info","yes");
//                                // loginedit.putString("username","Aman");
//                                loginedit.apply();

                               // Toast.makeText(Login.this, ""+pay1, Toast.LENGTH_SHORT).show();
                                if(pay==1) {
                                    finish();
                                    startActivity(new Intent(Login.this, PaymentPage.class));
                                }else{
                                    finish();
                                    startActivity(new Intent(Login.this, CitySelectionActivity.class));
                                }




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //
                      //  Toast.makeText(Login.this, httpResponseMsg, Toast.LENGTH_SHORT).show();


                    }else{
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(httpResponseMsg);
                            String messege = jsonObject.getString("msg");
                            Toast.makeText(Login.this, messege, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected String doInBackground(String... params) {


                    String jsonInputString="{\"method\":\"login\",\"emailormobile\":\""+Email+"\",\"password\":\""+Password+"\"}";

//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                    finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                    return finalResult;
                }
            }

            UserLoginClass userLoginClass = new UserLoginClass();

            userLoginClass.execute(method,Email,Password);
        }


    @Override
    public void onBackPressed(){
        finish();
       super.onBackPressed();
//        finish();
//        startActivity(new Intent(Login.this,CitySelectionActivity.class));

        }



    private void forgetPassword(String method, final String email) {

        class UserLoginClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Login.this,"Loading...",null,true,true);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(httpResponseMsg);
                    String status = jsonObject2.getString("status");

                    if(status.equals("200")){
                        Intent intent =new Intent(Login.this,Resetpassword.class);
                        intent.putExtra("mobile",""+email);
                         startActivity(intent);

                        String msg=jsonObject2.getString("msg");

                         Toast.makeText(Login.this, ""+msg, Toast.LENGTH_LONG).show();



                    }else{

                        String messege = jsonObject2.getString("msg");
                        Toast.makeText(Login.this, messege, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"chkResetPassword\",\"mobile_number\":\""+email+"\"}";

                //  Toast.makeText(PaymentPage.this, ""+book_id, Toast.LENGTH_SHORT).show();

//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(method,email);


    }
}

