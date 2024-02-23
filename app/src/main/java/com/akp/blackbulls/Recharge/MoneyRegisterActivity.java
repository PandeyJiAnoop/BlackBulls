package com.akp.blackbulls.Recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost1;


public class MoneyRegisterActivity extends AppCompatActivity {
    ImageView ivBack;
    EditText fna_et,last_name_et,pincode_et,add_et,state_et;
    RelativeLayout submit_et;
    String getMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_register);
        getMobile=getIntent().getStringExtra("mobile");
        ivBack=findViewById(R.id.ivBack);
        submit_et=findViewById(R.id.submit_et);
        fna_et=findViewById(R.id.fna_et);
        last_name_et=findViewById(R.id.last_name_et);
        pincode_et=findViewById(R.id.pincode_et);
        add_et=findViewById(R.id.add_et);
        state_et=findViewById(R.id.state_et);

        submit_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fna_et.getText().toString().equalsIgnoreCase("")){
                    fna_et.setError("Fields can't be blank!");
                    fna_et.requestFocus();
                }
                else if (last_name_et.getText().toString().equalsIgnoreCase("")){
                    last_name_et.setError("Fields can't be blank!");
                    last_name_et.requestFocus();
                }
                else if (pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_et.setError("Fields can't be blank!");
                    pincode_et.requestFocus();
                }
                else if (add_et.getText().toString().equalsIgnoreCase("")){
                    add_et.setError("Fields can't be blank!");
                    add_et.requestFocus();
                }
                else if (state_et.getText().toString().equalsIgnoreCase("")){
                    state_et.setError("Fields can't be blank!");
                    state_et.requestFocus();
                }
                else {
                    AddSenderDetails(add_et.getText().toString(),fna_et.getText().toString(),last_name_et.getText().toString(),getMobile,pincode_et.getText().toString(),state_et.getText().toString());
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void AddSenderDetails(String add, String fname, String lname, String mob, String pincode, String state) {
        String otp1 = new GlobalAppApis().PostAddSenderAPI(add,fname,lname,mob,pincode,state);
        ApiService client1 = getApiClient_ByPost1();
        Call<String> call1 = client1.AddSenderAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Status").equalsIgnoreCase("true")){
                        Intent intent=new Intent(getApplicationContext(),SenderOTPVerify.class);
                        intent.putExtra("send_otp",jsonObject.getString("Message"));
                        intent.putExtra("send_mob",getMobile);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MoneyRegisterActivity.this, call1, "", true); }
}