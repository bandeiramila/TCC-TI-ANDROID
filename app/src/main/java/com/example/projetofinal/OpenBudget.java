package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenBudget extends AppCompatActivity implements AdapterProductsPerBudget.OnProductClickListener {
    int id_orcamento;
    String nome_cliente, cadastro_cliente, empenho_orcamento, data_orcamento, url_lista;
    TextView input_nome, input_cadastro, input_empenho, input_data, input_total;
    List<ProductsBudget> productsBudgets;
    RecyclerView recyclerView;
    Double totalBudget;
    AdapterProductsPerBudget adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_budget);
        id_orcamento = getIntent().getIntExtra("id_orcamento",0);
        nome_cliente = getIntent().getStringExtra("nome");
        cadastro_cliente = getIntent().getStringExtra("cadastro");
        empenho_orcamento = getIntent().getStringExtra("empenho");
        data_orcamento = getIntent().getStringExtra("data");
        if (empenho_orcamento.isEmpty()) {
            empenho_orcamento = "Não";
        }

        input_nome = (TextView) findViewById(R.id.text_name_client_open_budget);
        input_cadastro = (TextView) findViewById(R.id.text_cpf_cnpj_client_open_budget);
        input_empenho = (TextView) findViewById(R.id.text_empenho_client_open_budget);
        input_data = (TextView) findViewById(R.id.text_date_open_budget);
        input_total = (TextView) findViewById(R.id.text_total_cost_open_budget);
        recyclerView = findViewById(R.id.products_in_open_budget);

        input_nome.setText("Cliente: " + nome_cliente + "");
        input_cadastro.setText("CPF/CNPJ: " + cadastro_cliente);
        input_empenho.setText("Empenho: " + empenho_orcamento);
        input_data.setText("Data: " + data_orcamento);

        url_lista = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaprodutosorcamento.php?orderby=id&sentido=desc&id_orcamento=" + id_orcamento;
        extractProductsBudget(url_lista);
    }

    public void extractProductsBudget(String url) {
        totalBudget = 0.0;
        productsBudgets = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject productObject = response.getJSONObject(i);
                        ProductsBudget budget = new ProductsBudget();
                        budget.setId(productObject.getInt("id"));
                        budget.setId_orcamento(productObject.getInt("id_orcamento"));
                        budget.setId_produto(productObject.getInt("id_produto"));
                        budget.setQuantidade(productObject.getInt("quantidade"));
                        budget.setValor_unitario(productObject.getDouble("valor_unitario"));
                        budget.setNome_produto(productObject.getString("nome_produto"));
                        productsBudgets.add(budget);

                        double valorTotal = budget.getValor_unitario() * budget.getQuantidade();
                        totalBudget = totalBudget + valorTotal;
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    showList(productsBudgets);
                    String total = String.format("R$ %1$.2f", totalBudget);
                    input_total.setText(total);
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

    private void showList(List<ProductsBudget> budgets) {
        recyclerView.setLayoutManager((new LinearLayoutManager(getApplicationContext())));
        adapter = new AdapterProductsPerBudget(getApplicationContext(), budgets);
        adapter.setOnProductClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductClick(ProductsBudget product) {

    }
}