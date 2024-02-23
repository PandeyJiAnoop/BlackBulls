package com.akp.blackbulls.Walet;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;


public class WalletHistoy extends AppCompatActivity {
ImageView menuImg;
AppCompatButton pending_btn,approved_btn,rejected_btn;
RelativeLayout pending_ll,approve_ll,rejected_ll;
ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
String UserId;
RecyclerView pending_rec,approved_rec,rejected_rec;
TextView wallet_amount_tv,credit_tv,debit_tv;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wallet_histoy);
    SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
    UserId= sharedPreferences.getString("U_id", "");
    menuImg=findViewById(R.id.menuImg);

    wallet_amount_tv=findViewById(R.id.wallet_amount_tv);
    credit_tv=findViewById(R.id.credit_tv);
    debit_tv=findViewById(R.id.debit_tv);


    pending_btn=findViewById(R.id.pending_btn);
    approved_btn=findViewById(R.id.approved_btn);
    rejected_btn=findViewById(R.id.rejected_btn);
    pending_ll=findViewById(R.id.pending_ll);
    approve_ll=findViewById(R.id.approve_ll);
    rejected_ll=findViewById(R.id.rejected_ll);
    pending_rec=findViewById(R.id.pending_rec);
    approved_rec=findViewById(R.id.approved_rec);
    rejected_rec=findViewById(R.id.rejected_rec);
    menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    pending_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pending_ll.setVisibility(View.VISIBLE);
            approve_ll.setVisibility(View.GONE);
                 rejected_ll.setVisibility(View.GONE);
                 pending_btn.setBackgroundResource(R.color.yellow);
                 approved_btn.setBackgroundResource(R.color.skyblue);
                 rejected_btn.setBackgroundResource(R.color.skyblue);
             }
         });

        approved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pending_ll.setVisibility(View.GONE);
                approve_ll.setVisibility(View.VISIBLE);
                rejected_ll.setVisibility(View.GONE);
                pending_btn.setBackgroundResource(R.color.skyblue);
                approved_btn.setBackgroundResource(R.color.yellow);
                rejected_btn.setBackgroundResource(R.color.skyblue);
            }
        });

        rejected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pending_ll.setVisibility(View.GONE);
                approve_ll.setVisibility(View.GONE);
                rejected_ll.setVisibility(View.VISIBLE);
                pending_btn.setBackgroundResource(R.color.skyblue);
                approved_btn.setBackgroundResource(R.color.skyblue);
                rejected_btn.setBackgroundResource(R.color.yellow);
            }
        });
        HistoryRejected();
        HistoryApproved();
        HistoryPending();
    GetWalletAPI();

    }




    public void HistoryPending(){
        String otp = new GlobalAppApis().WalletHistoryAPI("", UserId);
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getWalletHistory(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("PendingWithdrawalResp");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Log.v("sdadd",""+jsonObject1);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CurrentWallet", jsonObject1.getString("CurrentWallet"));
                            hm.put("Date", jsonObject1.getString("Date"));
                            hm.put("MemberId", jsonObject1.getString("MemberId"));
                            hm.put("MemberName", jsonObject1.getString("MemberName"));
                            hm.put("Remark", jsonObject1.getString("Remark"));
                            hm.put("ReqWallet", jsonObject1.getString("ReqWallet"));
                            arrayList.add(hm);
                        }
                        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
                                WalletHistoy.this,
                                LinearLayoutManager.VERTICAL,
                                false);
                        PendingAdapter customerListAdapter1 = new PendingAdapter(WalletHistoy.this, arrayList);
                        pending_rec.setLayoutManager(HorizontalLayout1);
                        pending_rec.setAdapter(customerListAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },WalletHistoy.this, call, "", true);
    }

    public void HistoryApproved(){
        String otp = new GlobalAppApis().WalletHistoryAPI("", UserId);
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getWalletHistory(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.v("ddadada",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("ApprovedWithdrawalResp");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CurrentWallet", jsonObject1.getString("CurrentWallet"));
                            hm.put("Date", jsonObject1.getString("Date"));
                            hm.put("MemberId", jsonObject1.getString("MemberId"));
                            hm.put("MemberName", jsonObject1.getString("MemberName"));
                            hm.put("Remark", jsonObject1.getString("Remark"));
                            hm.put("ReqWallet", jsonObject1.getString("ReqWallet"));
                            arrayList1.add(hm);
                        }
                        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
                                WalletHistoy.this,
                                LinearLayoutManager.VERTICAL,
                                false);
                        ApprovedAdapter customerListAdapter2 = new ApprovedAdapter(WalletHistoy.this, arrayList1);
                        approved_rec.setLayoutManager(HorizontalLayout1);
                        approved_rec.setAdapter(customerListAdapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },WalletHistoy.this, call, "", true);
    }


    public void HistoryRejected(){
        String otp = new GlobalAppApis().WalletHistoryAPI("", UserId);
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getWalletHistory(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.v("ddadada",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("RejectedWithdrawalResp");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CurrentWallet", jsonObject1.getString("CurrentWallet"));
                            hm.put("Date", jsonObject1.getString("Date"));
                            hm.put("MemberId", jsonObject1.getString("MemberId"));
                            hm.put("MemberName", jsonObject1.getString("MemberName"));
                            hm.put("Remark", jsonObject1.getString("Remark"));
                            hm.put("ReqWallet", jsonObject1.getString("ReqWallet"));
                            arrayList2.add(hm);
                        }
                        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
                                WalletHistoy.this,
                                LinearLayoutManager.VERTICAL,
                                false);
                        RejectedAdapter customerListAdapter = new RejectedAdapter(WalletHistoy.this, arrayList2);
                        rejected_rec.setLayoutManager(HorizontalLayout1);
                        rejected_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },WalletHistoy.this, call, "", true);
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
                            wallet_amount_tv.setText("\u20B9 "+jsonObject.getString("MainWallet"));
                            credit_tv.setText("\u20B9 "+jsonObject.getString("TopUpWallet"));
                            debit_tv.setText("\u20B9 "+jsonObject.getString("IncomeWallet"));
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
                Toast.makeText(WalletHistoy.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletHistoy.this);
        requestQueue.add(stringRequest);

    }

}