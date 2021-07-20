package com.microboss.dev;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microboss.dev.adapter_items.Session;
import com.microboss.dev.adapter_items.SessionAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import static com.microboss.dev.CautionLoading.actionButton;
import static com.microboss.dev.CautionLoading.processingText;
import static com.microboss.dev.UpdateService.DONATION;
import static com.microboss.dev.UpdateService.FB_CATEGORIES_USER;
import static com.microboss.dev.UpdateService.FB_IMAGES_FOLDER;
import static com.microboss.dev.UpdateService.PAYMENT_PRICE;
import static com.microboss.dev.UpdateService.PAYMENT_TYPE;
import static com.microboss.dev.UpdateService.READ_EXST;
import static com.microboss.dev.UpdateService.WRITE_EXST;
import static com.microboss.dev.UpdateService.alcType;
import static com.microboss.dev.UpdateService.database_alcType;
import static com.microboss.dev.UpdateService.dpFG;
import static com.microboss.dev.UpdateService.fetchedPic;
import static com.microboss.dev.UpdateService.flushDetails;
import static com.microboss.dev.UpdateService.getArchiveInformation;
import static com.microboss.dev.UpdateService.getLiveInformation;
import static com.microboss.dev.UpdateService.getUpcomingInformation;
import static com.microboss.dev.UpdateService.getUserInformation;
import static com.microboss.dev.UpdateService.uploadType;

public class MeetBoard extends AppCompatActivity {

    FrameLayout bioDataLay, topHUD, sessionsContainer;
    public static FrameLayout topLay, sponsorLay;
    LinearLayout mainLay,archLay, liveLay, donate_btn, upcominLay, storeLay, sponsorStoreLay, profileLogoLay;
    public static LinearLayout homeLay;
    ImageView optionsIMG, dropPop;
    public static ImageView uhHo;
    public static TextView fetchedName, fetchedMail, sponsorTxt,
            archTxt, liveTxt, upcTxt, logoutTxt, profileTxt;
    public static ListView archListV, liveListV, upcListV;
    boolean optionsOpened = false;
    public static SessionAdapter archiveAdapter, liveAdapter, upcomingAdapter;
    Session aSession;
    Boolean doubleBackToExitPressedOnce = false;
    public static Activity activity;

    int TAKE_IMAGE_CODE = 10001;
    int PICK_IMAGE = 1;
    String KEY_ID = "identity_document", KEY_SELFIE = "selfies";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateService.stopServiceX();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_board);




        Typeface head = Typeface.createFromAsset(getAssets(), "fonts/quickbold.ttf");
        Typeface body = Typeface.createFromAsset(getAssets(), "fonts/quickregular.ttf");
        Typeface tail = Typeface.createFromAsset(getAssets(), "fonts/quicklight.ttf");

        Typeface thick = Typeface.createFromAsset(getAssets(), "fonts/seguisbold.ttf");
        Typeface thin = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");


        UpdateService.thisUser = new User();
        activity = this;



//        archiveArrayList = new ArrayList<>();
//        liveArrayList = new ArrayList<>();
//        upcomingArrayList = new ArrayList<>();

//        Session s = new Session("Connecting The Dots","5k views | 12th May"," www.microboss.com/connecting.mp4", Uri.parse("dot image uri"));
//        Session t = new Session("Arranging The Lines","1.3k views | 19th May","www.microboss.com/arranging.mp4", Uri.parse("lines image uri"));
//        Session v = new Session("Underlining The Points","5k views | 7th June","www.microboss.com/underlining.mp4", Uri.parse("points image uri"));


        UpdateService.mDatabase = FirebaseDatabase.getInstance().getReference();
        UpdateService.mAuth = FirebaseAuth.getInstance();

        profileLogoLay = findViewById(R.id.profile_logo_lay);
        bioDataLay = findViewById(R.id.biodata_lay);
        topLay = findViewById(R.id.top_lay);
        mainLay = findViewById(R.id.main_layout);
        fetchedPic = findViewById(R.id.fetched_pic);
        fetchedName = findViewById(R.id.fetched_fullname);
        fetchedMail = findViewById(R.id.fetched_email);
        sponsorTxt = findViewById(R.id.sponsor_txt);
        profileTxt = findViewById(R.id.profile_logo_txt);
        optionsIMG = findViewById(R.id.options);
        dropPop = findViewById(R.id.drop_pop_details);
        dpFG = findViewById(R.id.dp_pack_fg);
        uhHo = findViewById(R.id.uh_oh);


        archLay = findViewById(R.id.archived_lay);
        liveLay = findViewById(R.id.live_lay);
        donate_btn = findViewById(R.id.donate_btn);
        upcominLay = findViewById(R.id.upcoming_lay);
        sponsorLay = findViewById(R.id.sponsor_lay);
        storeLay = findViewById(R.id.store_lay);
        topHUD = findViewById(R.id.hud);
        sessionsContainer = findViewById(R.id.upper_sessions_container);
        homeLay = findViewById(R.id.home_layout);
        sponsorStoreLay = findViewById(R.id.sponsor_store);

        archTxt = findViewById(R.id.archived_txt);
        liveTxt = findViewById(R.id.live_txt);
        upcTxt = findViewById(R.id.upcoming_txt);
        logoutTxt = findViewById(R.id.logout_txt);


        archListV = findViewById(R.id.archived_list);
        liveListV = findViewById(R.id.live_list);
        upcListV = findViewById(R.id.upcoming_list);


        archTxt.setTypeface(thick);
        liveTxt.setTypeface(thin);
        upcTxt.setTypeface(thin);
        profileTxt.setTypeface(thick);





        Uri imgUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.connecting_dot);
        Uri imgUri2 = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.black_solid);


        archiveAdapter = new SessionAdapter(getApplicationContext(),getArchiveInformation());
        archListV.setAdapter(archiveAdapter);
        liveAdapter = new SessionAdapter(getApplicationContext(),getLiveInformation());
        liveListV.setAdapter(liveAdapter);
        upcomingAdapter = new SessionAdapter(getApplicationContext(),getUpcomingInformation());
        upcListV.setAdapter(upcomingAdapter);


///***/
//        archiveArrayList.add(s);
//        liveArrayList.add(s);
//        upcomingArrayList.add(s);
//
//        archiveArrayList.add(t);
//        liveArrayList.add(t);
//        upcomingArrayList.add(t);
//
//        archiveArrayList.add(v);
//        liveArrayList.add(v);
//        upcomingArrayList.add(v);
///***/
//
//        //add sessions to sessionArrayList
//        archiveAdapter = new SessionAdapter(this,archiveArrayList);
//        liveAdapter = new SessionAdapter(this,liveArrayList);
//        upcomingAdapter = new SessionAdapter(this,upcomingArrayList);
//
//        archListV.setAdapter(archiveAdapter);
//        liveListV.setAdapter(liveAdapter);
//        upcListV.setAdapter(upcomingAdapter);
//
//        archListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MeetBoard.this, "arch | Title is: "+archiveArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "arch | Info is: "+archiveArrayList.get(position).getInfo(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "arch | Art is: "+archiveArrayList.get(position).getTArt().toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "arch | URL is: "+archiveArrayList.get(position).getStreamURL(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        liveListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MeetBoard.this, "live | Title is: "+archiveArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "live | Info is: "+archiveArrayList.get(position).getInfo(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "live | Art is: "+archiveArrayList.get(position).getTArt().toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "live | URL is: "+archiveArrayList.get(position).getStreamURL(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        upcListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MeetBoard.this, "upcoming | Title is: "+archiveArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "upcoming | Info is: "+archiveArrayList.get(position).getInfo(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "upcoming | Art is: "+archiveArrayList.get(position).getTArt().toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MeetBoard.this, "upcoming | URL is: "+archiveArrayList.get(position).getStreamURL(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        /***/

        if(alcType.equals("CORPORATE")){
            sponsorLay.setVisibility(View.VISIBLE);
            profileTxt.setVisibility(View.VISIBLE);
            profileLogoLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
            });

            sponsorLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(),SponsorPackages.class).putExtra(PAYMENT_TYPE,DONATION)
//                        .putExtra(PAYMENT_PRICE,"₦ "+)
                    );
                    overridePendingTransition(0,0);
//                lowPop(v.getId());
                }
            });
        }else if(alcType.equals("INDIVIDUAL")){
            donate_btn.setVisibility(View.VISIBLE);
        donate_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setAlpha(0f);

                v.animate().alpha(1.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setAlpha(1.0f);
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
        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayActivity.class).putExtra(PAYMENT_TYPE,DONATION).putExtra(PAYMENT_PRICE,""));
            }
        });
        }

        fetchedPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadType = FB_CATEGORIES_USER;

                if (Build.VERSION.SDK_INT >= 23) {

                    askForStoragePermission();

                }else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            }
        });

        archTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                archTxt.setBackgroundResource(R.drawable.white_linear_mirrored_highlight);
                liveTxt.setBackgroundColor(getResources().getColor(R.color.transparent));
                upcTxt.setBackgroundColor(getResources().getColor(R.color.transparent));

                archTxt.setTypeface(thick);
                liveTxt.setTypeface(thin);
                upcTxt.setTypeface(thin);

                archTxt.setShadowLayer(5.0f,0,0,getResources().getColor(R.color.white));
                liveTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                upcTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                return false;
            }
        });

        liveTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                liveTxt.setBackgroundResource(R.drawable.white_linear_mirrored_highlight);
                archTxt.setBackgroundColor(getResources().getColor(R.color.transparent));
                upcTxt.setBackgroundColor(getResources().getColor(R.color.transparent));

                liveTxt.setTypeface(thick);
                archTxt.setTypeface(thin);
                upcTxt.setTypeface(thin);

                liveTxt.setShadowLayer(5.0f,0,0,getResources().getColor(R.color.red_faded));
                archTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                upcTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                return false;
            }
        });
        upcTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                upcTxt.setBackgroundResource(R.drawable.white_linear_mirrored_highlight);
                archTxt.setBackgroundColor(getResources().getColor(R.color.transparent));
                liveTxt.setBackgroundColor(getResources().getColor(R.color.transparent));

                upcTxt.setTypeface(thick);
                archTxt.setTypeface(thin);
                liveTxt.setTypeface(thin);

                upcTxt.setShadowLayer(5.0f,0,0,getResources().getColor(R.color.white));
                liveTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                archTxt.setShadowLayer(0f,0,0,getResources().getColor(R.color.transparent));
                return false;
            }
        });

        archTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToView(v.getId());
            }
        });
        liveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToView(v.getId());
            }
        });
        upcTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToView(v.getId());
            }
        });
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flushDetails();
                finish();
            }
        });

//        Glide.with(c).load(CreateBlurredImage(c,bitmap, 19)).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                backg[1] = resource;
//                TransitionDrawable transitionDrawable = new TransitionDrawable(backg);
//                blurredParent.setBackground(transitionDrawable);
//                transitionDrawable.startTransition(500);
//            }
//        });


        mainLay.setAlpha(0);
        bioDataLay.setTranslationY(-1000);
        sponsorTxt.setTypeface(head);

        mainLay.animate().alpha(1.0f).setDuration(1500).setInterpolator(new AccelerateDecelerateInterpolator()).start();

        storeLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                storeLay.animate().alpha(0.1f).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        storeLay.animate().alpha(1.0f).setDuration(250).start();
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
        storeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                lowPop(v.getId());
                Toast.makeText(MeetBoard.this, "MicroBoss Store is currently under construction...", Toast.LENGTH_SHORT).show();
//                Snackbar.make(getApplicationContext(),homeLay,"", Snackbar.LENGTH_LONG).show();

            }
        });


        optionsIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionsOpened == false){

                    Uri imgPath = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.options_close);
                    optionsIMG.setImageURI(imgPath);
                    //move in
                    bioDataLay.animate().translationY(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                    profileTxt.animate().alpha(0f).setDuration(250).setInterpolator(new AccelerateInterpolator()).start();
                    optionsOpened = true;
                }else {
                    //move out

                    Uri imgPath = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.options_open);
                    optionsIMG.setImageURI(imgPath);

                    bioDataLay.animate().translationY(-1000).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                    profileTxt.animate().alpha(1.0f).setDuration(1500).setInterpolator(new DecelerateInterpolator()).start();
                    optionsOpened = false;
                }
            }
        });
        dropPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FullProfile.class).setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
            }
        });

        topLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bioDataLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        getUserInformation();

    }

//    private void lowPop(int id) {
//
//        sessionsContainer.animate().alpha(0.0f).setInterpolator(new DecelerateInterpolator()).setDuration(250).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                sponsorStoreLay.setVisibility(View.GONE);
//                sessionsContainer.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        if(id == storeLay.getId()){
//
//        }else if(id == sponsorLay.getId()){
//
//        }
//    }

    private void moveToView(int id) {
        if (id==archTxt.getId()){
            archLay.animate().translationX(0).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(250).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            liveLay.animate().translationX(1400).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
            upcominLay.animate().translationX(2100).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        }else if(id==liveTxt.getId()){
            liveLay.animate().translationX(0).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(250).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            archLay.animate().translationX(-1400).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
            upcominLay.animate().translationX(1400).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        }else if(id==upcTxt.getId()){
            upcominLay.animate().translationX(0).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(250).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            liveLay.animate().translationX(-1400).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
            archLay.animate().translationX(-2100).alpha(0.0f).scaleX(0.85f).scaleY(0.85f).setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            UpdateService.stopServiceX();
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void askForStoragePermission() {
//		andysContext = getApplicationContext();

        if (ContextCompat.checkSelfPermission(MeetBoard.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MeetBoard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
//
            if (ActivityCompat.shouldShowRequestPermissionRationale(MeetBoard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            }else {
                ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(MeetBoard.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
            }else {
                ActivityCompat.requestPermissions(MeetBoard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
            }
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                }
                selectImage();
            }
        }
    }
    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }
    private void takePhoto() {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    private void uploadToFirebase(Bitmap bitmap, String folder,String categories) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child(folder)
                .child(categories)
                .child(UpdateService.database_signup_email)
                .child(UpdateService.database_signup_email+".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void profilePicReady(Uri xx){
        fetchedPic.setImageURI(xx);
    }

    private void getDownloadUrl(StorageReference reference) {
        final String[] downloadUrl = {""};


        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUrl[0] = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");


                        if(selectedImage.getWidth()>selectedImage.getHeight()){
                            selectedImage = Bitmap.createBitmap(selectedImage,0,0,selectedImage.getHeight(),selectedImage.getHeight());
                        }else  if(selectedImage.getWidth()<selectedImage.getHeight()){
                            selectedImage = Bitmap.createBitmap(selectedImage,0,0,selectedImage.getWidth(),selectedImage.getWidth());
                        }

                        selectedImage = getResizedBitmap(selectedImage, 1000);

                        Glide.with(getApplicationContext()).load(selectedImage).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                                fetchedPic.setImageDrawable(resource);
                            }
                        });

                        dpFG.setVisibility(View.VISIBLE);
                        fetchedPic.setBackground(activity.getResources().getDrawable(R.drawable.shadowx));
                        fetchedPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        dpFG.setMaxHeight(fetchedPic.getMaxHeight());
                        dpFG.setMaxWidth(fetchedPic.getMaxWidth());
//                        dpFG.setPadding(3,2,3,7);
                        fetchedPic.setAdjustViewBounds(true);


                        uploadToFirebase(selectedImage, FB_IMAGES_FOLDER,uploadType);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                Bitmap bmp = BitmapFactory.decodeFile(picturePath);


                                if(bmp.getWidth()>bmp.getHeight()){
                                    bmp = Bitmap.createBitmap(bmp,0,0,bmp.getHeight(),bmp.getHeight());
                                }else  if(bmp.getWidth()<bmp.getHeight()){
                                    bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getWidth());
                                }

                                bmp = getResizedBitmap(bmp, 1000);

                                Glide.with(getApplicationContext()).load(bmp).into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                                        fetchedPic.setImageDrawable(resource);
                                    }
                                });

                                dpFG.setVisibility(View.VISIBLE);
                                fetchedPic.setBackground(activity.getResources().getDrawable(R.drawable.shadowx));
                                fetchedPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                dpFG.setMaxHeight(fetchedPic.getMaxHeight());
                                dpFG.setMaxWidth(fetchedPic.getMaxWidth());
                                fetchedPic.setAdjustViewBounds(true);

                                uploadToFirebase(bmp,  FB_IMAGES_FOLDER,uploadType);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}