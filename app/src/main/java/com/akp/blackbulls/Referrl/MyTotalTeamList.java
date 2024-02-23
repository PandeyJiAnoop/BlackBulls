package com.akp.blackbulls.Referrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.NetworkConnectionHelper;
import com.akp.blackbulls.Dashboard.AnimationHelper;
import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.EarningReport.ReferralPersonList;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTotalTeamList extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_total_team_list);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);

        sliding_menu=findViewById(R.id.back_img);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(MyTotalTeamList.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(MyTotalTeamList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getHistory();



    }
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(MyTotalTeamList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"MemberTeamLevelDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsenew",response);
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
                            hashlist.put("UserID", jsonObject.getString("UserID"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("PlanStatus", jsonObject.getString("PlanStatus"));
                            hashlist.put("ProfilePic", jsonObject.getString("ProfilePic"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));

                            hashlist.put("Invest", jsonObject.getString("Invest"));
                            hashlist.put("Upgrade", jsonObject.getString("Upgrade"));

                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyTotalTeamList .this, 1);
                        ReferralPersonListAdapter customerListAdapter = new ReferralPersonListAdapter(MyTotalTeamList.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(MyTotalTeamList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(MyTotalTeamList.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MyTotalTeamList.this);
        requestQueue.add(stringRequest);

    }




    public class ReferralPersonListAdapter extends RecyclerView.Adapter<ReferralPersonListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public ReferralPersonListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ReferralPersonListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_mytotalteam, viewGroup, false);
            ReferralPersonListAdapter.VH viewHolder = new ReferralPersonListAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReferralPersonListAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);

            vh.sr_no.setText(String.valueOf(" "+(i+1)));

            vh.bbid_tv.setText(arrayList.get(i).get("UserID"));
            vh.name_tv.setText(arrayList.get(i).get("MemberName"));
            vh.date_tv.setText(arrayList.get(i).get("RegDate"));


            vh.invest_tv.setText(arrayList.get(i).get("Invest")+" \u20B9");

            if (arrayList.get(i).get("Upgrade").equalsIgnoreCase("null")){
                vh.upgrade_tv.setText("");
            }
            else {
                vh.upgrade_tv.setText(arrayList.get(i).get("Upgrade"));
            }


            if (arrayList.get(i).get("PlanStatus").equalsIgnoreCase("true")){
//                vh.status_tv.setText("Active");
                vh.live_ll.setBackgroundResource(R.drawable.cardgreen);
            }
            else {
//                vh.status_tv.setText("Pending");
                vh.live_ll.setBackgroundResource(R.drawable.card);
            }

            if (arrayList.get(i).get("ProfilePic").equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(),"Image not found!",Toast.LENGTH_LONG).show();
            }
            else {
                Glide.with(getApplicationContext()).load(arrayList.get(i).get("ProfilePic")).error(R.drawable.logo).into(vh.cat_img);
            }


        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name_tv, date_tv,status_tv,sr_no,invest_tv,upgrade_tv,bbid_tv;
            LinearLayout live_ll;
            CircularImageView cat_img;


            public VH(@NonNull View itemView) {
                super(itemView);
                name_tv = itemView.findViewById(R.id.name_tv);
                date_tv = itemView.findViewById(R.id.date_tv);
                status_tv = itemView.findViewById(R.id.status_tv);
                live_ll = itemView.findViewById(R.id.live_ll);
                sr_no = itemView.findViewById(R.id.sr_no);
                bbid_tv = itemView.findViewById(R.id.bbid_tv);

                invest_tv = itemView.findViewById(R.id.invest_tv);
                upgrade_tv = itemView.findViewById(R.id.upgrade_tv);

                cat_img = itemView.findViewById(R.id.cat_img);

            }
        }
    }

}