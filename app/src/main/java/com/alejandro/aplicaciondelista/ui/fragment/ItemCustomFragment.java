package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ItemCustomFragment extends Fragment {

    public static final String ARG_ITEM = "item_custom";

    private Activity activity;
    private ItemProduct currentItem;

    public ItemCustomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

        argumentsReceived();
        initializeToolbar();

    }

    private void initializeToolbar(){

        CollapsingToolbarLayout appBarLayout = null;
        Activity activity = getActivity();

        if(activity != null)
            appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null){

            //appBarLayout.setTitle(currentItem.getName());
            //appBarLayout.setExpandedTitleColor(ContextCompat.getColor(activity, R.color.foregroundOnPrimary));

            //Typeface typeface = ResourcesCompat.getFont(activity, R.font.insanibu);
            //appBarLayout.setCollapsedTitleTypeface(typeface);
            //appBarLayout.setExpandedTitleTypeface(typeface);

        }

    }

    private void bindDataProducts(View fragmentView){

        EditText txtPrice = fragmentView.findViewById(R.id.txt_price);
        EditText txtDetails = fragmentView.findViewById(R.id.txt_details);
        //TextView tvTags = fragmentView.findViewById(R.id.tv_tags);

        if(txtPrice == null)
            txtPrice = activity.findViewById(R.id.txt_price);
        else
            ((EditText)fragmentView.findViewById(R.id.txt_name_product)).setText(currentItem.getName());

        if(txtPrice != null)
            txtPrice.setText(Utils.toPrice(currentItem.getPrice()));

        txtDetails.setText(currentItem.getDetails());
        //tvTags.setText(String.join(", ",currentItem.getTags()));

    }

    private void argumentsReceived(){

        Bundle arguments = getArguments();

        if(arguments != null)
            if (arguments.containsKey(ARG_ITEM))
                currentItem = arguments.getParcelable(ARG_ITEM);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_custom, container, false);

        if (currentItem != null)
            bindDataProducts(rootView);

        return rootView;
    }

}
