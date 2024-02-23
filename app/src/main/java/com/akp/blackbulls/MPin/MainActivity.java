package com.akp.blackbulls.MPin;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akp.blackbulls.Dashboard.DashboardActivity;
import com.akp.blackbulls.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PFSecurityManager.getInstance().setPinCodeHelper(new TestPFPinCodeHelperImpl());
    }


    private void showMainFragment() {
        final MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }


}
