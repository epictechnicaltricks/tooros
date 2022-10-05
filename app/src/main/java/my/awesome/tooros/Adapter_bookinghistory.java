package my.awesome.tooros;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class Adapter_bookinghistory  extends RecyclerView.Adapter<Adapter_bookinghistory.viewHolder> {
    ArrayList<Model_bookinghistory> arrayList;
    Context context;
    ProgressDialog progressDialog;

    JsonHttpParse jsonhttpParse = new JsonHttpParse();
    String finalResult ;
    //String HttpURL = "https://www.cakiweb.com/tooros/api/api.php";
    String HttpURL = "https://tooros.in/api/api.php";

    String booking_id;

    String ex;

    int  hour1,min1;

    public Adapter_bookinghistory(ArrayList<Model_bookinghistory> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_bookinghistory.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new Adapter_bookinghistory.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_bookinghistory.viewHolder holder, int position) {
        final Model_bookinghistory model=arrayList.get(position);


        holder.startdate.setText(model.getStartdate());
        holder.enddate.setText(model.getEnddate());
        holder.carname.setText(model.getCarname());
        holder.price.append(""+model.getPrice());

//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.invoice.setOnClickListener(v -> {

             booking_id=model.getBooking_id();

             getInvoice("getInvoice",booking_id);

            Snackbar.make(v, "Booking Invoice Downloaded !!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context,PdfViewActivity.class);
                            intent.putExtra("booking_id",booking_id);
                            context.startActivity(intent);
                            //context.startActivity(new Intent(context,PdfViewActivity.class));
                        }
                    })
                    .setActionTextColor(context.getResources().getColor(R.color.colorAccent))
                    .show();

        });


        holder.extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog3 = new AlertDialog.Builder(context).create();

                LayoutInflater inflater3 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View inflate = inflater3.inflate(R.layout.bookcus,null);

                dialog3.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog3.setView(inflate);

                final TextView st = (TextView) inflate.findViewById(R.id.st);
                final TextView sd = (TextView) inflate.findViewById(R.id.sd);
                TextView done  = (TextView) inflate.findViewById(R.id.done);
                TextView close  = (TextView) inflate.findViewById(R.id.close);
                final TextView amt = (TextView) inflate.findViewById(R.id.amt);

                final LinearLayout bg = (LinearLayout) inflate.findViewById(R.id.bg);


                dialog3.setCancelable(false);


                st.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFE8F5E9));
                sd.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFE8F5E9));
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        dialog3.dismiss();
                    }
                });
                st.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        _TimeDialog(st);
                    }
                });
                sd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        _DateDialog(sd);
                    }
                });
                dialog3.show();
            }
        });

    }


public void _TimeDialog(TextView textView){

    TimePickerDialog timePickerDialog=new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

             hour1=hourOfDay;

            // min1=00;
            String time1=hour1+":"+00;
            SimpleDateFormat f24hours1=new SimpleDateFormat("HH:mm");
            try {



                Date date1=f24hours1.parse(time1);
                SimpleDateFormat f12hour1=new SimpleDateFormat("hh:mm aa");
                textView.setText(f12hour1.format(date1));
                ex=textView.getText().toString();
                String extimee=f24hours1.format(date1);
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("Date", MODE_PRIVATE);
                final SharedPreferences.Editor myEdit = sharedPreferences1.edit();
                myEdit.putString("extime",""+ex);
                myEdit.putString("extimee",""+extimee);
                myEdit.apply();
                // Toast.makeText(CarBooking.this, ""+sd, Toast.LENGTH_SHORT).show();

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

    public void _DateDialog (final TextView _textview) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                _textview.setText(day + "/" + month + "/" + year);
            }
        };
        showDatePicker(datePickerListener);
    }

    public void showDatePicker(DatePickerDialog.OnDateSetListener listener) {

        /*Calendar c = Calendar.getInstance();
        c.set(nowyear-21, nowmonth,nowday);*/



        // this will provide the 2001 =  now year(2022) - 21 years
        // it will change by time automatic


        DatePickerDialog datePickerDialog= new DatePickerDialog(context);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.setOnDateSetListener(listener);
        datePickerDialog.show();

       /* DatePickerDialog datePicker = new DatePickerDialog(context);
        // datePicker.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        datePicker.setOnDateSetListener(listener);
        datePicker.show();*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void getInvoice(final String method,final String booking_id){

        class UserLoginClass extends AsyncTask<String,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(context,"Loading...",null,true,true);
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

                        String base64string=jsonObject2.getString("base64file");

                        // Toast.makeText(ThankYou.this, ""+base64string, Toast.LENGTH_LONG).show();
                        try {
                            download(base64string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }else{

                        String messege = jsonObject2.getString("msg");
                        Toast.makeText(context, messege, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {


                String jsonInputString="{\"method\":\"getInvoice\",\"booking_id\":\""+booking_id+"\"}";

                //  Toast.makeText(PaymentPage.this, ""+book_id, Toast.LENGTH_SHORT).show();

//                finalResult = jsonhttpParse.postRequest(method,Email,Password, HttpURL);
                finalResult = jsonhttpParse.postRequest(jsonInputString, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(method,booking_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void download(String base64string) throws IOException {

        base64string=base64string.replace("\r\n","");


        final File dwldsPath = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/"+booking_id+"_"+"Tooros_Invoice.pdf");
        //  final File dwldsPath = new File("/storage/emulated/0/Movies/Tooros_Booking_new.pdf");
        byte[] pdfAsBytes = Base64.getDecoder().decode(base64string);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(pdfAsBytes);
        os.flush();
        os.close();



    }

    public class viewHolder extends RecyclerView.ViewHolder{
       TextView startdate,enddate,carname,price , invoice, extend;
       LinearLayout linearLayout,bg;
       CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            startdate=itemView.findViewById(R.id.startdate);
            enddate=itemView.findViewById(R.id.enddate);

            carname=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            invoice=itemView.findViewById(R.id.invoicedownload);
         //   linearLayout=itemView.findViewById(R.id.linearlayout);
            cardView=itemView.findViewById(R.id.card1);
            bg = itemView.findViewById(R.id.bg);

            extend = itemView.findViewById(R.id.extend);


          // cardView.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[] {0xFF1E201D,0xFF98BC14}));

           bg.setBackground(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,new int[] {0xFF1E201D,0xFF98BC14}));
        }
    }

}
