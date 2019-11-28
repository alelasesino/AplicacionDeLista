package com.alejandro.aplicaciondelista.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.ui.components.GridSpacingItemDecoration;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.adapters.ItemViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;

/**
 * Activity principal de la aplicacion
 */
public class ItemListActivity extends AppCompatActivity {

    private boolean largeScreen;
    private RecyclerView recyclerView;

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

        recyclerView.setAdapter(new ItemViewAdapter(ItemContent.ITEMS, this::onCardItemClick));
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

            ItemProduct itemProduct = new ItemProduct();

            if(largeScreen)
                launchCustomFragment(itemProduct);
            else
                launchCustomActivity(itemProduct);

        });

        FloatingActionButton removeItemButton = findViewById(R.id.fab_remove_item);
        removeItemButton.setOnClickListener(view -> setEditModeRecyclerView(true));

    }

    private void setEditModeRecyclerView(boolean editMode){

        ItemViewAdapter adapter = (ItemViewAdapter) recyclerView.getAdapter();

        if(adapter != null && editMode != adapter.getEditMode())
            adapter.setEditMode(editMode);

    }

    private void launchCustomActivity(ItemProduct item){

        Intent intent = new Intent(this, ItemCustomActivity.class);
        intent.putExtra(ItemCustomFragment.ARG_ITEM, item);
        startActivity(intent);

    }

    private void launchCustomFragment(ItemProduct item){

        Bundle arguments = new Bundle();
        arguments.putParcelable(ItemCustomFragment.ARG_ITEM, item);

        ItemCustomFragment fragment = new ItemCustomFragment();
        fragment.setArguments(arguments);
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();

    }

    void onCardItemClick(View card, ItemProduct item, boolean editMode){

        if (largeScreen)
            if(editMode)
                launchCustomFragment(item);
            else
                launchItemDetailFragment(item);
        else
            if(editMode)
                launchCustomActivity(item);
            else
                launchItemDetailActivity(card, item);

    }

    private void launchItemDetailFragment(ItemProduct item){

        Bundle arguments = new Bundle();
        arguments.putParcelable(ItemDetailFragment.ARG_ITEM, item);

        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();

    }

    private void launchItemDetailActivity(View card, ItemProduct item){

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM, item);

        ImageView img = card.findViewById(R.id.image_card);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(img, "card_image_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent, options.toBundle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
