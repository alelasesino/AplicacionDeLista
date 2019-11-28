package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        EditText txtName = fragmentView.findViewById(R.id.txt_name);
        EditText txtDetails = fragmentView.findViewById(R.id.txt_details);
        EditText txtPrice = fragmentView.findViewById(R.id.txt_price);
        //TextView tvTags = fragmentView.findViewById(R.id.tv_tags);

        if(txtName == null)
            txtName = activity.findViewById(R.id.txt_name);

        if(txtPrice == null)
            txtPrice = activity.findViewById(R.id.txt_price);

        Utils.setText(txtName, currentItem.getName());
        Utils.setText(txtDetails, currentItem.getDetails());
        Utils.setText(txtPrice, Utils.toPrice(currentItem.getPrice()));
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
