package com.example.projetofinal;

import android.app.AlertDialog;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PopUpProductBudget extends DialogFragment {
    private static final String ITEM = "item";
    ProductsBudget productsBudget;
    private OnButtonClickListener listener;
    public PopUpProductBudget(){}

    public static PopUpProductBudget newInstance(ProductsBudget productsBudget) {
        PopUpProductBudget fragment = new PopUpProductBudget();
        Bundle args = new Bundle();
        args.putSerializable(ITEM, productsBudget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (getArguments() != null) {
            productsBudget = (ProductsBudget) args.getSerializable(ITEM);
        }
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_pop_up_item, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView titulo = view.findViewById(R.id.itemNome);
        String texto = "Produto: " + productsBudget.getNome_produto();
        titulo.setText(texto);

        Button btnEditar = view.findViewById(R.id.popup_botao_editar);
        Button btnExcluir = view.findViewById(R.id.popup_botao_excluir);
        Button btnCancelar = view.findViewById(R.id.popup_botao_cancelar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EDITAR AQUI
                dismiss();
                listener.onEditClick(productsBudget);
            }
        });
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXCLUIR AQUI
                showDialogConfirm(productsBudget.getId(), productsBudget.getNome_produto());
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
        builder.setMessage("Tem certeza que deseja excluir o produto \"" + nome +"\" do orçamento?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                excluirItem(getContext(), id);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {dialogInterface.dismiss();}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void excluirItem(Context context, int id) {
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/deletapo.php?id=" + id;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(requireContext(), "Produto removido com sucesso", Toast.LENGTH_SHORT).show();
                listener.onDeleteClick();
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(requireContext(), "Erro ao remover produto", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public interface OnButtonClickListener {
        void onEditClick(ProductsBudget productsBudget);
        void onDeleteClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            listener = (OnButtonClickListener) context;
        } else {
            throw new ClassCastException();
        }
    }
}
