package com.akp.blackbulls.Dashboard;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.akp.blackbulls.R;


public class MobileRecahrge extends AppCompatActivity {
ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recahrge);
        ivBack=findViewById(R.id.ivBack);

    }
}