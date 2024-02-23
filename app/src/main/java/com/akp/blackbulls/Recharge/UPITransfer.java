package com.akp.blackbulls.Recharge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

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


public class UPITransfer extends AppCompatActivity {
    ImageView back_img;
    TextView tag_tv,upiname_tv,title_name_tv;
    EditText rupee_et;
    EditText edt_msg,upi_et;
    AppCompatButton buttonOk;
    String getUPIId,UserId,UserName,UserMob,Getmoneytype;
    CardView upi_id_cv;
    AlertDialog alertDialog;
    private String get_operator_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_i_transfer);
        SharedPreferences sharedPreferences1 = getSharedPreferences("res_preference",MODE_PRIVATE);
        UserMob= sharedPreferences1.getString("U_mob", "");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        getUPIId=getIntent().getStringExtra("upiid");

        Getmoneytype=getIntent().getStringExtra("moneytype");
        upi_id_cv=findViewById(R.id.upi_id_cv);
        upi_et=findViewById(R.id.upi_et);


//        Log.d("scsddf",""+UserId+UserName+UserMob+getUPIId);

        back_img=findViewById(R.id.back_img);
        tag_tv=findViewById(R.id.tag_tv);
        upiname_tv=findViewById(R.id.upiname_tv);
        rupee_et=findViewById(R.id.rupee_et);
        edt_msg=findViewById(R.id.edt_msg);
        title_name_tv=findViewById(R.id.title_name_tv);
        title_name_tv.setText(UserName);

        buttonOk=findViewById(R.id.buttonOk);



        if (Getmoneytype.equalsIgnoreCase("qr")){
            if (getUPIId ==  null){
                finish();
            }
            else {
                tag_tv.setText(getUPIId);
                upiname_tv.setText(getUPIId);

            }
            upi_id_cv.setVisibility(View.VISIBLE);
            upi_et.setVisibility(View.GONE);



            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonOk.setEnabled(false);
                    if (rupee_et.getText().toString().equalsIgnoreCase("")){
                        rupee_et.setError("Fields can't be blank!");
                        rupee_et.requestFocus();
                    }
                    else {
                        UPITransferAPI(rupee_et.getText().toString(),UserMob,UserName,getUPIId,UserId);
                    }                    //      do your work here
                    Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonOk.setEnabled(true);
                                }
                            });
                        }
                    }, 7000); // delay button enable for 0.5 sec
                }
            });




//            buttonOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (rupee_et.getText().toString().equalsIgnoreCase("")){
//                        rupee_et.setError("Fields can't be blank!");
//                        rupee_et.requestFocus();
//                    }
//                    else {
//                        UPITransferAPI(rupee_et.getText().toString(),UserMob,UserName,getUPIId,UserId);
//                    }
//                }
//            });



        }
        else {
            upi_id_cv.setVisibility(View.GONE);
            upi_et.setVisibility(View.VISIBLE);


            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonOk.setEnabled(false);
                    if (rupee_et.getText().toString().equalsIgnoreCase("")){
                        rupee_et.setError("Fields can't be blank!");
                        rupee_et.requestFocus();
                    }
                    else if (upi_et.getText().toString().equalsIgnoreCase("")){
                        upi_et.setError("Fields can't be blank!");
                        upi_et.requestFocus();
                    }
                    else {
                        UPITransferAPI(rupee_et.getText().toString(),UserMob,UserName,upi_et.getText().toString(),UserId);
                    }                    Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonOk.setEnabled(true);
                                }
                            });
                        }
                    }, 7000); // delay button enable for 0.5 sec
                }
            });



         /*   buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rupee_et.getText().toString().equalsIgnoreCase("")){
                        rupee_et.setError("Fields can't be blank!");
                        rupee_et.requestFocus();
                    }
                    else if (upi_et.getText().toString().equalsIgnoreCase("")){
                        upi_et.setError("Fields can't be blank!");
                        upi_et.requestFocus();
                    }
                    else {
                        UPITransferAPI(rupee_et.getText().toString(),UserMob,UserName,upi_et.getText().toString(),UserId);
                    }
                }
            });*/
        }


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    public void  UPITransferAPI(String amount, String custmob, String custname,String upiid,String userid){
        String otp1 = new GlobalAppApis().UPITransfer(amount,custmob,custname,upiid,userid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getUPITransfer(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("sdsdsd","sdsfd"+result);

                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")){
                        get_operator_ref=object.getString("Message");
//                        get_payid=jsonObject.getString("payid");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } }}, UPITransfer.this, call1, "", true);
    }



    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(UPITransfer.this).inflate(R.layout.successfullycreated_popup_upi, viewGroup, false);
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
}
