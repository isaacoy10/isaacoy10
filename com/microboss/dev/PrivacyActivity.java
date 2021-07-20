package com.microboss.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0,0);
    }
}