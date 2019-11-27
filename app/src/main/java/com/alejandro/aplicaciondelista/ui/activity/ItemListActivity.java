package com.alejandro.aplicaciondelista.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.alejandro.aplicaciondelista.ui.components.GridSpacingItemDecoration;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.adapters.ItemViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;

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

        setupFloatingButtons();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new ItemViewAdapter(ItemContent.ITEMS, this::onCardItemClick));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, largeScreen));

    }

    private void setupFloatingButtons(){

        FloatingActionButton addItemButton = findViewById(R.id.fab_add_item);
        addItemButton.setOnClickListener(view -> {

            if(largeScreen)
                launchCustomFragment();
            else
                launchCustomActivity();

        });

        FloatingActionButton removeItemButton = findViewById(R.id.fab_remove_item);

    }

    private void launchCustomActivity(){

        Intent intent = new Intent(this, ItemCustomActivity.class);
        //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
        startActivity(intent);

    }

    private void launchCustomFragment(){

        ItemCustomFragment fragment = new ItemCustomFragment();
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();

    }

    void onCardItemClick(View card, ItemContent.ItemProduct item){

        if (largeScreen)
            launchItemDetailFragment(item);
        else
            launchItemDetailActivity(card, item);

    }

    private void launchItemDetailFragment(ItemContent.ItemProduct item){

        Bundle arguments = new Bundle();
        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);

        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();

    }

    private void launchItemDetailActivity(View card, ItemContent.ItemProduct item){

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

        ImageView img = card.findViewById(R.id.card_image);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(img, "card_image_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent, options.toBundle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
