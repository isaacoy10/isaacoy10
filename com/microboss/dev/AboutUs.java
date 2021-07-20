package com.microboss.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    TextView head, body, tail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        head = findViewById(R.id.about_us_title);
        body = findViewById(R.id.about_microbe);
        tail = findViewById(R.id.aboutus_body);

        head.setTypeface(UpdateService.thick);
        body.setTypeface(UpdateService.head);
        tail.setTypeface(UpdateService.thin);

    }
}