package com.microboss.dev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microboss.dev.adapter_items.Packages;
import com.microboss.dev.adapter_items.PackagesAdapter;
import com.microboss.dev.adapter_items.RecyclerViewTools.DividerItemDecoration;
import com.microboss.dev.adapter_items.RecyclerViewTools.RecyclerItemListener;
import com.microboss.dev.avi.AVLoadingIndicatorView;
import com.microboss.dev.avi.indicators.PacmanIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import static com.microboss.dev.CautionLoading.actionButton;
import static com.microboss.dev.CautionLoading.processingText;
import static com.microboss.dev.UpdateService.EVENT_CONFIRMATION;
import static com.microboss.dev.UpdateService.FB_CATEGORIES_BUSINESS;
import static com.microboss.dev.UpdateService.FB_CATEGORIES_USER;
import static com.microboss.dev.UpdateService.FB_IMAGES_FOLDER;
import static com.microboss.dev.UpdateService.READ_EXST;
import static com.microboss.dev.UpdateService.SPONSORSHIP_CONFIRMATION;
import static com.microboss.dev.UpdateService.STREAM_DATE;
import static com.microboss.dev.UpdateService.STREAM_IMAGE_URL;
import static com.microboss.dev.UpdateService.STREAM_INFO;
import static com.microboss.dev.UpdateService.STREAM_TITLE;
import static com.microboss.dev.UpdateService.STREAM_URL;
import static com.microboss.dev.UpdateService.WRITE_EXST;
import static com.microboss.dev.UpdateService.alcType;
import static com.microboss.dev.UpdateService.database_signup_email;
import static com.microboss.dev.UpdateService.event_confirmation;
import static com.microboss.dev.UpdateService.mDatabase;
import static com.microboss.dev.UpdateService.signup_email;
import static com.microboss.dev.UpdateService.sponsorship_confirmation;
import static com.microboss.dev.UpdateService.thick;
import static com.microboss.dev.UpdateService.uploadType;

public class ConfirmEventReg extends AppCompatActivity {

    LinearLayout brandLay, sponsorAndPayLayout, cardLay, bronzeBuck, silverBuck, goldBuck, platinumBuck, diamondBuck, homeLay;
    ImageView brandicon;
    AVLoadingIndicatorView sendLoader;
    TextView brandName, brandLocation, confirmRegTxt, payText, bodyMessage, sa, sb, sc, sd, se;
    AppCompatButton reserveBtn, collapseCard;
    FloatingActionButton askQ;
    HorizontalScrollView packagesHorizon;
    AppCompatEditText cardNum, cardMM, cardYY, cardCVV, askAboutEvent;
    TextView cardPrice;
    String number, month, year, cvv, price, question="";
    RecyclerView packagesRecyclerView;
    Card cardUni;
    Activity activity;
    long PRICE_TO_CHARGE = 0;
    String priceToDisplay = "";
    String bodyMessage_A = "You are about to reserve a spot for '";
    String bodyMessage_B = "' scheduled for ";
    String bodyMessage_C = ". \nDetails of your reservation will be mailed to you shortly after you reserve a spot by tapping the button below...\n" +
            "See you there!";
    int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event_reg);

        PaystackSdk.initialize(getApplicationContext());

        activity = this;

        sa = findViewById(R.id.scrolla);
        sb = findViewById(R.id.scrollb);
        sc = findViewById(R.id.scrollc);
        sd = findViewById(R.id.scrolld);
        se = findViewById(R.id.scrolle);

        brandName = findViewById(R.id.brand_name);
        packagesRecyclerView = findViewById(R.id.packages_recyclerview);
        brandLocation = findViewById(R.id.brand_location);
        sendLoader = findViewById(R.id.ask_loader);
        brandLay = findViewById(R.id.brand_container);
        cardLay = findViewById(R.id.card_lay);
        sponsorAndPayLayout = findViewById(R.id.sponsor_and_pay_lay);
        confirmRegTxt = findViewById(R.id.confirm_reg_text);
        brandicon = findViewById(R.id.brand_icon);
        reserveBtn = findViewById(R.id.reserve_btn);
        collapseCard = findViewById(R.id.collapse_card);
        askQ = findViewById(R.id.ask_q);
        payText = findViewById(R.id.pay_button);
        packagesHorizon = findViewById(R.id.packages_horizon);

        bronzeBuck = findViewById(R.id.bronze_buck);
        silverBuck = findViewById(R.id.silver_buck);
        goldBuck = findViewById(R.id.gold_buck);
        platinumBuck = findViewById(R.id.platinum_buck);
        diamondBuck = findViewById(R.id.diamond_buck);
        homeLay = findViewById(R.id.home_layout);

        cardNum = findViewById(R.id.edit_card_number);
        cardMM = findViewById(R.id.edit_card_mm);
        cardYY = findViewById(R.id.edit_card_yy);
        cardCVV = findViewById(R.id.edit_card_cvv);
        askAboutEvent = findViewById(R.id.ask_about_event);
        cardPrice = findViewById(R.id.edit_card_price);
        bodyMessage = findViewById(R.id.body_message_text);

        packagesRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.options_close)));
        packagesRecyclerView.setHasFixedSize(true);

        sendLoader.setIndicator(new PacmanIndicator());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        packagesRecyclerView.setLayoutManager(llm);

        /**populate recyclerview here*/
        ArrayList<Packages> packageType = new ArrayList<>();
        packageType.add(new Packages("BRONZE",getString(R.string.BRONZE_package),"₦200k",R.drawable.package_green_solid));
        packageType.add(new Packages("SILVER","Brand website and product visibility • Recognized partner • Gets brand featured in our event • Share live screen","₦300k",R.drawable.package_yellow_solid));
        packageType.add(new Packages("GOLD","Upgrade to Panelist • Brand website and product visibility • Recognized partner • Gets brand featured in our event • Share live screen","₦500k",R.drawable.package_red_solid));
        packageType.add(new Packages("PLATINUM","Access to participants' contacts • Upgrade to Panelist • Brand website and product visibility • Recognized as 'event partner' • Gets brand featured in our event • Share live screen","₦1m",R.drawable.package_purple_solid));
        packageType.add(new Packages("DIAMOND","Brand on incentives • VIP Access • Co-sponsor access • Access to participants' contacts • Upgrade to Panelist • Brand website and product visibility • Recognized as 'event partner' • Gets brand featured in our event • Share live screen","₦1.5m",R.drawable.package_blue_solid));

        PackagesAdapter ca = new PackagesAdapter(packageType);
        packagesRecyclerView.setAdapter(ca);

        packagesRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), packagesRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        System.out.println("On Click Item interface");
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));



        cardNum.addTextChangedListener(AllWatchers("cardNum"));
        cardMM.addTextChangedListener(AllWatchers("cardMM"));
        cardYY.addTextChangedListener(AllWatchers("cardYY"));
        cardCVV.addTextChangedListener(AllWatchers("cardCVV"));
        askAboutEvent.addTextChangedListener(AllWatchers("question"));

        brandicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadType = FB_CATEGORIES_BUSINESS;

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

        sponsorAndPayLayout.setVisibility(View.GONE);

        Intent metaIntent = getIntent();

        String META_TITLE = metaIntent.getStringExtra(STREAM_TITLE);
        String META_DATE = metaIntent.getStringExtra(STREAM_DATE);
        String META_INFO = metaIntent.getStringExtra(STREAM_INFO);
        String META_VIDEO_LINK = metaIntent.getStringExtra(STREAM_URL);
        String META_IMAGE_LINK = metaIntent.getStringExtra(STREAM_IMAGE_URL);


        bodyMessage.setText(bodyMessage_A+META_TITLE.toUpperCase()+bodyMessage_C);



        if(alcType.equals("CORPORATE")){
            sponsorAndPayLayout.setVisibility(View.VISIBLE);
        }
        confirmRegTxt.setTypeface(thick);
        collapseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collapseCard.getText().equals("×")){
                    collapseCard.setText("+");
                    packagesHorizon.setVisibility(View.GONE);
                    cardLay.setVisibility(View.GONE);
                }else if(collapseCard.getText().equals("+")){
                    collapseCard.setText("×");
                    packagesHorizon.setVisibility(View.VISIBLE);
                }else if(!alcType.equals("CORPORATE")){
                    Snackbar.make(getApplicationContext(),sponsorAndPayLayout,"Sorry! Only available to corporate accounts", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });

        bronzeBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        silverBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        goldBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        platinumBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });
        diamondBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packClick(v);
            }
        });

        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child("admin").child(EVENT_CONFIRMATION).setValue(signup_email+event_confirmation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reserveBtn.setText("Reserved!");

                        reserveBtn.animate().alpha(1.0f).setDuration(2500).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                reserveBtn.setText("Exit");
                                reserveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                });

            }
        });
        askQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                if(question.isEmpty()){
                    try {
                        Snackbar.make(homeLay,"Provide a question and try again...", Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Provide a question and try again...", Toast.LENGTH_SHORT).show();
                    }
                }else {

                    mDatabase.child("users").child(signup_email.replace(".","")).child(SPONSORSHIP_CONFIRMATION).child("QUESTION").setValue(question)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    v.setVisibility(View.VISIBLE);
                                    try {
                                        Snackbar.make(homeLay,"Message sending failed! Try again...", Snackbar.LENGTH_SHORT).show();
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                        Toast.makeText(activity, "Message sending failed! Try again...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            askAboutEvent.setText("Message sent! Response would be mailed back shortly...");
                            v.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        payText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                if(!number.isEmpty() && !month.isEmpty() && !year.isEmpty() && !cvv.isEmpty()){
                    cardUni = new Card(number,Integer.valueOf(month),Integer.parseInt(year),cvv);

                    if(cardUni.isValid()){
                        Charge charge = new Charge();
                        charge.setCard(cardUni);
                        charge.setAmount((int)PRICE_TO_CHARGE);
                        charge.setCurrency("NGN");
                        charge.setEmail(database_signup_email);

                        startActivity(new Intent(getApplicationContext(),CautionLoading.class).putExtra("BODY","Processing...").putExtra("BUTTON",false).setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
//                        payText.setText("Sent!");
//                        payText.animate().alpha(1.0f).setDuration(700).setListener(new )
                        actionButton.setVisibility(View.GONE);

                        PaystackSdk.chargeCard(activity, charge, new Paystack.TransactionCallback() {
                            @Override
                            public void onSuccess(Transaction transaction) {

                                processingText.setText("Processed!");
                                processingText.animate().alpha(1.0f).setDuration(2000).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        finish();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();

                                try {
                                    Snackbar.make(getApplicationContext(), payText,"Thank you for your sponsorship!", Snackbar.LENGTH_LONG)
                                            .setTextColor(activity.getResources().getColor(R.color.green))
                                            .show();
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(activity, "Thank you for your sponsorship!", Toast.LENGTH_SHORT).show();

                                }

                                mDatabase.child("users").child(signup_email.replace(".","")).child(SPONSORSHIP_CONFIRMATION).setValue(sponsorship_confirmation);
                                payText.setVisibility(View.VISIBLE);
                                v.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        payText.setText("Done!");
                                        v.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                            }

                            @Override
                            public void beforeValidate(Transaction transaction) {

                            }

                            @Override
                            public void onError(Throwable error, Transaction transaction) {
                                processingText.setText("Unsuccessful!\nPlease try again...");
                                processingText.animate().alpha(1.0f).setDuration(2000).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        finish();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();

                                payText.setVisibility(View.VISIBLE);
                                try {
                                    Snackbar.make(getApplicationContext(),reserveBtn,error.getMessage(), Snackbar.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else{
                        try {
                            Snackbar.make(getApplicationContext(), payText,"Card is not valid!", Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Card is not valid!", Toast.LENGTH_SHORT).show();

                        }
                        payText.setVisibility(View.VISIBLE);
                    }
                }else {
                    payText.setVisibility(View.VISIBLE);

                    try {
                        Snackbar.make(getApplicationContext(),reserveBtn,"Ensure to provide card details and try again", Snackbar.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void packClick(View view) {
        if(cardLay.getVisibility()==View.GONE){
            cardLay.setVisibility(View.VISIBLE);

        }else if(cardLay.getVisibility()==View.VISIBLE){
            cardLay.setVisibility(View.GONE);
        }

        if(view.getId() == R.id.bronze_buck || view.getId() == R.id.scrolla){
            PRICE_TO_CHARGE = 20000000;
            priceToDisplay = "₦200k";
        }else if(view.getId() == R.id.silver_buck || view.getId() == R.id.scrollb){
            PRICE_TO_CHARGE = 30000000;
            priceToDisplay = "₦300k";
        }else if(view.getId() == R.id.gold_buck || view.getId() == R.id.scrollc){
            PRICE_TO_CHARGE = 50000000;
            priceToDisplay = "₦500k";
        }else if(view.getId() == R.id.platinum_buck || view.getId() == R.id.scrolld){
            PRICE_TO_CHARGE = 100000000;
            priceToDisplay = "₦1m";
        }else if(view.getId() == R.id.diamond_buck || view.getId() == R.id.scrolle){
            PRICE_TO_CHARGE = 150000000;
            priceToDisplay = "₦1.5m";
        }

        cardPrice.setText(priceToDisplay);
    }

    private TextWatcher AllWatchers(String cardInfo) {
        TextWatcher universalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cardInfo.equals("cardNum")){
                    number = s.toString();
                }else if(cardInfo.equals("cardMM")){
                    month = s.toString();
                }else if(cardInfo.equals("cardYY")){
                    year = s.toString();
                }else if(cardInfo.equals("cardCVV")){
                    cvv = s.toString();
                }else if(cardInfo.equals("cardPrice")){
                    price = s.toString();
                }else if(cardInfo.equals("question")){
                    question = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return universalWatcher;
    }
    private void askForStoragePermission() {
//		andysContext = getApplicationContext();

        if (ContextCompat.checkSelfPermission(ConfirmEventReg.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ConfirmEventReg.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
//
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConfirmEventReg.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            }else {
                ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXST);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConfirmEventReg.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
            }else {
                ActivityCompat.requestPermissions(ConfirmEventReg.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXST);
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

    private void uploadToFirebase(Bitmap bitmap, String folder, String categories) {
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
                        brandicon.setImageBitmap(selectedImage);



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


                                brandicon.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                uploadToFirebase(BitmapFactory.decodeFile(picturePath),  FB_IMAGES_FOLDER,uploadType);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}