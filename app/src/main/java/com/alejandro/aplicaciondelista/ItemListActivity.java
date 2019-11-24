package com.alejandro.aplicaciondelista;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, largeScreen));

    }

    void onCardItemClick(View card, ItemContent.DummyItem item){

        if (largeScreen)
            launchItemDetailFragment(item);
        else
            launchItemDetailActivity(card, item);

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

    private void launchItemDetailActivity(View card, ItemContent.DummyItem item){

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

        ImageView img = card.findViewById(R.id.card_image);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(img, "card_image_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent, options.toBundle());

    }

}
