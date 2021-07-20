package com.microboss.dev;

import android.animation.Animator;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.microboss.dev.databinding.ActivityVideoStreamBinding;

import static com.microboss.dev.UpdateService.EVENT_NAME;
import static com.microboss.dev.UpdateService.SPONSORSHIP_CONFIRMATION;
import static com.microboss.dev.UpdateService.STREAM_IMAGE_URL;
import static com.microboss.dev.UpdateService.STREAM_INFO;
import static com.microboss.dev.UpdateService.STREAM_TITLE;
import static com.microboss.dev.UpdateService.STREAM_URL;
import static com.microboss.dev.UpdateService.database_firstname;
import static com.microboss.dev.UpdateService.database_signup_email;
import static com.microboss.dev.UpdateService.database_surname;
import static com.microboss.dev.UpdateService.getVideoUrl;
import static com.microboss.dev.UpdateService.mDatabase;
import static com.microboss.dev.UpdateService.messageFromStream;
import static com.microboss.dev.UpdateService.signup_email;
import static com.microboss.dev.UpdateService.sponsorship_confirmation;
import static com.microboss.dev.UpdateService.streamPlayer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoStreamActivity extends AppCompatActivity {


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    boolean sendMessageOpened = false;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    public static ActivityVideoStreamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityVideoStreamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;



//        binding.streamPlayer.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_two));
//        binding.streamPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                binding.streamPlayer.start();
//            }
//        });



        Intent metaIntent = getIntent();

        String META_TITLE = metaIntent.getStringExtra(STREAM_TITLE);
        String META_INFO = metaIntent.getStringExtra(STREAM_INFO);
        String META_VIDEO_LINK = metaIntent.getStringExtra(STREAM_URL);
        String META_IMAGE_LINK = metaIntent.getStringExtra(STREAM_IMAGE_URL);

        binding.streamPlayer.setVideoURI(getVideoUrl(META_VIDEO_LINK));
        binding.dropComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendMessageOpened == true){
                    binding.dropComment.setImageResource(R.drawable.send_msg_roll);
                    sendMessageOpened = false;
                    binding.messageContainer.setVisibility(View.GONE);
                }else{
                    binding.dropComment.setImageResource(R.drawable.options_close_xx);
                    sendMessageOpened = true;
                    binding.messageContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.nameOfSender.setText("from: "+database_firstname+" "+database_surname);
        binding.emailOfSender.setText(database_signup_email);
        binding.editSendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                messageFromStream = s.toString();
            }
        });
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.sendButton.setText("Sending...");
                mDatabase.child("users").child(signup_email.replace(".","")).child(EVENT_NAME).setValue(messageFromStream).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.sendButton.setText("Sent!");
                        binding.sendButton.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.sendButton.setText("Send");
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
                    }
                });

            }
        });
        binding.toggleForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.toggleForward.animate().alpha(0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.toggleForward.setAlpha(0.5f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

                return false;
            }
        });
        binding.toggleBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.toggleBack.animate().alpha(0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.toggleBack.setAlpha(0.5f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

                return false;
            }
        });
        binding.playStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.streamPlayer.isPlaying()){
                    binding.playStream.setImageResource(R.drawable.login);
                    binding.streamPlayer.pause();
                }else{
                    binding.playStream.setImageResource(R.drawable.pause);
                    binding.streamPlayer.start();
                }
            }
        });
//        binding.streamTimer.

        binding.toggleForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jump = binding.streamPlayer.getCurrentPosition()+30000;
                if(jump > binding.streamPlayer.getDuration()){
                    jump = binding.streamPlayer.getDuration();
                }
                binding.streamPlayer.seekTo(jump);
                binding.streamTimer.setText("+30 seconds");
            }
        });
        binding.toggleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jump = binding.streamPlayer.getCurrentPosition()-10000;
                if(jump < 0){
                    jump = 0;
                }
                binding.streamPlayer.seekTo(jump);
                binding.streamTimer.setText("-10 seconds");
            }
        });

        binding.streamPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.streamSeeker.setMax(binding.streamPlayer.getDuration());
            }
        });
        binding.streamSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    binding.streamPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        Toast.makeText(this, "title is: "+META_TITLE, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "info is: "+META_INFO, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "video link is: "+META_VIDEO_LINK, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "image link is: "+META_IMAGE_LINK, Toast.LENGTH_SHORT).show();

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.controls.setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        binding.dropComment.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        binding.dropComment.setVisibility(View.VISIBLE);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    public static void okayPlayNow(Uri uri){

        binding.streamPlayer.setVideoURI(uri);
        binding.streamPlayer.requestFocus();
        binding.streamPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.streamPlayer.start();
            }
        });
    }
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}