package com.alejandro.aplicaciondelista;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alejandro.aplicaciondelista.dummy.ItemContent;

public class ItemDetailFragment extends Fragment {

    static final String ARG_ITEM_ID = "item_id";

    private ItemContent.DummyItem currentItem;

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

        if (appBarLayout != null)
            appBarLayout.setTitle(currentItem.title);

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

        if (currentItem != null)
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(currentItem.details);

        return rootView;
    }

}
