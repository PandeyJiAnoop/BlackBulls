package com.akp.blackbulls.Recharge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akp.blackbulls.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


public class PAYQRSCAN extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_a_y_q_r_s_c_a_n);
        if (ContextCompat.checkSelfPermission(PAYQRSCAN.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(PAYQRSCAN.this, new String[] {Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }
    }

    private void startScanning() {
        mCodeScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, mCodeScannerView);
        mCodeScanner.startPreview();   // this line is very important, as you will not be able to scan your code without this, you will only get blank screen
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
//                String removeCurrency=result.getText().toString().substring(3);
//                String loginToken = result.getText().toString();
//                System.out.println( loginToken.substring( 13, loginToken.length() - 1 ) );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//
//////                        String URL = "upi://pay?pa=vishalgupta72729090@okhdfcbank&pn=Vishal%20Gupta&aid=uGICAgIDvoZuZPg";
//                        String strNew = result.getText().replace("upi://pay?pa=", ""); // strNew is 'bcdDCBA123'
//                        Log.d("sdsds","ss"+result);




                        Intent intent=new Intent(getApplicationContext(), UPITransfer.class);
                        intent.putExtra("upiid",result.getText());
                        intent.putExtra("moneytype","qr");
                        startActivity(intent);
                        Toast.makeText(PAYQRSCAN.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //now if you want to scan again when you click on scanner then do this.
        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}