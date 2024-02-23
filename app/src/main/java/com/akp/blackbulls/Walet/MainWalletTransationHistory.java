package com.akp.blackbulls.Walet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainWalletTransationHistory extends AppCompatActivity {
    ImageView menuImg;
    String UserId;
    TextView wallet_amount_tv,credit_tv,debit_tv,coin_tv;

    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wallet_transation_history);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
        wallet_amount_tv=findViewById(R.id.wallet_amount_tv);
        credit_tv=findViewById(R.id.credit_tv);
        debit_tv=findViewById(R.id.debit_tv);
        coin_tv=findViewById(R.id.coin_tv);

        GetWalletAPI();
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        WalletHistoryAPI();
    }

    public void GetWalletAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadasdfa","sadsad"+response);

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
                            credit_tv.setText("\u20B9 "+jsonObject.getString("MurdaWallet"));
                            debit_tv.setText("\u20B9 "+jsonObject.getString("ServiceWallet"));
                            coin_tv.setText("\u20B9 "+jsonObject.getString("MainWalletTransationHistory"));
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
                Toast.makeText(MainWalletTransationHistory.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainWalletTransationHistory.this);
        requestQueue.add(stringRequest);

    }



    public void WalletHistoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"WalletTranscationReport", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("FromMemberId", jsonObject.getString("FromMemberId"));
                            hashlist.put("ToMemberId", jsonObject.getString("ToMemberId"));
                            hashlist.put("TransType", jsonObject.getString("TransType"));
                            hashlist.put("TransFor", jsonObject.getString("TransFor"));
                            hashlist.put("PreAmount", jsonObject.getString("PreAmount"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Remark", jsonObject.getString("Remark"));
                            hashlist.put("EntryBy", jsonObject.getString("EntryBy"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));



                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(MainWalletTransationHistory.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainWalletTransationHistory.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("TransFor", "MAIN");
                params.put("Member_Id", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainWalletTransationHistory.this);
        requestQueue.add(stringRequest);
    }
    public class FriendsListAdapter extends RecyclerView.Adapter<MainWalletTransationHistory.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public MainWalletTransationHistory.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainWalletTransationHistory.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transactions, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final MainWalletTransationHistory.FriendsList holder, final int position) {
            holder.tv1.setText("Amount :-  "+data.get(position).get("Amount")+" Rs."+"\nRemark :- "+data.get(position).get("Remark"));
            holder.tv2.setText("Approved By :- "+data.get(position).get("EntryBy"));
            holder.tv3.setText(data.get(position).get("EntryDate"));
            if (data.get(position).get("TransType").equalsIgnoreCase("Cr")){
                holder.tv4.setText("Credit");
                holder.tv4.setTextColor(Color.GREEN);
            }
            else {
                holder.tv4.setText("Debit");
                holder.tv4.setTextColor(Color.RED);
            }



        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tv4;

        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);

        }
    }

}