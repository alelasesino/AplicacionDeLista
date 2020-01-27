package com.alejandro.aplicaciondelista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class Utils {

    public static final String URL_BURGER_KING = "https://www.burgerking.es/carta";
    public static final String OWNER_GITHUB = "https://github.com/alelasesino/AplicacionDeLista";

    /**
     * Convierte precio en una cadena con el formato de euros
     * @param price Double con el precio a convertir
     * @return Cadena con formato del precio
     */
    public static String toPrice(double price){
        return new DecimalFormat("##,##0.00 €").format(price);
    }

    /**
     * Convierte la cadena de precio en formato de euros en un double
     * @param price Cadena con el precio a convertir
     * @return Double con el precio convertido
     */
    public static double priceToString(String price){

        if(price.equals("")) return 0;

        return Double.parseDouble(price.replace(",", ".").replace("€", ""));

    }

    /**
     * Establece el texto en un campo de texto si no es nulo
     * @param editText Campo de texto
     * @param text Texto para establecer
     */
    public static void setText(TextView editText, String text){

        if(editText != null)
            editText.setText(text);

    }

    /**
     * Convierte la primera letra mayuscula y todas las demas minusculas
     * @param text Texto para capitalizar
     * @return Texto capitalizado
     */
    public static String capitalize(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    /**
     * Devuelve dependiendo de la densidad de pixeles de dispositivo
     * la cadena correspondiente a esa densidad de pixeles
     * @param metrics Densidad de pixeles del dispositivo
     * @return Cadena con su correpondiente desidad de pixeles
     */
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

    /**
     * Obtiene de una url la imagen correspondiente a la densidad de pixeles
     * del dispositivo y la inserta en un image view
     * @param context Contexto
     * @param imageView Image view para insertar la imagen
     * @param urlImage Url de la imagen
     */
    public static void loadPicassoImage(Context context, ImageView imageView, String urlImage){

        if(imageView != null){
            String URL = ItemContent.URL_IMAGES_BASE + getDensityString(context.getResources().getDisplayMetrics()) + urlImage + ".png";
            Picasso.with(context).load(URL).placeholder(R.drawable.ic_broken_image_black).into(imageView);
        }

    }

    /**
     * Abre el navegador predeterminado del dispositivo con la url
     * recibida por parametro
     * @param context Contexto
     * @param url Url para abrir
     */
    public static void openWeb(Context context, String url){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);

    }

    public static File createTemporaryFile(String part, String ext, Context myContext) throws Exception {

        File tempDir = myContext.getExternalCacheDir();
        tempDir = new File(tempDir.getAbsolutePath() + "/temp/");

        if (!tempDir.exists())
            tempDir.mkdir();

        return File.createTempFile(part, ext, tempDir);

    }

    public static Bitmap getImage(Context context, Uri uri) {

        Bitmap result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is;

        try {

            is = context.getContentResolver().openInputStream(uri);
            result = BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(result != null)
            return Bitmap.createScaledBitmap(result, 400, 400, true);

        return null;
    }

    public static byte[] getBytes(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return stream.toByteArray();

    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
