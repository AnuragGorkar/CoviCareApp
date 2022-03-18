package com.example.covicareapp.ui.activities.qrscan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.covicareapp.R;
import com.example.covicareapp.ui.activities.MainActivity;
import com.google.zxing.Result;

public class AddOnlineUserScanQrActivity extends AppCompatActivity {

    //    UI Variables
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_online_user_scan_qr);

        //        UI Hooks
        codeScannerView = findViewById(R.id.scanner_view);
        scannedCode = findViewById(R.id.scanned_value);

        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("The result from the QR Code", result.getText());
                        scannedCode.setText(result.getText());

                        Intent intent = new Intent(AddOnlineUserScanQrActivity.this, MainActivity.class);
                        intent.putExtra("Vitals Data", result.getText());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
}