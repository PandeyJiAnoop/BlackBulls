package com.akp.blackbulls.EarningReport;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.NetworkConnectionHelper;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamEarning extends AppCompatActivity {
    ImageView back_img;
    LinearLayout dynamic_ll;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RecyclerView chat_recyclerView;
    private SharedPreferences sharedPreferences;
    String UserId,Username;
    TextView norecord_tv;
    SwipeRefreshLayout srl_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_earning);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        Username = sharedPreferences.getString("U_name", "");
        dynamic_ll=findViewById(R.id.dynamic_ll);
        chat_recyclerView=findViewById(R.id.chat_recyclerView);
        norecord_tv=findViewById(R.id.norecord_tv);
        back_img=findViewById(R.id.back_img);



        srl_refresh = findViewById(R.id.srl_refresh);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(TeamEarning.this)) {
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
                    Toast.makeText(TeamEarning.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetPostionList();
//        for (int i=0;i<2;i++){
//            GetPostionList(i);
//        }


    }

    public void GetPostionList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"LevelIncomeReport", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray  = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("TeamId", jsonObject1.getString("TeamId"));
                        hm.put("DiffAmount", jsonObject1.getString("DiffAmount"));
                        hm.put("Bussiness", jsonObject1.getString("Bussiness"));
                        hm.put("Levels", jsonObject1.getString("Levels"));
                        norecord_tv.setVisibility(View.GONE);
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(TeamEarning.this, 1);
                    PositionrAdapter HistoryAdapte = new PositionrAdapter(TeamEarning.this, arrayList);
                    chat_recyclerView.setLayoutManager(gridLayoutManager);
                    chat_recyclerView.setAdapter(HistoryAdapte);
                } catch (JSONException e) {
                    Toast.makeText(TeamEarning.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(TeamEarning.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params;
        }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(TeamEarning.this);
        requestQueue.add(stringRequest);

    }



    public class PositionrAdapter extends RecyclerView.Adapter<PositionrAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        public PositionrAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public PositionrAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_teamearning, viewGroup, false);
            PositionrAdapter.VH viewHolder = new PositionrAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PositionrAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
            vh.type_tv.setText(arrayList.get(i).get("TeamId"));
            vh.date_tv.setText(arrayList.get(i).get("DiffAmount"));
            vh.date_tv1.setText(arrayList.get(i).get("Levels"));
            vh.amount_tv.setText("₹ "+arrayList.get(i).get("Bussiness"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView type_tv, date_tv, date_tv1,amount_tv;

            public VH(@NonNull View itemView) {
                super(itemView);
                type_tv = itemView.findViewById(R.id.type_tv);
                date_tv = itemView.findViewById(R.id.date_tv);
                amount_tv = itemView.findViewById(R.id.amount_tv);
                date_tv1 = itemView.findViewById(R.id.date_tv1);
            }
        }
    }

}