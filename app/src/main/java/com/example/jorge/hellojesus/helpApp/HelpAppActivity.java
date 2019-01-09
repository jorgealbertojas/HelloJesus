package com.example.jorge.hellojesus.helpApp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.util.Common;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.common.base.Preconditions.checkNotNull;

public class HelpAppActivity extends AppCompatActivity implements HelpContract.View, View.OnTouchListener {

    public RelativeLayout constraintLayoutHelp;

    public CardView cardView;

    public ImageView ic_move_down_up;
    public ImageView ic_close;

    private HelpContract.UserActionsListener mActionsListener;
    private static AtomicInteger lastFldId = null;

    List<String> name = null;
    List<String> description  = null;
    List<Integer>  x = null;
    List<Integer> y  = null;
    List<String> top  = null;

    TextView textHelp = null;

    private int _xDelta;
    private int _yDelta;



    private void loadSessionConfig(){
        try {

         //   mActionsListener = new HelpPresenter(Injection.provideWordsRepository(this),this);

       //     mActionsListener.loadHelp();

        } catch (Exception e) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_app);

        setTitle("");

        final int newColor = getResources().getColor(R.color.NoAccent2);
        final int newColorRed = getResources().getColor(R.color.red);


        constraintLayoutHelp = (RelativeLayout) findViewById(R.id.constraintLayoutHelp);
        textHelp = (TextView) findViewById(R.id.text_explanation);
        cardView = (CardView) findViewById(R.id.cardViewHelp);


        ic_move_down_up = (ImageView) findViewById(R.id.ic_move_down_up);

        ic_move_down_up.setOnTouchListener(this);
        ic_close = (ImageView) findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        textHelp.setBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.6f));

        mActionsListener = new HelpPresenter(
                Injection.provideWordsRepository(getApplicationContext()), this);

        name = (List<String>) getIntent().getSerializableExtra("HELP_ID");
        x = (List<Integer>) getIntent().getSerializableExtra("HELP_X");
        y = (List<Integer>) getIntent().getSerializableExtra("HELP_Y");
        top = (List<String>) getIntent().getSerializableExtra("HELP_TOP");


        for (int i = 0; i < name.size(); i++){
            // Create TextView
            CircleImageView imageHelp = new CircleImageView(this);
            imageHelp.setX(x.get(i));
            imageHelp.setY(y.get(i));
            imageHelp.setCircleBackgroundColor(Common.getColorWithAlpha(newColorRed, 0.6f));

             imageHelp.setTag(Integer.toString(i));
            ConstraintLayout.LayoutParams lm = new ConstraintLayout.LayoutParams((int) getResources().getDimension(R.dimen.image_help_width), (int) getResources().getDimension(R.dimen.image_help_width));
            imageHelp.setLayoutParams(lm);
            imageHelp.setImageResource(R.mipmap.ic_help);
            imageHelp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Animation pulse = AnimationUtils.loadAnimation(((ImageView) v).getContext(), R.anim.pulse);
                    //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                   // pulse.setInterpolator(interpolator);
                    //((ImageView) v).startAnimation(pulse);
                    EmptyAnimation(constraintLayoutHelp);
                    ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            ((ImageView) v),
                            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                            PropertyValuesHolder.ofFloat("scaleY", 1.2f));
                    scaleDown.setDuration(310);

                    scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
                    scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

                    scaleDown.start();

                    textHelp.setText(name.get(Integer.parseInt(v.getTag().toString())));
                    v.setTag("00");
                }
            });
            constraintLayoutHelp.addView(imageHelp);
            if (top.get(i).equals("1")) {
               cardView.setY(y.get(i)  + (cardView.getPaddingTop() * 3) );
            }
        }

        loadSessionConfig();

        String name5e = null;
        Field[] campose = R.id.class.getFields();
        for(Field fe:campose){
            try{
                if(constraintLayoutHelp.getId()==fe.getInt(null)){
                    name5e = fe.getName();
                    break;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }




    public void EmptyAnimation(RelativeLayout relativeLayout) {
        for (int i = 0; i < relativeLayout.getChildCount(); i++ ) {
            View child = relativeLayout.getChildAt(i);
            if (child instanceof ImageView){
                ImageView childImageView = (ImageView) child;
                if (childImageView.getTag() != null){
                    if (childImageView.getTag().toString().equals("00")){
                        childImageView.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }
    }


    @Override
    public void setPresenter(HelpContract.UserActionsListener presenter) {
        mActionsListener = checkNotNull(presenter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                //_xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                //layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                //layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                cardView.setLayoutParams(layoutParams);
                break;
        }
        constraintLayoutHelp.invalidate();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
