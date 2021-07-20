package com.microboss.dev;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.microboss.dev.avi.AVLoadingIndicatorView;
import com.microboss.dev.avi.indicators.BallBeatIndicator;

import static com.microboss.dev.UpdateService.alcType;
import static com.microboss.dev.UpdateService.city;
import static com.microboss.dev.UpdateService.company;
import static com.microboss.dev.UpdateService.firstname;
import static com.microboss.dev.UpdateService.head;
import static com.microboss.dev.UpdateService.jobRole;
import static com.microboss.dev.UpdateService.mAuth;
import static com.microboss.dev.UpdateService.mDatabase;
import static com.microboss.dev.UpdateService.mobile;
import static com.microboss.dev.UpdateService.nameTitle;
import static com.microboss.dev.UpdateService.password;
import static com.microboss.dev.UpdateService.sex;
import static com.microboss.dev.UpdateService.signup_confirm_password;
import static com.microboss.dev.UpdateService.signup_email;
import static com.microboss.dev.UpdateService.signup_password;
import static com.microboss.dev.UpdateService.surname;
import static com.microboss.dev.UpdateService.thick;
import static com.microboss.dev.UpdateService.thin;

public class LoginSignupActivity extends AppCompatActivity {
    VideoView bgVid;
    CheckBox licenseBox;
    AVLoadingIndicatorView logging_loading,signingup_loading;
    FloatingActionButton signup_btn,loginBtn;
    TextView TandC, registerTxt, loginTxt, title, subtitle, signup_title, signup_subtitle, headingDesc, subHeadingDesc;
    FrameLayout signInFrame, signupFrame, logMailLay, logPassLay, signBtnLay, logBtnLay, homePageLay;
    FrameLayout upTitle, upSur, upFirst, upMail, upPhone, upCo, upJob, upSex, upAddress,
            upCity, upCountry, upPassword, upConfirmPass;
    Button individual, agent, corporate;
    LinearLayout peopleLayout, titleLayout, descBox;
    public static AppCompatEditText emailEdit, passEdit, signup_email_edit, signup_pass_edit,
            signup_pass_confirm_edit, name_title_edit, surname_edit, firstname_edit,
            mobile_edit, company_edit, job_edit, gender_edit, address_edit, city_edit,
            country_edit;
    String agentString = "Agent", individualString = "Individual", corporateString = "Corporate";
    public static String email = "";
    private Activity activity;

    String indDesc = "Register as an individual where you have access to all our free resources, webinar archives and register for coming events...";
    String copDesc = "Be a corporate sponsoring partner with us! Corporate partners get to feature their brands in live sessions among numerous benefits...";
    String agnDesc = "As a representative agent, you get incentives and exclwusive merchs! This benefit is limited to registered sales representatives";



    TextView mott, rc;
    ImageView mic;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            bgVid.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            emailEdit.setText("");
            passEdit.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgVid.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_activity);

//        FirebaseUser newUser = mAuth.createUserWithEmailAndPassword(signup_email,signup_password);

        startService(new Intent(getApplicationContext(),UpdateService.class));
        activity = this;



        finders();
        clickers();
        editTextWatchers();
        focusChangers();


        //font faces
        mott.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf"));
        rc.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/seguisbold.ttf"));

        title.setTypeface(head);
        signup_title.setTypeface(head);

        subtitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf"));
        signup_subtitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf"));

        registerTxt.setTypeface(head);
        loginTxt.setTypeface(head);

        headingDesc.setTypeface(thick);
        subHeadingDesc.setTypeface(thin);

        headingDesc.setText(individualString);
        subHeadingDesc.setText(indDesc);

        descBox.setAlpha(0f);


        TranslateAnimation moveLeft = new TranslateAnimation(0.0f,-1000.0f,0.0f,0.0f); moveLeft.setDuration(1500); moveLeft.setInterpolator(new AccelerateInterpolator());
        AlphaAnimation fadeUpward = new AlphaAnimation(0.0f,1.0f); fadeUpward.setDuration(1500); fadeUpward.setInterpolator(new AccelerateInterpolator());
        TranslateAnimation moveRight = new TranslateAnimation(0.0f,1000.0f,0.0f,0.0f);
        moveRight.setDuration(1500); moveRight.setInterpolator(new AccelerateInterpolator());

        moveLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AnimationSet group = new AnimationSet(false);
        ScaleAnimation zoomIn = new ScaleAnimation(0.75f,1f,0.75f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f,1.0f);
        zoomIn.setInterpolator(new DecelerateInterpolator());
        zoomIn.setDuration(5000);
        fadeIn.setDuration(5000);
        group.addAnimation(zoomIn);
        group.addAnimation(fadeIn);
        group.setInterpolator(new AccelerateDecelerateInterpolator());

        group.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(headingDesc.getText().equals("INDIVIDUAL")){

                            headingDesc.setText("AGENT");
                            subHeadingDesc.setText(agnDesc);
                            descBox.startAnimation(fadeUpward);

                }else if(headingDesc.getText().equals("AGENT")){

                            headingDesc.setText("CORPORATE");
                            subHeadingDesc.setText(copDesc);
                            descBox.startAnimation(moveLeft);

                }else if(headingDesc.getText().equals("CORPORATE")){

                            headingDesc.setText("INDIVIDUAL");
                            subHeadingDesc.setText(indDesc);
                            descBox.startAnimation(moveRight);

                }else {

                    descBox.startAnimation(group);
                }

                descBox.startAnimation(group);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logging_loading.setIndicator(new BallBeatIndicator());
        signingup_loading.setIndicator(new BallBeatIndicator());

        //default transparencies
        peopleLayout.setAlpha(0.0f);
        mott.setAlpha(0.0f);
        rc.setAlpha(0.0f);


        Uri vidPath = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_two);
        bgVid.setVideoURI(vidPath);//eoURI(new Uri());
        bgVid.setAlpha(1.0f);
        bgVid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        bgVid.start();


        //animators
        AlphaAnimation pack = new AlphaAnimation(0f,1.0f);
        pack.setDuration(4500);
        pack.setInterpolator(new AccelerateDecelerateInterpolator());


        pack.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                peopleLayout.setAlpha(1.0f);

                mott.animate().alpha(1.0f).setDuration(1500).start();
                rc.animate().alpha(1.0f).setDuration(1500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        descBox.startAnimation(group);
                        animateDesc(descBox,"Individual");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

                TranslateAnimation moveUp = new TranslateAnimation(0f,0f,1000f,0f);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f,1.0f);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(moveUp);
                animationSet.addAnimation(fadeIn);
                animationSet.setDuration(2000);
                peopleLayout.startAnimation(animationSet);


//                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                overridePendingTransition(0,0);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        peopleLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        peopleLayout.setClickable(true);
                        peopleLayout.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mic.startAnimation(pack);

    }
    private void finders(){

        licenseBox = findViewById(R.id.read_license_box);
        TandC = findViewById(R.id.terms_conditions);
        headingDesc = findViewById(R.id.heading);
        subHeadingDesc = findViewById(R.id.sub_heading);
        bgVid = findViewById(R.id.bg_video);
        logging_loading = findViewById(R.id.signin_loading);
        signingup_loading = findViewById(R.id.signup_loading);
        peopleLayout = findViewById(R.id.people_layout);
        loginBtn = findViewById(R.id.log_in_btn);
        signup_btn = findViewById(R.id.signup_btn);
        registerTxt = findViewById(R.id.reg_new_txt);
        title = findViewById(R.id.signin_title);
        subtitle = findViewById(R.id.signin_subtitle);
        signup_subtitle = findViewById(R.id.signup_subtitle);
        signup_title = findViewById(R.id.signup_title);
        agent = findViewById(R.id.agent);
        individual = findViewById(R.id.individual);
        corporate = findViewById(R.id.corporate);
        loginTxt = findViewById(R.id.login_txt);
        emailEdit = findViewById(R.id.email_edit);
        logMailLay = findViewById(R.id.login_mail_layout);
        logPassLay = findViewById(R.id.login_pass_layout);
        passEdit = findViewById(R.id.pass_edit);
        mic = findViewById(R.id.microbes);
        mott = findViewById(R.id.motto);
        rc = findViewById(R.id.rc);
        titleLayout = findViewById(R.id.title_motto_layout);
        descBox = findViewById(R.id.description);
        signInFrame = findViewById(R.id.sign_in_frame);
        signupFrame = findViewById(R.id.sign_up_frame);

        signBtnLay = findViewById(R.id.signup_btn_layout);
        logBtnLay = findViewById(R.id.login_btn_layout);
        homePageLay = findViewById(R.id.home_page);

        name_title_edit = findViewById(R.id.signup_title_edit);
        surname_edit = findViewById(R.id.signup_surname_edit);
        firstname_edit = findViewById(R.id.signup_firstname_edit);
        signup_email_edit = findViewById(R.id.signup_email_edit);
        mobile_edit = findViewById(R.id.signup_mobile_edit);
        company_edit = findViewById(R.id.signup_company_edit);
        job_edit = findViewById(R.id.signup_jobrole_edit);
        gender_edit = findViewById(R.id.signup_sex_edit);
        address_edit = findViewById(R.id.signup_personal_address_edit);
        city_edit = findViewById(R.id.signup_city_edit);
        country_edit = findViewById(R.id.signup_country_edit);
        signup_pass_edit = findViewById(R.id.signup_pass_edit);
        signup_pass_confirm_edit = findViewById(R.id.signup_confirmpass_edit);

        upTitle = findViewById(R.id.signup_title_layout);
        upSur = findViewById(R.id.signup_surname_layout);
        upFirst = findViewById(R.id.signup_firstname_layout);
        upMail = findViewById(R.id.signup_email_layout);
        upPhone = findViewById(R.id.signup_mobile_layout);
        upCo = findViewById(R.id.signup_company_layout);
        upJob = findViewById(R.id.signup_jobrole_layout);
        upSex = findViewById(R.id.signup_sex_layout);
        upAddress = findViewById(R.id.signup_personal_address_layout);
        upCity = findViewById(R.id.signup_city_layout);
        upCountry = findViewById(R.id.signup_country_layout);
        upPassword = findViewById(R.id.signup_pass_layout);
        upConfirmPass = findViewById(R.id.signup_confirmpass_layout);


    }

    private void clickers(){

        TandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PrivacyActivity.class));
                overridePendingTransition(0,0);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateService.loginWithFirebase(activity, v, password);
//                startActivity(new Intent(getApplicationContext(),MeetBoard.class));
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(signup_email.contains("@") && signup_email.contains(".")){
                    if(signup_password.equals(signup_confirm_password)){

                        signBtnLay.setVisibility(View.VISIBLE);
                        if(!surname.isEmpty() && !firstname.isEmpty() && !signup_email.isEmpty() && !mobile.isEmpty() && !UpdateService.country.isEmpty()){

                            if(licenseBox.isChecked()){

                                v.setVisibility(View.GONE);

                                /**register new user */
                                User user = new User(nameTitle,surname,firstname,signup_email.replace(".",""),mobile,company,jobRole,sex,UpdateService.address,city,UpdateService.country,signup_password, title.getText().toString());
                                UpdateService.postUserInformation(activity,v,user,signup_email,signup_password);

                            }else{
                                Snackbar.make(individual,"Agree to our privacy policy before proceeding",Snackbar.LENGTH_LONG).show();

                            }


                        }else{
                            Snackbar.make(individual,"fields marked '*' are important!",Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        Snackbar.make(individual,"passwords do not match!",Snackbar.LENGTH_SHORT).show();
                    }
                }else if(signup_email.isEmpty()){
                }else{
                    Snackbar.make(individual,"invalid email address",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupFrame.setEnabled(true);

                signInFrame.animate().translationX(1500.0f).setDuration(500).start();
                signupFrame.animate().translationX(0.0f).setDuration(500).start();

                signInFrame.setEnabled(false);
            }
        });
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFrame.setEnabled(true);

                signInFrame.animate().translationX(0.0f).setDuration(500).start();
                signupFrame.animate().translationX(-1500.0f).setDuration(500).start();

                signupFrame.setEnabled(false);
            }
        });
        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLayout.animate().alpha(0.0f).setDuration(500).start();
                signInFrame.animate().translationX(0.0f).setDuration(700).start();
                individual.setBackgroundColor(getResources().getColor(R.color.transparent));
                agent.setBackgroundColor(getResources().getColor(R.color.transparent));
                corporate.setBackgroundColor(getResources().getColor(R.color.transparent));
                v.setBackgroundResource(R.drawable.black_solid);

                headingDesc.setVisibility(View.GONE);
                subHeadingDesc.setVisibility(View.GONE);
                peopleLayout.setVisibility(View.GONE);

                agent.setEnabled(false);
                corporate.setEnabled(false);

                agent.setAlpha(0.25f);
                corporate.setAlpha(0.25f);

                title.setText("INDIVIDUAL");
                subtitle.setText("Log in as an 'individual'\n\n");
                alcType = "INDIVIDUAL";

                signup_title.setText("INDIVIDUAL");
                signup_subtitle.setText("Creating account as an 'individual'\n\n"+indDesc);

                individual.setOnClickListener(null);

                peopleLayout.animate().alpha(0.0f).translationY(100.f).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();


                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

                params.setMargins(20,40,20,20);
                signupFrame.setLayoutParams(params);
                signInFrame.setLayoutParams(params);

            }
        });
        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLayout.animate().alpha(0.0f).setDuration(500).start();
                signInFrame.animate().translationX(0.0f).setDuration(700).start();
                individual.setBackgroundColor(getResources().getColor(R.color.transparent));
                agent.setBackgroundColor(getResources().getColor(R.color.transparent));
                corporate.setBackgroundColor(getResources().getColor(R.color.transparent));
                v.setBackgroundResource(R.drawable.black_solid);

                headingDesc.setVisibility(View.GONE);
                subHeadingDesc.setVisibility(View.GONE);
                peopleLayout.setVisibility(View.GONE);

                individual.setEnabled(false);
                corporate.setEnabled(false);
                individual.setAlpha(0.25f);
                corporate.setAlpha(0.25f);

                title.setText("AGENT");
                subtitle.setText("Log in as a referring 'agent'\n\n");
                alcType = "AGENT";

                signup_title.setText("AGENT");
                signup_subtitle.setText("Creating account as a referring 'agent'\n\n"+agnDesc);

                agent.setOnClickListener(null);
                peopleLayout.animate().alpha(0.0f).translationY(100.f).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

                params.setMargins(20,40,20,20);
                signupFrame.setLayoutParams(params);
                signInFrame.setLayoutParams(params);
            }
        });
        corporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLayout.animate().alpha(0.0f).setDuration(500).start();
                signInFrame.animate().translationX(0.0f).setDuration(700).start();
                individual.setBackgroundColor(getResources().getColor(R.color.transparent));
                agent.setBackgroundColor(getResources().getColor(R.color.transparent));
                corporate.setBackgroundColor(getResources().getColor(R.color.transparent));
                v.setBackgroundResource(R.drawable.black_solid);

                headingDesc.setVisibility(View.GONE);
                subHeadingDesc.setVisibility(View.GONE);
                peopleLayout.setVisibility(View.GONE);

                individual.setEnabled(false);
                agent.setEnabled(false);
                individual.setAlpha(0.25f);
                agent.setAlpha(0.25f);

                title.setText("CORPORATE");
                subtitle.setText("Log in to your 'corporate' account\n\n");
                alcType = "CORPORATE";

                signup_title.setText("CORPORATE");
                signup_subtitle.setText("Creating a 'corporate' account for partnership & sponsorship\n\n"+copDesc);

                corporate.setOnClickListener(null);
                peopleLayout.animate().alpha(0.0f).translationY(100.f).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

                params.setMargins(20,40,20,20);
                signupFrame.setLayoutParams(params);
                signInFrame.setLayoutParams(params);
            }
        });
        signInFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        emailEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                activateEditText(emailEdit,logMailLay);
                disableEditText(passEdit,logPassLay);
                return false;
            }
        });
        passEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                activateEditText(passEdit,logPassLay);
                disableEditText(emailEdit,logMailLay);
                return false;
            }
        });
        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    activateEditText(emailEdit,logMailLay);
                    disableEditText(passEdit,logPassLay);
                }
            }
        });
        passEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    activateEditText(passEdit,logPassLay);
                    disableEditText(emailEdit,logMailLay);
                }
            }
        });
    }

    private void focusChangers(){

        name_title_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(name_title_edit,upTitle);
                }else{
                    disableEditText(name_title_edit,upTitle);
                }
            }
        });

        surname_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(surname_edit,upSur);
                }else{
                    disableEditText(surname_edit,upSur);
                }
            }
        });
        firstname_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(firstname_edit,upFirst);
                }else{
                    disableEditText(firstname_edit,upFirst);
                }
            }
        });
        signup_email_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(signup_email_edit,upMail);
                }else{
                    disableEditText(signup_email_edit,upMail);
                }
            }
        });
        mobile_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(mobile_edit,upPhone);
                }else{
                    disableEditText(mobile_edit,upPhone);
                }
            }
        });
        company_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(company_edit,upCo);
                }else{
                    disableEditText(company_edit,upCo);
                }
            }
        });
        job_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(job_edit,upJob);
                }else{
                    disableEditText(job_edit,upJob);
                }
            }
        });
        gender_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(gender_edit,upSex);
                }else{
                    disableEditText(gender_edit,upSex);
                }
            }
        });
        address_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(address_edit,upAddress);
                }else{
                    disableEditText(address_edit,upAddress);
                }
            }
        });
        city_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(city_edit,upCity);
                }else{
                    disableEditText(city_edit,upCity);
                }
            }
        });
        country_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(country_edit,upCountry);
                }else{
                    disableEditText(country_edit,upCountry);
                }
            }
        });
        signup_pass_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(signup_pass_edit,upPassword);
                }else{
                    disableEditText(signup_pass_edit,upPassword);
                }
            }
        });
        signup_pass_confirm_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    execBG(signup_pass_confirm_edit,upConfirmPass);
                }else{
                    disableEditText(signup_pass_confirm_edit,upConfirmPass);
                }
            }
        });

    }
    private void editTextWatchers(){

        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                activateEditText(emailEdit,logMailLay);
                disableEditText(passEdit,logPassLay);


                email = s.toString();
                signup_email = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = s.toString();
                signup_email = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

                email = s.toString();
                signup_email = s.toString();

            }
        });
        passEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                activateEditText(passEdit,logPassLay);
                disableEditText(emailEdit,logMailLay);


                password = s.toString();
                signup_password = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                signup_password = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

                password = s.toString();
                signup_password = s.toString();
            }
        });
        name_title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTitle = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        surname_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                surname = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        firstname_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstname = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup_email_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                signup_email = s.toString();
                email = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup_email = s.toString();
                email = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

                signup_email = s.toString();
                email = s.toString();
            }
        });
        mobile_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobile = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        company_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                company = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        job_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jobRole = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        gender_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sex = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateService.address = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                city = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        country_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateService.country = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup_pass_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup_password = s.toString();
                password = s.toString();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup_pass_confirm_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                signup_confirm_password = s.toString();
                password = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup_confirm_password = s.toString();
                password = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(signup_password)){
                    Snackbar.make(agent,"Passwords dont match", Snackbar.LENGTH_SHORT).show();
                }

                signup_confirm_password = s.toString();
                password = s.toString();
            }
        });
    }


    private void activateEditText(AppCompatEditText edit, FrameLayout bucket){
        bucket.setBackground(ResourcesCompat.getDrawable(getResources(),R.mipmap.shadow,null));
        edit.setBackgroundResource(R.drawable.white_solid_stoke);
    }
    private void disableEditText(AppCompatEditText edit, FrameLayout bucket){
        bucket.setBackgroundColor(getResources().getColor(R.color.transparent));
        edit.setBackgroundResource(R.drawable.white_solid);
    }
    private void execBG(AppCompatEditText view, FrameLayout frame){
        upTitle.setBackgroundResource(R.color.transparent);
        upSur.setBackgroundResource(R.color.transparent);
        upFirst.setBackgroundResource(R.color.transparent);
        upMail.setBackgroundResource(R.color.transparent);
        upPhone.setBackgroundResource(R.color.transparent);
        upCo.setBackgroundResource(R.color.transparent);
        upJob.setBackgroundResource(R.color.transparent);
        upSex.setBackgroundResource(R.color.transparent);
        upAddress.setBackgroundResource(R.color.transparent);
        upCity.setBackgroundResource(R.color.transparent);
        upCountry.setBackgroundResource(R.color.transparent);
        upPassword.setBackgroundResource(R.color.transparent);
        upConfirmPass.setBackgroundResource(R.color.transparent);

        signup_email_edit.setBackgroundResource(R.drawable.white_solid);
        signup_pass_edit.setBackgroundResource(R.drawable.white_solid);
        signup_pass_confirm_edit.setBackgroundResource(R.drawable.white_solid);
        name_title_edit.setBackgroundResource(R.drawable.white_solid);
        surname_edit.setBackgroundResource(R.drawable.white_solid);
        firstname_edit.setBackgroundResource(R.drawable.white_solid);
        mobile_edit.setBackgroundResource(R.drawable.white_solid);
        company_edit.setBackgroundResource(R.drawable.white_solid);
        job_edit.setBackgroundResource(R.drawable.white_solid);
        gender_edit.setBackgroundResource(R.drawable.white_solid);
        address_edit.setBackgroundResource(R.drawable.white_solid);
        city_edit.setBackgroundResource(R.drawable.white_solid);
        country_edit.setBackgroundResource(R.drawable.white_solid);

        frame.setBackground(ResourcesCompat.getDrawable(getResources(),R.mipmap.shadow,null));
        view.setBackgroundResource(R.drawable.white_solid_stoke);
    }
    private void animateDesc(View bucket, String title){

        descBox.setAlpha(1.0f);
        
        /**move left*/
        TranslateAnimation moveLeft = new TranslateAnimation(0.0f,-1000.0f,0.0f,0.0f);
        moveLeft.setDuration(700);
        moveLeft.setInterpolator(new AccelerateInterpolator());

        /**move right*/
        TranslateAnimation moveRight = new TranslateAnimation(0.0f,1000.0f,0.0f,0.0f);
        moveRight.setDuration(700);
        moveRight.setInterpolator(new AccelerateInterpolator());

        /**fade in*/
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f,1.0f);
        fadeIn.setDuration(700);
        fadeIn.setInterpolator(new AccelerateInterpolator());

        /**expand in*/
        ScaleAnimation moveUpwards = new ScaleAnimation(0.9f,1.0f,0.9f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        moveUpwards.setInterpolator(new DecelerateInterpolator());
        moveUpwards.setDuration(5000);


        /**group animations*/
        AnimationSet groupAppear = new AnimationSet(false);
        groupAppear.addAnimation(moveUpwards);
        groupAppear.addAnimation(fadeIn);
        groupAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(title.equals(agent)){
                    bucket.startAnimation(moveLeft);
                }else if(title.equals(individual)){
                    bucket.startAnimation(moveRight);
                }else if(title.equals(corporate)){
                    bucket.startAnimation(moveUpwards);
                }else{
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        moveRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bucket.startAnimation(groupAppear);
                headingDesc.setText(agentString);
                subHeadingDesc.setText(agnDesc);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moveLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bucket.startAnimation(groupAppear);
                headingDesc.setText(individualString);
                subHeadingDesc.setText(indDesc);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moveUpwards.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bucket.startAnimation(groupAppear);
                headingDesc.setText(corporateString);
                subHeadingDesc.setText(copDesc);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bucket.startAnimation(groupAppear);


    }
}