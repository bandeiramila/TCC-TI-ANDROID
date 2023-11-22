package com.example.projetofinal;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.jar.JarException;

public class PopUpEditProductBudget extends DialogFragment {

    private static final String ITEM = "item";
    ProductsBudget productsBudget;
    private OnProductEditedListener listener;

    public PopUpEditProductBudget() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProductEditedListener) {
            listener = (OnProductEditedListener) context;
        } else {
            throw new ClassCastException();
        }
    }

    public static PopUpEditProductBudget newInstance(ProductsBudget productsBudget) {
        PopUpEditProductBudget fragment = new PopUpEditProductBudget();
        Bundle args = new Bundle();
        args.putSerializable(ITEM, (Serializable) productsBudget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            productsBudget = (ProductsBudget) args.getSerializable(ITEM);
        }
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pop_up_edit_product_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar_produto_popup);
        Button btnEnviar = view.findViewById(R.id.popup_botao_enviar_produto_popup);
        TextView nome = view.findViewById(R.id.edita_produto_nome_popup);
        EditText quantidade = view.findViewById(R.id.input_digita_quantidade_popup);
        EditText valor = view.findViewById(R.id.input_digita_valor_produto_popup);

        String recebeNome = productsBudget.getNome_produto();
        String textoNome = "Produto: " + recebeNome;
        String textoQuantidade = String.valueOf(productsBudget.getQuantidade());
        String textoPreco = String.valueOf(productsBudget.getValor_unitario());

        nome.setText(textoNome);
        quantidade.setText(textoQuantidade);
        valor.setText(textoPreco);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dismiss();}
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int novaQuantidade = Integer.parseInt(quantidade.getText().toString());
                double novoValor = Double.parseDouble(valor.getText().toString());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", productsBudget.getId());
                    jsonObject.put("quantidade", novaQuantidade);
                    jsonObject.put("valor", novoValor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editaDados(getContext(), jsonObject);
            }
        });
    }

    private void editaDados(Context context, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/editapo.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onEditSuccess();
                Toast.makeText(context, "Produto editado com sucesso!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao editar produto!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface OnProductEditedListener {
        void onEditSuccess();
    }
}