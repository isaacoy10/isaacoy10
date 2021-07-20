package com.microboss.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.microboss.dev.avi.AVLoadingIndicatorView;
import com.microboss.dev.avi.indicators.BallBeatIndicator;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import static com.microboss.dev.CautionLoading.actionButton;
import static com.microboss.dev.CautionLoading.processingText;
import static com.microboss.dev.UpdateService.CHARGE_CARD;
import static com.microboss.dev.UpdateService.DONATION;
import static com.microboss.dev.UpdateService.FIXED;
import static com.microboss.dev.UpdateService.PAYMENT_PRICE;
import static com.microboss.dev.UpdateService.PAYMENT_TYPE;
import static com.microboss.dev.UpdateService.SPONSORSHIP_CONFIRMATION;
import static com.microboss.dev.UpdateService.database_signup_email;
import static com.microboss.dev.UpdateService.mDatabase;
import static com.microboss.dev.UpdateService.signup_email;
import static com.microboss.dev.UpdateService.sponsorship_confirmation;

public class PayActivity extends AppCompatActivity {

    LinearLayout card_lay_pay, cardHome;
    AppCompatEditText edit_card_number_pay, edit_card_mm_pay, edit_card_yy_pay, edit_card_cvv_pay, edit_card_price_pay;
    String card_number = "", card_mm = "", card_yy = "", card_cvv = "", card_price = "";
    TextView pay_button_pay, fixedPrice;
    Activity activity;
    AVLoadingIndicatorView beatsLoader;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        PaystackSdk.initialize(getApplicationContext());
        {
            activity = this;
            card_lay_pay =  findViewById(R.id.card_lay_pay);
            cardHome =  findViewById(R.id.card_home);

            edit_card_number_pay =  findViewById(R.id.edit_card_number_pay);
            edit_card_mm_pay =  findViewById(R.id.edit_card_mm_pay);
            edit_card_yy_pay =  findViewById(R.id.edit_card_yy_pay);
            edit_card_cvv_pay =  findViewById(R.id.edit_card_cvv_pay);
            edit_card_price_pay =  findViewById(R.id.edit_card_price_pay);

            pay_button_pay =  findViewById(R.id.pay_button_pay);
            fixedPrice =  findViewById(R.id.fixed_price);

            beatsLoader =  findViewById(R.id.beats_loader);

            edit_card_number_pay.addTextChangedListener(Watchers(edit_card_number_pay.getId()));
            edit_card_mm_pay.addTextChangedListener(Watchers(edit_card_mm_pay.getId()));
            edit_card_yy_pay.addTextChangedListener(Watchers(edit_card_yy_pay.getId()));
            edit_card_cvv_pay.addTextChangedListener(Watchers(edit_card_cvv_pay.getId()));
            edit_card_price_pay.addTextChangedListener(Watchers(edit_card_price_pay.getId()));



        }
        Intent launchIntent = getIntent();
        String paymentType = launchIntent.getStringExtra(PAYMENT_TYPE);
        String paymentPrice = launchIntent.getStringExtra(PAYMENT_PRICE);

        if(paymentType.equals(FIXED)){
            fixedPrice.setVisibility(View.VISIBLE);
            fixedPrice.setText(paymentPrice);
            edit_card_price_pay.setVisibility(View.GONE);

        }else if(paymentType.equals(DONATION)){
            fixedPrice.setVisibility(View.GONE);
            edit_card_price_pay.setVisibility(View.VISIBLE);

        }
        cardHome.animate().translationY(0).setDuration(500).start();
        pay_button_pay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pay_button_pay.setVisibility(View.GONE);

                if(!card_number.isEmpty() && !card_mm.isEmpty() && !card_yy.isEmpty() && !card_cvv.isEmpty() && !card_price.isEmpty()){
                    Card card =new Card(card_number,Integer.parseInt(card_mm),Integer.parseInt(card_yy),card_cvv);
                    if(card.isValid()){
                        Charge charge = new Charge();
                        charge.setCard(card);
                        charge.setCurrency("NGN");
                        charge.setAmount(Integer.parseInt(card_price+"00"));
                        charge.setEmail(database_signup_email);

                        startActivity(new Intent(getApplicationContext(),CautionLoading.class));
                        processingText.setText("Processing...");
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
                                Snackbar.make(getApplicationContext(), pay_button_pay,"Thank you for your sponsorship!", Snackbar.LENGTH_LONG)
                                        .setTextColor(activity.getResources().getColor(R.color.green))
                                        .show();

                                mDatabase.child("users").child(signup_email.replace(".","")).child("Free"+SPONSORSHIP_CONFIRMATION).setValue(sponsorship_confirmation);

                                pay_button_pay.setVisibility(View.VISIBLE);
                                pay_button_pay.setText("Done");
                                pay_button_pay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
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

                                pay_button_pay.setVisibility(View.VISIBLE);

                                try {
                                    Snackbar.make(pay_button_pay,error.getMessage(), Snackbar.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });



                    }else{
                        pay_button_pay.setVisibility(View.VISIBLE);
                        Snackbar.make(pay_button_pay,"Card is invalid, please use another card...", Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    pay_button_pay.setVisibility(View.VISIBLE);
                    Snackbar.make(pay_button_pay,"Please supply card details and try again...", Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

        });
        beatsLoader.setIndicator(new BallBeatIndicator());
        beatsLoader.setIndicatorColor(getResources().getColor(R.color.green));
    }

    private TextWatcher Watchers(int id) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                      if(id == edit_card_number_pay.getId()){
                          card_number = s.toString();
                }else if(id == edit_card_mm_pay.getId()){
                          card_mm = s.toString();
                }else if(id == edit_card_yy_pay.getId()){
                          card_yy = s.toString();
                }else if(id == edit_card_cvv_pay.getId()){
                          card_cvv = s.toString();
                }else if(id == edit_card_price_pay.getId()){
                          card_price = s.toString();
                }

            }
        };
    }
}