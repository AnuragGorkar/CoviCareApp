package com.example.covicareapp.ui.activities.qrscan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.covicareapp.R;
import com.example.covicareapp.logic.EncryptDecryptData;
import com.example.covicareapp.ui.activities.ContinuousDataActivity;
import com.example.covicareapp.ui.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

public class ContinuousMonitorScanQRActivity extends AppCompatActivity {
    String url;

    //    UI Variables
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView scannedCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_monitor_scan_q_r);

        //        UI Hooks
        codeScannerView = findViewById(R.id.scanner_view);
        scannedCode = findViewById(R.id.scanned_value);
        progressBar = findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);
        scannedCode.setVisibility(View.VISIBLE);

        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        scannedCode.setText("Adding User");
                        FirebaseAuth currentFirebaseAuth = FirebaseAuth.getInstance();
                        String currentEmail = currentFirebaseAuth.getCurrentUser().getEmail();

                        EncryptDecryptData encryptDecryptData = new EncryptDecryptData();

                        Log.i("Result   ", result.getText());

                        if (!result.getText().contains("Data=") || !result.getText().contains("Salt=") || !result.getText().contains("Password=")) {
                            progressBar.setVisibility(View.GONE);
                            scannedCode.setTextColor(getColor(R.color.error_900));
                            scannedCode.setText("Scan valid QR code please");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ContinuousMonitorScanQRActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                }
                            }, 1000);

                        } else {
                            url = encryptDecryptData.decryptURLContinuous(result.getText());

                            Log.i("URL  ", url);
                            scannedCode.setTextColor(getColor(R.color.success_400));
                            scannedCode.setText("Connecting to device...");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ContinuousMonitorScanQRActivity.this, ContinuousDataActivity.class);
                                    intent.putExtra("URL", url);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }, 1000);


                        }


                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ContinuousMonitorScanQRActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}