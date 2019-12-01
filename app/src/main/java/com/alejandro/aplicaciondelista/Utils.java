package com.alejandro.aplicaciondelista;

import android.widget.TextView;

import java.text.DecimalFormat;

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

}
