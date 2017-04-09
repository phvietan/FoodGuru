package com.findfood.findfood;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class SuggestActivity extends AppCompatActivity {
    int dis, cat, pri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        randomQuestion();
    }

    private String getRandomString(String [] s) {
        Random rand = new Random();
        return s[rand.nextInt(s.length)];
    }

    private void setQuestion() {
        TextView x = (TextView)findViewById(R.id.question0);
        x.setText(getRandomString(getResources().getStringArray(R.array.ask_user_category)));
        x = (TextView)findViewById(R.id.question1);
        x.setText(getRandomString(getResources().getStringArray(R.array.ask_user_price)));
        x = (TextView)findViewById(R.id.question2);
        x.setText(getRandomString(getResources().getStringArray(R.array.ask_user_distance)));
    }

    private void setAnswer() {
        RadioButton x = (RadioButton)findViewById(R.id.answer00);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_category_eat)));
        x = (RadioButton)findViewById(R.id.answer01);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_category_drink)));
        x = (RadioButton)findViewById(R.id.answer10);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_price_cheap)));
        x = (RadioButton)findViewById(R.id.answer11);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_price_expensive)));
        x = (RadioButton)findViewById(R.id.answer20);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_distance_near)));
        x = (RadioButton)findViewById(R.id.answer21);
        x.setText(getRandomString(getResources().getStringArray(R.array.answer_distance_far)));
    }

    private void randomQuestion() {
        setQuestion();
        setAnswer();
    }

    public void SuggestMe(View view) {
        TextView error = (TextView)findViewById(R.id.error_message);
        ResultAnswer x = new ResultAnswer();
        RadioGroup y = (RadioGroup)findViewById(R.id.GroupCategory);
        ////////
        if (y.getCheckedRadioButtonId()==-1) {
            error.setVisibility(View.VISIBLE);
            return;
        }
        ////////
        cat = x.getResult(0, (RadioButton)findViewById(y.getCheckedRadioButtonId())
                ,getResources().getStringArray(R.array.answer_category_eat));
        y = (RadioGroup)findViewById(R.id.GroupPrice);
        ////////
        if (y.getCheckedRadioButtonId()==-1) {
            error.setVisibility(View.VISIBLE);
            return;
        }
        ////////
        pri = x.getResult(1,(RadioButton)findViewById(y.getCheckedRadioButtonId()),
                getResources().getStringArray(R.array.answer_price_cheap) );
        y = (RadioGroup)findViewById(R.id.GroupDistance);
        ////////
        if (y.getCheckedRadioButtonId()==-1) {
            error.setVisibility(View.VISIBLE);
            return;
        }
        ////////
        dis = x.getResult(2,(RadioButton)findViewById(y.getCheckedRadioButtonId()),
                getResources().getStringArray(R.array.answer_distance_near) );
        ////
        Intent data = new Intent(this, ShowSuggestActivity.class);
        data.putExtra("SuggestCat",cat);
        data.putExtra("SuggestPri",pri);
        data.putExtra("SuggestDis",dis);
        startActivity(data);
    }

}
