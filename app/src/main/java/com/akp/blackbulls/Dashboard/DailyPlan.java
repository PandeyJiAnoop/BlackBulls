package com.akp.blackbulls.Dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ViewPdf;
import com.akp.blackbulls.Basic.ViewPdfDip;
import com.akp.blackbulls.EMIReport.EMIChart;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DailyPlan extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plan);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Log.v("addadada", UserId);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        GetUserListAPI();
    }



    public void GetUserListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PackagePurchaseList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadasdfa","sadsad"+response);

//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
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
                            hashlist.put("Member_ID", jsonObject.getString("Member_ID"));
                            hashlist.put("Package_Cost", jsonObject.getString("Package_Cost"));
                            hashlist.put("Package_name", jsonObject.getString("Package_name"));
                            hashlist.put("TotalDay", jsonObject.getString("TotalDay"));
                            hashlist.put("TotalReturnAmt", jsonObject.getString("TotalReturnAmt"));
                            hashlist.put("rewardPoint", jsonObject.getString("rewardPoint"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("ActivationDate", jsonObject.getString("ActivationDate"));
                            hashlist.put("ValidityDate", jsonObject.getString("ValidityDate"));
                            hashlist.put("AccountNo", jsonObject.getString("AccountNo"));
                            hashlist.put("ReturnType", jsonObject.getString("ReturnType"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(DailyPlan.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DailyPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Member_ID", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DailyPlan.this);
        requestQueue.add(stringRequest);
    }

    public class FriendsListAdapter extends RecyclerView.Adapter<DailyPlan.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public DailyPlan.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DailyPlan.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_dip_plan, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final DailyPlan.FriendsList holder, final int position) {
//            holder.Member_IDtv.setText("Sr.No. "+position);
//            holder.MemberNametv.setText(data.get(position).get("MemberName"));
            holder.Package_Costtv.setText(data.get(position).get("Package_Cost"));
            holder.Package_nametv.setText(data.get(position).get("Package_name"));

            holder.TotalDaytv.setText(data.get(position).get("TotalDay"));
            holder.TotalReturnAmttv.setText(data.get(position).get("TotalReturnAmt"));
            holder.rewardPointtv.setText(data.get(position).get("rewardPoint"));
            holder.ActivationDatetv.setText(data.get(position).get("ActivationDate"));
            holder.ValidityDatetv.setText(data.get(position).get("ValidityDate"));

            holder.account_notv.setText("Account No:- "+data.get(position).get("AccountNo"));
            holder.ReturnTypetv.setText(data.get(position).get("ReturnType"));

            if (data.get(position).get("ReturnType").equalsIgnoreCase("Daily Return")){
                holder.rewardPointtvll.setVisibility(View.VISIBLE);
            }
            else {
                holder.rewardPointtvll.setVisibility(View.GONE);

            }

            holder.emi_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), EMIChart.class);
                    intent.putExtra("acno",data.get(position).get("AccountNo"));
                    startActivity(intent);
                }
            });


            holder.pdf_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), ViewPdfDip.class);
                    intent.putExtra("acno",data.get(position).get("AccountNo"));
                    startActivity(intent);                }
            });


//            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView MemberNametv,Package_Costtv,Package_nametv,TotalDaytv,TotalReturnAmttv,rewardPointtv,ActivationDatetv,ValidityDatetv,account_notv,emi_tv,ReturnTypetv;

        LinearLayout rewardPointtvll;
        ImageView pdf_img;


        public FriendsList(View itemView) {
            super(itemView);
            MemberNametv=itemView.findViewById(R.id.MemberNametv);

            Package_Costtv=itemView.findViewById(R.id.Package_Costtv);
            Package_nametv=itemView.findViewById(R.id.Package_nametv);
            TotalDaytv=itemView.findViewById(R.id.TotalDaytv);
            emi_tv=itemView.findViewById(R.id.emi_tv);

            TotalReturnAmttv=itemView.findViewById(R.id.TotalReturnAmttv);
            rewardPointtv=itemView.findViewById(R.id.rewardPointtv);
            ActivationDatetv=itemView.findViewById(R.id.ActivationDatetv);

            ValidityDatetv=itemView.findViewById(R.id.ValidityDatetv);
            account_notv=itemView.findViewById(R.id.account_notv);
            ReturnTypetv=itemView.findViewById(R.id.ReturnTypetv);

            rewardPointtvll=itemView.findViewById(R.id.rewardPointtvll);


            pdf_img=itemView.findViewById(R.id.pdf_img);
        }
    }


}