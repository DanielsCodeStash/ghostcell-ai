package com.ghostcell.io;

import java.text.DecimalFormat;

public class IoUtil {

    private static DecimalFormat df = new DecimalFormat("#.###");

    public static String round(double value) {
        if(value > 0.99999) {
            return "1.000";
        }

        if(value < 0.0001) {
            return "0.000";
        }

        String s = df.format(value);
        int zerosToAdd = 5-s.length();
        for(int i=0; i < zerosToAdd; i++) {
            s += "0";
        }
        return s;
    }
}
