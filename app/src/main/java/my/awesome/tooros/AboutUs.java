package my.awesome.tooros;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AboutUs extends AppCompatActivity {


    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    LinearLayout l;
    private SharedPreferences sh;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        l = findViewById(R.id.l);
        sh = getSharedPreferences("sh", Activity.MODE_PRIVATE);


      /*

      // this is for testing


      c = Calendar.getInstance();



        // format of shared preference date :  dd-MM-yyyy hh:00 aaa

       sh.edit().putString("drop_date", new SimpleDateFormat("dd-MM-yyyy hh:00 aaa").format(c.getTime())).apply();

        String specificDate = new SimpleDateFormat("dd-MM-yyyy hh:00 aaa").format(c.getTime()).substring(0,2);
        sh.edit().putString("drop_exact_day",specificDate).apply();

        String specificMonth = new SimpleDateFormat("dd-MM-yyyy hh:00 aaa").format(c.getTime()).substring(3,5);

        sh.edit().putString("drop_exact_month",specificMonth).apply();



        Toast.makeText(this,"demo Drop date \n\n"+ sh.getString("drop_date","")+" "+sh.getString("drop_exact_day","")+" "+sh.getString("drop_exact_month",""), Toast.LENGTH_LONG).show();


*/

    /*
       reviewManager = ReviewManagerFactory.create(this);
          Task<ReviewInfo> request = reviewManager.requestReviewFlow();
          request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
            } else {
                // There was some problem, continue regardless of the result.
            }
        });
     */

        init();

    }



    private  void init(){

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Task<Void> flow = reviewManager.launchReviewFlow(AboutUs.this, reviewInfo);
                flow.addOnCompleteListener(task -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });*/
            }
        });
    }


}

