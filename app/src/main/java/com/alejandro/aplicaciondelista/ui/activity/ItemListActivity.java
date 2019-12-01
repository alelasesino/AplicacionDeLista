package com.alejandro.aplicaciondelista.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.ItemCustomActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.adapters.ItemViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.ui.components.GridSpacingItemDecoration;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Activity principal de la aplicacion
 */
public class ItemListActivity extends AppCompatActivity implements ItemCardActionListener, ItemCustomActionListener {

    private static final int CUSTOM_ACTIVITY = 1;
    private boolean largeScreen;
    private RecyclerView recyclerView;
    private ItemViewAdapter itemAdapter;
    private ItemCustomFragment itemCustomFragment;
    private ItemDetailFragment itemDetailFragment;
    private ItemProduct currentItem;

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
        this.recyclerView = recyclerView;

        itemAdapter = new ItemViewAdapter(this, ItemContent.ITEMS, this);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, largeScreen));

    }

    private void setupFloatingButtons(){

        FloatingActionsMenu mainButton = findViewById(R.id.fab_main);
        mainButton.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {}

            @Override
            public void onMenuCollapsed() {

                setEditModeRecyclerView(false);

            }
        });

        FloatingActionButton addItemButton = findViewById(R.id.fab_add_item);
        addItemButton.setOnClickListener(view -> {

            currentItem = new ItemProduct();

            if(largeScreen)
                launchCustomFragment();
            else
                launchCustomActivity();

        });

        FloatingActionButton removeItemButton = findViewById(R.id.fab_remove_item);
        removeItemButton.setOnClickListener(view -> setEditModeRecyclerView(true));

    }

    private void setEditModeRecyclerView(boolean editMode){

        if(itemAdapter != null && editMode != itemAdapter.getEditMode()) {

            itemAdapter.setEditMode(editMode);
            changeState();

        }

    }

    private void changeState(){

        removeFragment(itemCustomFragment);
        removeFragment(itemDetailFragment);

    }

    private void removeFragment(Fragment fragment){

        if(fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();

    }

    private void launchCustomActivity(){

        Intent intent = new Intent(this, ItemCustomActivity.class);
        intent.putExtra(ItemCustomFragment.ARG_ITEM, currentItem);

        startActivityForResult(intent, CUSTOM_ACTIVITY);

    }

    private void launchCustomFragment(){

        Bundle arguments = new Bundle();
        arguments.putParcelable(ItemCustomFragment.ARG_ITEM, currentItem);

        itemCustomFragment = new ItemCustomFragment(this);
        itemCustomFragment.setArguments(arguments);
        itemCustomFragment.setEnterTransition(new Fade());
        itemCustomFragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, itemCustomFragment)
                .commit();

    }

    @Override
    public void onCardItemClick(View card, ItemProduct item, boolean editMode){
        this.currentItem = item;

        if (largeScreen)
            if(editMode)
                launchCustomFragment();
            else
                launchItemDetailFragment();
        else
            if(editMode)
                launchCustomActivity();
            else
                launchItemDetailActivity(card);

    }

    private void launchItemDetailFragment(){

        Bundle arguments = new Bundle();
        arguments.putParcelable(ItemDetailFragment.ARG_ITEM, currentItem);

        itemDetailFragment = new ItemDetailFragment(this);
        itemDetailFragment.setArguments(arguments);
        itemDetailFragment.setEnterTransition(new Fade());
        itemDetailFragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, itemDetailFragment)
                .commit();

    }

    private void launchItemDetailActivity(View card){

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM, currentItem);

        ImageView img = card.findViewById(R.id.image_card);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(img, "card_image_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent, options.toBundle());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CUSTOM_ACTIVITY){
            if(data != null)
                onSaveItemCustom(data.getParcelableExtra(ItemCustomFragment.ARG_ITEM));
        }

    }

    @Override
    public void onSaveItemCustom(ItemProduct item) {

        if(!itemAdapter.getEditMode())
            itemAdapter.addItem(item);
        else
            itemAdapter.updateItem(item);

        changeState();

    }

    @Override
    public void onCardItemRemoved(View card, ItemProduct item) {

        if(currentItem != null)
            if(item.getId().equals(currentItem.getId()))
                changeState();

    }

    @Override
    public void onChangeFavoriteState(boolean isFavorite) {
        currentItem.setFavorite(isFavorite);
        itemAdapter.updateItem(currentItem);
        Log.d("PRUEBA", "FAV2: " + isFavorite);
    }

    public void doSmoothScroll(int position){
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

}
