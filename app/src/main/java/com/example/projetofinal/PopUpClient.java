package com.example.projetofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class PopUpClient extends DialogFragment {
    private static final String ITEM = "item";
    Clients client;
    private OnButtonClickListener listener;
    public PopUpClient(){   }

    public static PopUpClient newInstance(Clients client) {
        PopUpClient fragment = new PopUpClient();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pop_up_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titulo = view.findViewById(R.id.itemNome);
        String texto = "Cliente: " + client.getNomeCliente();
        titulo.setText(texto);

        Button btnEditar = view.findViewById(R.id.popup_botao_editar);
        Button btnExcluir = view.findViewById(R.id.popup_botao_excluir);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EDITAR AQUI
                dismiss();
                listener.onEditClick(client);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXCLUIR AQUI, mas lógica fica na função
                showDialogConfirm(client.getId(), client.getNomeCliente());
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

    private void showDialogConfirm(int id, String nome) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação");
        builder.setMessage("Tem certeza que deseja excluir o cliente \"" + nome + "\"?");

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
        String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/deletacliente.php?id=" + id;

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(requireContext(), "Cliente removido com sucesso", Toast.LENGTH_SHORT).show();
                listener.onDeleteClick();
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(requireContext(), "Erro ao remover cliente", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public interface OnButtonClickListener {
        void onEditClick(Clients client);
        void onDeleteClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener){
            listener = (OnButtonClickListener) context;
        } else {
            throw new ClassCastException();
        }
    }
}
