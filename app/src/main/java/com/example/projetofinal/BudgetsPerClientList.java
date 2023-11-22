package com.example.projetofinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class BudgetsPerClientList extends AppCompatActivity implements AdapterBudgetsPerClient.OnBudgetClickListener{
    private int id;
    private String nome, cpf_cnpj;
    RecyclerView recyclerView;
    List<ClientsBudget> budgets;
    private static final String URL_ORC = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaorcamentos.php?orderby=data_solicitacao&sentido=desc&cliente=";
    AdapterBudgetsPerClient adapter;
    Boolean askRefresh = false;
    TextView titulo;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets_per_client_list);
        id = getIntent().getIntExtra("id_client",0);
        nome = getIntent().getStringExtra("name_client");
        cpf_cnpj = getIntent().getStringExtra("cnpj_client");
        recyclerView = findViewById(R.id.budgetsPerClientList);
        titulo = (TextView) findViewById(R.id.title_budgets_per_client_list);
        texto = "Orçamentos de: " + nome;
        titulo.setText(texto);
        extractBudgets(URL_ORC + id, false);
    }

    public void extractBudgets(String url, boolean askRefresh) {
        budgets = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject budgetObject = response.getJSONObject(i);

                        ClientsBudget budget = new ClientsBudget();
                        budget.setId(budgetObject.getInt("id"));
                        budget.setId_cliente(budgetObject.getInt("id_cliente"));
                        budget.setEmpenho(budgetObject.getString("empenho"));
                        budget.setData_solicitacao(budgetObject.getString("data_solicitacao"));
                        budgets.add(budget);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!askRefresh) {
                    showList(budgets);
                } else {
                    refreshList(budgets);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void showList(List<ClientsBudget> budgets) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterBudgetsPerClient(getApplicationContext(), budgets);
        adapter.setOnBudgetClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshList(List<ClientsBudget> budgets) {
        adapter.budgets = budgets;
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBudgetClick(ClientsBudget budget) {
        openProductsBudget(budget);
    }

    public void openProductsBudget(ClientsBudget budget) {
        int id = budget.getId();
        String nome_cliente = nome;
        String cadastro = cpf_cnpj;
        String empenho = budget.getEmpenho();
        String data = budget.getData_solicitacao();

        Intent intent = new Intent(this, OpenBudget.class);
        intent.putExtra("id_orcamento", id);
        intent.putExtra("nome", nome_cliente);
        intent.putExtra("cadastro", cadastro);
        intent.putExtra("empenho", empenho);
        intent.putExtra("data", data);
        startActivity(intent);
    }
















}