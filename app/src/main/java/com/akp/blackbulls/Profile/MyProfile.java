package com.akp.blackbulls.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.LoginScreen;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.MPin.MainActivity;
import com.akp.blackbulls.R;
import com.akp.blackbulls.Walet.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;


public class MyProfile extends AppCompatActivity {
 ImageView menuImg;
 String UserId;
 EditText sponser_id_et,name_et,mobile_et,email_et,gender_et,ACCOUNTNO_et,BANKNAME_et,ACCOUNTHOLDER_et,IFSC_et;
AppCompatButton btn_submit;
LinearLayout KYCUpdate_ll;

    ImageView img_showProfile;
    EditText mob_et;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    CircularImageView profule;
    String temp;
    AlertDialog alertDialog1;
    TextView kycststus_tv;
    ImageView checkifsc_img;
    EditText branch_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        sponser_id_et=findViewById(R.id.sponser_id_et);
        name_et=findViewById(R.id.name_et);
        mobile_et=findViewById(R.id.mobile_et);
        email_et=findViewById(R.id.email_et);
        gender_et=findViewById(R.id.gender_et);

        ACCOUNTNO_et=findViewById(R.id.ACCOUNTNO_et);
        BANKNAME_et=findViewById(R.id.BANKNAME_et);
        ACCOUNTHOLDER_et=findViewById(R.id.ACCOUNTHOLDER_et);
        KYCUpdate_ll=findViewById(R.id.KYCUpdate_ll);
        IFSC_et=findViewById(R.id.IFSC_et);
        profule=findViewById(R.id.profule);
        kycststus_tv=findViewById(R.id.kycststus_tv);
        branch_et=findViewById(R.id.branch_et);
        checkifsc_img=findViewById(R.id.checkifsc_img);



        profule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateprofilepopup();
            }
        });


        IFSC_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });



     /*   checkifsc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIFSCAPI();
            }
        });*/

        btn_submit=findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ACCOUNTNO_et.getText().toString().equalsIgnoreCase("")){
                    ACCOUNTNO_et.setError("Fields can't be blank!");
                    ACCOUNTNO_et.requestFocus();
                }
                else if (BANKNAME_et.getText().toString().equalsIgnoreCase("")){
                    BANKNAME_et.setError("Fields can't be blank!");
                    BANKNAME_et.requestFocus();
                }
               else if (ACCOUNTHOLDER_et.getText().toString().equalsIgnoreCase("")){
                    ACCOUNTHOLDER_et.setError("Fields can't be blank!");
                    ACCOUNTHOLDER_et.requestFocus();
                }
               else if (IFSC_et.getText().toString().equalsIgnoreCase("")){
                    IFSC_et.setError("Fields can't be blank!");
                    IFSC_et.requestFocus();
                }
                else {
                    UpdateProfile();
                }
            }
        });



        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
            }
        });
        ProfileAPI();
    }



    public void ProfileAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsdfsdsdsds",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String UserID       = jsonobject.getString("UserID");
                            String MemberName    = jsonobject.getString("MemberName");
                            String MobileNo      = jsonobject.getString("MobileNo");
                            String EmaiLID       = jsonobject.getString("EmaiLID");
                            String RefrerralId       = jsonobject.getString("RefrerralId");
                            String RegDate    = jsonobject.getString("RegDate");

                            String bankName      = jsonobject.getString("BankName");
                            String ifscCode       = jsonobject.getString("IfscCode");
                            String accountNumber       = jsonobject.getString("AccountNumber");
                            String accountHolderName    = jsonobject.getString("AccountHolderName");
                            String ProfilePic    = jsonobject.getString("ProfilePic");

                            String AadharKYC    = jsonobject.getString("AadharKYC");
                            String PanKYC    = jsonobject.getString("ProfilePic");

                            String BranchName = jsonobject.getString("BranchName");











                            if (AadharKYC.equalsIgnoreCase("1")){
                                kycststus_tv.setText("Done");
                                kycststus_tv.setTextColor(Color.GREEN);
                                KYCUpdate_ll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getApplicationContext(),ViewAdharPan.class);
                                        startActivity(intent);
                                    }
                                });

                            }
                            else if (PanKYC.equalsIgnoreCase("1")){
                                kycststus_tv.setText("Done");
                                kycststus_tv.setTextColor(Color.GREEN);
                                KYCUpdate_ll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getApplicationContext(),ViewAdharPan.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else {
                                KYCUpdate_ll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getApplicationContext(),PanVerification.class);
                                        startActivity(intent);
                                    }
                                });

                            }


                            if (ProfilePic.equalsIgnoreCase("")){
                                Toast.makeText(getApplicationContext(),"Image not found!",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Glide.with(getApplicationContext()).load(ProfilePic).error(R.drawable.logo).into(profule);
                            }



                           /* if (UserID.equalsIgnoreCase("")){
                            }
                            if (UserID.equalsIgnoreCase("")){
                            }
                            if (UserID.equalsIgnoreCase("")){
                            }
                            if (UserID.equalsIgnoreCase("")){
                            }*/



                            if (MemberName.equalsIgnoreCase("")){
                            }
                            else {
                                name_et.setEnabled(false);
                                name_et.setClickable(false);
                            }
                            if (MobileNo.equalsIgnoreCase("")){
                            }
                            else {
                                mobile_et.setEnabled(false);
                                mobile_et.setClickable(false);
                            }
                            if (EmaiLID.equalsIgnoreCase("")){

                            }
                            else {
                                email_et.setEnabled(false);
                                email_et.setClickable(false);
                            }
                            if (RegDate.equalsIgnoreCase("")){
                            }
                            else {
                                gender_et.setEnabled(false);
                                gender_et.setClickable(false);
                            }
                            if (accountNumber.equalsIgnoreCase("")){
                            }
                            else {
                                ACCOUNTNO_et.setEnabled(false);
                                ACCOUNTNO_et.setClickable(false);
                            }

                            if (bankName.equalsIgnoreCase("")){
                            }
                            else {
                                BANKNAME_et.setEnabled(false);
                                BANKNAME_et.setClickable(false);
                            }
                            if (accountHolderName.equalsIgnoreCase("")){
                            }
                            else {
                                ACCOUNTHOLDER_et.setEnabled(false);
                                ACCOUNTHOLDER_et.setClickable(false);
                            }
                            if (ifscCode.equalsIgnoreCase("")){
                            }
                            else {
                                IFSC_et.setEnabled(false);
                                IFSC_et.setClickable(false);
                            }

                            if (BranchName.equalsIgnoreCase("")){
                            }
                            else {
                                branch_et.setEnabled(false);
                                branch_et.setClickable(false);
                            }



                            if (name_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                name_et.setEnabled(false);
                                name_et.setClickable(false);
                            }
                            if (mobile_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                mobile_et.setEnabled(false);
                                mobile_et.setClickable(false);
                            }
                            if (email_et.getText().toString().equalsIgnoreCase("N/A")){

                            }
                            else {
                                email_et.setEnabled(false);
                                email_et.setClickable(false);
                            }
                            if (gender_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                gender_et.setEnabled(false);
                                gender_et.setClickable(false);
                            }
                            if (ACCOUNTNO_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                ACCOUNTNO_et.setEnabled(false);
                                ACCOUNTNO_et.setClickable(false);
                            }

                            if (BANKNAME_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                BANKNAME_et.setEnabled(false);
                                BANKNAME_et.setClickable(false);
                            }
                            if (ACCOUNTHOLDER_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                ACCOUNTHOLDER_et.setEnabled(false);
                                ACCOUNTHOLDER_et.setClickable(false);
                            }
                            if (IFSC_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                IFSC_et.setEnabled(false);
                                IFSC_et.setClickable(false);
                            }

                            if (branch_et.getText().toString().equalsIgnoreCase("N/A")){
                            }
                            else {
                                branch_et.setEnabled(false);
                                branch_et.setClickable(false);
                            }





                            sponser_id_et.setText(UserID);
                            name_et.setText(MemberName);
                            mobile_et.setText(MobileNo);
                            email_et.setText(EmaiLID);
                            gender_et.setText(RegDate);


                            ACCOUNTNO_et.setText(accountNumber);
                            BANKNAME_et.setText(bankName);
                            ACCOUNTHOLDER_et.setText(accountHolderName);
                            IFSC_et.setText(ifscCode);
                            branch_et.setText(BranchName);

                        }
                    }
                    else {
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
                Toast.makeText(MyProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);
    }


    public void UpdateProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileUpdate", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
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
                Toast.makeText(MyProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                params.put("MemberName",name_et.getText().toString());
                params.put("MobileNo",mobile_et.getText().toString());
                params.put("EmialId",email_et.getText().toString());
                params.put("AccountNumber",ACCOUNTNO_et.getText().toString());
                params.put("BankName",BANKNAME_et.getText().toString());
                params.put("AccountHolderName",ACCOUNTHOLDER_et.getText().toString());
                params.put("IfscCode",IFSC_et.getText().toString());
                params.put("BranchName",branch_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);
    }





    private void updateprofilepopup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup1 = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView1 = LayoutInflater.from(MyProfile.this).inflate(R.layout.uploadprofile, viewGroup1, false);
        RelativeLayout ok1 = (RelativeLayout) dialogView1.findViewById(R.id.update_rl);
        img_showProfile=dialogView1.findViewById(R.id.imageView);
        img_showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp == null){
                    UpdateProfileAPI("");

                }
                else {
                    UpdateProfileAPI(temp);

                }

//                alertDialog1.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView1);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(MyProfile.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        img_showProfile.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }

    public void UpdateProfileAPI(final String imgpath) {
        final ProgressDialog progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Member_ProfilePicUpdate", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            alertDialog1.dismiss();
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
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
                Toast.makeText(MyProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                params.put("ProfilePic", imgpath);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);


    }




    public void CheckIFSCAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ifsc.razorpay.com/"+IFSC_et.getText().toString(), new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    branch_et.setText(object.getString("BRANCH"));
                    BANKNAME_et.setText(object.getString("BANK"));
                    checkifsc_img.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyProfile.this, "IFSC Code Invalid", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        CheckIFSCAPI();
    }
}