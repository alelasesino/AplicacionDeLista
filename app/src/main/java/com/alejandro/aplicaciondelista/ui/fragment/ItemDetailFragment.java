package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import com.alejandro.aplicaciondelista.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alejandro.aplicaciondelista.model.ItemContent;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private ItemContent.ItemProduct currentItem;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCurrentItem();
        initializeToolbar();

    }

    private void initializeToolbar(){

        CollapsingToolbarLayout appBarLayout = null;
        Activity activity = getActivity();

        if(activity != null)
            appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null){

            appBarLayout.setTitle(currentItem.title);
            appBarLayout.setExpandedTitleColor(ContextCompat.getColor(activity, R.color.foregroundOnPrimary));

            Typeface typeface = ResourcesCompat.getFont(activity, R.font.insanibu);
            appBarLayout.setCollapsedTitleTypeface(typeface);
            appBarLayout.setExpandedTitleTypeface(typeface);

        }

    }

    private void setCurrentItem(){

        Bundle arguments = getArguments();

        if(arguments != null)
            if (arguments.containsKey(ARG_ITEM_ID))
                currentItem = ItemContent.ITEM_MAP.get(arguments.getString(ARG_ITEM_ID));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        /*if (currentItem != null)
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(currentItem.details);*/

        return rootView;
    }

}
