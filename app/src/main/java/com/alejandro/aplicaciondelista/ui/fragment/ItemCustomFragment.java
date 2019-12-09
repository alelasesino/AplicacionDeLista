package com.alejandro.aplicaciondelista.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.aplicaciondelista.ItemCustomActionListener;
import com.alejandro.aplicaciondelista.R;
import com.alejandro.aplicaciondelista.Utils;
import com.alejandro.aplicaciondelista.adapters.TagViewAdapter;
import com.alejandro.aplicaciondelista.model.ItemProduct;
import com.alejandro.aplicaciondelista.ui.activity.ItemListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Fragmento encargado de añadir y actualizar productos
 */
public class ItemCustomFragment extends Fragment {

    public static final String ARG_ITEM = "item_custom";

    private Activity activity;
    private ImageView headerImageView;
    private EditText txtName, txtDetails, txtPrice, txtTag;
    private TagViewAdapter tagAdapter;

    private ItemProduct currentItem;
    private ItemCustomActionListener customActionListener;

    private boolean largeScreen;

    public ItemCustomFragment(){}

    public ItemCustomFragment(ItemCustomActionListener customActionListener) {
        this.customActionListener = customActionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();

        /*Si la activity que contiene este fragment es la activity que muestra la lista de productos
         * significa que la pantalla es superior a w900dp*/
        largeScreen = activity instanceof ItemListActivity;

        argumentsReceived();

    }

    private void argumentsReceived(){

        Bundle arguments = getArguments();

        if(arguments != null)
            if(arguments.containsKey(ARG_ITEM))
                currentItem = arguments.getParcelable(ARG_ITEM);

    }

    /**
     * Obtiene el producto creado o actualizado de todos los campos de la vista
     * @return Producto de los campos de la vista
     */
    private ItemProduct getItemProductFromFields(){

        ItemProduct item = new ItemProduct();
        item.setId(currentItem.getId());
        item.setName(txtName.getText().toString());
        item.setDetails(txtDetails.getText().toString());
        item.setPrice(Utils.priceToString(txtPrice.getText().toString()));
        item.setImageUrl(currentItem.getImageUrl());
        item.setFavorite(currentItem.isFavorite());

        item.setTags(tagAdapter.getTags());

        return item;

    }

    private void initializeComponents(View fragmentView){

        Button btAddTag;
        FloatingActionButton btSave;

        txtDetails = fragmentView.findViewById(R.id.txt_details);
        btAddTag = fragmentView.findViewById(R.id.bt_add_tag);
        txtTag = fragmentView.findViewById(R.id.txt_tag);

        if(largeScreen){

            headerImageView = fragmentView.findViewById(R.id.custom_image_header);
            txtName = fragmentView.findViewById(R.id.txt_name);
            txtPrice = fragmentView.findViewById(R.id.txt_price);
            btSave = fragmentView.findViewById(R.id.fab_save_item);

        } else {

            headerImageView = activity.findViewById(R.id.custom_image_header);
            txtName = activity.findViewById(R.id.txt_name);
            txtPrice = activity.findViewById(R.id.txt_price);
            btSave = activity.findViewById(R.id.fab_save_item);

        }

        RecyclerView tagRecycler = fragmentView.findViewById(R.id.tags_recycler);

        tagAdapter = new TagViewAdapter(currentItem.getTags(), true);
        tagRecycler.setAdapter(tagAdapter);

        if(btSave != null)
            btSave.setOnClickListener(view -> {

                if(customActionListener != null)
                    customActionListener.onSaveItemCustom(getItemProductFromFields());
                else
                    closeActivityWithResult();

            });

        if(btAddTag != null)
            btAddTag.setOnClickListener(view -> {

                if(!txtTag.getText().toString().equals("")){

                    tagAdapter.addItem(Utils.capitalize(txtTag.getText().toString()));
                    txtTag.setText("");

                }

            });

        bindDataProducts();

    }

    private void closeActivityWithResult(){

        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARG_ITEM, getItemProductFromFields());

        activity.setResult(Activity.RESULT_OK, resultIntent);
        activity.finishAfterTransition();

    }

    private void bindDataProducts(){

        Utils.loadPicassoImage(getActivity(), headerImageView, currentItem.getImageUrl());
        Utils.setText(txtName, currentItem.getName());
        Utils.setText(txtDetails, currentItem.getDetails());

        if(currentItem.getPrice() == 0)
            Utils.setText(txtPrice, "");
        else
            Utils.setText(txtPrice, Utils.toPrice(currentItem.getPrice()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_custom, container, false);

        if(currentItem != null && rootView != null)
            initializeComponents(rootView);

        return rootView;
    }

}
