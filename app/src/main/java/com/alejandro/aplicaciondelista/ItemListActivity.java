package com.alejandro.aplicaciondelista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.alejandro.aplicaciondelista.adapters.AnimalItemViewAdapter;
import com.alejandro.aplicaciondelista.dummy.ItemContent;

/**
 * Activity principal de la aplicacion
 */
public class ItemListActivity extends AppCompatActivity {

    private boolean largeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) //large-screen layouts (res/values-w900dp)
            largeScreen = true;

        setupRecyclerView(findViewById(R.id.item_list));

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new AnimalItemViewAdapter(ItemContent.ITEMS, this::onCardItemClick));

    }

    void onCardItemClick(ItemContent.DummyItem item){

        if (largeScreen)
            launchItemDetailFragment(item);
        else
            launchItemDetailActivity(item);

    }

    private void launchItemDetailFragment(ItemContent.DummyItem item){

        Bundle arguments = new Bundle();
        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);

        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();

    }

    private void launchItemDetailActivity(ItemContent.DummyItem item){

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

        startActivity(intent);

    }

}