package com.microboss.dev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import static com.microboss.dev.UpdateService.MY_ADDRESS;
import static com.microboss.dev.UpdateService.MY_CITY;
import static com.microboss.dev.UpdateService.MY_COMPANY;
import static com.microboss.dev.UpdateService.MY_COUNTRY;
import static com.microboss.dev.UpdateService.MY_FIRST_NAME;
import static com.microboss.dev.UpdateService.MY_JOB;
import static com.microboss.dev.UpdateService.MY_PHONE;
import static com.microboss.dev.UpdateService.MY_SEX;
import static com.microboss.dev.UpdateService.MY_SURNAME;
import static com.microboss.dev.UpdateService.MY_TITLE;
import static com.microboss.dev.UpdateService.database_address;
import static com.microboss.dev.UpdateService.database_city;
import static com.microboss.dev.UpdateService.database_company;
import static com.microboss.dev.UpdateService.database_country;
import static com.microboss.dev.UpdateService.database_firstname;
import static com.microboss.dev.UpdateService.database_jobRole;
import static com.microboss.dev.UpdateService.database_mobile;
import static com.microboss.dev.UpdateService.database_nameTitle;
import static com.microboss.dev.UpdateService.database_sex;
import static com.microboss.dev.UpdateService.database_signup_email;
import static com.microboss.dev.UpdateService.database_surname;
import static com.microboss.dev.UpdateService.mAuth;
import static com.microboss.dev.UpdateService.mDatabase;
import static com.microboss.dev.UpdateService.mUser;
import static com.microboss.dev.UpdateService.signup_email;

public class FullProfile extends AppCompatActivity {

    LinearLayout baseLayout;

    AppCompatTextView profileHead, profileSubHeading,
    defaultTitle, defaultSurname, defaultFirstName, defaultMobile, defaultGender,
            defaultCompany, defaultRole, defaultHomeAddress, defaultCity, defaultCountry;

    AppCompatEditText newTitle, newSurname, newFirstName, newMobile, newGender,
            newCompany, newRole, newHomeAddress, newCity, newCountry;
    AppCompatButton resetPassword, saveChangesBtn, deleteProfileBtn;

    String tit = "", sur = "", fir = "", mob = "", sex = "", com = "", rol = "", add = "", cit = "", cou = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile);

        baseLayout = findViewById(R.id.base_layout);
        profileHead = findViewById(R.id.profile_head);
        profileSubHeading = findViewById(R.id.heading_message);
        defaultTitle = findViewById(R.id.default_title);
        defaultSurname = findViewById(R.id.default_surname);
        defaultFirstName = findViewById(R.id.default_firstname);
        defaultMobile = findViewById(R.id.default_mobile);
        defaultGender = findViewById(R.id.default_sex);
        defaultCompany = findViewById(R.id.default_company);
        defaultRole = findViewById(R.id.default_role);
        defaultHomeAddress = findViewById(R.id.default_home_address);
        defaultCity = findViewById(R.id.default_city);
        defaultCountry = findViewById(R.id.default_country);

        newTitle = findViewById(R.id.new_title);
        newSurname = findViewById(R.id.new_surname);
        newFirstName = findViewById(R.id.new_firstname);
        newMobile = findViewById(R.id.new_mobile);
        newGender = findViewById(R.id.new_sex);
        newCompany = findViewById(R.id.new_organisation);
        newRole = findViewById(R.id.new_role);
        newHomeAddress = findViewById(R.id.new_address);
        newCity = findViewById(R.id.new_city);
        newCountry = findViewById(R.id.new_country);
        resetPassword = findViewById(R.id.reset_password);
        saveChangesBtn = findViewById(R.id.save_changes);
        deleteProfileBtn = findViewById(R.id.delete_profile);

        tit = UpdateService.database_nameTitle;
        sur = UpdateService.database_surname;
        fir = UpdateService.database_firstname;
        mob = UpdateService.database_mobile;
        sex = UpdateService.database_sex;
        com = UpdateService.database_company;
        rol = UpdateService.database_jobRole;
        add = UpdateService.database_address;
        cit = UpdateService.database_city;
        cou = UpdateService.database_country;

        newTitle.setHint(tit);
        newSurname.setHint(sur);
        newFirstName.setHint(fir);
        newMobile.setHint(mob);
        newGender.setHint(sex);
        newCompany.setHint(com);
        newRole.setHint(rol);
        newHomeAddress.setHint(add);
        newCity.setHint(cit);
        newCountry.setHint(cou);

        newTitle.addTextChangedListener(Watcher(newTitle));
        newSurname.addTextChangedListener(Watcher(newSurname));
        newFirstName.addTextChangedListener(Watcher(newFirstName));
        newMobile.addTextChangedListener(Watcher(newMobile));
        newGender.addTextChangedListener(Watcher(newGender));
        newCompany.addTextChangedListener(Watcher(newCompany));
        newRole.addTextChangedListener(Watcher(newRole));
        newHomeAddress.addTextChangedListener(Watcher(newHomeAddress));
        newCity.addTextChangedListener(Watcher(newCity));
        newCountry.addTextChangedListener(Watcher(newCountry));

        baseLayout.animate().translationY(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAlpha(0);
                v.animate().alpha(1.0f).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();

                UpdateService.mAuth.sendPasswordResetEmail(UpdateService.database_signup_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        profileSubHeading.setText("Please check your email for the reset link!\n\n\nFollow the url or paste it into a browser to change the password. Your new password will be used at your next log in.\nTo maintain your present password, ignore the mail sent to you.");
                        profileSubHeading.setTextColor(getResources().getColor(R.color.green));
                        profileSubHeading.animate().alpha(1.0f).setDuration(10000).setListener(new Animator.AnimatorListener() {
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
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(FullProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_TITLE).setValue(tit);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_FIRST_NAME).setValue(sur);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_SURNAME).setValue(fir);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_PHONE).setValue(mob);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_COMPANY).setValue(com);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_JOB).setValue(rol);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_SEX).setValue(sex);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_ADDRESS).setValue(add);
                mDatabase.child("users").child(signup_email.replace(".","")).child(MY_CITY).setValue(cit);
                mDatabase.child("users").child(signup_email.replace(".", "")).child(MY_COUNTRY).setValue(cou).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        saveChangesBtn.setVisibility(View.GONE);
                        Toast.makeText(FullProfile.this, "Updated!", Toast.LENGTH_SHORT).show();

                        newTitle.setHint(tit);
                        newSurname.setHint(sur);
                        newFirstName.setHint(fir);
                        newMobile.setHint(mob);
                        newGender.setHint(sex);
                        newCompany.setHint(com);
                        newRole.setHint(rol);
                        newHomeAddress.setHint(add);
                        newCity.setHint(cit);
                        newCountry.setHint(cou);

                        database_surname = sur;
                        database_nameTitle = tit;
                        database_firstname = fir;
                        database_company = com;
                        database_city = cit;
                        database_country = cou;
                        database_address = add;
                        database_mobile = mob;
                        database_jobRole = rol;
                        database_sex = sex;

                    }
                });
            }
        });

        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileSubHeading.setTextColor(getResources().getColor(R.color.red));
                profileSubHeading.setText("You are about to delete your account: "+database_signup_email+".\nPlease note that this action clears your data completely off our storage servers and your deleted data(full account profile) is irrecoverable by us.\n\nAre you sure this is what you want?");
                deleteProfileBtn.setText("Confirm Delete");

            deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            MeetBoard.activity.finish();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginSignupActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    });

                }
            });

            }
        });
    }

    private TextWatcher Watcher(AppCompatEditText view) {
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveChangesBtn.setVisibility(View.VISIBLE);

                if(view.getId() == R.id.new_title){
                    tit = s.toString();

                }else if(view.getId() == R.id.new_surname){
                    sur = s.toString();

                }else if(view.getId() == R.id.new_firstname){
                    fir = s.toString();

                }else if(view.getId() == R.id.new_mobile){
                    mob = s.toString();

                }else if(view.getId() == R.id.new_sex){
                    sex = s.toString();

                }else if(view.getId() == R.id.new_organisation){
                    com = s.toString();

                }else if(view.getId() == R.id.new_role){
                    rol = s.toString();

                }else if(view.getId() == R.id.new_address){
                    add = s.toString();

                }else if(view.getId() == R.id.new_city){
                    cit = s.toString();

                }else if(view.getId() == R.id.new_country){
                    cou = s.toString();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

}