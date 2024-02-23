package com.akp.blackbulls.Basic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


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

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;

public class LoginScreen extends AppCompatActivity {
    TextView forget_tv;
    EditText user_name_et,pass_et;
    RelativeLayout btn_login;
    private PopupWindow popupWindow;
    RelativeLayout mail_rl;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    RelativeLayout btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        user_name_et=findViewById(R.id.user_name_et);
        pass_et=findViewById(R.id.pass_et);
        forget_tv=findViewById(R.id.forget_tv);
        btn_login=findViewById(R.id.btn_login);
        mail_rl=findViewById(R.id.mail_rl);
        btn_reg=findViewById(R.id.btn_reg);
        TextView textv = (TextView) findViewById(R.id.tv1);
//        textv.getPaint().setStrokeWidth(1);
//        textv.getPaint().setStyle(Paint.Style.STROKE);
        TextView textv1 = (TextView) findViewById(R.id.tv2);
//        textv1.getPaint().setStrokeWidth(1);
//        textv1.getPaint().setStyle(Paint.Style.STROKE);
       /* help_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(getApplicationContext(), CustomerRegistrtaion.class);
             startActivity(intent);
            }
        });*/

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name_et.getText().toString().equalsIgnoreCase("")){
                    user_name_et.setError("Fields can't be blank!");
                    user_name_et.requestFocus();
                }
                else if (pass_et.getText().toString().equalsIgnoreCase("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else {
//                    Toast.makeText(LoginScreen.this, "Enter passcode-  12345 ", Toast.LENGTH_SHORT).show();
                    LoginScreenAPI();
                }
            }
        });
        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpasswordpopup();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignupScreen.class);
                startActivity(intent);
            }
        });
    }

    private void forgetpasswordpopup() {
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.forget_password,null);
        ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        final EditText email_et = (EditText) customView.findViewById(R.id.email_et);
        AppCompatButton continue_btn = (AppCompatButton) customView.findViewById(R.id.continue_btn);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow.showAtLocation(mail_rl, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ForgetPassword", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res","cxc"+response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getString("Status").equalsIgnoreCase("true")){
                                    JSONArray Jarray = object.getJSONArray("Response");
                                    for (int i = 0; i < Jarray.length(); i++) {
                                        JSONObject jsonobject = Jarray.getJSONObject(i);
                                        Toast.makeText(getApplicationContext(), jsonobject.getString("Msg")+"\nPassword- "+ jsonobject.getString("Password"), Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }
                                }
                                else {
                                    Snackbar.make(mail_rl, object.getString("Message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                                    Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("MobileNo",email_et.getText().toString());
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
        popupWindow.update();

        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    public void LoginScreenAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res","cxc"+response);
                progressDialog.dismiss();
                try {
                   JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String msg       = jsonobject.getString("msg");
                            String username    = jsonobject.getString("UserName");
                            String password    = jsonobject.getString("Password");
                            String name      = jsonobject.getString("Name");
                                login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                                login_editor = login_preference.edit();
                                login_editor.putString("U_id",username);
                                login_editor.putString("U_name",name);
                                login_editor.commit();
                                Toast.makeText(getApplicationContext(), jsonobject.getString("msg"), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent); }
                    }
                    else {
                        Snackbar.make(mail_rl, object.getString("Message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();

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
                Toast.makeText(LoginScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",user_name_et.getText().toString());
                params.put("Action","");
                params.put("Password", pass_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);
        requestQueue.add(stringRequest);
    }





}