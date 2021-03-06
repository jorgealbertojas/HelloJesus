package com.softjads.jorge.hellojesus.speech;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.speech.support.MessageDialogFragment;
import com.softjads.jorge.hellojesus.util.Common;

import java.util.List;

import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_BUNDLE_CONTENT;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_LAST;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_MP3;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_NAME;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_SOURCE_NAME;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_TIME;

public class SpeechActivity extends AppCompatActivity implements MessageDialogFragment.Listener {
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private List<Content> mContents;
    private Bundle mBundle;
    private int mTime;
    private String mMp3;

    private String mName;
    private String mSatusSave;

    private String mSourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);



        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_CONTENT);
        mContents = (List<Content>) mBundle.getSerializable(EXTRA_CONTENT);
        mTime = (int) mBundle.getInt(EXTRA_CONTENT_TIME);
        mMp3 = (String) mBundle.getString(EXTRA_CONTENT_MP3);
        mSourceName = (String) mBundle.getString(EXTRA_CONTENT_SOURCE_NAME);
        mName = (String) mBundle.getString(EXTRA_CONTENT_NAME);
        mSatusSave = (String) mBundle.getString(EXTRA_CONTENT_LAST);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(SpeechFragment.newInstance(mContents,mTime,mMp3,mSatusSave,mName,mSourceName));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.speechFrame, productFragment);
        transaction.commit();


    }
    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionMessageDialog() {


        LayoutInflater factory2 = LayoutInflater.from(this);
        final View deleteDialogView2 = factory2.inflate(
                R.layout.dialog_permission, null);
        final AlertDialog deleteDialog2 = new AlertDialog.Builder(this).create();
        deleteDialog2.setView(deleteDialogView2);

        TextView nTextView2 = (TextView) deleteDialogView2.findViewById(R.id.txt_dia);
        nTextView2.setText(getString(R.string.permission_message));

        deleteDialogView2.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteDialog2.dismiss();
            }
        });

        deleteDialog2.show();
     //   MessageDialogFragment
     //           .newInstance(getString(R.string.permission_message))
     //           .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }
}
