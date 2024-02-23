package com.akp.blackbulls.Dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akp.blackbulls.Adapter.CustomAdapter;
import com.akp.blackbulls.Basic.AnnouncementList;
import com.akp.blackbulls.Basic.ApiService;
import com.akp.blackbulls.Basic.AppUrls;
import com.akp.blackbulls.Basic.ConnectToRetrofit;
import com.akp.blackbulls.Basic.ContactUsScreen;
import com.akp.blackbulls.Basic.GlobalAppApis;
import com.akp.blackbulls.Basic.LoginScreen;
import com.akp.blackbulls.Basic.NetworkConnectionHelper;
import com.akp.blackbulls.Basic.NotificationList;
import com.akp.blackbulls.Basic.RetrofitCallBackListenar;
import com.akp.blackbulls.Basic.SplashScreen;
import com.akp.blackbulls.BuildConfig;
import com.akp.blackbulls.EarningReport.ReferalEarning;
import com.akp.blackbulls.EarningReport.ReferralPersonList;
import com.akp.blackbulls.EarningReport.TaskEarning;
import com.akp.blackbulls.EarningReport.TaskEarningReport;
import com.akp.blackbulls.EarningReport.TeamEarning;
import com.akp.blackbulls.Profile.MyProfile;
import com.akp.blackbulls.Profile.PanVerification;
import com.akp.blackbulls.Profile.ViewAdharPan;
import com.akp.blackbulls.R;
import com.akp.blackbulls.Referrl.FundTransferHistory;
import com.akp.blackbulls.Referrl.MyTotalTeamList;
import com.akp.blackbulls.Referrl.WalletTransferHistory;
import com.akp.blackbulls.Walet.AddWalletRecord;
import com.akp.blackbulls.Walet.AllWallet;
import com.akp.blackbulls.Walet.CoinSaveWallet;
import com.akp.blackbulls.Walet.MainWalletTransationHistory;
import com.akp.blackbulls.Walet.MudraWallet;
import com.akp.blackbulls.Walet.ServiceWallet;
import com.akp.blackbulls.Walet.WalletHistoy;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

import static com.akp.blackbulls.Basic.API_Config.getApiClient_ByPost;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView sliding_menu;
    private DrawerLayout mDrawer;
    TextView logout_ll;
    TextView plan_tv,wallet_tv,account_tv,ourservices_tv,recharge_tv,privacypolicy_tv,share_tv,call_tv,home_tv,txt_username,time_tv,das_username;
    TextView income_wallet_tv,topup_wallet_tv,recharge_wallet_tv;
    LinearLayout daily_plan_ll,income_wallet_ll,topup_wallet_ll,recharge_wallet_ll,fd_ll;
    ImageView civ_profile_image,wallet_amount_tv;

    CirclePageIndicator indicator;
    ViewPager pager;
    Integer[] imageId = {R.drawable.a_one, R.drawable.a_two, R.drawable.a_three,  R.drawable.a_four};
    String[] imagesName = {"image1","image2","image3","image4"};
    Timer timer;
    String UserId,UserName;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    LinearLayout all_plan_ll,plan_ll,dd_ll;
    TextView dd_plan_tv,fdplan_tv,dipplan_tv;
    int i=0;
    int j=0;
    int k=0;
    LinearLayout adip_ll,add_ll,afd_ll;
    SwipeRefreshLayout srl_refresh;
    AppCompatButton dipstart_btn,ddstart_btn,fdstart_btn,walletstart_btn,fund_tranfer_friend_btn;
    ImageView account_img,notification_img;
    private android.app.AlertDialog alertDialog;
    TextView status_tv;
    EditText rupee_et,mobile_et;
    String FriendUserId;
    LinearLayout amount_ll;


    LinearLayout earnimg_ll;
    TextView referal_tv,team_tv,task_tv,all_earning_ll,earning_tv;

    ImageView referimg;
    TextView referperson_tv;

    LinearLayout team_referral_ll,all_team_ll;
    TextView myreferal_tv,my_team_tv,fundtransfer_tv,walletTransfer_tv,announcement_tv;
    String versionName = "", versionCode = "";
    TextView version_tv;
    private TextView txtMarquee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        version_tv=findViewById(R.id.version_tv);


        fund_tranfer_friend_btn=findViewById(R.id.fund_tranfer_friend_btn);
        referimg=findViewById(R.id.referimg);
        referperson_tv=findViewById(R.id.referperson_tv);
        // casting of textview
        txtMarquee = (TextView)findViewById(R.id.marqueeText);
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);

        verifyStoragePermission(DashboardActivity.this);
        findViewId();
        setListner();
        ProfileAPI();
        getWishMsg();
//        getBanner();
//        name_tv.setText("Name: -"+UserName);
//        menuonclick event
//        getDashboardCatAPI();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawer.openDrawer(Gravity.START);
            }});
        getVersionInfo();
        Announcement();
        /*       indicator = (CirclePageIndicator) findViewById(R.id.indicator);
         pager = findViewById(R.id.pager1);
        PagerAdapter adapter = new CustomAdapter(DashboardActivity.this,imageId,imagesName);
        pager.setAdapter(adapter);
        pager.setClipToPadding(false);
        indicator.setViewPager(pager);
        indicator.setFillColor(Color.RED);
//        viewPager.setPageMargin(24);
//        viewPager.setPadding(48, 8, 130, 8);
        pager.setOffscreenPageLimit(3);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                pager.post(new Runnable(){
                    @Override
                    public void run() {
                        pager.setCurrentItem((pager.getCurrentItem()+1)%imageId.length);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);*/
     srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(DashboardActivity.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(DashboardActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getBanner();
        GetWalletAPI();

        fund_tranfer_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fundfriendttransferpopup();
            }
        });


        fundtransfer_tv=findViewById(R.id.fundtransfer_tv);
        walletTransfer_tv=findViewById(R.id.walletTransfer_tv);

        all_team_ll=findViewById(R.id.all_team_ll);
        team_referral_ll=findViewById(R.id.team_referral_ll);
        myreferal_tv=findViewById(R.id.myreferal_tv);
        my_team_tv=findViewById(R.id.my_team_tv);


        all_earning_ll=findViewById(R.id.all_earning_ll);
        earnimg_ll=findViewById(R.id.earnimg_ll);
        referal_tv=findViewById(R.id.referal_tv);
        team_tv=findViewById(R.id.team_tv);
        task_tv=findViewById(R.id.task_tv);
        earning_tv=findViewById(R.id.earning_tv);
        announcement_tv=findViewById(R.id.announcement_tv);
        all_earning_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j == 0) {
                    earnimg_ll.setVisibility(View.GONE);
                   j++;
                } else if (j == 1) {
                    earnimg_ll.setVisibility(View.VISIBLE);
                    j = 0;
                }
            }
        });


        all_team_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k == 0) {
                    team_referral_ll.setVisibility(View.GONE);
                    k++;
                } else if (k == 1) {
                    team_referral_ll.setVisibility(View.VISIBLE);
                    k = 0;
                }
            }
        });

        referperson_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k == 0) {
                    team_referral_ll.setVisibility(View.GONE);
                    k++;
                } else if (k == 1) {
                    team_referral_ll.setVisibility(View.VISIBLE);
                    k = 0;
                }
            }
        });


        myreferal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ReferralPersonList.class);
                startActivity(intent);
            }
        });
        my_team_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MyTotalTeamList.class);
                startActivity(intent);
            }
        });

        fundtransfer_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FundTransferHistory.class);
                startActivity(intent);
            }
        });
        walletTransfer_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WalletTransferHistory.class);
                startActivity(intent);
            }
        });



        announcement_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AnnouncementList.class);
                startActivity(intent);
            }
        });



        referal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ReferalEarning.class);
                startActivity(intent);
            }
        });
        team_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), TeamEarning.class);
                startActivity(intent);
            }
        });
        task_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), TaskEarningReport.class);
                startActivity(intent);
            }
        });
        earning_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), TaskEarning.class);
                startActivity(intent);
            }
        });


        dipstart_btn=findViewById(R.id.dipstart_btn);
        ddstart_btn=findViewById(R.id.ddstart_btn);
        fdstart_btn=findViewById(R.id.fdstart_btn);
        walletstart_btn=findViewById(R.id.walletstart_btn);
        notification_img=findViewById(R.id.notification_img);
        account_img=findViewById(R.id.account_img);
        dipstart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PlanPurchase.class);
                startActivity(intent);
            }
        });
        ddstart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DDPlanPurchase.class);
                startActivity(intent);
            }
        });
        fdstart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),FixedDepositPlan.class);
                startActivity(intent);
            }
        });
        walletstart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllWallet.class);
                startActivity(intent);
            }
        });
        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NotificationList.class);
                startActivity(intent);
            }
        });
        account_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyProfile.class);
                startActivity(intent);
            }
        });

        share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                takeScreenShot(referimg);
                createlink();
            }
        });

    }

    private void getWishMsg() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            time_tv.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time_tv.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            time_tv.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            time_tv.setText("Good Evening");
        }
    }

    private void findViewId() {
//        sliding menu id
        txt_username=findViewById(R.id.txt_username);
        mDrawer=findViewById(R.id.drawer_layout);
        sliding_menu=findViewById(R.id.sliding_menu);
        logout_ll=findViewById(R.id.logout_ll);
        home_tv=findViewById(R.id.home_tv);
        plan_tv=findViewById(R.id.plan_tv);
        wallet_tv=findViewById(R.id.wallet_tv);
        account_tv=findViewById(R.id.account_tv);
        ourservices_tv=findViewById(R.id.ourservices_tv);
        recharge_tv=findViewById(R.id.recharge_tv);
        privacypolicy_tv=findViewById(R.id.privacypolicy_tv);
        share_tv=findViewById(R.id.share_tv);
        call_tv=findViewById(R.id.call_tv);
        civ_profile_image=findViewById(R.id.civ_profile_image);

        all_plan_ll=findViewById(R.id.all_plan_ll);
        plan_ll=findViewById(R.id.plan_ll);
        dd_plan_tv=findViewById(R.id.dd_plan_tv);
        fdplan_tv=findViewById(R.id.fdplan_tv);
        dipplan_tv=findViewById(R.id.dipplan_tv);

        srl_refresh=findViewById(R.id.srl_refresh);
        time_tv=findViewById(R.id.time_tv);

        fd_ll=findViewById(R.id.fd_ll);
        dd_ll=findViewById(R.id.dd_ll);

        adip_ll=findViewById(R.id.adip_ll);
        add_ll=findViewById(R.id.add_ll);
        afd_ll=findViewById(R.id.afd_ll);
//        dashboard id
        das_username=findViewById(R.id.das_username);
        wallet_amount_tv=findViewById(R.id.wallet_amount_tv);
        income_wallet_tv=findViewById(R.id.income_wallet_tv);
        topup_wallet_tv=findViewById(R.id.topup_wallet_tv);
        recharge_wallet_tv=findViewById(R.id.recharge_wallet_tv);
//dashboard row layout id
        daily_plan_ll=findViewById(R.id.daily_plan_ll);
        income_wallet_ll=findViewById(R.id.income_wallet_ll);
        topup_wallet_ll=findViewById(R.id.topup_wallet_ll);
        recharge_wallet_ll=findViewById(R.id.recharge_wallet_ll);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);

    }

    private void setListner() {
        plan_tv.setOnClickListener(this);
        wallet_tv.setOnClickListener(this);
        account_tv.setOnClickListener(this);
        ourservices_tv.setOnClickListener(this);
        recharge_tv.setOnClickListener(this);
        privacypolicy_tv.setOnClickListener(this);
        call_tv.setOnClickListener(this);
        logout_ll.setOnClickListener(this);
        daily_plan_ll.setOnClickListener(this);
        income_wallet_ll.setOnClickListener(this);
        topup_wallet_ll.setOnClickListener(this);
        recharge_wallet_ll.setOnClickListener(this);
        fd_ll.setOnClickListener(this);
        dd_ll.setOnClickListener(this);
        wallet_amount_tv.setOnClickListener(this);

        all_plan_ll.setOnClickListener(this);
        dd_plan_tv.setOnClickListener(this);
        fdplan_tv.setOnClickListener(this);
        dipplan_tv.setOnClickListener(this);

        add_ll.setOnClickListener(this);
        afd_ll.setOnClickListener(this);
        adip_ll.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.home_tv:
            mDrawer.closeDrawer(Gravity.START);
            break;
            case R.id.plan_tv:
                if (i == 0) {
                    plan_ll.setVisibility(View.GONE);
                    i++;
                } else if (i == 1) {
                    plan_ll.setVisibility(View.VISIBLE);
                    i = 0;
                }
//                startActivity(new Intent(this, DailyPlan.class));
//                mDrawer.closeDrawer(Gravity.START);
                break;
            case R.id.wallet_tv:
                startActivity(new Intent(this, WalletHistoy.class));
                mDrawer.closeDrawer(Gravity.START);
                break;
            case R.id.account_tv:
                startActivity(new Intent(this, MyProfile.class));
                 break;
            case R.id.ourservices_tv:
                mDrawer.closeDrawer(Gravity.START);
                startActivity(new Intent(this, OurService.class));
                break;
            case R.id.recharge_tv:
                mDrawer.closeDrawer(Gravity.START);
                startActivity(new Intent(this, RechargeSystem.class));
                break;
            case R.id.privacypolicy_tv:
                mDrawer.closeDrawer(Gravity.START);
                startActivity(new Intent(this, PrivacyPolicy.class));
                break;
//            case R.id.share_tv:
//                createlink();
//               /* takeScreenShot(civ_profile_image);*/
//                break;

            case R.id.call_tv:
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + "+91 1234567890"));
//                startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
                startActivity(new Intent(this, ContactUsScreen.class));

                break;

            case R.id.logout_ll:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure want to Logout?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        startActivity(intent);
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(Color.BLACK);
                nbutton.setTextColor(Color.WHITE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(Color.RED);
                pbutton.setTextColor(Color.WHITE);

                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.daily_plan_ll:
                Intent intent1=new Intent(getApplicationContext(), PlanPurchase.class);
                startActivity(intent1);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.income_wallet_ll:
                Intent intent2=new Intent(getApplicationContext(), MudraWallet.class);
                startActivity(intent2);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.topup_wallet_ll:
                Intent intent3=new Intent(getApplicationContext(), ServiceWallet.class);
                startActivity(intent3);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.recharge_wallet_ll:
                Intent intent4=new Intent(getApplicationContext(), CoinSaveWallet.class);
                startActivity(intent4);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.fd_ll:
                Intent intent5=new Intent(getApplicationContext(), FixedDepositPlan.class);
                startActivity(intent5);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.dd_ll:
                Intent dd=new Intent(getApplicationContext(), DDPlanPurchase.class);
                startActivity(dd);
                mDrawer.closeDrawer(Gravity.START);
                break;

            case  R.id.wallet_amount_tv:
                Intent intent6=new Intent(getApplicationContext(), AllWallet.class);
                startActivity(intent6);
                break;
            case  R.id.all_plan_ll:
                        if (i == 0) {
                            plan_ll.setVisibility(View.GONE);
                            i++;
                        } else if (i == 1) {
                            plan_ll.setVisibility(View.VISIBLE);
                            i = 0;
                        }
                break;
            case  R.id.dd_plan_tv:
               startActivity(new Intent(this, AllDDPlan.class));
                mDrawer.closeDrawer(Gravity.START);  break;
            case  R.id.fdplan_tv:
                startActivity(new Intent(this, AllFDPlan.class));
                mDrawer.closeDrawer(Gravity.START);  break;
            case  R.id.dipplan_tv:
                startActivity(new Intent(this, DailyPlan.class));
                mDrawer.closeDrawer(Gravity.START); break;
            case  R.id.add_ll:
                startActivity(new Intent(this, DDPlanPurchase.class));
                mDrawer.closeDrawer(Gravity.START); break;
            case  R.id.afd_ll:
                startActivity(new Intent(this, FixedDepositPlan.class));
                mDrawer.closeDrawer(Gravity.START); break;
            case  R.id.adip_ll:
                startActivity(new Intent(this, PlanPurchase.class));
                mDrawer.closeDrawer(Gravity.START); break;
        }
    }



    //Permissions Check

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }




 /*   private void takeScreenShot(View view) {
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);

        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }

            String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Share ScreenShot
    private void shareScreenShot(File imageFile) {
        Uri uri =  FileProvider.getUriForFile(getApplicationContext(), "com.akp.blackbulls.provider", imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Download Black Bulls From PlayStore \n https://play.google.com/store/apps/details?id=com.akp.blackbulls" +"\n User MY REFERRAL CODE :- "+UserId);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    //Permissions Check
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }*/
    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"BannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(DashboardActivity.this, bannerData));
                    indicator.setViewPager(pager);
                    indicator.setFillColor(Color.RED);
                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            pager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) { }
                        @Override
                        public void onPageScrollStateChanged(int pos) { }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
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
//                            wallet_amount_tv.setText("\u20B9 "+jsonObject.getString("MainWallet"));
                            income_wallet_tv.setText("\u20B9 "+jsonObject.getString("MurdaWallet")+"\n(View History)");
                            topup_wallet_tv.setText("\u20B9 "+jsonObject.getString("ServiceWallet")+"\n(View History)");
                            recharge_wallet_tv.setText("\u20B9 "+jsonObject.getString("CoinSaveWallet")+"\n(View History)");
                        }

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }




    private void fundfriendttransferpopup() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.friend_withdrawpopup, viewGroup, false);
        final AppCompatButton Submit_btn = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn);
        final AppCompatButton Submit_btn1 = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn1);
        rupee_et=(EditText)dialogView.findViewById(R.id.rupee_et);
        mobile_et=dialogView.findViewById(R.id.mobile_et);
         status_tv=dialogView.findViewById(R.id.status_tv);
        amount_ll=dialogView.findViewById(R.id.amount_ll);

        Submit_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetFriendDetails", new  Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("Status").equalsIgnoreCase("true")){
                                JSONArray Jarray = object.getJSONArray("Response");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject jsonobject = Jarray.getJSONObject(i);
                                    status_tv.setText("Account Verified("+jsonobject.getString("MemberName")+")");
                                    FriendUserId = jsonobject.getString("UserID");
                                    Submit_btn1.setVisibility(View.GONE);
                                    amount_ll.setVisibility(View.VISIBLE);
                                    status_tv.setTextColor(Color.GREEN);

                                }
                            }
                            else {
                                status_tv.setText(object.getString("Message"));
                                status_tv.setTextColor(Color.RED);
                                Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                                Submit_btn1.setVisibility(View.VISIBLE);
                                amount_ll.setVisibility(View.GONE);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardActivity.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("MobileNo",mobile_et.getText().toString());
                        params.put("UserId",UserId);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        Submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferMoney(FriendUserId,rupee_et.getText().toString());
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }








    public void TransferMoney(final String fid,final String amount){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"FundtransferTofriend", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Intent intent=new Intent(DashboardActivity.this, MainWalletTransationHistory.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("FriendId",fid);
                params.put("Amount",amount);
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }


    public void createlink(){
        Log.e("main", "create link ");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://blackbulls.in/"))
                .setDomainUriPrefix("blackbullsin.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer "+ dynamicLink.getUri());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        createReferlink(UserId, UserId);
    }
    public void createReferlink(String custid, String prodid){
        // manuall link
        String sharelinktext  = "https://blackbullsin.page.link/?"+
                "link=https://blackbulls.in/myrefer.php?custid="+custid +"-"+prodid+
                "&apn="+ getPackageName()+
                "&st="+"BlackBulls Refer Link"+
                "&sd="+"Download Black Bulls From PlayStore User MY REFERRAL CODE:- "+UserId+" "+
                "&si="+"https://blackbulls.in/website-assets/images/logo.png";
        Log.e("mainactivity", "sharelink - "+sharelinktext);
        // shorten the link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                //.setLongLink(dynamicLink.getUri())    // enable it if using firebase method dynamicLink
                .setLongLink(Uri.parse(sharelinktext))  // manually
                .buildShortDynamicLink()
                .addOnCompleteListener(DashboardActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Date date = new Date();
                            CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);
                            try {
                                File mainDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
                                if (!mainDir.exists()) {
                                    boolean mkdir = mainDir.mkdir();
                                }
                                String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
                                referimg.setDrawingCacheEnabled(true);
                                Bitmap bitmap = Bitmap.createBitmap(referimg.getDrawingCache());
                                referimg.setDrawingCacheEnabled(false);
                                File imageFile = new File(path);
                                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                Uri uri =  FileProvider.getUriForFile(getApplicationContext(), "com.akp.blackbulls.provider", imageFile);
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_TEXT, "Hi,\n" +
                                        "Inviting you to join BlackBulls\n" +
                                        "an interesting app which provides you incredible offers on Plan,Recharge, Money Transfer, Shopping & many more.\n\n" +"Use my referrer code :- "+UserId+"\n\nDownload app from link : "+shortLink.toString());
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                try {
                                    startActivity(Intent.createChooser(intent, "Share With"));
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(DashboardActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                            Log.e("main ", "short link "+ shortLink.toString());
//                            // share app dialog
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);
                        } else {
                            // Error
                            // ...
                            Log.e("main", " error "+task.getException() );

                        }
                    }
                });

    }












    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure you want to Exit?</font>"));
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        //Set negative button background
        nbutton.setBackgroundColor(Color.BLACK);
        //Set negative button text color
        nbutton.setTextColor(Color.WHITE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.RED);
        //Set positive button text color
        pbutton.setTextColor(Color.WHITE);
    }





    public void ProfileAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
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
                            String UserID       = jsonobject.getString("UserID");
                            String MemberName    = jsonobject.getString("MemberName");
                            if (MemberName.equalsIgnoreCase("")){
                                txt_username.setText(UserName);
                            }
                            else {
                                txt_username.setText(MemberName);
                            }

                            if (MemberName.equalsIgnoreCase("")){
                                das_username.setText("Welcome, "+UserName);
                            }
                            else {
                                das_username.setText("Welcome, "+MemberName);
                            }

                        } }
                    else {
//                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }




    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));
            version_tv.setText("Version : "+versionName);
            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void Announcement() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+ "AnouncementList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String anouncementDes    = jsonobject.getString("AnouncementDes");
                            txtMarquee.setText(Html.fromHtml(anouncementDes));
                        } }
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
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }

}