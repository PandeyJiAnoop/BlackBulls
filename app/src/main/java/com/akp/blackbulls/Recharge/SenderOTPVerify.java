package com.akp.blackbulls.Recharge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.LoginOTP;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.MPin.MainActivity;
import com.akp.blackbulls.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost1;


public class SenderOTPVerify extends AppCompatActivity {
    TextView tvMobileNo, tvResend;
    //EditText
    EditText etCode1, etCode2, etCode3, etCode4;
    int check = 0;
    TextView tvTimerText,tvOtp;
    //Button
    Button btnSubmit;
    String Get_OTP,Mobile,ReferralCode;
    ImageView ivBack;

    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;

    LinearLayout main_ll;
    TextView tvResendOtp;
    CountDownTimer startTimer;
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_o_t_p_verify);
        tvResendOtp=findViewById(R.id.tvResendOtp);


        Get_OTP = getIntent().getStringExtra("send_otp");
        Mobile = getIntent().getStringExtra("send_mob");
        ReferralCode = getIntent().getStringExtra("ref");
//        Toast.makeText(getApplicationContext(),Get_OTP,Toast.LENGTH_LONG).show();

        ivBack=findViewById(R.id.ivBack);
        main_ll=findViewById(R.id.main_ll);

        //Button
        btnSubmit = findViewById( R.id.btnSubmit );
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //EditText
        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        etCode4 = findViewById(R.id.etCode4);
//        etCode5 = findViewById( R.id.etCode5 );
//        etCode6 = findViewById( R.id.etCode6 );
        tvOtp = findViewById( R.id.tvOtp );
        tvMobileNo = findViewById( R.id.tvMobileNo );
        tvOtp.setText("Your Otp " +Get_OTP );
        tvMobileNo.setText("+91 " +Mobile );

        etCode1.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode2.requestFocus();
            }
        } );

        etCode2.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode3.requestFocus();
            }
        } );

        etCode3.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode4.requestFocus();
            }
        } );

        etCode4.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0)
//                    etCode5.requestFocus();
            }
        } );

       /* etCode5.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode6.requestFocus();
            }
        } );

        etCode6.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        } );*/

        etCode1.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode1.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );

        etCode2.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode2.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );


        etCode3.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode3.getText().toString().trim().isEmpty()) {
                        etCode2.requestFocus();
                    } }
                return false;
            }
        } );


        etCode4.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode4.getText().toString().trim().isEmpty()) {
                        etCode3.requestFocus();
                    } }
                return false;
            }
        } );
        startTimer();

     /*   etCode5.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    if (etCode5.getText().toString().trim().isEmpty()) {
                        etCode4.requestFocus();
                    }
                }
                return false;
            }
        } );

        etCode6.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    if (etCode6.getText().toString().trim().isEmpty()) {
                        etCode5.requestFocus();
                    }
                }
                return false;
            }
        } );*/
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etCode1.getText().toString().trim() +
                        etCode2.getText().toString().trim() +
                        etCode3.getText().toString().trim() +
                        etCode4.getText().toString().trim();
                if (otp.length() != 4) {
                    AppUtils.showToastSort( getApplicationContext(),"Enter Valid OTP");
                } else {
                    Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
//                    if (ReferralCode == null){
//                        Otpverify(otp,"");
//
//                    }
//                    else {
//                        Otpverify(otp,ReferralCode);
//
//                    }
                }
            }


        });
    }
    public void Otpverify(String otp,String refcode) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "OtpVerify", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String msg       = jsonobject.getString("Msg");
                            String username    = jsonobject.getString("UserID");
                            String name      = jsonobject.getString("MobileNo");
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("U_id",username);
                            login_editor.putString("U_name",name);
                            login_editor.commit();
                            Toast.makeText(getApplicationContext(), jsonobject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent); }
                    }
                    else {
                        Snackbar.make(main_ll, object.getString("Message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SenderOTPVerify.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",Mobile);
                params.put("Action","");
                params.put("Otp",otp);
                params.put("RefrerralId",refcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SenderOTPVerify.this);
        requestQueue.add(stringRequest);
    }



    private void startTimer() {
        startTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvResendOtp.setText(String.format("( %02d SEC LEFT)", sec));
                if(sec == 1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvResendOtp.setText("( 00 SEC LEFT)");
                        }
                    }, 1000);
                }
            }

            public void onFinish() {
                tvResendOtp.setText("Resend OTP");
                tvResendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ReferralCode == null){
                            ResendOtp("");
                        }
                        else {
                            ResendOtp(ReferralCode);
                        }
                    }
                });
            }
        }.start();
    }












    public void ResendOtp(String refid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "MobileLogin", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String username=jsonObject.getString("Id");
                            String mobile=jsonObject.getString("MobileNo");
                            String otp=jsonObject.getString("OTP");
                            Toast.makeText(SenderOTPVerify.this, "Success", Toast.LENGTH_SHORT).show();
                            startTimer();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SenderOTPVerify.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",Mobile);
                params.put("Action","");
                params.put("Otp","");
                params.put("RefrerralId",refid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SenderOTPVerify.this);
        requestQueue.add(stringRequest);
    }

}