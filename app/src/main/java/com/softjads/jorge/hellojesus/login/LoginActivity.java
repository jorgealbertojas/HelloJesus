package com.softjads.jorge.hellojesus.login;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.main.MainActivity;


import com.softjads.jorge.hellojesus.splash.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softjads.jorge.hellojesus.util.createJson;

import android.support.annotation.NonNull;

import java.io.IOException;

import static com.softjads.jorge.hellojesus.util.download.Utility.TAG_LOGIN_FIREBASE;


public class LoginActivity extends AppCompatActivity


{

    private static FirebaseAuth mAuth;

    Fragment frag_login;
    ProgressBar pbar;
    View button_login, button_icon, ic_menu1, ic_menu2;

    TextView button_label;

    public static EditText email;
    public static EditText password;
    public static EditText confirmPassword;
    static ValueAnimator va;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pbar = (ProgressBar) findViewById(R.id.mainProgressBar1);
        button_icon = findViewById(R.id.button_icon);
        button_label = findViewById(R.id.button_label);

        // Login with FireBase
        mAuth = FirebaseAuth.getInstance();

        createJson CrunchifyJSONFileWrite = new createJson();
           try {
                CrunchifyJSONFileWrite.main(this);
           } catch (IOException e) {
                e.printStackTrace();
          }


        dm = getResources().getDisplayMetrics();
        button_login = findViewById(R.id.button_login);
        button_login.setTag(0);
        pbar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        frag_login = new LoginFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_login).commit();
        va = new ValueAnimator();
        va.setDuration(1500);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator p1) {
                RelativeLayout.LayoutParams button_login_lp = (RelativeLayout.LayoutParams) button_login.getLayoutParams();
                button_login_lp.width = Math.round((Float) p1.getAnimatedValue());
                button_login.setLayoutParams(button_login_lp);
            }


        });

        button_login.animate().translationX(dm.widthPixels + button_login.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        button_login.animate().translationX(0).setStartDelay(6500).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                if ((int) button_login.getTag() != 4) {

                    if (notNullEmailAndPassword()) {
                        ExistAccount();
                    }else{
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_login),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // CREATE NEW account WITH E-MAIL


                    if (validateEmailAndPassword()) {


                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG_LOGIN_FIREBASE, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG_LOGIN_FIREBASE, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });


                        button_login.setTag(1);
                        va.setFloatValues(button_login.getMeasuredWidth(), button_login.getMeasuredHeight());
                        va.start();
                        pbar.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
                        button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
                        button_login.animate().setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(4000).setDuration(1000).scaleX(30).scaleY(30).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator p1) {
                                pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
                            }

                            @Override
                            public void onAnimationEnd(Animator p1) {
                                try {

                                } catch (Exception e) {
                                }
                                button_login.animate().setStartDelay(0).alpha(1).setDuration(1000).scaleX(1).scaleY(1).x(dm.widthPixels - button_login.getMeasuredWidth() - 100).y(dm.heightPixels - button_login.getMeasuredHeight() - 100).setListener(new Animator.AnimatorListener() {

                                    @Override
                                    public void onAnimationStart(Animator p1) {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator p1) {

                                        Intent i = new Intent(LoginActivity.this, SplashActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                        finish();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator p1) {
                                        // TODO: Implement this method
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator p1) {
                                        // TODO: Implement this method
                                    }
                                }).start();
                            }

                            @Override
                            public void onAnimationCancel(Animator p1) {
                                // TODO: Implement this method
                            }

                            @Override
                            public void onAnimationRepeat(Animator p1) {
                                // TODO: Implement this method
                            }
                        }).start();


                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_information),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        email = frag_login.getView().findViewById(R.id.mainEditText1);
        password = frag_login.getView().findViewById(R.id.mainEditText2);
        confirmPassword = frag_login.getView().findViewById(R.id.mainEditText3);




        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);


        email.setText(sharedPref.getString(getString(R.string.preference_key_email),null));
        password.setText(sharedPref.getString(getString(R.string.preference_key_password),null));

    }

    public boolean validateEmailAndPassword() {

        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmPassword.getText().toString();

        if (Email != null && Password != null && ConfirmPassword != null && (!Email.equals("")) && (!Password.equals(""))  && (!ConfirmPassword.equals("")))  {

            if (Password.equals(ConfirmPassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean notNullEmailAndPassword() {


        if (email.getText().toString() != null && password.getText().toString() != null) {
            if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                return true;
            }
        }

        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_null),
                Toast.LENGTH_SHORT).show();
        return false;


    }

        public void ExistAccount(){

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG_LOGIN_FIREBASE, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                login();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG_LOGIN_FIREBASE, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_not_exist),
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }


        public void login(){
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.preference_key_email), email.getText().toString());
            editor.putString(getString(R.string.preference_key_password), password.getText().toString());
            editor.commit();

            if ((int) button_login.getTag() == 1) {
                return;
            } else if ((int) button_login.getTag() == 2) {
                button_login.animate().x(dm.widthPixels / 2).y(dm.heightPixels / 2).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).setListener(null).setDuration(1000).setStartDelay(0).start();
                button_login.animate().setStartDelay(600).setDuration(1000).scaleX(40).scaleY(40).setInterpolator(new EasingInterpolator(Ease.CUBIC_IN_OUT)).start();
                button_icon.animate().alpha(0).rotation(90).setStartDelay(0).setDuration(800).start();
                return;
            }
            button_login.setTag(1);
            va.setFloatValues(button_login.getMeasuredWidth(), button_login.getMeasuredHeight());
            va.start();
            pbar.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
            button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
            button_login.animate().setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(4000).setDuration(1000).scaleX(30).scaleY(30).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator p1) {
                    pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
                }

                @Override
                public void onAnimationEnd(Animator p1) {
                    try {

                    } catch (Exception e) {
                    }
                    button_login.animate().setStartDelay(0).alpha(1).setDuration(1000).scaleX(1).scaleY(1).x(dm.widthPixels - button_login.getMeasuredWidth() - 100).y(dm.heightPixels - button_login.getMeasuredHeight() - 100).setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator p1) {
                            // TODO: Implement this method
                        }

                        @Override
                        public void onAnimationEnd(Animator p1) {
                            button_icon.animate().setDuration(0).setStartDelay(0).rotation(85).alpha(1).start();
                            button_icon.animate().setDuration(2000).setInterpolator(new BounceInterpolator()).setStartDelay(0).rotation(0).start();
                            button_login.setTag(2);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                            finish();
                        }

                        @Override
                        public void onAnimationCancel(Animator p1) {
                            // TODO: Implement this method
                        }

                        @Override
                        public void onAnimationRepeat(Animator p1) {
                            // TODO: Implement this method
                        }
                    }).start();
                }

                @Override
                public void onAnimationCancel(Animator p1) {
                    // TODO: Implement this method
                }

                @Override
                public void onAnimationRepeat(Animator p1) {
                    // TODO: Implement this method
                }
            }).start();

        }


    }

