package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private TextView mAnswerTextView;
    private static final String KEY_INDEX="index1";
    private boolean mLooked;
    private Button mShowAnswerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            mLooked=savedInstanceState.getBoolean(KEY_INDEX,false);
            setAnswerShownResult(mLooked);
        }
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mAnswerTextView=(TextView)findViewById(R.id.answer_text_view);
        mShowAnswerButton=(Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }else mAnswerTextView.setText(R.string.false_button);
                setAnswerShownResult(true);
                mLooked=true;
            }
        });
    }
    public static boolean wasAnswerShown(Intent result){
        boolean c= result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
        System.out.println(c);
        return c;
    }
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data=new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_INDEX,mLooked);
    }
}
