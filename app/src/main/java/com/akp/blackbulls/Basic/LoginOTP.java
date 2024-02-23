package com.akp.blackbulls.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.blackbulls.Dashboard.AllDDPlan;
import com.akp.blackbulls.Profile.MyProfile;
import com.akp.blackbulls.R;
import com.akp.blackbulls.Recharge.SenderOTPVerify;
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

import java.util.HashMap;
import java.util.Map;

public class LoginOTP extends AppCompatActivity {
    Button btnSubmit;
    EditText mobile_number;
    EditText ref_et;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    CheckBox chk;
    RelativeLayout ref_rl;
    TextView checkref_img;
    String Getreferral_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_o_t_p);
        Getreferral_id=getIntent().getStringExtra("referral_id");
        btnSubmit = findViewById(R.id.btnSubmit);
        mobile_number = findViewById(R.id.mob_et);
        ref_et=findViewById(R.id.ref_et);
        ref_rl=findViewById(R.id.ref_rl);
        checkref_img=findViewById(R.id.checkref_img);

        if (Getreferral_id == null){
        }
        else {
            checkref_img.setText("Verified Referral");
            checkref_img.setTextColor(Color.GREEN);
            ref_et.setClickable(false);
            ref_et.setFocusable(false);
        }

        ref_et.setText(Getreferral_id);


        chk = (CheckBox) findViewById(R.id.chk1);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    ref_rl.setVisibility(View.VISIBLE);
                }
                else{
                    ref_rl.setVisibility(View.GONE);
                }
            }
        });

        ref_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile_number.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter the mobile no.",Toast.LENGTH_SHORT).show();
                else if(mobile_number.getText().length()<10)
                    Toast.makeText(getApplicationContext(),"Please enter correct mobile no.",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getApplicationContext(), SenderOTPVerify.class);
                    startActivity(intent);
//                    if (ref_et.getText().toString().equalsIgnoreCase("")){
//                        sendOtp("");
//                    }
//                    else {
//                        sendOtp(ref_et.getText().toString());
//                    }
                }

            }
        });

    }
    public void sendOtp(String refid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "MobileLogin", new Response.Listener<String>() {
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
                            Toast.makeText(LoginOTP.this, "Verify Your OTP!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SenderOTPVerify.class);
                            intent.putExtra("send_otp",jsonObject.getString("OTP"));
                            intent.putExtra("send_mob",jsonObject.getString("MobileNo"));
                            intent.putExtra("ref",ref_et.getText().toString());
                            startActivity(intent);
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
                Toast.makeText(LoginOTP.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",mobile_number.getText().toString());
                params.put("Action","");
                params.put("Otp","");
                params.put("RefrerralId",refid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginOTP.this);
        requestQueue.add(stringRequest);
    }



    public void verifyReferralAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"VerifyMemberRefrerral", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            checkref_img.setTextColor(Color.GREEN);
                            checkref_img.setText(jsonobject.getString("Msg"));
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        checkref_img.setTextColor(Color.RED);
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                        checkref_img.setText(object.getString("Message"));
                        ref_et.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginOTP.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",ref_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginOTP.this);
        requestQueue.add(stringRequest);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        verifyReferralAPI();
    }
}