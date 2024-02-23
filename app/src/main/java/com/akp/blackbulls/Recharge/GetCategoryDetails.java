package com.akp.blackbulls.Recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;


public class GetCategoryDetails extends AppCompatActivity {
    ImageView ivBack;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String getServicename,getId;
    TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_category_details);
        getServicename=getIntent().getStringExtra("service_name");
        getId=getIntent().getStringExtra("ProviderId");
        title_tv=findViewById(R.id.title_tv);
        title_tv.setText(getServicename+" Services");
        ivBack=findViewById(R.id.ivBack);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHistory("",getId);
    }


    public void  getHistory(String service,String providerid){
        String otp1 = new GlobalAppApis().Operator(service,providerid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OperatorService(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
//                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
//                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
//                jsonString = jsonString.replace("</string>"," ");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ProviderRes");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("OperatorName", jsonObject1.getString("OperatorName"));
                        hm.put("Image", jsonObject1.getString("Image"));
                        hm.put("OperatorId", jsonObject1.getString("OperatorId"));
                        hm.put("ProviderId", jsonObject1.getString("ProviderId"));
                        hm.put("Code", jsonObject1.getString("Code"));
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(GetCategoryDetails.this, 3);
                    CategoryServiceListAdapter customerListAdapter = new CategoryServiceListAdapter(GetCategoryDetails.this, arrayList1);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, GetCategoryDetails.this, call1, "", true);
    }

}