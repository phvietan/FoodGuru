package com.findfood.findfood;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;

public class ResultAnswer {
    public int getResult(int num, RadioButton chosen, String[] st) {
        if (num==0) return getResultCategory(chosen,st);
        else if (num==1) return getResultPrice(chosen,st);
        return getResultDistance(chosen,st);
    }
    private int getResultCategory(RadioButton chosen, String[] st) {
        String now = (String)chosen.getText();
        for (String x : st) {
            if (x.equals(now)) return 287;
        }
        return 480;
    }
    private int getResultPrice(RadioButton chosen, String[] st) {
        String now = chosen.getText().toString();
        for (String x : st) {
            if (x.equals(now)) return 3;
        }
        return 15;

    }
    private int getResultDistance(RadioButton chosen, String[] st) {
        String now = chosen.getText().toString();
        for (String x : st)
            if (x.equals(now)) return 3;
        return 20;
    }
}
