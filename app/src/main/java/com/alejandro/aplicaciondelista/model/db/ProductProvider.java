package com.alejandro.aplicaciondelista.model.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    private static final String uri = "content://com.alejandro.aplicaciondelista/products";
    public static final Uri CONTENT_URI = Uri.parse(uri);

    private ProductSQLiteHelper productHelper;
    private static final String BD_NOMBRE = "ProductsDB";
    private static final int BD_VERSION = 1;
    private static final String PRODUCT_TABLE = "Product";

    public static UriMatcher uriMatcher;
    public static final int PRODUCTS = 100;
    public static final int PRODUCTS_ID = 101;

    //private ContentResolver resolver;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.alejandro.aplicaciondelista", "products", PRODUCTS);
        uriMatcher.addURI("com.alejandro.aplicaciondelista", "products/#", PRODUCTS_ID);
    }

    @Override
    public boolean onCreate() {
        productHelper = new ProductSQLiteHelper(getContext(), BD_NOMBRE, null, BD_VERSION);
        //resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String where = selection;

        if(uriMatcher.match(uri) == PRODUCTS_ID)
            where = BaseColumns._ID + "=" + uri.getLastPathSegment();

        SQLiteDatabase db = productHelper.getWritableDatabase();

        Cursor c = db.query(PRODUCT_TABLE, projection, where, selectionArgs, null, null, sortOrder);
        //c.setNotificationUri(resolver, ProductProvider.CONTENT_URI);
        Log.d("PRUEBA", "query: " + c);
        return c;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return "vnd.android.cursor.dir/vnd.alejandro.producto";
            case PRODUCTS_ID:
                return "vnd.android.cursor.item/vnd.alejandro.producto";
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long id = 1;

        SQLiteDatabase db = productHelper.getWritableDatabase();

        id = db.insert(PRODUCT_TABLE, null, contentValues);

        return ContentUris.withAppendedId(CONTENT_URI, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        String where = selection;

        if(uriMatcher.match(uri) == PRODUCTS_ID)
            where = BaseColumns._ID + "=" + uri.getLastPathSegment();

        SQLiteDatabase db = productHelper.getWritableDatabase();

        return db.delete(PRODUCT_TABLE, where, selectionArgs);

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        String where = selection;

        if(uriMatcher.match(uri) == PRODUCTS_ID)
            where = BaseColumns._ID + "=" + uri.getLastPathSegment();

        SQLiteDatabase db = productHelper.getWritableDatabase();

        return db.update(PRODUCT_TABLE, contentValues, where, selectionArgs);

    }

    public static final class ItemProduct implements BaseColumns {
        private ItemProduct() {}

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_FAVORITE = "favorite";

    }

}
