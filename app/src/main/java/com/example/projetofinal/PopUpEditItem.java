package com.example.projetofinal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PopUpEditItem extends Fragment {
    private static final String ITEM = "item";
    Products product;

    public PopUpEditItem() {
        // Required empty public constructor
    }

    public static PopUpEditItem newInstance(Products product) {
        PopUpEditItem fragment = new PopUpEditItem();
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
        return inflater.inflate(R.layout.fragment_pop_up_edit_item, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //COLOCA FUNÇÕES NOS BOTÕES
    }


}