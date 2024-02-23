package com.akp.blackbulls.Walet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AllWallet extends AppCompatActivity {
    LinearLayout add_fund_ll,mudra_ll,service_ll,coin_safe_ll;
 Button add_fund_btn;
 RelativeLayout header;
 TextView income_wallet_tv,topup_wallet_tv,recharge_wallet_tv,wallet_tv;
 String UserId,UserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        add_fund_ll=findViewById(R.id.add_fund_ll);
        mudra_ll=findViewById(R.id.mudra_ll);
        service_ll=findViewById(R.id.service_ll);
        coin_safe_ll=findViewById(R.id.coin_safe_ll);
        add_fund_btn=findViewById(R.id.add_fund_btn);
        header=findViewById(R.id.header);
        wallet_tv=findViewById(R.id.wallet_tv);
        income_wallet_tv=findViewById(R.id.income_wallet_tv);
        topup_wallet_tv=findViewById(R.id.topup_wallet_tv);
        recharge_wallet_tv=findViewById(R.id.recharge_wallet_tv);
        GetWalletAPI();

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_fund_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(getApplicationContext(), MainWalletTransationHistory.class);
                startActivity(intent6);
            }
        });

        add_fund_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(getApplicationContext(), AddWalletRecord.class);
                startActivity(intent6);
            }
        });



        service_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(getApplicationContext(), ServiceWallet.class);
                startActivity(intent6);
            }
        });

        mudra_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(getApplicationContext(), MudraWallet.class);
                startActivity(intent6);
            }
        });

        coin_safe_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(getApplicationContext(), CoinSaveWallet.class);
                startActivity(intent6);
            }
        });

    }

    public void GetWalletAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            wallet_tv.setText("Available balance:-( \u20B9 "+jsonObject.getString("MainWallet")+")");
                            income_wallet_tv.setText("\u20B9 "+jsonObject.getString("MurdaWallet"));
                            topup_wallet_tv.setText("\u20B9 "+jsonObject.getString("ServiceWallet"));
                            recharge_wallet_tv.setText("\u20B9 "+jsonObject.getString("CoinSaveWallet"));
                        }

                    } else {
//                        Toast.makeText(DailyPlan.this, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(AllWallet.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Member_ID", UserId);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AllWallet.this);
        requestQueue.add(stringRequest);

    }


}