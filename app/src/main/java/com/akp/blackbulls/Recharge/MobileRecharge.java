package com.akp.blackbulls.Recharge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;


public class MobileRecharge extends AppCompatActivity {
    ImageView ivBack;
    LinearLayout provider_ll;
    TextView provoider_et;
    String service_name,UserId,getcode;
    String getProvider_id,roletype;
    AppCompatButton btnSubmit;
    private SharedPreferences sharedPreferences;
    EditText mob_et,amount_et;
    AlertDialog alertDialog;
    String get_operator_ref,get_payid,getonlyservice;
    TextView title_tv,numb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        title_tv=findViewById(R.id.title_tv);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        service_name=getIntent().getStringExtra("servicename");
        getProvider_id=getIntent().getStringExtra("provider_id");
        getonlyservice=getIntent().getStringExtra("onlyservice");
        getcode=getIntent().getStringExtra("Code");
//        Toast.makeText(getApplicationContext(), getcode,Toast.LENGTH_LONG).show();
        numb=findViewById(R.id.numb);

        btnSubmit=findViewById(R.id.btnSubmit);
        ivBack=findViewById(R.id.ivBack);
        provider_ll=findViewById(R.id.provider_ll);
        provoider_et=findViewById(R.id.provoider_et);
        mob_et=findViewById(R.id.mob_et);
        amount_et=findViewById(R.id.amount_et);
        title_tv.setText(service_name);

//        mob_et.setText(Mobile);
        if (getonlyservice.equalsIgnoreCase("11")){
            mob_et.setHint("Enter your mobile number");
            numb.setVisibility(View.VISIBLE);
            mob_et.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else {
            mob_et.setHint("Enter your Unique Id Number");
            numb.setVisibility(View.GONE);
            mob_et.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if(service_name ==null){
        }
        else {
            provoider_et.setText(service_name);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setEnabled(false);
                Payment(amount_et.getText().toString(),"19",mob_et.getText().toString(),UserId,getcode);
                //      do your work here
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSubmit.setEnabled(true);
                            }
                        });
                    }
                }, 7000); // delay button enable for 0.5 sec
            }
        });

       /* btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dfsdgfdhaf",amount_et.getText().toString()+"19"+mob_et.getText().toString()+UserId+getcode);
            }

        });*/
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

/*
    public void PAYMENTAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(MobileRecharge.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://msc.mscpaycash.com/api/telecom/v1/payment?api_token=98bUAIRRHAH1klt3Nr5hk2hRSQQqAv60uiezDnXOw980C9vTTzVWjSAvdPZk&number="+mob_et.getText().toString()+"&amount="+amount_et.getText().toString()+"&provider_id=9"+"&client_id="+UserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){
                        get_operator_ref=jsonObject.getString("operator_ref");
                        get_payid=jsonObject.getString("payid");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MobileRecharge.this);
        requestQueue.add(stringRequest);
    }
*/

    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MobileRecharge.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
        TextView id_tv= dialogView.findViewById(R.id.id_tv);
        TextView pass_tv= dialogView.findViewById(R.id.pass_tv);
        id_tv.setText("Operator_ref- "+get_operator_ref+" ("+UserId+")");
//        pass_tv.setText("Payid- "+get_payid);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void Payment(String Amount, String Circle, String MobileNo, String UserId,String optr) {
        String otp1 = new GlobalAppApis().PostRechargeAPI(Amount,Circle,MobileNo,UserId,optr);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.PostRecharge(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Status").equalsIgnoreCase("true")){
                        get_operator_ref=jsonObject.getString("Message");
//                        get_payid=jsonObject.getString("payid");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MobileRecharge.this, call1, "", true);

    }

}