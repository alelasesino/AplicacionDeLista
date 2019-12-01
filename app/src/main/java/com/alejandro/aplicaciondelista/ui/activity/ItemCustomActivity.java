package com.alejandro.aplicaciondelista.ui.activity;

import android.os.Bundle;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

public class ItemCustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_custom);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_save_item);
        fab.setOnClickListener(view -> {



        });

        setHomeButtonActionBar();
        argumentsReceived(savedInstanceState);

    }

    private void argumentsReceived(Bundle savedInstanceState){

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemCustomFragment.ARG_ITEM, getIntent().getParcelableExtra(ItemCustomFragment.ARG_ITEM));

            ItemCustomFragment fragment = new ItemCustomFragment(null);
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_custom_container, fragment)
                    .commit();
        }

    }

    private void setHomeButtonActionBar(){

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //navigateUpTo(new Intent(this, ItemListActivity.class), options.toBundle());
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
