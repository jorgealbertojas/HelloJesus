package com.example.jorge.hellojesus.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.hellojesus.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.jorge.hellojesus.util.download.Utility.TAG_LOGIN_FIREBASE;

public class LoginFragment extends Fragment
{
    TextView button_label;
    View button_login;
    private static FirebaseAuth mAuth;

    public static EditText mainEditText2;
    public static EditText mainEditText3;
    public static EditText mainEditText1;
    public static ImageView mainImageView3;

    View form_login, imglogo, label_create, darkoverlay, label_forget, label_signup;
    KenBurnsView kbv;
    private DisplayMetrics dm;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_login, container, false);
        dm=getResources().getDisplayMetrics();
        form_login=v.findViewById(R.id.form_login);
        imglogo=v.findViewById(R.id.fragmentloginLogo);
        kbv=(KenBurnsView) v.findViewById(R.id.fragmentloginKenBurnsView1);
        darkoverlay=v.findViewById(R.id.fragmentloginView1);
        mAuth = FirebaseAuth.getInstance();
        mainEditText1 = v.findViewById(R.id.mainEditText1);
        mainEditText2 = v.findViewById(R.id.mainEditText2);
        mainEditText3 = v.findViewById(R.id.mainEditText3);
        mainImageView3 = v.findViewById(R.id.mainImageView3);

        button_label = (TextView) getActivity().findViewById(R.id.button_label);
        button_login = getActivity().findViewById(R.id.button_login);

        label_signup = v.findViewById(R.id.label_signup);

        label_create=v.findViewById(R.id.label_create);
        label_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainEditText3.setVisibility(View.VISIBLE);
                mainImageView3.setVisibility(View.VISIBLE);
                button_login.setTag(4);
                button_label.setText(getActivity().getResources().getString(R.string.create));
            }
        });

        label_forget=v.findViewById(R.id.label_forget);
        label_forget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emailAddress = mainEditText1.getText().toString();

                if (notNullEmail(emailAddress)){
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG_LOGIN_FIREBASE, "Email sent.");

                                        Toast.makeText(getActivity(), getResources().getString(R.string.login_email_send),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }

            }
        });



        return v;
    }

    public boolean notNullEmail(String email) {


        if (email != null ) {
            if (!email.toString().equals("") ) {
                return true;
            }
        }

        Toast.makeText(getActivity(), getResources().getString(R.string.login_email_null),
                Toast.LENGTH_SHORT).show();
        return false;


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        kbv.setTransitionGenerator(generator);
        //imglogo.animate().setStartDelay(5000).setDuration(2000).alpha(1).start();
        //YoYo.with(Techniques.BounceInDown)
        //        .duration(2000)
       //         .delay(5000)
        //       .playOn(imglogo);
        darkoverlay.animate().setStartDelay(5000).setDuration(2000).alpha(0.6f).start();
        label_signup.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
        form_login.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
        form_login.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(6000).start();
    }
}
// auto typing with adb (for demo purpose)
// sleep 6;input text "agusibrahim@mail.com";input keyevent 61;input text "thisispasswd"

