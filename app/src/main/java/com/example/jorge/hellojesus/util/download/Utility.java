package com.example.jorge.hellojesus.util.download;

import android.os.Environment;

/**
 * Created by jorge on 30/11/2017.
 * Data for support all App
 */

public class Utility {

    /** Base URL for get Json */
    public final static String BASE_URL =
            "http://pbmedia.pepblast.com/";

    /** Base URL for get Json */
    public final static String COMPLEMENT_URL = "/pz_challenge/assets.json";

    /** code permission for download */
    public static final int PERMISSION_REQUEST_CODE = 1;

    /** Message progress for download */
    public static final String MESSAGE_PROGRESS = "message_progress";


    /** Const for PUT EXTRA*/
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_DOWNLOAD = "extra_download";
    public static final String EXTRA_POSITION_NUMBER = "extra_position_number";

    public static final String KEY_EXTRA_DATA = "key_extra_data";


    public static final String BASE_URL_IMAGE = "https://firebasestorage.googleapis.com/v0/b/mycourseenglis.appspot.com/o/";
    public static final String BASE_URL_IMAGE_COMPLEMENT = "assets/";

    public static final String BASE_URL_DOWNLOAD = "https://firebasestorage.googleapis.com/v0/b/mycourseenglis.appspot.com/o/";


   // public static final String BASE_STORAGE =  Environment.getExternalStoragePublicDirectory(BASE_STORAGE).toString();//Environment.getRootDirectory().getAbsolutePath();

   public static final String BASE_STORAGE =  Environment.getDataDirectory().getAbsolutePath() + "/user/0/com.example.jorge.hellojesus/files/";

    public static final String TAG_INFORMATION = "information";

    public static final String TAG_LOGIN_FIREBASE = "TAG_LOGIN_FIREBASE";

    public static final String   FILE_DOWNLOAD_COMPLETE = "File Download Complete";


}
