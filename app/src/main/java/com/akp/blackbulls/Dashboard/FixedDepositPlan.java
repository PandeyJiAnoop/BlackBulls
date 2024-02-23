package com.akp.blackbulls.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.AppUrls;
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
import java.util.List;
import java.util.Map;

public class FixedDepositPlan extends AppCompatActivity {
    ImageView menuImg;
    TextView wallet_amount_tv,credit_tv,debit_tv,coin_tv;
    String UserId;
    TextView raise_add_tv;

    Spinner sp_state,sp_plan;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    String stateid,selectedVideoPath;

    // create array of Strings
    // and store name of courses
    String[] courses = { "DD", "FD"};

    RecyclerView wallet_histroy;
    public static AppCompatButton previousSeletedButton = null;
    ArrayList<HashMap<String, String>> arrayList4 = new ArrayList<>();
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    AppCompatButton btn_submit;
    String deposite_tv,maturity_tv;
    AppCompatButton threemonth_rl,sixmonth_rl;
    CardView details_cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_deposit_plan);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);

        wallet_amount_tv=findViewById(R.id.wallet_amount_tv);
        credit_tv=findViewById(R.id.credit_tv);
        debit_tv=findViewById(R.id.debit_tv);
        sp_state=findViewById(R.id.sp_state);
//        sp_plan=findViewById(R.id.sp_plan);
        btn_submit=findViewById(R.id.btn_submit);
        raise_add_tv=findViewById(R.id.raise_add_tv);
        wallet_histroy = findViewById(R.id.wallet_histroy);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        coin_tv=findViewById(R.id.coin_tv);

        threemonth_rl = findViewById(R.id.threemonth_rl);
        sixmonth_rl=findViewById(R.id.sixmonth_rl);
        details_cv=findViewById(R.id.details_cv);

        /* sp_plan.setOnItemSelectedListener(this);
        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        sp_plan.setAdapter(ad);
*/

        threemonth_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threemonth_rl.setBackgroundResource(R.color.blue);
                sixmonth_rl.setBackgroundResource(R.color.white);

                arrayList4.clear();
                getHistory("3");
            }
        });
        sixmonth_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixmonth_rl.setBackgroundResource(R.color.blue);
                threemonth_rl.setBackgroundResource(R.color.white);
                arrayList4.clear();
                getHistory("6");
            }
        });


        GetCategory();
       /* btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv3.getText().toString().equalsIgnoreCase("Deposit Amount :- NA")){
                    tv3.setError("Data not found!");
                }
                else if (tv4.getText().toString().equalsIgnoreCase("Maturity Amount :- NA")){
                    tv4.setError("Data not found!");
                }
                else {
                    SubmitPurchaseAPI();
                }
            }
        });*/

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        WalletAPI("",UserId);
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        }
                    }

//                    CityName.clear();
                    arrayList4.clear();
                    getHistory(stateid);
                    GetAllPlanDetailsByPlanCodeDetails(stateid);
                }
                if (position == 0) {
//                    CityName.clear();

                }
/*
                if (id == 0) {
                    CityName.clear();
                    ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, spinnercity);
                    sp_city.setAdapter(spinnerCity);
                }
*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GetWalletAPI();
    }

    void GetCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetFDPlanList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                Log.v("addadada", response);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName.add("Select Package");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("PlanCode"));
                        String statename = jsonObject1.getString("Plan_Name");
                        String planDuration = jsonObject1.getString("PlanDuration");
                        String depositAmo = jsonObject1.getString("DepositAmo");
                        StateName.add(statename);
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(FixedDepositPlan.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FixedDepositPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanType", "FD");
//                params.put("DepositAmount", dipamount);
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FixedDepositPlan.this);
        requestQueue.add(stringRequest);
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
                progressDialog1.dismiss();
                Toast.makeText(FixedDepositPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FixedDepositPlan.this);
        requestQueue.add(stringRequest);

    }

/*
    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id)
    {
        StateName.clear();

        GetCategory(courses[position]);
        // make toastof name of course
        // which is selected in spinner
//        Toast.makeText(getApplicationContext(), courses[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView arg0)
    {
        // Auto-generated method stub
    }*/



    public void getHistory(final String dur) {
        final ProgressDialog progressDialog = new ProgressDialog(FixedDepositPlan.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"New_FDDDPlanList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REsponse_Data", response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("PlanCode", jsonObject1.getString("PlanCode"));
                        hm.put("Plan_Name", jsonObject1.getString("Plan_Name"));
                        hm.put("PlanDuration", jsonObject1.getString("PlanDuration"));
                        hm.put("DepositAmo", jsonObject1.getString("DepositAmo"));
                        hm.put("MaturityAmo", jsonObject1.getString("MaturityAmo"));
                        hm.put("PlanDay", jsonObject1.getString("PlanDay"));
                        hm.put("TotalDepositAmo", jsonObject1.getString("TotalDepositAmo"));
                        arrayList4.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(FixedDepositPlan.this, 3, GridLayoutManager.VERTICAL, false);
                    DDPlanAmountAdapter customerListAdapter = new DDPlanAmountAdapter(FixedDepositPlan.this, arrayList4);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(FixedDepositPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanType","FD");
                params.put("PlanDuration",dur);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FixedDepositPlan.this);
        requestQueue.add(stringRequest);
    }




    public class DDPlanAmountAdapter extends RecyclerView.Adapter<DDPlanAmountAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;

        public DDPlanAmountAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public DDPlanAmountAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_amountlist, viewGroup, false);
            DDPlanAmountAdapter.VH viewHolder = new DDPlanAmountAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final DDPlanAmountAdapter.VH vh, final int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.cat_btn.setText(arrayList.get(i).get("DepositAmo"));
//        Animation animBlink = AnimationUtils.loadAnimation(context,
//                R.anim.blink);
//        vh.cat_name.startAnimation(animBlink);

//        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
////            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
//        }
            //Set on click listener for each item view





            vh.cat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    details_cv.setVisibility(View.VISIBLE);

                    if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_btn ) ) {
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.yellow) );
                    }
                    else{
                        previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.white) );
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.yellow) );
                    }
                    previousSeletedButton = vh.cat_btn;

                    GetAllPlanDetailsByPlanCodeDetails(arrayList.get(i).get("PlanCode"));


                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tv3.getText().toString().equalsIgnoreCase("Deposit Amount :- NA")){
                                tv3.setError("Data not found!");
                            }
                            else if (tv4.getText().toString().equalsIgnoreCase("Maturity Amount :- NA")){
                                tv4.setError("Data not found!");
                            }
                            else {
                                SubmitPurchaseAPI(arrayList.get(i).get("PlanCode"));
                            }
                        }
                    });

//                    arrayList2.clear();
//                    arrayList1.clear();
////                    customerListAdapter.notifyDataSetChanged();
////                    customerListAdapter1.notifyDataSetChanged();
//                    getHistory2(arrayList.get(i).get("Subjectcode"),arrayList.get(i).get("BatchId"));
//                    getHistory1(arrayList.get(i).get("Subjectcode"),arrayList.get(i).get("BatchId"));

                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            AppCompatButton cat_btn;
            public VH(@NonNull View itemView) {
                super(itemView);
                cat_btn = itemView.findViewById(R.id.cat_btn);
            }
        }
    }


    public void GetAllPlanDetailsByPlanCodeDetails(final String plancode) {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetAllPlanDetailsByPlanCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("12", response);

//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            deposite_tv=jsonObject.getString("DepositAmo");
                            maturity_tv=jsonObject.getString("MaturityAmo");
                            tv1.setText("Plan Name:- "+jsonObject.getString("Plan_Name"));
                            tv2.setText("Plan Duration:- "+jsonObject.getString("PlanDuration")+" Month");
                            tv3.setText("Deposit Amount:- \u20B9 "+jsonObject.getString("DepositAmo"));
                            tv4.setText("Maturity Amount:- "+jsonObject.getString("MaturityAmo"));
                            tv5.setText("Plan Day:- "+jsonObject.getString("PlanDay")+" Days");
                            tv6.setText("Total Deposit Amount:- \u20B9"+jsonObject.getString("TotalDepositAmo"));
                            tv7.setText("Profit Amount:- \u20B9"+jsonObject.getString("ProfitAmount"));
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
                Toast.makeText(FixedDepositPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanCode", plancode);
                params.put("DepositAmount", "0");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FixedDepositPlan.this);
        requestQueue.add(stringRequest);

    }

    public void SubmitPurchaseAPI(final String pcode) {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PurchasePlan", new Response.Listener<String>() {
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
                            Toast.makeText(FixedDepositPlan.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(intent);

                        }
                    } else {
                        Toast.makeText(FixedDepositPlan.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(FixedDepositPlan.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("PlanCode", pcode);
                params.put("Member_Id", UserId);
                params.put("DepositAmount", deposite_tv);
                params.put("MaturityAmount", maturity_tv);
                params.put("IsDeductWallet", "0");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FixedDepositPlan.this);
        requestQueue.add(stringRequest);

    }
}
