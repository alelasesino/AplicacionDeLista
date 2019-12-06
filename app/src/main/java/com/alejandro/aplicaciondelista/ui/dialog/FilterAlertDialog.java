package com.alejandro.aplicaciondelista.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.TagViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FilterAlertDialog extends DialogFragment {

    public static final String FILTER_PREFERENCE = "filter_options";
    public static final String PREF_FILTER = "filter_switch";
    public static final String PREF_PRICE = "filter_price";
    public static final String PREF_TAGS = "filter_tags";

    private Activity activity;
    private Switch switchFilter;
    private Spinner spinnerPrice /*spinnerName*/;
    private EditText txtTag;
    private TagViewAdapter tagAdapter;
    private IFilterAlertDialog listener;

    public static FilterAlertDialog newInstance(String title, IFilterAlertDialog listener) {

        FilterAlertDialog frag = new FilterAlertDialog(listener);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;

    }

    public FilterAlertDialog(){}

    private FilterAlertDialog(IFilterAlertDialog listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filter_dialog, container);

        initializeComponents(rootView);

        return rootView;

    }

    private void initializeComponents(View rootView){

        switchFilter = rootView.findViewById(R.id.switch_filter);
        spinnerPrice = rootView.findViewById(R.id.spinner_precio);
        //spinnerName = rootView.findViewById(R.id.spinner_nombre);
        Button btAccept = rootView.findViewById(R.id.bt_aceptar);
        Button btCancel = rootView.findViewById(R.id.bt_cancelar);
        Button btAddTag = rootView.findViewById(R.id.bt_add_tag);
        txtTag = rootView.findViewById(R.id.txt_tag);
        RecyclerView recyclerTags = rootView.findViewById(R.id.tags_recycler);

        tagAdapter = new TagViewAdapter(null, true);
        recyclerTags.setAdapter(tagAdapter);

        btAddTag.setOnClickListener(view -> {

            if(!txtTag.getText().toString().equals("")){//TODO REFACTORIZAR

                tagAdapter.addItem(Utils.capitalize(txtTag.getText().toString()));
                txtTag.setText("");

            }

        });

        btCancel.setOnClickListener(view -> dismiss());

        btAccept.setOnClickListener(view -> {

            boolean priceAsc = spinnerPrice.getSelectedItemPosition() == 1; //BARATO A CARO SELECCIONADO
            //boolean nameAsc = spinnerName.getSelectedItemPosition() == 0;

            listener.onFilterAlertAccept(switchFilter.isChecked(), priceAsc, tagAdapter.getTags());

            dismiss();

        });

        bindDataViews();

    }

    private void bindDataViews(){

        activity = getActivity();

        if(activity != null){

            String[] orderBy = {"Caro a barato", "Barato a caro"};

            ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, Arrays.asList(orderBy));
            spinnerPrice.setAdapter(orderAdapter);
            //spinnerName.setAdapter(orderAdapter);

            recoverFilterPreferences();

        }

    }

    private void recoverFilterPreferences(){

        SharedPreferences pref = activity.getSharedPreferences(FILTER_PREFERENCE, Context.MODE_PRIVATE);
        switchFilter.setChecked(pref.getBoolean(PREF_FILTER, false));
        spinnerPrice.setSelection(pref.getInt(PREF_PRICE, 0));

        Set<String> fetch = pref.getStringSet(PREF_TAGS, null);

        if(fetch != null){

            List<String> list = new ArrayList<>(fetch);
            for(String s: list)
                tagAdapter.addItem(s);

        }

    }

    public interface IFilterAlertDialog{
        void onFilterAlertAccept(boolean applyFilter, boolean priceAsc, /*boolean nameAsc,*/ String[] tags);
    }

}
