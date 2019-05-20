package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mShowButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private boolean mIsCheater;
    private TextView mShowText;
    private int mCheatNum=0;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX1 = "index1";
    private static final String KEY_INDEX2 = "index2";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_IS_NUM = "com.bignerdranch.android.geoquiz.answer_is_num";
    private static final int REQUEST_CODE_CHEAT=0;
    private TextView mQuestionTextView;
    private int mTrueNum=0;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_one,true),
            new Question(R.string.question_two,true),
            new Question(R.string.question_three,false),
    };
    private int mCurrentIndex=0;
    private boolean[] mCheats=new boolean[mQuestionBank.length];
    public static Intent newIntent(Context packageContext,boolean answerIsTrue,int num){
        Intent intent=new Intent(packageContext,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        intent.putExtra(EXTRA_ANSWER_IS_NUM,num);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG," onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater=savedInstanceState.getBoolean(KEY_INDEX1,false);
            mCheats=savedInstanceState.getBooleanArray(KEY_INDEX2);
        }
        mQuestionTextView=findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateuestion();
            }
        });
        mTrueButton= (Button) findViewById(R.id.true_button);

        mFalseButton=(Button)findViewById(R.id.false_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast toast=  Toast.makeText(MainActivity.this,R.string.correct_toast,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();*/
                checkAnswer(true);

                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this,R.string.incorrect_toast,Toast.LENGTH_SHORT).show();
                checkAnswer(false);
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            }
        });
        mNextButton=(ImageButton)findViewById(R.id.next_button);
        //mNextButton=(ImageButton)findViewById(R.id.question_text_view);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateuestion();
                mIsCheater=false;
                mFalseButton.setEnabled(true);
                mTrueButton.setEnabled(true);
            }
        });
        mPrevButton=(ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex-1+mQuestionBank.length)%mQuestionBank.length;
                updateuestion();
                mFalseButton.setEnabled(true);
                mTrueButton.setEnabled(true);
            }
        });
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(newIntent(MainActivity.this,mQuestionBank[mCurrentIndex].ismAnswerTrue(),mCheatNum),REQUEST_CODE_CHEAT);
            }
        });

        mShowButton=(Button)findViewById(R.id.show_button);
        mShowText=(TextView)findViewById(R.id.show_text);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence cs;
                cs="当前api级别为："+ Build.VERSION.SDK_INT;
                mShowText.setText(cs);
            }
        });
        updateuestion();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_CODE_CHEAT){
            if (data==null){
                return;
            }
            mIsCheater=CheatActivity.wasAnswerShown(data);
            mCheatNum=CheatActivity.wasAnswerNum(data);
            mCheats[mCurrentIndex]=mIsCheater;
        }
    }
    private void updateuestion(){
       // Log.d(TAG,"Updating question text",new Exception());
        int quest=mQuestionBank[mCurrentIndex].getmTextResId();
        mQuestionTextView.setText(quest);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue=mQuestionBank[mCurrentIndex].ismAnswerTrue();
        int messageResId=0;
        if (mCheats[mCurrentIndex]){
            messageResId=R.string.judgment_toast;
        }else {
            if (userPressedTrue==answerIsTrue){
                messageResId=R.string.correct_toast;
                mTrueNum++;
            }else {
                messageResId=R.string.incorrect_toast;
            }
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        if (mCurrentIndex==mQuestionBank.length-1){
            StringBuffer sb=new StringBuffer();
            sb.append("正确率为:");
            sb.append(mTrueNum*1.0/mQuestionBank.length*100);
            sb.append("%");
            Toast.makeText(MainActivity.this,sb,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG," onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState");
        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBoolean(KEY_INDEX1,mIsCheater);
        outState.putBooleanArray(KEY_INDEX2,mCheats);
    }
}
