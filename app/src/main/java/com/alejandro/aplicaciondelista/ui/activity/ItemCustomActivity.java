package com.alejandro.aplicaciondelista.ui.activity;

import android.os.Bundle;

import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

/**
 * Clase que controla la activity que permite añadir y actualizar productos
 */
public class ItemCustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_custom);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setHomeButtonActionBar();
        argumentsReceived(savedInstanceState);

    }

    /**
     * Metodo que obtiene los argumentos recibido de la activity que fue llamada
     * y se los manda al fragmento dinamico ItemCustomFragment
     * @param savedInstanceState Bundle guardado
     */
    private void argumentsReceived(Bundle savedInstanceState){

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemCustomFragment.ARG_ITEM, getIntent().getParcelableExtra(ItemCustomFragment.ARG_ITEM));
            arguments.putBoolean(ItemCustomFragment.ARG_UPDATE_ITEM,getIntent().getBooleanExtra(ItemCustomFragment.ARG_UPDATE_ITEM, false));

            ItemCustomFragment fragment = new ItemCustomFragment(null);
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_custom_container, fragment)
                    .commit();
        }

    }

    /**
     * Inicializa el actionbar del home
     */
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
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
