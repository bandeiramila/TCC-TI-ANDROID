package com.example.projetofinal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Serializable;

public class PopUpItem extends DialogFragment {
    private static final String ITEM = "item";
    Products product;
    private OnEditClickListener listener;

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
        setCancelable(false);

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
        String texto = "ítem: " + product.getProductName();

        titulo.setText(texto);

        Button btnEditar = view.findViewById(R.id.popup_botao_editar);
        Button btnExcluir = view.findViewById(R.id.popup_botao_excluir);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EDITAR AQUI
                dismiss();
                listener.onEditClick(product);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXCLUIR AQUI, mas lógica fica na função
                dismiss();
                showDialogConfirm();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CANCELA
                dismiss();
            }
        });
    }

    private void showDialogConfirm() {
        int id = product.getId();
        String nome = product.getProductName();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação");
        builder.setMessage("Tem certeza que deseja excluir \"" + nome + "\"?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para excluir o item aqui

                excluirItem(getContext(), id);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void excluirItem(Context context, int id){
        String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/deletaproduto.php?id=" + id;

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(requireContext(), "Item excluído com sucesso", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(requireContext(), "Erro ao excluir o item", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public interface OnEditClickListener {
        void onEditClick(Products produto);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnEditClickListener){
            listener = (OnEditClickListener) context;
        } else {
            throw new ClassCastException();
        }
    }

}