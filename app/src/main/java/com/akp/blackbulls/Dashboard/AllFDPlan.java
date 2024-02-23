package com.akp.blackbulls.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ViewPdf;
import com.akp.blackbulls.EMIReport.DDFDEMIChart;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllFDPlan extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private AllFDPlan.FriendsListAdapter pdfAdapTer;
    String UserId;
    int j=0;
    ImageView norecord_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_f_d_plan);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Purchase_FDDDPlanList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                            hashlist.put("Member_Id", jsonObject.getString("Member_Id"));
                            hashlist.put("account_no", jsonObject.getString("account_no"));
                            hashlist.put("DepsitAMo", jsonObject.getString("DepsitAMo"));
                            hashlist.put("Maturity", jsonObject.getString("Maturity"));
                            hashlist.put("Enter_date", jsonObject.getString("Enter_date"));
                            hashlist.put("Enter_time", jsonObject.getString("Enter_time"));
                            hashlist.put("paymode", jsonObject.getString("paymode"));
                            hashlist.put("Plan_Name", jsonObject.getString("Plan_Name"));
                            hashlist.put("PlanDuration", jsonObject.getString("PlanDuration"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new AllFDPlan.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);

                        Toast.makeText(AllFDPlan.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AllFDPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanType", "FD");
                params.put("Member_ID", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AllFDPlan.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<AllFDPlan.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public AllFDPlan.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AllFDPlan.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_plan, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final AllFDPlan.FriendsList holder, final int position) {
            holder.Member_IDtv.setText("Account No. "+data.get(position).get("account_no"));
            holder.MemberNametv.setText(data.get(position).get("MemberName"));
            holder.Package_nametv.setText(data.get(position).get("DepsitAMo"));

            holder.TotalDaytv.setText(data.get(position).get("Maturity"));
            holder.TotalReturnAmttv.setText(data.get(position).get("Enter_date"));
            holder.rewardPointtv.setText(data.get(position).get("Enter_time"));
            holder.ActivationDatetv.setText(data.get(position).get("paymode"));
            holder.ValidityDatetv.setText(data.get(position).get("Plan_Name"));
            holder.ValidityDatetv1.setText(data.get(position).get("PlanDuration")+ " Months");
//            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);

            holder.emi_tv.setVisibility(View.GONE);
          /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), DDFDEMIChart.class);
                    intent.putExtra("acno",data.get(position).get("account_no"));
                    startActivity(intent);
                }
            });*/

            holder.only_dd_ll.setVisibility(View.GONE);


            holder.pdf_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), ViewPdf.class);
                    intent.putExtra("acno",data.get(position).get("account_no"));
                    startActivity(intent);                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView Member_IDtv,MemberNametv,Package_nametv,TotalDaytv,TotalReturnAmttv,rewardPointtv,ActivationDatetv,ValidityDatetv,ValidityDatetv1,emi_tv,NextDueDatetv,PayDatetv,BalanceAmttv;
        ImageView pdf_img;
        LinearLayout only_dd_ll;

        public FriendsList(View itemView) {
            super(itemView);
            Member_IDtv=itemView.findViewById(R.id.Member_IDtv);
            MemberNametv=itemView.findViewById(R.id.MemberNametv);
            Package_nametv=itemView.findViewById(R.id.Package_nametv);
            TotalDaytv=itemView.findViewById(R.id.TotalDaytv);


            TotalReturnAmttv=itemView.findViewById(R.id.TotalReturnAmttv);
            rewardPointtv=itemView.findViewById(R.id.rewardPointtv);
            ActivationDatetv=itemView.findViewById(R.id.ActivationDatetv);

            ValidityDatetv=itemView.findViewById(R.id.ValidityDatetv);
            ValidityDatetv1=itemView.findViewById(R.id.ValidityDatetv1);
            emi_tv=itemView.findViewById(R.id.emi_tv);
            pdf_img=itemView.findViewById(R.id.pdf_img);

            NextDueDatetv=itemView.findViewById(R.id.NextDueDatetv);
            PayDatetv=itemView.findViewById(R.id.PayDatetv);
            BalanceAmttv=itemView.findViewById(R.id.BalanceAmttv);

            only_dd_ll=itemView.findViewById(R.id.only_dd_ll);
        }
    }


}