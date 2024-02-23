package com.akp.blackbulls.EMIReport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;

public class EMIChart extends AppCompatActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrRepaymentList = new ArrayList<>();
    RelativeLayout rlHeader;
    String UserId, getAccountNo;
    private SharedPreferences sharedPreferences;
    double totalPrice = 0, LateStatus = 0;
    Button btnCalculate;
    TextView tvAmountPaid, tvLateFee, tvPaymentMode;
    EditText tvPaidAmount;
    LinearLayout llCalculateDate;
    Button btnSave;
    Spinner spinner;
    JSONArray array = new JSONArray();
    private ArrayList<String> arrRepaymentListt = new ArrayList<String>();
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private EMIChart.FriendsListAdapter pdfAdapTer;
    String[] FootBallPlayers = new String[]{"Select Payment Mood", "Cash", "Cheque", "Online Payment"};
    String selectedName="";
    LinearLayout llcheck,llRefrence;
    EditText edtChequeNo,edtRefernceNumber;
    TextView tvChequeDate;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarr = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }
    };
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendarr.set(Calendar.YEAR, year);
            myCalendarr.set(Calendar.MONTH, monthOfYear);
            myCalendarr.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel1();
        }
    };  ImageView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_m_i_chart2);
        rcvMemberLoanDetails = findViewById(R.id.rcgEmi);
        rlHeader = findViewById(R.id.rlHeader);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvLateFee = findViewById(R.id.tvLateFee);
        spinner = findViewById(R.id.spinner);
        tvPaidAmount = findViewById(R.id.tvPaidAmount);
        llCalculateDate = findViewById(R.id.llCalculateDate);
        btnSave = findViewById(R.id.btnSave);
        llcheck = findViewById(R.id.llcheck);
        llRefrence = findViewById(R.id.llRefrence);
//        tvDate = findViewById(R.id.tvDate);
        tvChequeDate = findViewById(R.id.tvChequeDate);
        edtChequeNo = findViewById(R.id.edtChequeNo);
        edtRefernceNumber = findViewById(R.id.edtRefernceNumber);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        
        getAccountNo=getIntent().getStringExtra("acno");
        norecord_tv=findViewById(R.id.norecord_tv);
        EMIChartListAPI();

        final List<String> plantsList = new ArrayList<>(Arrays.asList(FootBallPlayers));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        spinner.setAdapter(spinnerArrayAdapter);
        tvChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EMIChart.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

       /* tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EMIChart.this, date1, myCalendarr
                        .get(Calendar.YEAR), myCalendarr.get(Calendar.MONTH),
                        myCalendarr.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().trim() == "Select Payment Mood") {
                } else {
                    selectedName = String.valueOf(spinner.getSelectedItem());
                    if(selectedName.equalsIgnoreCase("Cash"))
                    {
                        llcheck.setVisibility(View.GONE);
                        llRefrence.setVisibility(View.GONE);
                    }
                    else if(selectedName.equalsIgnoreCase("Cheque"))
                    {
                        llcheck.setVisibility(View.VISIBLE);
                        llRefrence.setVisibility(View.GONE);
                    }
                    else if(selectedName.equalsIgnoreCase("Online Payment"))

                    {
                        llcheck.setVisibility(View.GONE);
                        llRefrence.setVisibility(View.VISIBLE);
                    }
                    Log.v( "easd", selectedName );


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );






        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openpaymentdetail(advanceAmt, extraPer, totalPrice, LateStatus);
            }
        });

    }



    public void PaymentaddEMI() {
        String otp1 = new GlobalAppApis().PaymentaddEMI(arrRepaymentListt,getAccountNo);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.PaymentaddEMI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        Toast.makeText(EMIChart.this, "EMI Add Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                    else {
                        Toast.makeText(EMIChart.this, "Something Wrong try again...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, EMIChart.this, call1, "", true);

    }



    public void SelectSpinnerValue(View view) {
        spinner.setSelection(2);
    }
    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        tvDate.setText(sdf.format(myCalendarr.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvChequeDate.setText(sdf.format(myCalendar.getTime()));
    }





    public void EMIChartListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PackagePurchase_EMI_Detail", new Response.Listener<String>() {
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
                            hashlist.put("Member_Id", jsonObject.getString("Member_Id"));
                            hashlist.put("Account_No", jsonObject.getString("Account_No"));
                            hashlist.put("Payment_status", jsonObject.getString("Payment_status"));
                            hashlist.put("Payment_given_date", jsonObject.getString("Payment_given_date"));
                            hashlist.put("Instalment_no", jsonObject.getString("Instalment_no"));
                            hashlist.put("InsDate", jsonObject.getString("InsDate"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new EMIChart.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvMemberLoanDetails.setLayoutManager(layoutManager);
                        rcvMemberLoanDetails.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(EMIChart.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EMIChart.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("AccountNo", getAccountNo);
                params.put("Member_Id", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(EMIChart.this);
        requestQueue.add(stringRequest);
    }

    public class FriendsListAdapter extends RecyclerView.Adapter<EMIChart.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public EMIChart.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EMIChart.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final EMIChart.FriendsList holder, final int position) {
            holder.Payment_statustv.setText(data.get(position).get("Payment_status"));
            holder.Payment_given_datetv.setText(data.get(position).get("Payment_given_date"));
            holder.Instalment_notv.setText(data.get(position).get("Instalment_no"));
            holder.InsDatetv.setText(data.get(position).get("InsDate"));
            holder.tvEMIAmount.setText(data.get(position).get("Amount"));
//            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);

//

            if (data.get(position).get("Payment_status").equalsIgnoreCase("Paid")){
                holder.checkbox.setChecked(true);
                holder.checkbox.setClickable(false);
                if (holder.checkbox.isChecked())
                {
                    holder.checkbox.setEnabled(false);
                }
            }



            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.checkbox.isChecked()) {

                        totalPrice += Double.parseDouble(data.get(position).get("Amount"));
                        arrRepaymentListt.add(data.get(position).get("Instalment_no"));




                    } else {
                        totalPrice -= Double.parseDouble(data.get(position).get("Amount"));
                        arrRepaymentListt.remove(data.get(position).get("Instalment_no"));

                    }

                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentaddEMI();

//                PaymentaddEMI(edtChequeNo.getText().toString(),tvDate.getText().toString(),UserName,tvLateFee.getText().toString(),tvAmountPaid.getText().toString(),ProfarmaNo,tvChequeDate.getText().toString(),edtRefernceNumber.getText().toString());
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView Payment_statustv,Payment_given_datetv,Instalment_notv,InsDatetv,tvEMIAmount;
        CheckBox checkbox;


        public FriendsList(View itemView) {
            super(itemView);
            Payment_statustv=itemView.findViewById(R.id.Payment_statustv);
            Payment_given_datetv=itemView.findViewById(R.id.Payment_given_datetv);
            Instalment_notv=itemView.findViewById(R.id.Instalment_notv);
            InsDatetv=itemView.findViewById(R.id.InsDatetv);
            tvEMIAmount=itemView.findViewById(R.id.tvEMIAmount);
            checkbox=itemView.findViewById(R.id.checkbox);
        }
    }

}