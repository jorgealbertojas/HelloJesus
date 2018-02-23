package com.example.jorge.hellojesus.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;

/**
 * Created by jorge on 21/02/2018.
 */

public class Common {


    /**
     * checks if internet is ok .
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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


                        // int adapterPosition = getAdapterPosition();
                        // Content content = data.get(adapterPosition);
                        //  mClickHandler.onClick(content);
/*
                        String url = "https://www.google.com.br/search?hl=pt-BR&tbm=isch&source=hp&biw=1080&bih=1765&ei=X_Z8Wu-8K4iiwgTsvqKADw&q=" + ((TextView) view).getText().toString() + "&oq=" + ((TextView) view).getText().toString() + "&gs_l=img.3...2937.4111.0.4927.0.0.0.0.0.0.0.0..0.0....0...1ac.1.64.img..0.0.0....0.dYQYv-zXKss";

                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));*/
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
