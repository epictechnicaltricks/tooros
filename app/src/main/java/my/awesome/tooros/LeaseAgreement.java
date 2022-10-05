package my.awesome.tooros;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LeaseAgreement extends AppCompatActivity {

    WebView web;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vla_app);
       tv1=findViewById(R.id.cr2);
       String str="\nThis Vehicle Lease Agreement is made and effective from the date of booking as reflected in the booking details on the App (the “ Agreement ”)\n" +
               "\n" +
               "BETWEEN\n" +
               "The “ Lessor ” (which expression shall, unless repugnant to the context or meaning thereof, be deemed to mean and include his/her heirs, executors, administrators and assigns) of the ONE PART and details of which shall be provided in the App at the time of booking.\n" +
               "\n" +
               "AND\n" +
               "The “ Lessee ” (which expression shall, unless repugnant to the context or meaning thereof, be deemed to mean and include his/her heirs, executors, administrators, and assigns) of the OTHER PART as shall be captured in the booking details ; The Lessor and the Lessee shall be hereinafter collectively referred to as “ Parties ” and individually as the “ Party ”.\n" +
               "\n" +
               "WHEREAS:\n" +
               "The Lessor is the sole legal, beneficial, and registered owner of the Vehicle(details of which are captured in the App).\n" +
               "The Lessor had listed the Vehicle for leasing on the Platform (defined below) subject to the terms and conditions specified therein.\n" +
               "Lessee, a user of the Platform, wishes to lease the Vehicle from the Lessor and the Lessor is willing to lease it to the Lessee, on the terms and conditions of this Agreement. ";



       tv1.setText(str);




    }

    public void adview(View view) {

        setContentView(R.layout.activity_vla_viewmore);
        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show();
        web = findViewById(R.id.web);
        web.loadUrl("https://tooros.in/page.php?page=6");
        WebSettings webSettings=web.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

    }
}
