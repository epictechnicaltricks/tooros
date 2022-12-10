package my.awesome.tooros;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;


public class SplashActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    private static int splash_time=1500;
    private Timer _timer = new Timer();
    private TimerTask time;
    //in app update

//    private AppUpdateManager appUpdateManager;
//    private InstallStateUpdatedListener installStateUpdatedListener;
//    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;



    //in app update

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "Sample";
    private InAppUpdateManager inAppUpdateManager;



    private static final String CHANNEL_ID = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        _transparentStatusAndNavigation();

//        // Add this:  this line help required for android 12+ devices
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
//                .detectLeakedClosableObjects()
//                .build());

//        appUpdateManager = AppUpdateManagerFactory.create(SplashActivity.this);
//        installStateUpdatedListener = state -> {
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                popupSnackBarForCompleteUpdate();
//            } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                removeInstallStateUpdateListener();
//            } else {
//               // Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
//            }
//        };
//
//        appUpdateManager.registerListener(installStateUpdatedListener);
//        checkUpdate();



        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.IMMEDIATE)

                // default is false. If is set to true you,
                // have to manage the user confirmation when
                // you detect the InstallStatus.DOWNLOADED status,
                .useCustomNotification(true)

                .handler(SplashActivity.this);

        inAppUpdateManager.checkForAppUpdate();



        createNotificationChannel();
        getToken();
        subscribe_to_topic();


    }

////////////////// FIREBASE FCM //////////////


    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                if(!task.isSuccessful()){

                    Log.d("FCM FIREBASE", "onComplete: Failed to get the Token");

                }

                //Token
                String token = task.getResult();
                Log.d("FCM FIREBASE", "onComplete: " + token);
            }

        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Receve Firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    private void subscribe_to_topic(){

        // this topic on php file project/notify.php then /topic/all

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(task -> {

            String msg = "Subscribed";
            if (!task.isSuccessful()) {
                msg = "Subscribe failed";
            }
            Log.d("FCM FIREBASE", msg);

            //Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
        });

    }


//////////////////////////////////// FIREBASE FCM ///////



  /*  private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });

    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, SplashActivity.FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {

                Letsgo();

               // Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {

               Letsgo();

               // Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
        */


        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                //inAppUpdateManager.checkForAppUpdate();
                finishAffinity();


                Log.d("App Update", "Update flow failed! Result code: " + resultCode);
            } else {

                Letsgo();
            }


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
       // removeInstallStateUpdateListener();
    }


    private void Letsgo(){


        time = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {

                    startActivity(new Intent(SplashActivity.this,CitySelectionActivity.class));
                    finish();

                    time.cancel();
                });
            }
        };
        _timer.schedule(time, splash_time);
    }

/*
    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }


                })
                .setActionTextColor(0xFF4aeb26)
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
*/



    public void _transparentStatusAndNavigation () {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }
    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onInAppUpdateError(int i, Throwable throwable) {

        Toast.makeText(this, "Update Error", Toast.LENGTH_SHORT).show();
        Letsgo();
    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        /*
         * If the update downloaded, ask user confirmation and complete the update
         */

        if(!status.isUpdateAvailable()){
            Letsgo();
           // Toast.makeText(this, "Update Available", Toast.LENGTH_SHORT).show();
        }

if(status.isDownloading()){

   // Toast.makeText(this, "Updating in background..", Toast.LENGTH_SHORT).show();
}
        if (status.isDownloaded()) {
            inAppUpdateManager.completeUpdate();


         //  startActivity(new Intent(SplashActivity.this,SplashActivity.class));
            finishAffinity();

            }
        }



    }


