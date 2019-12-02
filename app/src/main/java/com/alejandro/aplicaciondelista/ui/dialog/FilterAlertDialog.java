package com.alejandro.aplicaciondelista.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.ui.activity.ItemListActivity;

public class FilterAlertDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //AlertDialog.Builder builder = new AlertDialog.Builder(ItemListActivity.this);
        //View rootView = getLayoutInflater().inflate(R.la)

        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //listener
    }
}
