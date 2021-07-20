package com.microboss.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.microboss.dev.avi.AVLoadingIndicatorView;
import com.microboss.dev.avi.indicators.BallBeatIndicator;
import com.microboss.dev.avi.indicators.PacmanIndicator;

public class CautionLoading extends AppCompatActivity {

    public static AVLoadingIndicatorView loaderProcess;
    public static TextView processingText;
    public static AppCompatButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caution_loading);

        loaderProcess = findViewById(R.id.loader_processing);
        processingText = findViewById(R.id.text_processing);
        actionButton = findViewById(R.id.processing_action_button);

        Intent intent = getIntent();
        String message = intent.getStringExtra("BODY");
        boolean showButton = intent.getBooleanExtra("BUTTON",false);

        loaderProcess.setIndicatorColor(getResources().getColor(R.color.green));
        processingText.setTextColor(getResources().getColor(R.color.green));
        processingText.setText(message);

        if(showButton){
            actionButton.setVisibility(View.VISIBLE);
        }else {
            actionButton.setVisibility(View.GONE);
        }

        processingText.setTypeface(UpdateService.body);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loaderProcess.setIndicator(new PacmanIndicator());
    }
}