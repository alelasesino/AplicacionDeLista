package com.alejandro.aplicaciondelista;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Utils {

    public static String toPrice(double price){
        return new DecimalFormat("##,##0.00 €").format(price);
    }

    public static double priceToString(String price){
        return Double.parseDouble(price.replace(",", ".").replace("€", ""));
    }

    public static void setText(TextView editText, String text){

        if(editText != null)
            editText.setText(text);

    }

    public static String capitalize(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String getDensityString(DisplayMetrics metrics){

        String metric = "xhdpi/";

        switch(metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                metric = "ldpi/";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                metric = "mdpi/";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                metric = "hdpi/";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                metric = "xhdpi/";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                metric = "xxhdpi/";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                metric = "xxxhdpi/";
                break;
        }

        return metric;

    }

}
