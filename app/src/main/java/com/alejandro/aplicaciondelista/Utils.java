package com.alejandro.aplicaciondelista;

import android.widget.TextView;

import java.text.DecimalFormat;

public class Utils {

    public static String toPrice(double price){
        return new DecimalFormat("##,##0.00 €").format(price);
    }

    public static void setText(TextView editText, String text){

        if(editText != null)
            editText.setText(text);

    }

}
