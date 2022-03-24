package com.example.covicareapp.ui.activities.qrscan;

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
import com.example.covicareapp.helpers.VitalsSQLiteHelper;
import com.example.covicareapp.logic.EncryptDecryptData;
import com.example.covicareapp.models.OnlineUserVitalsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.Map;
import java.util.Objects;

public class ScanQrActivity extends AppCompatActivity {

    private static final String TAG = "ScanQrActivity";
    //    UI Variables
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView scannedCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        //        UI Hooks
        codeScannerView = findViewById(R.id.scanner_view);
        scannedCode = findViewById(R.id.scanned_value);
        progressBar = findViewById(R.id.progress_indicator);

        progressBar.setVisibility(View.GONE);

        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("The result from the QR Code", result.getText());

                        EncryptDecryptData encryptDecryptData = new EncryptDecryptData();


                        Map<String, Object> vitals = encryptDecryptData.decryptVitals(result.getText());


                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                        db.collection("users").whereEqualTo("email", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.toString());
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    Log.d(TAG, "onSuccess: snapshot : " + snapshot.getData());
                                    Map<String, Object> map = snapshot.getData();
                                    Log.d(TAG, "onSuccess: raspiUId : " + map.get("raspiUId"));
                                    String raspiUId = (String)  map.get("raspiUId");



                                    OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, (String) vitals.get("raspiId"), userId,
                                            Double.valueOf((String) vitals.get("hb")),
                                            Double.valueOf((String) vitals.get("o2")),
                                            Double.valueOf((String) vitals.get("temp")),
                                            Integer.valueOf((String) vitals.get("cough_value")),
                                            Long.valueOf(String.valueOf(vitals.get("timeStamp"))),
                                            "Analysis Result");



                                    VitalsSQLiteHelper vitalsSQLiteHelper = new VitalsSQLiteHelper(ScanQrActivity.this);

                                    vitalsSQLiteHelper.addOneVitalsEntryOnline(onlineUserVitalsModel);
                                    try {
                                        Log.i("Data from QR", String.valueOf(encryptDecryptData.decryptVitals(result.getText())));

                                        scannedCode.setText("Vitals Extracted Successfully");
                                        scannedCode.setTextColor(getColor(R.color.success_400));

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.VISIBLE);
                                                // Do something after 5s = 5000ms
                                                scannedCode.setText("Analysing Vitals...");
                                                scannedCode.setTextColor(getColor(R.color.purple_200));

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // WHO Logic here


                                                    }
                                                }, 800);

                                            }
                                        }, 600);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // Todo analyse result and show score for succiptibility using WHO rules and then nnavigate to the vitals history activity


//                                    Intent intent = new Intent(ScanQrActivity.this, MainActivity.class);
//                                    intent.putExtra("Vitals Data", result.getText());
//                                    intent.putExtra("Fragment", "Vitals History");
//
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            }
                        });

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