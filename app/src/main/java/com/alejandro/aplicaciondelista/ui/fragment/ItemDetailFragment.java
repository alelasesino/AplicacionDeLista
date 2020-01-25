package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.TagViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.model.db.ProductProvider;
import com.alejandro.aplicaciondelista.ui.activity.ItemListActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragmento que muestra los detalles de un producto
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item_detail";
    public static final String ARG_FAVORITE = "favorite";

    private Activity activity;
    private ItemProduct currentItem;

    private ImageView imageDetailsHeader;
    private Button btFavorite;
    private TextView tvPrice, tvDetails, tvName;
    private ItemCardActionListener listener;

    private boolean largeScreen;

    public ItemDetailFragment() {
    }

    public ItemDetailFragment(ItemCardActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

        /*Si la activity que contiene este fragment es la activity que muestra la lista de productos
        * significa que la pantalla es superior a w900dp*/
        largeScreen = activity instanceof ItemListActivity;

        argumentsReceived();
        initializeToolbar();

    }

    private void initializeToolbar(){

        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null){

            appBarLayout.setTitle(currentItem.getName());
            appBarLayout.setExpandedTitleColor(ContextCompat.getColor(activity, R.color.foregroundOnPrimary));

            Typeface typeface = ResourcesCompat.getFont(activity, R.font.insanibu);
            appBarLayout.setCollapsedTitleTypeface(typeface);
            appBarLayout.setExpandedTitleTypeface(typeface);

        }

    }

    private void initializeComponents(View fragmentView){

        FloatingActionButton fabWebDetails;

        tvDetails = fragmentView.findViewById(R.id.tv_details);

        if(largeScreen){

            tvName = fragmentView.findViewById(R.id.tv_name);
            imageDetailsHeader = fragmentView.findViewById(R.id.image_details_header);
            tvPrice = fragmentView.findViewById(R.id.tv_price);
            fabWebDetails = fragmentView.findViewById(R.id.fab_web_details);
            btFavorite = fragmentView.findViewById(R.id.bt_favorite);

        } else {

            imageDetailsHeader = activity.findViewById(R.id.image_details_header);
            tvPrice = activity.findViewById(R.id.tv_price);
            fabWebDetails = activity.findViewById(R.id.fab_web_details);
            btFavorite = activity.findViewById(R.id.bt_favorite);

        }

        if(fabWebDetails != null)
            fabWebDetails.setOnClickListener(view -> Utils.openWeb(activity, Utils.URL_BURGER_KING));

        if(btFavorite != null)
            btFavorite.setOnClickListener(view -> setUpFavoriteState(!currentItem.isFavorite()));

        RecyclerView tagRecycler = fragmentView.findViewById(R.id.tags_recycler);
        tagRecycler.setAdapter(new TagViewAdapter(currentItem.getTags(), false));

        bindDataProducts();

    }

    private void bindDataProducts(){

        if(currentItem.getImageUrl() != null && currentItem.getImageUrl().contains("://")){

            String[] projection = new String[]{ProductProvider.ItemProduct.COLUMN_IMAGE};
            String where = currentItem.getId() + "=" + BaseColumns._ID;

            Cursor c = activity.getContentResolver().query(ProductProvider.CONTENT_URI, projection, where, null,null);
            Bitmap bitmap;

            if(c != null){
                c.moveToFirst();
                bitmap = Utils.getImage(c.getBlob(c.getColumnIndex(ProductProvider.ItemProduct.COLUMN_IMAGE)));
                imageDetailsHeader.setImageBitmap(bitmap);
            }

        } else {

            Utils.loadPicassoImage(getActivity(), imageDetailsHeader, currentItem.getImageUrl());

        }

        //Utils.loadPicassoImage(activity, imageDetailsHeader, currentItem.getImageUrl());
        Utils.setText(tvName, currentItem.getName());
        Utils.setText(tvDetails, currentItem.getDetails());
        Utils.setText(tvPrice, Utils.toPrice(currentItem.getPrice()));

        setUpFavoriteState(currentItem.isFavorite());

    }

    private void setUpFavoriteState(boolean isFavorite){

        currentItem.setFavorite(isFavorite);

        if(listener != null)
            listener.onChangeFavoriteState(isFavorite);

        if(btFavorite != null)
            btFavorite.setBackgroundResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

    }

    private void argumentsReceived(){

        Bundle arguments = getArguments();

        if(arguments != null)
            if (arguments.containsKey(ARG_ITEM))
                currentItem = arguments.getParcelable(ARG_ITEM);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (currentItem != null && rootView != null)
            initializeComponents(rootView);

        return rootView;
    }

    public void closeActivityWithResult(){

        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARG_FAVORITE, currentItem.isFavorite());

        activity.setResult(Activity.RESULT_OK, resultIntent);
        activity.finishAfterTransition();

    }

}
