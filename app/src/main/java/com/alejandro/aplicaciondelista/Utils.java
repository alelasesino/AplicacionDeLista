package com.alejandro.aplicaciondelista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;

public class Utils {

    public static final String URL_BURGER_KING = "https://www.burgerking.es/carta";
    public static final String OWNER_GITHUB = "https://github.com/alelasesino/AplicacionDeLista";

    public static String toPrice(double price){
        return new DecimalFormat("##,##0.00 €").format(price);
    }

    public static double priceToString(String price){

        if(price.equals("")) return 0;

        return Double.parseDouble(price.replace(",", ".").replace("€", ""));

    }

    public static void setText(TextView editText, String text){

        if(editText != null)
            editText.setText(text);

    }

    public static String capitalize(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private static String getDensityString(DisplayMetrics metrics){

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

    public static void loadPicassoImage(Context context, ImageView imageView, String urlImage){

        if(imageView != null){
            String URL = ItemContent.URL_IMAGES_BASE + getDensityString(context.getResources().getDisplayMetrics()) + urlImage + ".png";
            Picasso.with(context).load(URL).placeholder(R.drawable.ic_broken_image_black).into(imageView);
        }

    }

    public static void openWeb(Context context, String url){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);

    }

}
