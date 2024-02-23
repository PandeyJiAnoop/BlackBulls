package com.akp.blackbulls.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.akp.blackbulls.R;


public class RechargeSystem extends AppCompatActivity {
LinearLayout mobilerecharge_ll;
ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_system);
        ivBack=findViewById(R.id.ivBack);

        mobilerecharge_ll=findViewById(R.id.mobilerecharge_ll);
        mobilerecharge_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MobileRecahrge.class);
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}