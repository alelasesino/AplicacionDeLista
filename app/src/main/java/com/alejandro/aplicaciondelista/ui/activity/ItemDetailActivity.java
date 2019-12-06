package com.alejandro.aplicaciondelista.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailFragment itemDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        setHomeButtonActionBar();
        argumentsReceived(savedInstanceState);

    }

    private void argumentsReceived(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Intent intent = getIntent();

            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_ITEM, intent.getParcelableExtra(ItemDetailFragment.ARG_ITEM));

            itemDetailFragment = new ItemDetailFragment();
            itemDetailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, itemDetailFragment)
                    .commit();

        }

    }

    private void setHomeButtonActionBar(){

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public void openWebDetails(){

        Utils.openWeb(this,Utils.URL_BURGER_KING);

    }

    private void sendEmail(ItemProduct itemProduct) {

        String[] TO = {"random@gmail.com"};
        String[] CC = {"random2@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, itemProduct.getName());
        emailIntent.putExtra(Intent.EXTRA_TEXT, itemProduct.getDetails());
        emailIntent.setType("message/rfc822");

        try {

            startActivity(Intent.createChooser(emailIntent, "Enviar correo"));

        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay clientes email instalados", Toast.LENGTH_SHORT).show();
        }

    }

    private void shareItemProduct(ItemProduct itemProduct){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, itemProduct.getDetails());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, itemProduct.getName());
        startActivity(shareIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_web_details:
                openWebDetails();
                break;

            case R.id.menu_share:
                shareItemProduct(getIntent().getParcelableExtra(ItemDetailFragment.ARG_ITEM));
                break;

            case R.id.menu_send_correo:
                sendEmail(getIntent().getParcelableExtra(ItemDetailFragment.ARG_ITEM));
                break;

            default:
                itemDetailFragment.closeActivityWithResult();
                //onBackPressed();
                return true;

        }

        //navigateUpTo(new Intent(this, ItemListActivity.class), options.toBundle());

        return super.onOptionsItemSelected(item);

    }

}
