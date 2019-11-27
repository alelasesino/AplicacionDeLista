package com.alejandro.aplicaciondelista;

import java.text.DecimalFormat;

public class Utils {

    public static String toPrice(double price){
        return new DecimalFormat("##,##0.00 €").format(price);
    }

}
