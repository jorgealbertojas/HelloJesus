package com.example.jorge.hellojesus.tipWord;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.helloWord.HelloWordContract;
import com.example.jorge.hellojesus.util.Common;
import com.example.jorge.hellojesus.util.buttonAnimation.MorphingButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TipWordFragment extends Fragment {


    private ViewGroup mContainer;
    private static int[] locationInScreen = new int[]{0,0};

    private static int mWidth = 0;

    private int mMorphCounter1 = 1;
    private int mMorphCounter2 = 1;
    private int mMorphCounter3 = 1;
    private int mMorphCounter4 = 1;
    private int mMorphCounter5 = 1;
    private int mMorphCounter6 = 1;

    private static List<String> mHelloWord;

    private TextView mTextValue;

    private static int mPosition = 0;

    public static TipWordFragment newInstance(int x, int y, int width, List<String> stringList, int position) {
        mHelloWord = stringList;
        mPosition = position;
        locationInScreen[0] = x;
        locationInScreen[1] = y;
        mWidth = width;
        return new TipWordFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_word, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContainer = container;

        final int newColorRed = getResources().getColor(R.color.colorAccent);

        container.setBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.8f));

        if (container != null) {
            doCircularReveal(container);
        }



        mTextValue = (TextView) view.findViewById(R.id.textValue);
        mTextValue.setText(mHelloWord.get(mPosition));


/*        final MorphingButton btnMorph1 = (MorphingButton) view.findViewById(R.id.btnMorph1);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        //        onMorphButton1Clicked(btnMorph1,mHelloWord.getMtip1());
            }
        });

        final MorphingButton btnMorph2 = (MorphingButton) view.findViewById(R.id.btnMorph2);
        btnMorph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // onMorphButton2Clicked(btnMorph2, mHelloWord.getMtip2());
            }
        });

        final MorphingButton btnMorph3 = (MorphingButton) view.findViewById(R.id.btnMorph3);
        btnMorph3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  onMorphButton3Clicked(btnMorph3, mHelloWord.getMtip3());
            }
        });*/

        final MorphingButton btnMorph4 = (MorphingButton) view.findViewById(R.id.btnMorph4);
        btnMorph4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowserImage(getContext(),mTextValue);
                onMorphButton4Clicked(btnMorph4);
            }
        });

        final MorphingButton btnMorph5 = (MorphingButton) view.findViewById(R.id.btnMorph5);
        btnMorph5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton5Clicked(btnMorph5);
                openBrowserExplanation(getContext(),mTextValue);
            }
        });

        final MorphingButton btnMorph6 = (MorphingButton) view.findViewById(R.id.btnMorph6);
        btnMorph6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton6Clicked(btnMorph6);
                openBrowserTranslate(getContext(),mTextValue);
            }
        });


     //   morphToFailure(btnMorph1, 0);
     //   morphToFailure(btnMorph2, 0);
     //   morphToFailure(btnMorph3, 0);
        morphToFailureImage(btnMorph4, 0);
        morphToFailureExplanation(btnMorph5, 0);
        morphToFailureTranslate(btnMorph6, 0);





        return view;
    }


    @OnClick(R.id.ic_close)
    protected void clickNext() {

        doExitReveal(mContainer);
    }

    public void doExitReveal(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int initialRadius = view.getWidth();
                    int endRadius = Math.max(mWidth, mWidth);

                    Animator anim = ViewAnimationUtils.createCircularReveal(view,
                            locationInScreen[0], locationInScreen[1], initialRadius, endRadius);
                    anim.setDuration(300);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {

                            super.onAnimationEnd(animation);
                            view.setVisibility(View.INVISIBLE);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

                        }
                    });

                    anim.start();
                } else {

                }
            }
        });
    }

    private void doCircularReveal(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int startRadius = mWidth;

                    int endRadius = Math.max(view.getWidth(), view.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(view,
                            locationInScreen[0], locationInScreen[1], startRadius, endRadius);
                    anim.setDuration(500);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });

                    anim.start();
                }
            }
        });
    }

/*    private void onMorphButton1Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            morphToSuccess(btnMorph);
        }
    }*/

    private void onMorphButton1Clicked(final MorphingButton btnMorph,String value) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToFailure(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),value);
        }
    }

    private void onMorphButton2Clicked(final MorphingButton btnMorph, String value) {
        if (mMorphCounter2 == 0) {
            mMorphCounter2++;
            morphToFailure(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter2 == 1) {
            mMorphCounter2 = 0;
            morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),value);
        }
    }

    private void onMorphButton3Clicked(final MorphingButton btnMorph,String value) {
        if (mMorphCounter3 == 0) {
            mMorphCounter3++;
            morphToFailure(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter3 == 1) {
            mMorphCounter3 = 0;
            morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),value);
        }
    }

    private void onMorphButton4Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter4 == 0) {
            mMorphCounter4++;
            morphToFailureImage(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter4 == 1) {
            mMorphCounter4 = 0;
           // morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),"");
            morphToSuccess(btnMorph,R.drawable.ic_collections_white_24dp, getResources().getString(R.string.button_images));
        }
    }

    private void onMorphButton5Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter5 == 0) {
            mMorphCounter5++;
            morphToFailureExplanation(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter5 == 1) {
            mMorphCounter5 = 0;
           // morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),"");
            morphToSuccess(btnMorph,R.drawable.ic_g_translate_white_24dp,getResources().getString(R.string.button_explanation));
        }
    }

    private void onMorphButton6Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter6 == 0) {
            mMorphCounter6++;
            morphToFailureTranslate(btnMorph,  getActivity().getResources().getInteger(R.integer.mb_animation));
        } else if (mMorphCounter6 == 1) {
            mMorphCounter6 = 0;
           // morphToSquare(btnMorph, getActivity().getResources().getInteger(R.integer.mb_animation),"");
            morphToSuccess(btnMorph,R.drawable.ic_translate_white_24dp,getResources().getString(R.string.button_translate));
        }
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration, String value) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_width_200))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_green))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_blue_dark))
                .text(value);
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph, int icon, String text) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(getActivity().getResources().getInteger(R.integer.mb_animation))
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_width_200))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_green))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_green_dark))
                .text(text)
                .icon(icon);

        btnMorph.morph(circle);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_red))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_red_dark))
                .icon(R.drawable.ic_check_white_24dp);
        btnMorph.morph(circle);
    }

    private void morphToFailureTranslate(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_red))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_red_dark))
                .icon(R.drawable.ic_translate_white_24dp);
        btnMorph.morph(circle);
    }

    private void morphToFailureImage(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_red))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_red_dark))
                .icon(R.drawable.ic_collections_white_24dp);
        btnMorph.morph(circle);
    }

    private void morphToFailureExplanation(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .width(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .height(getActivity().getResources().getDimensionPixelSize(R.dimen.mb_height_56))
                .color(getActivity().getResources().getColor(R.color.mb_red))
                .colorPressed(getActivity().getResources().getColor(R.color.mb_red_dark))
                .icon(R.drawable.ic_g_translate_white_24dp);
        btnMorph.morph(circle);
    }


    private String nextSpace(String phrase, TextView TextView){
        int i = 0;
        int count = 0;
        int countWord = 1;


        while (i < TextView.getText().toString().length()){

            if ((TextView.getText().toString().toString().substring(i,i+1).indexOf(" ")==0)){
                countWord ++;
            }
            i++;
        }

        i = 0;
        while (i < phrase.length()){

            if ((phrase.toString().substring(i,i+1).indexOf(" ")==0)){
                count ++;
                if (count > countWord) {
                    return phrase.substring(0,i);
                }
            }
            i++;
        }
        return phrase.substring(0,i);

    }


    public void openBrowserImage(Context context, TextView word) {
        String url = "https://www.google.com.br/search?hl=pt-BR&tbm=isch&source=hp&biw=1080&bih=1765&ei=X_Z8Wu-8K4iiwgTsvqKADw&q=" + word.getText().toString() + "&oq=" + word.getText().toString() + "&gs_l=img.3...2937.4111.0.4927.0.0.0.0.0.0.0.0..0.0....0...1ac.1.64.img..0.0.0....0.dYQYv-zXKss";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }


    public void openBrowserExplanation(Context context, TextView word) {
        String url = "https://en.oxforddictionaries.com/definition/" + word.getText().toString();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }


    public void openBrowserTranslate(Context context, TextView word) {
        String url = "https://translate.google.com.br/?hl=pt-BR&tab=wT#en/pt/" + word.getText().toString();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_arrow_back_white_24dp));

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) context, Uri.parse(url));
    }

    private int firstSpace(String textView){

        int i = textView.indexOf(" ");

        return i;

    }


    private int priorSpace(String textView, int off){
        int i = off;
        while (i > 0){

            if ((textView.toString().substring(i,i+1).indexOf(" ")==0)){
                return i;
            }
            i--;
        }
        return i;

    }

}
