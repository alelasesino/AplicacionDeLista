package com.alejandro.aplicaciondelista.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alejandro.aplicaciondelista.R;

public class FilterAlertDialog extends DialogFragment {

    public static FilterAlertDialog newInstance(String title) {
        FilterAlertDialog frag = new FilterAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_dialog, container);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //listener
    }
}
