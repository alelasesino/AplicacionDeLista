package com.alejandro.aplicaciondelista.ui.activity;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
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
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.ItemCardActionListener;
import com.alejandro.aplicaciondelista.ItemCustomActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.ItemViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.model.Tag;
import com.alejandro.aplicaciondelista.model.db.ProductProvider;
import com.alejandro.aplicaciondelista.ui.components.GridSpacingItemDecoration;
import com.alejandro.aplicaciondelista.ui.dialog.FilterDialogFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemCustomFragment;
import com.alejandro.aplicaciondelista.ui.fragment.ItemDetailFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que controla la Activity principal de la aplicacion que muestra la lista de los productos
 */
public class ItemListActivity extends AppCompatActivity implements ItemCardActionListener, ItemCustomActionListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CUSTOM_ACTIVITY = 1;
    private static final int DETAILS_ACTIVITY = 2;

    /*Pantalla grande, superior a w900d*/
    private boolean largeScreen;

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

        setupNavigationView(findViewById(R.id.nav_view));
        setupRecyclerView(findViewById(R.id.item_list));
        setupFloatingButtons();

        loaderData();

    }

    /**
     * Inicializa el recycler view que muestra la lista de los productos
     * @param recyclerView Recycler para inicializar
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        itemAdapter = new ItemViewAdapter(this, this);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, largeScreen));

    }

    private void insertItem(ItemProduct item){

        ContentValues cv = getItemContentValues(item);
        getContentResolver().insert(ProductProvider.CONTENT_URI, cv);
        loaderData();

    }

    private void updateItem(ItemProduct item){

        ContentValues cv = getItemContentValues(item);
        getContentResolver().update(ProductProvider.CONTENT_URI, cv, ProductProvider.ItemProduct.COLUMN_ID + " = ?", new String[]{item.getId()});
        loaderData();

    }

    private void removeItem(String id){

        getContentResolver().delete(ProductProvider.CONTENT_URI, ProductProvider.ItemProduct.COLUMN_ID + " = ?", new String[]{id});
        loaderData();

    }

    private void updateFavoriteItem(String id, boolean isFavorite){

        ContentValues cv = new ContentValues();
        cv.put(ProductProvider.ItemProduct.COLUMN_FAVORITE, isFavorite ? 1 : 0);
        getContentResolver().update(ProductProvider.CONTENT_URI, cv, ProductProvider.ItemProduct.COLUMN_ID + " = ?", new String[]{id});
        loaderData();

    }

    private ContentValues getItemContentValues(ItemProduct item) {

        ContentValues cv = new ContentValues();
        cv.put(ProductProvider.ItemProduct.COLUMN_IMAGE_URL, item.getImageUrl());
        cv.put(ProductProvider.ItemProduct.COLUMN_NAME, item.getName());
        cv.put(ProductProvider.ItemProduct.COLUMN_DETAILS, item.getDetails());
        cv.put(ProductProvider.ItemProduct.COLUMN_PRICE, item.getPrice());
        cv.put(ProductProvider.ItemProduct.COLUMN_FAVORITE, item.isFavorite() ? 1 : 0);

        try{
            cv.put(ProductProvider.ItemProduct.COLUMN_IMAGE, Utils.getBytes(Utils.getImage(this, Uri.parse(item.getImageUrl()))));
        }catch (Exception e){
            e.printStackTrace();
        }

        return cv;

    }

    private void loaderData(){
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    /**
     * Inicializa todas las acciones de los menus de la barra de navegacion lateral
     * @param navigationView Navigation para inicializar
     */
    private void setupNavigationView(NavigationView navigationView){

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            /*case R.id.nav_all:
                    itemAdapter.setTagsFilter(false, null);
                    break;
                case R.id.nav_burgers:
                    itemAdapter.setTagsFilter(true, new Tag[]{new Tag("Burger")});
                    break;
                case R.id.nav_bebidas:
                    itemAdapter.setTagsFilter(true, new Tag[]{new Tag("Bebida")});
                    break;
                case R.id.nav_postres:
                    itemAdapter.setTagsFilter(true, new Tag[]{new Tag("Postre")});
                    break;
                case R.id.nav_salsas:
                    itemAdapter.setTagsFilter(true, new Tag[]{new Tag("Salsa")});
                    break;*/

            if (menuItem.getItemId() == R.id.nav_visit) {
                Utils.openWeb(this, Utils.OWNER_GITHUB);
            }

            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START, true);

            return false;

        });

    }

    /**
     * Inicializa las acciones de los botones flotantes
     */
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

    /**
     * Crea un nuevo producto lanzando la actividad o fragmento correspondiente
     */
    private void createNewItem(){

        currentItem = new ItemProduct();

        if(largeScreen)
            launchCustomFragment();
        else
            launchCustomActivity(null);

    }

    /**
     * Establece al adaptador el modo de edicion
     * @param editMode Modo de edicion
     */
    private void setEditModeRecyclerView(boolean editMode){

        if(itemAdapter != null && editMode != itemAdapter.getEditMode()) {

            itemAdapter.setEditMode(editMode);
            removeFragments();

        }

    }

    /**
     * Elimina el fragmentos de los detalles de un producto y
     * el fragmento de añadir o actualizar un producto
     */
    private void removeFragments(){

        removeFragment(itemCustomFragment);
        removeFragment(itemDetailFragment);

    }

    /**
     * Elimina el fragmento recibido por parametro
     * @param fragment Fragmento a borrar
     */
    private void removeFragment(Fragment fragment){

        if(fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();

    }

    /**
     * Lanza la activity que permite añadir y actualizar productos
     * para pantallas no superior a w900dp
     * @param card Vista de la tarjeta para editar
     */
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

    /**
     * Lanza el fragmento que permite añadir y actualizar productos
     * para pantallas superiores a w900dp
     */
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

    /**
     * Lanza el fragmento que muestra los detalles del producto
     * para pantallas superiores a w900dp
     */
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

    /**
     * Lanza la activity que muestra los detalles del producto
     * para pantallas no superiores a w900dp
     * @param card Vista de la tarjeta para mostrar
     */
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
            insertItem(item);
        else
            updateItem(item);

        removeFragments();

    }

    @Override
    public void onCardItemRemoved(View card, ItemProduct item) {

        if(currentItem != null)
            if(item.getId().equals(currentItem.getId()))
                removeFragments();

        removeItem(item.getId());

    }

    @Override
    public void onChangeFavoriteState(boolean isFavorite) {

        if(currentItem != null){
            currentItem.setFavorite(isFavorite);
            updateFavoriteItem(currentItem.getId(), isFavorite);
        }

    }
    /*
    /**
     * El recycler view realiza un suave scroll hacia la posicion deseada
     * @param position Posicion
     */
     /*void doSmoothScroll(int position){
        recyclerView.smoothScrollToPosition(position);
    }*/

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
        //itemAdapter.setFavoriteFilter(menu.isChecked());

    }

    /**
     * Muestra el fragmento de dialogo para filtrar los productos
     */
    private void showFilterDialogFragment(){

        FragmentManager manager = getSupportFragmentManager();
        FilterDialogFragment dialog = FilterDialogFragment.newInstance("Filtrar productos", this::onFilterAlertAccept);
        dialog.show(manager, "filter_dialog");

    }

    private void onFilterAlertAccept(boolean applyFilter, boolean priceAsc, Tag[] tags){

        /*if(tags.length > 0)
            itemAdapter.setTagsFilter(applyFilter, tags);

        itemAdapter.orderByPrice(priceAsc);*/

        saveFilterPreferences(applyFilter, priceAsc, tags);

    }

    /**
     * Guarda las preferencias de filtrado del fragmento de dialogo
     * @param applyFilter Aplicar filtro
     * @param priceAsc Precio ascendente
     * @param tags Tags de filtrado
     */
    private void saveFilterPreferences(boolean applyFilter, boolean priceAsc, Tag[] tags){

        SharedPreferences prefs = getSharedPreferences(FilterDialogFragment.FILTER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FilterDialogFragment.PREF_FILTER, applyFilter);
        editor.putInt(FilterDialogFragment.PREF_PRICE, priceAsc ? 1 : 0);

        if(tags != null){

            String[] str_tags = new String[tags.length];

            for(int i = 0; i<tags.length; i++)
                str_tags[i] = tags[i].getTag();

            Set<String> tagSet = new HashSet<>();
            Collections.addAll(tagSet, str_tags);

            editor.putStringSet(FilterDialogFragment.PREF_TAGS, tagSet);

        }

        editor.apply();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, ProductProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(itemAdapter != null)
            itemAdapter.changeCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }

}
