package com.alejandro.aplicaciondelista.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.ItemCustomActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.ItemViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.ui.components.GridSpacingItemDecoration;
import com.alejandro.aplicaciondelista.ui.dialog.FilterAlertDialog;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Activity principal de la aplicacion
 */
public class ItemListActivity extends AppCompatActivity implements ItemCardActionListener, ItemCustomActionListener {

    private static final int CUSTOM_ACTIVITY = 1;
    private static final int DETAILS_ACTIVITY = 2;
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

        ItemContent.loadItemsApiRest(this, () -> setupRecyclerView(findViewById(R.id.item_list)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) //large-screen layouts (res/values-w900dp)
            largeScreen = true;

        setupNavigationView(findViewById(R.id.nav_view));
        setupFloatingButtons();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        itemAdapter = new ItemViewAdapter(this, ItemContent.ITEMS, this);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, largeScreen));

    }

    private void setupNavigationView(NavigationView navigationView){

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()){
                case R.id.nav_all:
                    itemAdapter.setTagsFilter(false, null);
                    break;
                case R.id.nav_burgers:
                    itemAdapter.setTagsFilter(true, new String[]{"Burger"});
                    break;
                case R.id.nav_bebidas:
                    itemAdapter.setTagsFilter(true, new String[]{"Bebida"});
                    break;
                case R.id.nav_postres:
                    itemAdapter.setTagsFilter(true, new String[]{"Postre"});
                    break;
                case R.id.nav_salsas:
                    itemAdapter.setTagsFilter(true, new String[]{"Salsa"});
                    break;
                case R.id.nav_visit:
                    Utils.openWeb(this, Utils.OWNER_GITHUB);
                    break;
            }

            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START, true);

            return false;

        });

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
        addItemButton.setOnClickListener(view -> createNewItem());

        FloatingActionButton removeItemButton = findViewById(R.id.fab_remove_item);
        removeItemButton.setOnClickListener(view -> setEditModeRecyclerView(true));

    }

    private void createNewItem(){

        currentItem = new ItemProduct();

        if(largeScreen)
            launchCustomFragment();
        else
            launchCustomActivity(null);

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

    private void launchCustomActivity(View card){


        Intent intent = new Intent(this, ItemCustomActivity.class);
        intent.putExtra(ItemCustomFragment.ARG_ITEM, currentItem);

        if(card != null){

            ImageView img = card.findViewById(R.id.image_card);

            Pair<View, String> pair = new Pair<>(img, "card_image_transition");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pair);

            startActivityForResult(intent, CUSTOM_ACTIVITY, options.toBundle());

        } else {

            startActivityForResult(intent, CUSTOM_ACTIVITY);

        }

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
                launchCustomActivity(card);
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

        ImageView img = card.findViewById(R.id.image_card);

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM, currentItem);

        Pair<View, String> pair = new Pair<>(img, "card_image_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pair);

        startActivityForResult(intent, DETAILS_ACTIVITY, options.toBundle());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CUSTOM_ACTIVITY){
            if(data != null)
                onSaveItemCustom(data.getParcelableExtra(ItemCustomFragment.ARG_ITEM));
        } else if(requestCode == DETAILS_ACTIVITY){
            if(data != null)
                onChangeFavoriteState(data.getBooleanExtra(ItemDetailFragment.ARG_FAVORITE, false));
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
        if(currentItem != null){
            currentItem.setFavorite(isFavorite);
            itemAdapter.updateFavoriteState(currentItem);
        }
    }

    public void doSmoothScroll(int position){
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_add_item:
                createNewItem();
                break;
            case R.id.menu_filtrar:
                showFilterDialogFragment();
                break;
            case R.id.menu_favoritos:
                setFavoriteMode(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFavoriteMode(MenuItem menu){

        menu.setChecked(!menu.isChecked());
        itemAdapter.setFavoriteFilter(menu.isChecked());

    }

    private void showFilterDialogFragment(){

        FragmentManager manager = getSupportFragmentManager();
        FilterAlertDialog dialog = FilterAlertDialog.newInstance("Filtrar productos", this::onFilterAlertAccept);
        dialog.show(manager, "filter_dialog");

    }

    private void onFilterAlertAccept(boolean applyFilter, boolean priceAsc, /*boolean nameAsc,*/ String[] tags){

        if(tags.length > 0)
            itemAdapter.setTagsFilter(applyFilter, tags);

        itemAdapter.orderByPrice(priceAsc);

        saveFilterPreferences(applyFilter, priceAsc, tags);

    }

    private void saveFilterPreferences(boolean applyFilter, boolean priceAsc, String[] tags){

        SharedPreferences prefs = getSharedPreferences(FilterAlertDialog.FILTER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FilterAlertDialog.PREF_FILTER, applyFilter);
        editor.putInt(FilterAlertDialog.PREF_PRICE, priceAsc ? 1 : 0);

        if(tags != null){

            Set<String> tagSet = new HashSet<>();
            Collections.addAll(tagSet, tags);

            editor.putStringSet(FilterAlertDialog.PREF_TAGS, tagSet);

        }

        editor.apply();

    }

}
