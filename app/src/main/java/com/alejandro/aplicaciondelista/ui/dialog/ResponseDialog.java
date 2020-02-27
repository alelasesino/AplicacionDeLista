package com.alejandro.aplicaciondelista.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class ResponseDialog extends DialogFragment {

    private AppCompatActivity activity;
    private String title, message;

    public ResponseDialog(AppCompatActivity context, String title, String message){
        this.activity = context;
        this.title = title;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setPositiveButton("Aceptar", null);

        return builder.create();

    }

    public void show() {
        show(activity.getSupportFragmentManager(), "dialog");
    }

}
