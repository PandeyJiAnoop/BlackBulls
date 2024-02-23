package com.akp.blackbulls.Basic;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;

import com.akp.blackbulls.Dashboard.DailyPlan;
import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.MPin.MainActivity;
import com.akp.blackbulls.NewLoginJanuary.JanuaryLogin;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


public class SplashScreen extends AppCompatActivity {
    //Handler
    private final Handler handler = new Handler();
    //SPLASHTIMEOUT
    private static int SPLASHTIMEOUT = 3000; // Splash screen timer
    String UserId;
    String prodid;
    String TAG ="splash";
    //Handler
    //SPLASHTIMEOUT
    String versionName = "", versionCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
        ImageView ivsplash=findViewById(R.id.ivsplash);
        RelativeLayout rl=findViewById(R.id.rl);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        ivsplash.startAnimation(animation1);
//        navigte();
        checkLogin();
//        UpdateVersion();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
//                            Toast.makeText(getApplicationContext(),deepLink.toString(),Toast.LENGTH_LONG).show();
                            Log.e(TAG, " my referlink "+deepLink.toString());
                            //   "http://www.blueappsoftware.com/myrefer.php?custid=cust123-prod456"
                            String referlink = deepLink.toString();
                            try {
                                referlink = referlink.substring(referlink.lastIndexOf("=")+1);
                                Log.e(TAG, " substring "+referlink); //cust123-prod456
                                String custid = referlink.substring(0, referlink.indexOf("-"));
                                prodid = referlink.substring(referlink.indexOf("-")+1);
                                Log.e(TAG, " custid "+custid +"----prpdiid "+ prodid);
                                // shareprefernce (prodid, custid);
                                //sharepreference  (refercustid, custid)
                            }catch (Exception e){
                                Log.e(TAG, " error "+e.toString());
                            }
                        }
                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });



    }

/*    public void navigte() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                if (UserId.equalsIgnoreCase("")){
                    startActivity(new Intent(SplashScreen.this, LoginOTP.class));
                    finish();
                }
                else {
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

//                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
//                    finish();
                }
            }
        }, SPLASHTIMEOUT);
    }*/






    private void checkLogin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    handler.postDelayed(this, 30);

            }
        }, 200);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(SplashScreen.this)) {
                    if (UserId.equalsIgnoreCase("")){
//                        Intent intent=new Intent(getApplicationContext(), JanuaryLogin.class);
//                        intent.putExtra("referral_id",prodid);
//                        startActivity(intent);
//                        finish();

                        Intent intent=new Intent(getApplicationContext(), LoginOTP.class);
                        intent.putExtra("referral_id",prodid);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

//                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
//                    finish();
                    }
                } else {

                    Toast.makeText(SplashScreen.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                } }
        }, SPLASHTIMEOUT); }

    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // or finish();
              /*  if (code.equalsIgnoreCase("")){
                    Intent myIntent = new Intent(SplashScreen.this,WelcomeSlider.class);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(myIntent);
                }*/
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));

            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void UpdateVersion() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetAppVersion", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String UpdateVersion = jsonObject.getString("AppVersion");
                            String status1 = object.getString("Message");
                            if (status1.equalsIgnoreCase("Success"))
                                getVersionInfo();
                            {
                                if (versionName.equalsIgnoreCase(UpdateVersion)) {
                                    checkLogin();
                                } else {
                                    AlertVersion();
                                    //checkLogin();
                                }
                            }

                        }

                    } else {
                        Toast.makeText(SplashScreen.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

              /*  try {
                    JSONObject object = new JSONObject(response);
                    String UpdateVersion = object.getString("Version");
//                        Toast.makeText(getApplicationContext(),""+UpdateVersion,Toast.LENGTH_LONG).show();
                    String status = object.getString("messege");
                    if (status.equalsIgnoreCase("success"))
                        getVersionInfo();
                    {
                        if (versionName.equalsIgnoreCase(UpdateVersion)) {
                            checkLogin();
                        } else {
                            AlertVersion();
                            //checkLogin();
                        } }
                }catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}