package my.awesome.tooros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HelpAndSupport extends AppCompatActivity {

    WebView web;
    TextView address,phone,email,wa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);

        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        wa = findViewById(R.id.wa);
      //  9040007374
        address.setOnClickListener(view -> _Open_url("https://goo.gl/maps/z5daHvjyTHbVcWrz8"));
        phone.setOnClickListener(view -> _Open_url("tel:9040007374"));
        email.setOnClickListener(view -> _Open_url("mailto:support@tooros.in"));
        wa.setOnClickListener(view -> _Open_url("https://api.whatsapp.com/send/?phone=+919040007374&text=Hello! Tooros please help me..."));

    }

    public void _Open_url (final String _url) {
        String url = _url;

        try{

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }catch(Exception e){


            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    /*    Intent in = new Intent();
            in.setClass(this,CitySelectionActivity.class);
            in.putExtra("rate","true");
            startActivity(in);
            */
            finish();

    }
}
