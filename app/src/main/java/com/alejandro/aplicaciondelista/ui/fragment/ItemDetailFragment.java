package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.TagViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item_detail";
    public static final String ARG_FAVORITE = "favorite";

    private Activity activity;
    private ItemProduct currentItem;

    private ImageView imageDetailsHeader;
    private Button btFavorite;
    private TextView tvPrice;
    private TextView tvDetails;
    private ItemCardActionListener listener;

    public ItemDetailFragment() {
    }

    public ItemDetailFragment(ItemCardActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

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

    private void initializeComponents(View fragmentView){ //TODO CREAR UN ESTADO EVITAR LOS == NULL

        imageDetailsHeader = fragmentView.findViewById(R.id.image_details_header);
        tvPrice = fragmentView.findViewById(R.id.tv_price);
        tvDetails = fragmentView.findViewById(R.id.tv_details);
        FloatingActionButton fabWebDetails = fragmentView.findViewById(R.id.fab_web_details);
        btFavorite = fragmentView.findViewById(R.id.bt_favorite);

        if(imageDetailsHeader == null)
            imageDetailsHeader = activity.findViewById(R.id.image_details_header);

        if(btFavorite == null)
            btFavorite = activity.findViewById(R.id.bt_favorite);

        if(tvPrice == null)
            tvPrice = activity.findViewById(R.id.tv_price);
        else
            ((TextView)fragmentView.findViewById(R.id.tv_name)).setText(currentItem.getName());

        if(fabWebDetails == null)
            fabWebDetails = activity.findViewById(R.id.fab_web_details);

        if(fabWebDetails != null)
            fabWebDetails.setOnClickListener(view -> Utils.openWeb(activity, Utils.URL_BURGER_KING));

        if(btFavorite != null)
            btFavorite.setOnClickListener(view -> setUpFavoriteState(!currentItem.isFavorite()));

        RecyclerView tagRecycler = fragmentView.findViewById(R.id.tags_recycler);
        tagRecycler.setAdapter(new TagViewAdapter(currentItem.getTags(), false));

        bindDataProducts();

    }

    private void bindDataProducts(){

        Utils.loadPicassoImage(activity, imageDetailsHeader, currentItem.getImageUrl());
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
