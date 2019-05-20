package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_IS_NUM = "com.bignerdranch.android.geoquiz.answer_is_num";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private TextView mAnswerTextView;
    private static final String KEY_INDEX="index1";
    private boolean mLooked;
    private Button mShowAnswerButton;
    private int mCheatNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cheat);
        if (savedInstanceState!=null){
            mLooked=savedInstanceState.getBoolean(KEY_INDEX,false);
            setAnswerShownResult(mLooked);
        }
        mAnswerTextView=(TextView)findViewById(R.id.answer_text_view);


        mAnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mCheatNum=getIntent().getIntExtra(EXTRA_ANSWER_IS_NUM,0);
        CharSequence cs="现在的次数为"+mCheatNum;
        mAnswerTextView.setText(cs);
        mShowAnswerButton=(Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheatNum>=2){
                    mAnswerTextView.setText("查看次数已经两次");
                    return;
                }
                if (mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }else mAnswerTextView.setText(R.string.false_button);
                mCheatNum++;
                setAnswerShownResult(true);
                mLooked=true;
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    int cx=mShowAnswerButton.getWidth()/2;
                    int cy=mShowAnswerButton.getHeight()/2;
                    float radius=mShowAnswerButton.getWidth();
                    Animator anim= ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy,radius,0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public static boolean wasAnswerShown(Intent result){
        boolean c= result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
        System.out.println(c);
        return c;
    }
    public static int wasAnswerNum(Intent result){
        int c1= result.getIntExtra("jij",0);

        return c1;
    }
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data=new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        data.putExtra("jij",mCheatNum);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_INDEX,mLooked);
    }
}
