package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ContinuousDataActivity extends AppCompatActivity {
    List<String> vitalsDataList;

    Integer prevHBInt = 0, prevTempInt = 0, prevO2Int = 0;
    String prev_temp = "00", prev_o2 = "00", prev_hb = "00";

    TextView hbTV, o2TV, tempTV;
    MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_data);

        vitalsDataList = new ArrayList(22);

        vitalsDataList.add("{\"temp\": 31, \"hb\" : 75, \"o2\" : 98.42}");
        vitalsDataList.add("{\"temp\": 32, \"hb\" : 78, \"o2\" : 97.25}");
        vitalsDataList.add("{\"temp\": 27, \"hb\" : 79, \"o2\" : 96.53}");
        vitalsDataList.add("{\"temp\": 29, \"hb\" : 88, \"o2\" : 93.72}");
        vitalsDataList.add("{\"temp\": 30, \"hb\" : 85, \"o2\" : 96.26}");
        vitalsDataList.add("{\"temp\": 30, \"hb\" : 83, \"o2\" : 98.98}");
        vitalsDataList.add("{\"temp\": 34, \"hb\" : 81, \"o2\" : 99.00}");
        vitalsDataList.add("{\"temp\": 31, \"hb\" : 77, \"o2\" : 97.42}");
        vitalsDataList.add("{\"temp\": 29, \"hb\" : 90, \"o2\" : 99.24}");
        vitalsDataList.add("{\"temp\": 29, \"hb\" : 93, \"o2\" : 99.17}");
        vitalsDataList.add("{\"temp\": 30, \"hb\" : 99, \"o2\" : 98.89}");
        vitalsDataList.add("{\"temp\": 25, \"hb\" : 120, \"o2\" : 93.42}");
        vitalsDataList.add("{\"temp\": 26, \"hb\" : 100, \"o2\" : 94.25}");
        vitalsDataList.add("{\"temp\": 27, \"hb\" : 82, \"o2\" : 95.53}");
        vitalsDataList.add("{\"temp\": 28, \"hb\" : 88, \"o2\" : 96.72}");
        vitalsDataList.add("{\"temp\": 33, \"hb\" : 85, \"o2\" : 98.26}");
        vitalsDataList.add("{\"temp\": 34, \"hb\" : 94, \"o2\" : 98.98}");
        vitalsDataList.add("{\"temp\": 35, \"hb\" : 110, \"o2\" : 95.00}");
        vitalsDataList.add("{\"temp\": 36, \"hb\" : 130, \"o2\" : 93.42}");
        vitalsDataList.add("{\"temp\": 23, \"hb\" : 99, \"o2\" : 92.24}");
        vitalsDataList.add("{\"temp\": 27, \"hb\" : 93, \"o2\" : 98.17}");
        vitalsDataList.add("{\"temp\": 30, \"hb\" : 99, \"o2\" : 98.89}");

        Random rand = new Random();

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        OkHttpClient client = new OkHttpClient();

        Intent intent = getIntent();
        String ipAddress = intent.getStringExtra("URL");

//        String url = "http://" + ipAddress + ":8080";
        String url = "http://192.168.2.107:8080/";

        hbTV = findViewById(R.id.hb_value);
        o2TV = findViewById(R.id.o2_value);
        tempTV = findViewById(R.id.temp_value);

        hbTV.setText(prev_hb);


        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> responseMap = getMapFromString(vitalsDataList.get(rand.nextInt(vitalsDataList.size())));
                                hbTV.setText(responseMap.get("\"hb\""));
                                tempTV.setText(responseMap.get("\"temp\""));
                                o2TV.setText(responseMap.get("\"o2\""));
                            }
                        });


                    } catch (Exception e) {

                    }
                }
            }
        };

        t.start();


//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Map<String, String> responseMap = getMapFromString(  vitalsDataList.get(rand.nextInt(vitalsDataList.size())));


//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            String mapString = response.body().string();
//                            Map<String, String> responseMap = getMapFromString(mapString);
//
//                            ContinuousDataActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ValueAnimator animator = new ValueAnimator();
//                                    animator.setObjectValues(prevHBInt, Integer.parseInt(responseMap.get("hb")));
//                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                        public void onAnimationUpdate(ValueAnimator animation) {
//                                            hbTV.setText(responseMap.get("hb"));
//                                            prevHBInt = Integer.parseInt(responseMap.get("hb"));
//                                        }
//                                    });
//                                    animator.setDuration(500);
//                                    animator.start();
//
//                                    animator.setObjectValues(prevTempInt, Integer.parseInt(responseMap.get("temp")));
//                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                        public void onAnimationUpdate(ValueAnimator animation) {
//                                            tempTV.setText(responseMap.get("temp"));
//                                            prevTempInt = Integer.parseInt(responseMap.get("temp"));
//                                        }
//                                    });
//                                    animator.setDuration(500);
//                                    animator.start();
//
//                                    animator.setObjectValues(prevO2Int, Integer.parseInt(responseMap.get("o2")));
//                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                        public void onAnimationUpdate(ValueAnimator animation) {
//                                            o2TV.setText(responseMap.get("o2"));
//                                            prevO2Int = Integer.parseInt(responseMap.get("o2"));
//                                        }
//                                    });
//                                    animator.setDuration(500);
//                                    animator.start();
//
//                                }
//                            });
//
//
//
//
//                        }
//                    }
//                });
//            }
//        }, 0, 6000);

    }

    public Map<String, String> getMapFromString(String mapString) {
        Map<String, String> map = new HashMap<String, String>();
        mapString = mapString.trim();
        mapString = mapString.substring(1, mapString.length() - 1);

        String[] keyValuePairs = mapString.split(",");              //split the string to creat key-value pairs

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(":", 2);
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        Log.i("Response Map:    ", map.toString());
        Log.i("Map Keys  ", String.valueOf(map.containsKey("\"hb\"")));

        return map;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ContinuousDataActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}