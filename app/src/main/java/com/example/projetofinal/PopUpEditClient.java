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

import java.io.Serializable;

public class PopUpEditClient extends DialogFragment {
    private static final String ITEM = "item";
    Clients client;

    private OnClientEditedListener listener;

    public PopUpEditClient() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnClientEditedListener) {
            listener = (OnClientEditedListener) context;
        } else {
            throw new ClassCastException();
        }
    }

    public static PopUpEditClient newInstance(Clients client) {
        PopUpEditClient fragment = new PopUpEditClient();
        Bundle args = new Bundle();
        args.putSerializable(ITEM, (Serializable) client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            client = (Clients) args.getSerializable(ITEM);
        }
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pop_up_edit_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar_cliente_popup);
        Button btnEnviar = view.findViewById(R.id.popup_botao_enviar_cliente_popup);

        EditText nome = view.findViewById(R.id.input_nome_cliente_popup);
        EditText pfpj = view.findViewById(R.id.input_digita_pfpj_popup);
        EditText telefone = view.findViewById(R.id.input_digita_telefone_popup);
        EditText email = view.findViewById(R.id.input_digita_email_popup);
        EditText cep = view.findViewById(R.id.input_digita_cep_popup);
        EditText cidade = view.findViewById(R.id.input_digita_cidade_popup);
        EditText bairro = view.findViewById(R.id.input_digita_bairro_popup);
        EditText logradouro = view.findViewById(R.id.input_digita_logradouro_popup);
        EditText numero = view.findViewById(R.id.input_digita_numero_popup);

        String textoNome = client.getNomeCliente();
        String textoPfpj = client.getCpf_cnpj();
        String textoTelefone = client.getTelefone();
        String textoEmail = client.getEmail();
        String textoCep = client.getCep();
        String textoCidade = client.getCidade();
        String textoBairro = client.getBairro();
        String textoLogradouro = client.getLogradouro();
        String textoNumero = client.getNumero();

        nome.setText(textoNome);
        pfpj.setText(textoPfpj);
        telefone.setText(textoTelefone);
        email.setText(textoEmail);
        cep.setText(textoCep);
        cidade.setText(textoCidade);
        bairro.setText(textoBairro);
        logradouro.setText(textoLogradouro);
        numero.setText(textoNumero);

        //COLOCANDO FUNÇÕES NOS BOTÕES
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
                String novoPfpj = pfpj.getText().toString();
                String novoTelefone = telefone.getText().toString();
                String novoEmail = email.getText().toString();
                String novoCep = cep.getText().toString();
                String novoCidade = cidade.getText().toString();
                String novoBairro = bairro.getText().toString();
                String novoLogradouro = logradouro.getText().toString();
                String novoNumero = numero.getText().toString();

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("id", client.getId());
                    jsonData.put("nome", novoNome);
                    jsonData.put("cpf_cnpj", novoPfpj);
                    jsonData.put("telefone", novoTelefone);
                    jsonData.put("email", novoEmail);
                    jsonData.put("cep", novoCep);
                    jsonData.put("cidade", novoCidade);
                    jsonData.put("bairro", novoBairro);
                    jsonData.put("logradouro", novoLogradouro);
                    jsonData.put("numero", novoNumero);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editarDados(getContext(), jsonData);
            }
        });
    }

    private void editarDados(Context context, JSONObject jsonData) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/editacliente.php";

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
                        Toast.makeText(context, "Erro ao editar ítem!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
        );
        queue.add(postRequest);
    }

    public interface OnClientEditedListener {
        void onEditSuccess();
    }

}