package com.example.projetofinal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

public class PopUpItem extends DialogFragment {
    private static final String ITEM = "item";
    Products product;

    public PopUpItem() {

        // Required empty public constructor
    }

    public static PopUpItem newInstance(Products product) {

        PopUpItem fragment = new PopUpItem();
        Bundle args = new Bundle();
        args.putSerializable(ITEM, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            product = (Products) args.getSerializable(ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titulo = view.findViewById(R.id.itemNome);

        titulo.setText(product.getProductName());
    }
}