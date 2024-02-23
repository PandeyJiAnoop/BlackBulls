package com.akp.blackbulls.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.R;
import com.akp.blackbulls.Walet.WalletHistoy;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;

public class PlanPurchase extends AppCompatActivity {

    ImageView menuImg;
    TextView wallet_amount_tv, credit_tv, debit_tv,coin_tv;
    String UserId;
    TextView raise_add_tv;

    Spinner sp_state;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();

    ArrayList<String> package_amount = new ArrayList<>();
    TextView tv1,tv2,tv3,tv4,tv5;


    String stateid,pakamount;
    AppCompatButton btn_submit;
    CardView details_cv;

    AppCompatButton daily_btn,onetime_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_purchase);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        menuImg = findViewById(R.id.menuImg);

        wallet_amount_tv = findViewById(R.id.wallet_amount_tv);
        credit_tv = findViewById(R.id.credit_tv);
        debit_tv = findViewById(R.id.debit_tv);
        sp_state = findViewById(R.id.sp_state);
        btn_submit = findViewById(R.id.btn_submit);
        coin_tv=findViewById(R.id.coin_tv);

        daily_btn=findViewById(R.id.daily_btn);
        onetime_btn=findViewById(R.id.onetime_btn);

        raise_add_tv = findViewById(R.id.raise_add_tv);
        details_cv= findViewById(R.id.details_cv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        WalletAPI("",UserId);
        GetCategory();
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            pakamount = package_amount.get(j - 1);

                            break;
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        daily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_btn.setBackgroundResource(R.color.blue);
                onetime_btn.setBackgroundResource(R.color.white);
                tv4.setVisibility(View.VISIBLE);
                GetAllPlanDetailsByPlanCodeDetails(stateid,"1");
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetPurchasePlanAPI(stateid,"1");
                    }
                });

            }
        });
        onetime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onetime_btn.setBackgroundResource(R.color.blue);
                daily_btn.setBackgroundResource(R.color.white);
                tv4.setVisibility(View.GONE);

                GetAllPlanDetailsByPlanCodeDetails(stateid,"0");
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetPurchasePlanAPI(stateid,"0");
                    }
                });
            }
        });



        GetWalletAPI();
    }

    void GetCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "DailyPlanList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName.add("Select Plan Amount");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("Id"));
                        package_amount.add(jsonObject1.getString("Package_Cost"));
                        String statename = jsonObject1.getString("Package_name");
                        StateName.add(statename);
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(PlanPurchase.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlanPurchase.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanType", "DD");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlanPurchase.this);
        requestQueue.add(stringRequest);
    }

    public void GetPurchasePlanAPI(final String pacid,final  String isdaily) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PackagePurchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(PlanPurchase.this, "Package Purchase Successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PlanPurchase.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlanPurchase.this, "Error:- Select Plan Package!!!" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Member_ID", UserId);
                params.put("PackageID", pacid);
                params.put("Amount", pakamount);
                params.put("IsDaily", isdaily);

//                Log.d("res123","cxc"+params);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlanPurchase.this);
        requestQueue.add(stringRequest);
    }


    public void GetWalletAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
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
                            coin_tv.setText("\u20B9 "+jsonObject.getString("CoinSaveWallet"));
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
                Toast.makeText(PlanPurchase.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PlanPurchase.this);
        requestQueue.add(stringRequest);

    }



    public void GetAllPlanDetailsByPlanCodeDetails(final String plancode,final String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"DIPPlanDetail", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("12dfffffdfgdg", response);

//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        details_cv.setVisibility(View.VISIBLE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            tv1.setText("Package Name:- "+jsonObject.getString("Package_name"));
                            tv2.setText("Package Cost:- "+jsonObject.getString("Package_Cost"));
                            tv3.setText("Total Profit Amount:- \u20B9 "+jsonObject.getString("TotalProfit"));
                            tv4.setText("Daily Profit:- "+jsonObject.getString("rewardPoint"));
                            tv5.setText("Total Days:- "+jsonObject.getString("TotalDays")+" Days");


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
                Toast.makeText(PlanPurchase.this, "Select Plan", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanCode", plancode);
                params.put("IsDaily", type);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlanPurchase.this);
        requestQueue.add(stringRequest);

    }

}


