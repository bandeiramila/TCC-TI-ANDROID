package com.example.projetofinal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class PopUpEditItem extends DialogFragment {
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
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pop_up_edit_item, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar_popup);
        Button btnEnviar = view.findViewById(R.id.popup_botao_enviar_popup);

        EditText nome = view.findViewById(R.id.input_nome_produto_popup);
        EditText codigo = view.findViewById(R.id.input_digita_codigo_popup);
        EditText quantidade = view.findViewById(R.id.input_digita_quantidade_popup);
        EditText preco = view.findViewById(R.id.input_digita_valor_popup);

        String textoNome = product.getProductName();
        String textoCodigo = product.getBarCode();
        String textoQuantidade = product.getQuant();
        String textoPreco = String.valueOf(product.getPrice());

        nome.setText(textoNome);
        codigo.setText(textoCodigo);
        quantidade.setText(textoQuantidade);
        preco.setText(textoPreco);

        //COLOCA FUNÇÕES NOS BOTÕES
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}