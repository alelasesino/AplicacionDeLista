package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Typeface;
import android.os.Bundle;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item_detail";

    private Activity activity;
    private ItemProduct currentItem;

    public ItemDetailFragment() {
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

    private void bindDataProducts(View fragmentView){

        TextView tvPrice = fragmentView.findViewById(R.id.tv_price);
        TextView tvDetails = fragmentView.findViewById(R.id.tv_details);
        TextView tvTags = fragmentView.findViewById(R.id.tv_tags);

        if(tvPrice == null)
            tvPrice = activity.findViewById(R.id.tv_price);
        else
            ((TextView)fragmentView.findViewById(R.id.tv_name)).setText(currentItem.getName());

        Utils.setText(tvDetails, currentItem.getDetails());
        Utils.setText(tvPrice, Utils.toPrice(currentItem.getPrice()));
        Utils.setText(tvTags, String.join(", ",currentItem.getTags()));

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

        if (currentItem != null)
            bindDataProducts(rootView);

        return rootView;
    }

}
