package com.example.jorge.hellojesus.helpApp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

public class HelpAppActivity extends AppCompatActivity implements HelpContract.View {

    public ImageView vievHelp;

    public ConstraintLayout constraintLayoutHelp;

    private HelpContract.UserActionsListener mActionsListener;
    private static AtomicInteger lastFldId = null;

    List<String> name = null;
    List<String> description  = null;
    List<Integer>  x = null;
    List<Integer> y  = null;

    TextView textHelp = null;



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


        constraintLayoutHelp = (ConstraintLayout) findViewById(R.id.constraintLayoutHelp);
        textHelp = (TextView) findViewById(R.id.text_explanation);
        textHelp.setBackgroundColor(getColorWithAlpha(newColorRed, 0.6f));

        vievHelp = (ImageView) findViewById(R.id.helpView);


        mActionsListener = new HelpPresenter(
                Injection.provideWordsRepository(getApplicationContext()), this);

        name = (List<String>) getIntent().getSerializableExtra("HELP_ID");
        x = (List<Integer>) getIntent().getSerializableExtra("HELP_X");
        y = (List<Integer>) getIntent().getSerializableExtra("HELP_Y");


        for (int i = 0; i < name.size(); i++){
            // Create TextView
            ImageView imageHelp = new ImageView(this);
            imageHelp.setX(x.get(i));
            imageHelp.setY(y.get(i));
            imageHelp.setTag(Integer.toString(i));
            ConstraintLayout.LayoutParams lm = new ConstraintLayout.LayoutParams((int) getResources().getDimension(R.dimen.image_help_width), (int) getResources().getDimension(R.dimen.image_help_width));
            imageHelp.setLayoutParams(lm);
            imageHelp.setImageResource(R.mipmap.ic_help);
            imageHelp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    textHelp.setText(name.get(Integer.parseInt(v.getTag().toString())));
                }
            });
            constraintLayoutHelp.addView(imageHelp);
        }

        loadSessionConfig();






      //  vievHelp.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        vievHelp.setBackgroundColor(getColorWithAlpha(newColor, 0.2f));
        vievHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        //vievHelp2.setBackgroundColor(getColorWithAlpha(newColor, 0.6f));
   //     constraintLayoutHelp.setBackgroundColor(getColorWithAlpha(newColor, 0.0f));


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

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }




    @Override
    public void setPresenter(HelpContract.UserActionsListener presenter) {
        mActionsListener = checkNotNull(presenter);
    }
}
