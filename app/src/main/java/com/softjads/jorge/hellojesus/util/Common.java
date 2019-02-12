package com.softjads.jorge.hellojesus.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.R;

import java.lang.reflect.Field;

/**
 * Created by jorge on 21/02/2018.
 */

public class Common {

    public static Integer lastFldId = 0;


    /**
     * Get Component Screen for
     */
    public static Integer getResourceString(String name) {


      //  if(lastFldId == null) {
            int maxFld = 0;
            String fldName = "";
            Field[] flds = R.id.class.getDeclaredFields();

            R.id inst = new R.id();

            for (int i = 0; i < flds.length; i++) {
                Field fld = flds[i];

                try {
                    int value = fld.getInt(inst);

                    if (value > maxFld) {
                        maxFld = value;
                        fldName = fld.getName();
                    }
                    if (name.equals(fld.getName())){
                          return value;
                    }

                } catch (IllegalAccessException e) {

                }
            }
            lastFldId = new Integer(maxFld);
      //  }

        return 0;
    }


    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }


    /**
     * checks if internet is ok .
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Call screen the permission for download
     */
    public static boolean checkPermission(Context context){
        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    public static RelativeLayout createTagDynamic(RelativeLayout nRelativeLayout, String[]nTags, final Context context, boolean buttonImageTag){


        int id = 0;
        int nextLine = 0;
        float tagWidth = 0;
        int fistTag = 0;

        int countLine = 1;


        for (int i = 0; i < nTags.length; i++) {

            if (countLine < 3) {
                TextView nTextView = new TextView(context);
                nTextView.setText(nTags[i].toString());

                //nTextView.setPadding(18, 12, 18, 12);
                nTextView.setPadding(0, 0, 0, 0);
                nTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf"));
                nTextView.setTextColor(context.getResources().getColor(R.color.blue));
                nTextView.setTextSize(25);
                nTextView.setId(i + 1);

                View.OnClickListener nTextViewClick = new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                    }

                };
                nTextView.setOnClickListener(nTextViewClick);

                nTextView.measure(0, 0);
                tagWidth = tagWidth + nTextView.getMeasuredWidth() + (20);

                if (buttonImageTag){

                    tagWidth = tagWidth + 20;
                }


                fistTag = fistTag + 1;

                if (tagWidth > Utilities.deviceWidth) {
                    nextLine = nextLine + 1;
                    tagWidth = 40;
                    fistTag = 1;
                    countLine = countLine + 1;
                }

                nTextView.setBackgroundResource(R.drawable.btn_blue_border);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (nextLine == 0) {
                    if (i > 0) {
                        params.addRule(RelativeLayout.BELOW, id);
                        params.addRule(RelativeLayout.RIGHT_OF, id);
                        params.addRule(RelativeLayout.END_OF, id);
                        params.addRule(RelativeLayout.ALIGN_TOP, id);
                        if (fistTag == 1) {
                            params.setMargins(40, 0, 0, 0);
                        } else {
                            params.setMargins(10, 0, 0, 0);
                        }
                    } else {
                        params.setMargins(40, 0, 0, 0);
                    }
                } else if (nextLine == 1) {
                    params.addRule(RelativeLayout.BELOW, 1);

                    params.setMargins(40, 10, 0, 0);
                    nextLine = 0;
                }


                id = i + 1;

                nTextView.setLayoutParams(params);


                nRelativeLayout.addView(nTextView);
            }
        }

        return nRelativeLayout;

    }

    public static String returnFile(String fileReturn){
        if (fileReturn.indexOf("?") > 0) {
            return fileReturn.substring(0, fileReturn.indexOf("?"));
        }else{
            return fileReturn;
        }
    }

    public static void GetConfigurationSize(Context context, Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Utilities.mScreenWidth = displaymetrics.widthPixels;

        //calculate value on current device
        Utilities.mCellWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources()
                .getDisplayMetrics());

        //get offset of list to the right (gap to the left of the screen from the left side of first item)
        final int mOffset = (Utilities.mScreenWidth / 2) - (Utilities.mCellWidth / 2);

        //HeaderItem width (blue rectangle in graphic)
        Utilities.mHeaderItemWidth = mOffset + Utilities.mCellWidth;

        // Test loading animation

        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Utilities.deviceWidth = displaymetrics.widthPixels;
        Utilities.deviceHeight = displaymetrics.heightPixels;
    }


}
