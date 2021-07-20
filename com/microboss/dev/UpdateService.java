package com.microboss.dev;

import android.animation.Animator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.microboss.dev.adapter_items.Session;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.microboss.dev.LoginSignupActivity.address_edit;
import static com.microboss.dev.LoginSignupActivity.city_edit;
import static com.microboss.dev.LoginSignupActivity.company_edit;
import static com.microboss.dev.LoginSignupActivity.country_edit;
import static com.microboss.dev.LoginSignupActivity.email;
import static com.microboss.dev.LoginSignupActivity.emailEdit;
import static com.microboss.dev.LoginSignupActivity.firstname_edit;
import static com.microboss.dev.LoginSignupActivity.gender_edit;
import static com.microboss.dev.LoginSignupActivity.job_edit;
import static com.microboss.dev.LoginSignupActivity.mobile_edit;
import static com.microboss.dev.LoginSignupActivity.name_title_edit;
import static com.microboss.dev.LoginSignupActivity.passEdit;
import static com.microboss.dev.LoginSignupActivity.signup_email_edit;
import static com.microboss.dev.LoginSignupActivity.signup_pass_confirm_edit;
import static com.microboss.dev.LoginSignupActivity.signup_pass_edit;
import static com.microboss.dev.LoginSignupActivity.surname_edit;
import static com.microboss.dev.MeetBoard.activity;
import static com.microboss.dev.MeetBoard.archListV;
import static com.microboss.dev.MeetBoard.archiveAdapter;
import static com.microboss.dev.MeetBoard.fetchedMail;
import static com.microboss.dev.MeetBoard.fetchedName;
import static com.microboss.dev.MeetBoard.homeLay;
import static com.microboss.dev.MeetBoard.liveAdapter;
import static com.microboss.dev.MeetBoard.liveListV;
import static com.microboss.dev.MeetBoard.profilePicReady;
import static com.microboss.dev.MeetBoard.topLay;
import static com.microboss.dev.MeetBoard.uhHo;
import static com.microboss.dev.MeetBoard.upcListV;
import static com.microboss.dev.MeetBoard.upcomingAdapter;
import static com.microboss.dev.VideoStreamActivity.okayPlayNow;

public class UpdateService extends Service {
    public static User thisUser;
    public static FirebaseAuth mAuth;
    public static FirebaseUser mUser;
    public static DatabaseReference mDatabase;
    private static FirebaseAuth.AuthStateListener mAuthStateListener;
    public static ImageView fetchedPic, dpFG;


    static FirebaseStorage firebaseStorage;
    static StorageReference storageReference;
    static StorageReference ref;

    private static Context UpdateServiceContext;

    public static Typeface head;
    public static Typeface body;
    public static Typeface tail;

    public static String messageFromStream = "";

    public static Typeface thick;
    public static Typeface thin;
    public static ArrayList<String> videosTitleArchive;

    public static String confirmReg = "", emailMessage = "", eventName = "", password = "", signup_email = "",
            signup_password = "", signup_confirm_password = "", event_confirmation = "", sponsorship_confirmation = "";
    public static String surname = "", firstname = "", nameTitle = "", mobile = "",
            company = "", jobRole = "", address = "", sex = "", country = "", city = "", alcType = "", uploadType = "",
            FB_IMAGES_FOLDER = "images",  FB_CATEGORIES_BUSINESS = "businesspic",
            FB_CATEGORIES_USER = "userpic";

    public static String database_surname = "", database_firstname = "", database_nameTitle = "", database_mobile = "",
            database_company = "", database_jobRole = "", database_address = "", database_sex = "", database_country = "", database_city = "", database_alcType = "",
            database_signup_email = "", database_signup_password = "",SELECTED_VIDEO = "kotlin-editor.mp4",
            CHARGE_CARD = "CHARGE_CARD", PAYMENT_TYPE = "PAYMENT_TYPE", FIXED = "FIXED", DONATION = "DONATION", PAYMENT_PRICE = "PAYMENT_PRICE";
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    ;


    public static VideoView streamPlayer;

    public static String
            EVENTS = "EVENTS",
            MY_TITLE = "TITLE",
            MY_SURNAME = "SURNAME",
            MY_FIRST_NAME = "FIRST_NAME",
            MY_EMAIL = "EMAIL",
            MY_PHONE = "PHONE",
            MY_COMPANY = "COMPANY",
            MY_JOB = "JOB",
            MY_SEX = "GENDER",
            MY_ADDRESS = "ADDRESS",
            MY_CITY = "CITY",
            MY_COUNTRY = "COUNTRY",
            MY_PASSWORD = "PASSWORD",
            EVENT_CONFIRMATION = "EVENT_CONFIRMATION",
            SPONSORSHIP_CONFIRMATION = "SPONSORSHIP_CONFIRMATION",
            EVENT_NAME = "EVENT_NAME",

            MY_ACCOUNT_TYPE = "ACCOUNT_TYPE",

            STREAM_INFO = "STREAM_INFO",
            STREAM_TITLE = "STREAM_TITLE",
            STREAM_DATE = "STREAM_DATE",
            STREAM_URL = "STREAM_URL",
            STREAM_IMAGE_URL = "STREAM_IMAGE_URL",

            SECTION_ARCHIVE = "SECTION_ARCHIVE",
            SECTION_LIVE = "SECTION_LIVE",
            SECTION_UPCOMING = "SECTION_UPCOMING";






    public static void PayWithDetails(String cardNum, String cardYY, String cardMM, String cardCVV, String price){

    }

    public static Uri getVideoUrl(String link) {
        final Uri[] videoLink = new Uri[1];

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        ref = storageReference.child(link);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                videoLink[0] = uri;
                okayPlayNow(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return videoLink[0];
    }
    public static Uri getProfilePicUrl(String link) {
        final Uri[] picLink = new Uri[1];

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        ref = storageReference.child(link);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                picLink[0] = uri;
                profilePicReady(uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return picLink[0];
    }

    public static void flushDetails(){
        password = ""; signup_email = ""; signup_password = ""; signup_confirm_password = "";
        surname = ""; firstname = ""; nameTitle = ""; mobile = "";
        company = ""; jobRole = ""; address = ""; sex = ""; country = ""; city = ""; //alcType = "";

        database_surname = ""; database_firstname = ""; database_nameTitle = ""; database_mobile = "";
        database_company = ""; database_jobRole = ""; database_address = ""; database_sex = ""; database_country = ""; database_city = ""; database_alcType = "";
        database_signup_email = ""; database_signup_password = "";

        emailEdit.setText("");
        passEdit.setText("");
        signup_email_edit.setText("");
        signup_pass_edit.setText("");
        signup_pass_confirm_edit.setText("");
        name_title_edit.setText("");
        surname_edit.setText("");
        firstname_edit.setText("");
        mobile_edit.setText("");
        company_edit.setText("");
        job_edit.setText("");
        gender_edit.setText("");
        address_edit.setText("");
        city_edit.setText("");
        country_edit.setText("");
    }

    public UpdateService() {
    }

    public static void stopServiceX() {
//        UpdateServiceContext.stopService(UpdateServiceContext.getSystemService())
    }

    public static void loginWithFirebase(Activity act, View v, String password) {
        mUser = mAuth.getCurrentUser();

        if(email.contains("@") && email.contains(".") && !password.isEmpty()){
            v.setVisibility(View.GONE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        v.setVisibility(View.VISIBLE);
                        act.startActivity(new Intent(act.getApplicationContext(),MeetBoard.class));
                        act.overridePendingTransition(0,R.anim.fade_out);
                    }else {
                        v.setVisibility(View.VISIBLE);
                        Toast.makeText(act.getApplicationContext(), "FAILED: "+task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public static void postUserInformation(Activity activity, View v, User user, String signup_email, String signup_password) {

        mAuth.createUserWithEmailAndPassword(signup_email,signup_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_TITLE).setValue(nameTitle);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_FIRST_NAME).setValue(firstname);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_SURNAME).setValue(surname);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_EMAIL).setValue(signup_email);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_PHONE).setValue(mobile);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_COMPANY).setValue(company);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_JOB).setValue(jobRole);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_SEX).setValue(sex);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_ADDRESS).setValue(address);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_CITY).setValue(city);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_COUNTRY).setValue(country);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_PASSWORD).setValue(signup_confirm_password);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(EVENT_CONFIRMATION).setValue(event_confirmation);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(SPONSORSHIP_CONFIRMATION).setValue(sponsorship_confirmation);
                        mDatabase.child("users").child(signup_email.replace(".","")).child(MY_ACCOUNT_TYPE).setValue(alcType).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot ds) {

                                        if(task.isSuccessful()){
                                            activity.startActivity(new Intent(activity.getApplicationContext(),MeetBoard.class));
                                        }else{
                                            v.setVisibility(View.VISIBLE);
                                            Toast.makeText(activity.getApplicationContext(), "Failed: Please try again...", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        });
                    }
                });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
            }
        };

        videosTitleArchive = new ArrayList<>();

        try {
            head = Typeface.createFromAsset(getAssets(), "fonts/quickbold.ttf");
            body = Typeface.createFromAsset(getAssets(), "fonts/quickregular.ttf");
            tail = Typeface.createFromAsset(getAssets(), "fonts/quicklight.ttf");

            thick = Typeface.createFromAsset(getAssets(), "fonts/seguisbold.ttf");
            thin = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;


//        mDatabase.child("users").child("admin").child(SECTION_LIVE).child("1").child(STREAM_TITLE).setValue("What Are The Dots?");
//        mDatabase.child("users").child("admin").child(SECTION_LIVE).child("1").child(STREAM_INFO).setValue("7,512 Attendees | 30th June, 2022");
//        mDatabase.child("users").child("admin").child(SECTION_LIVE).child("1").child(STREAM_URL).setValue("www.microboss.org/what.mp4");
//        mDatabase.child("users").child("admin").child(SECTION_LIVE).child("1").child(STREAM_IMAGE_URL).setValue("www.microboss.org/what.jpg");
//
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("1").child(STREAM_TITLE).setValue("Upcoming Dots");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("1").child(STREAM_INFO).setValue("12 Attendees | 4th Dec, 2022");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("1").child(STREAM_URL).setValue("www.microboss.org/upc.mp4");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("1").child(STREAM_IMAGE_URL).setValue("www.microboss.org/upc.jpg");
//
//
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("2").child(STREAM_TITLE).setValue("Upcoming Dots");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("2").child(STREAM_INFO).setValue("12 Attendees | 4th Dec, 2022");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("2").child(STREAM_URL).setValue("www.microboss.org/upc.mp4");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("2").child(STREAM_IMAGE_URL).setValue("www.microboss.org/upc.jpg");
//
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("3").child(STREAM_TITLE).setValue("Upcoming Dots");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("3").child(STREAM_INFO).setValue("12 Attendees | 4th Dec, 2022");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("3").child(STREAM_URL).setValue("www.microboss.org/upc.mp4");
//        mDatabase.child("users").child("admin").child(SECTION_UPCOMING).child("3").child(STREAM_IMAGE_URL).setValue("www.microboss.org/upc.jpg");

        UpdateServiceContext = this;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public static void getUserInformation(){

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()){
//
//
//                    database_nameTitle = ds.child("users").child(email.replace(".", "")).child(MY_TITLE).getValue().toString();
//                    database_firstname = ds.child("users").child(email.replace(".", "")).child(MY_FIRST_NAME).getValue().toString();
//                    database_surname = ds.child("users").child(email.replace(".", "")).child(MY_SURNAME).getValue().toString();
//                    database_signup_email = ds.child("users").child(email.replace(".", "")).child(MY_EMAIL).getValue().toString();
//                    database_mobile = ds.child("users").child(email.replace(".", "")).child(MY_PHONE).getValue().toString();
//                    database_company = ds.child("users").child(email.replace(".", "")).child(MY_COMPANY).getValue().toString();
//                    database_jobRole = ds.child("users").child(email.replace(".", "")).child(MY_JOB).getValue().toString();
//                    database_address = ds.child("users").child(email.replace(".", "")).child(MY_ADDRESS).getValue().toString();
//                    database_city = ds.child("users").child(email.replace(".", "")).child(MY_CITY).getValue().toString();
//                    database_country = ds.child("users").child(email.replace(".", "")).child(MY_COUNTRY).getValue().toString();
//                    database_alcType = ds.child("users").child(email.replace(".", "")).child(MY_ACCOUNT_TYPE).getValue().toString();
//                    database_signup_password = ds.child("users").child(email.replace(".", "")).child(MY_PASSWORD).getValue().toString();
//
//
//
//                    fetchedName.setText(database_firstname
//                            +" "+ database_surname);
//                    fetchedMail.setText(database_signup_email +" | "+database_country);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                try {
                    database_nameTitle = ds.child("users").child(email.replace(".", "")).child(MY_TITLE).getValue().toString();
                    database_firstname = ds.child("users").child(email.replace(".", "")).child(MY_FIRST_NAME).getValue().toString();
                    database_surname = ds.child("users").child(email.replace(".", "")).child(MY_SURNAME).getValue().toString();
                    database_signup_email = ds.child("users").child(email.replace(".", "")).child(MY_EMAIL).getValue().toString();
                    database_mobile = ds.child("users").child(email.replace(".", "")).child(MY_PHONE).getValue().toString();
                    database_company = ds.child("users").child(email.replace(".", "")).child(MY_COMPANY).getValue().toString();
                    database_jobRole = ds.child("users").child(email.replace(".", "")).child(MY_JOB).getValue().toString();
                    database_address = ds.child("users").child(email.replace(".", "")).child(MY_ADDRESS).getValue().toString();
                    database_city = ds.child("users").child(email.replace(".", "")).child(MY_CITY).getValue().toString();
                    database_country = ds.child("users").child(email.replace(".", "")).child(MY_COUNTRY).getValue().toString();
                    database_alcType = ds.child("users").child(email.replace(".", "")).child(MY_ACCOUNT_TYPE).getValue().toString();
                    database_signup_password = ds.child("users").child(email.replace(".", "")).child(MY_PASSWORD).getValue().toString();

                    if(!database_alcType.equals(alcType)){
                        Snackbar.make(topLay,"Account doesn't exist for "+alcType.toUpperCase()+".\nTry again with "+database_alcType+" account type...",Snackbar.LENGTH_LONG)
                                
                                .setDuration(5000)
                                .setTextColor(MeetBoard.activity.getResources().getColor(R.color.green))
                                .show();
                    homeLay.animate().alpha(0.7f).setDuration(5000).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MeetBoard.activity.finish();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
                    }else{

                        fetchedName.setText(database_firstname
                                + " " + database_surname);
                        fetchedMail.setText(database_signup_email + "  |  " + database_country);
                        fetchedMail.setTextColor(UpdateServiceContext.getResources().getColor(R.color.purple_transparent_50));

                        StorageReference sr = FirebaseStorage.getInstance().getReference();
                        StorageReference pr = sr.child("images/userpic/"+database_signup_email+"/"+database_signup_email+".jpeg");

                        final long ONEBYTE = 1024*1024;
                        pr.getBytes(ONEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                dpBG.setVisibility(View.VISIBLE);
//                                dpBG.setAlpha(0.25f);

                                Glide.with(activity.getApplicationContext()).load(bmp).into(new SimpleTarget<Drawable>() {
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
//                                if(bmp.getHeight()>bmp.getWidth()){
//                                    fetchedPic.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//                                    fetchedPic.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                                }else {
//                                    fetchedPic.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                                    fetchedPic.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });

                        confirmReg = "Your event details will be sent to\n" + database_signup_email;

                        emailMessage = "Hi " + database_firstname +
                                "" +
                                "\n\n" +
                                "" +
                                "Thank you for registering for:\n" + eventName + "\n" +
                                "As we look forward to having you, do check page and stay up to date with future events from MicroBoss Technologies at www.microboss.org";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static ArrayList<Session> getArchiveInformation(){
        ArrayList<Session> archiveListFromDB = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                long total = ds.child("users").child("admin").child(SECTION_ARCHIVE).getChildrenCount();
                for(long i = 1; i<=total; i++){

                    String sessionTitle = ds.child("users").child("admin").child(SECTION_ARCHIVE).child(String.valueOf(i)).child(STREAM_TITLE).getValue().toString();
                    String sessionInfo  = ds.child("users").child("admin").child(SECTION_ARCHIVE).child(String.valueOf(i)).child(STREAM_INFO).getValue().toString();
                    String sessionDate  = ds.child("users").child("admin").child(SECTION_ARCHIVE).child(String.valueOf(i)).child(STREAM_DATE).getValue().toString();
                    String sessionStreamURL  = ds.child("users").child("admin").child(SECTION_ARCHIVE).child(String.valueOf(i)).child(STREAM_URL).getValue().toString();
                    String sessionImageURL  = ds.child("users").child("admin").child(SECTION_ARCHIVE).child(String.valueOf(i)).child(STREAM_IMAGE_URL).getValue().toString();

                    videosTitleArchive.add(STREAM_URL);

                    StorageReference sr = FirebaseStorage.getInstance().getReference();
                    StorageReference pr = sr.child(sessionImageURL);

                    final long ONEBYTE = 1024*1024;
                    pr.getBytes(ONEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Session session = new Session(sessionTitle,sessionInfo,sessionDate,sessionStreamURL, bytes);
                            archiveListFromDB.add(session);

                            archiveAdapter.notifyDataSetChanged();
                        }
                    });


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        archListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UpdateServiceContext.startActivity(new Intent(UpdateServiceContext,VideoStreamActivity.class)
                        .setFlags(FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(STREAM_URL,archiveListFromDB.get(position).getStreamURL())
                        .putExtra(STREAM_TITLE,archiveListFromDB.get(position).getTitle())
                        .putExtra(STREAM_DATE,archiveListFromDB.get(position).getDate())
                        .putExtra(STREAM_INFO,archiveListFromDB.get(position).getInfo())
                        .putExtra(STREAM_IMAGE_URL,archiveListFromDB.get(position).getTArt().toString())
                );
            }
        });
        return archiveListFromDB;
    }
    public static ArrayList<Session> getLiveInformation(){
        ArrayList<Session> liveListFromDB = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                long total = ds.child("users").child("admin").child(SECTION_LIVE).getChildrenCount();
                for(long i = 1; i<=total; i++){

                    String sessionTitle = ds.child("users").child("admin").child(SECTION_LIVE).child(String.valueOf(i)).child(STREAM_TITLE).getValue().toString();
                    String sessionInfo  = ds.child("users").child("admin").child(SECTION_LIVE).child(String.valueOf(i)).child(STREAM_INFO).getValue().toString();
                    String sessionDate = ds.child("users").child("admin").child(SECTION_LIVE).child(String.valueOf(i)).child(STREAM_DATE).getValue().toString();
                    String sessionStreamURL  = ds.child("users").child("admin").child(SECTION_LIVE).child(String.valueOf(i)).child(STREAM_URL).getValue().toString();
                    String sessionImageURL  = ds.child("users").child("admin").child(SECTION_LIVE).child(String.valueOf(i)).child(STREAM_IMAGE_URL).getValue().toString();


                    StorageReference sr = FirebaseStorage.getInstance().getReference();
                    StorageReference pr = sr.child(sessionImageURL);

                    final long ONEBYTE = 1024*1024;
                    pr.getBytes(ONEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Session session = new Session(sessionTitle,sessionInfo,sessionDate,sessionStreamURL, bytes);
                            liveListFromDB.add(session);

                            liveAdapter.notifyDataSetChanged();
                        }
                    });

                }
                if(total>0){
                    uhHo.setVisibility(View.GONE);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        liveListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UpdateServiceContext.startActivity(new Intent(UpdateServiceContext,VideoStreamActivity.class)
                        .putExtra(STREAM_URL,liveListFromDB.get(position).getStreamURL())
                        .putExtra(STREAM_TITLE,liveListFromDB.get(position).getTitle())
                        .putExtra(STREAM_INFO,liveListFromDB.get(position).getInfo())
                        .putExtra(STREAM_IMAGE_URL,liveListFromDB.get(position).getTArt().toString())
                );
            }
        });
        return liveListFromDB;
    }
    public static ArrayList<Session> getUpcomingInformation(){
        ArrayList<Session> upcomingListFromDB = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                long total = ds.child("users").child("admin").child(SECTION_UPCOMING).getChildrenCount();
                for(long i = 1; i<=total; i++){

                    String sessionTitle = ds.child("users").child("admin").child(SECTION_UPCOMING).child(String.valueOf(i)).child(STREAM_TITLE).getValue().toString();
                    String sessionInfo  = ds.child("users").child("admin").child(SECTION_UPCOMING).child(String.valueOf(i)).child(STREAM_INFO).getValue().toString();
                    String sessionDate  = ds.child("users").child("admin").child(SECTION_UPCOMING).child(String.valueOf(i)).child(STREAM_DATE).getValue().toString();
                    String sessionStreamURL  = ds.child("users").child("admin").child(SECTION_UPCOMING).child(String.valueOf(i)).child(STREAM_URL).getValue().toString();
                    String sessionImageURL  = ds.child("users").child("admin").child(SECTION_UPCOMING).child(String.valueOf(i)).child(STREAM_IMAGE_URL).getValue().toString();


                    StorageReference sr = FirebaseStorage.getInstance().getReference();
                    StorageReference pr = sr.child(sessionImageURL);

                    final long ONEBYTE = 1024*1024;
                    pr.getBytes(ONEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Session session = new Session(sessionTitle,sessionInfo,sessionDate,sessionStreamURL, bytes);
                            upcomingListFromDB.add(session);

                            upcomingAdapter.notifyDataSetChanged();
                        }
                    });

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        upcListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UpdateServiceContext.startActivity(new Intent(UpdateServiceContext,ConfirmEventReg.class)
                        .setFlags(FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(STREAM_URL,upcomingListFromDB.get(position).getStreamURL())
                        .putExtra(STREAM_TITLE,upcomingListFromDB.get(position).getTitle())
                        .putExtra(STREAM_INFO,upcomingListFromDB.get(position).getInfo())
                        .putExtra(STREAM_IMAGE_URL,upcomingListFromDB.get(position).getTArt().toString())
                );


            }
        });
        return upcomingListFromDB;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}