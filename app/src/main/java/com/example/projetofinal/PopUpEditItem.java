package com.example.projetofinal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PopUpEditItem extends DialogFragment {
    private static final String ITEM = "item";
    Products product;

    private OnItemEditedListener listener;

    public PopUpEditItem() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnItemEditedListener) {
            listener = (OnItemEditedListener) context;
        } else {
            throw new ClassCastException();
        }
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
                String novoNome = nome.getText().toString();
                String novoCodigo = codigo.getText().toString();
                int novaQuantidade = Integer.parseInt(quantidade.getText().toString());
                double novoValor = Double.parseDouble(preco.getText().toString());

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("id", product.getId());
                    jsonData.put("nome_produto", novoNome);
                    jsonData.put("codigo_de_barras", novoCodigo);
                    jsonData.put("quantidade", novaQuantidade);
                    jsonData.put("valor", novoValor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editarDados(getContext(), jsonData);
            }
        });

    }

    private void editarDados(Context context, JSONObject jsonData) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/editaproduto.php";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listener.onEditSuccess();
                        Toast.makeText(context, "Item editado com sucesso!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao editar item!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
        );
        queue.add(postRequest);
    }

    public interface OnItemEditedListener {
        void onEditSuccess();
    }

}