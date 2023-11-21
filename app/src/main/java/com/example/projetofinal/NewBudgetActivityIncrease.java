package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewBudgetActivityIncrease extends AppCompatActivity implements AdapterProductsPerBudget.OnProductClickListener{
    int id_budget, id_client, id_product_selected;
    String name_client, registerClient, url_lista;
    List <Clients> client;
    Clients aClient;
    List <Products> products;
    Products product;
    List<ProductsBudget> productsBudgets;
    ArrayList<String> productsName;
    TextView insertClientName, insertNacionalRegisterClient;
    Spinner spinner;
    Button btnInserir, btnLimpar;
    EditText inputQuant, inputValue;
    RecyclerView recyclerView;
    AdapterProductsPerBudget adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_increase_activity);
        id_budget = getIntent().getIntExtra("id_budget",0);
        id_client = getIntent().getIntExtra("id_client", 0);
        name_client = getIntent().getStringExtra("name_client");
        registerClient =getIntent().getStringExtra("cpf_cnpj");
        insertClientName = (TextView) findViewById(R.id.textNameClientNewBudgetIncrease);
        insertNacionalRegisterClient = (TextView) findViewById(R.id.textClientCPF_CNPJNewBudgetIncrease);
        insertClientName.setText(name_client);
        insertNacionalRegisterClient.setText(registerClient);
        btnInserir = (Button) findViewById(R.id.botao_inserir_new_budget_increase);
        btnLimpar = (Button) findViewById(R.id.botao_limpar_new_budget_increase);
        inputQuant = (EditText) findViewById(R.id.input_product_quant_new_budget_increase);
        inputValue = (EditText) findViewById(R.id.input_product_value_new_budget_increase);
        recyclerView = findViewById(R.id.products_in_budget_increase);
        url_lista = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaprodutosorcamento.php?orderby=id&sentido=desc&id_orcamento=" + id_budget;
        extractProductsBudget(url_lista);
    }
    @Override
    protected void onResume(){
        super.onResume();
        fetchProductList();
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float valorProduto;
                int quantidade;
                int idproduto;
                if (inputValue.getText().toString().isEmpty()) {
                    valorProduto = 0;
                } else {
                    valorProduto = Float.parseFloat(inputValue.getText().toString());
                }
                if (inputQuant.getText().toString().isEmpty()) {
                    quantidade = 0;
                } else {
                    quantidade = Integer.parseInt(inputQuant.getText().toString());
                }
                idproduto = id_product_selected;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_orcamento",id_budget);
                    jsonObject.put("id_produto",idproduto);
                    jsonObject.put("quantidade", quantidade);
                    jsonObject.put("valor",valorProduto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (idproduto == 0 || quantidade == 0 || valorProduto == 0) {
                    Toast.makeText(NewBudgetActivityIncrease.this, "TODOS OS CAMPOS DEVEM SER PREENCHIDOS", Toast.LENGTH_SHORT).show();
                } else {
                    cadastraProduto(getApplicationContext(),jsonObject);
                    inputQuant.setText("");
                    inputValue.setText("");
                }
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputQuant.setText("");
                inputValue.setText("");
            }
        });
    }

    private void fetchProductList(){
        products = new ArrayList<>();
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaprodutos.php?orderby=nome_produto&sentido=asc";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                productsName = new ArrayList<>();
                productsName.add("Escolha o produto: ");

                for (int i = 0; i < response.length(); i ++) {
                    try {
                        JSONObject productObject = response.getJSONObject(i);
                        product = new Products();
                        product.setId(productObject.getInt("id"));
                        product.setProductName(productObject.getString("nome_produto"));
                        product.setQuant(productObject.getString("quantidade"));
                        product.setPrice((float) productObject.getDouble("valor"));
                        products.add(product);

                        String productName = productObject.getString("nome_produto");
                        productsName.add(productName);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                productsName.add("Cadastrar novo produto");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewBudgetActivityIncrease.this, android.R.layout.simple_spinner_dropdown_item, productsName);
                spinner = findViewById(R.id.input_product_name_new_budget_increase);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position > 0) {
                            if (position == productsName.size()-1){
                                Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                onProductSelected(position);
                            }
                        } else {
                            id_product_selected = 0;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void onProductSelected(int position) {
        Products products1 = products.get(position-1);
        id_product_selected = products1.getId();
    }

    private void cadastraProduto(Context context, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/novopo.php?";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Produto cadastrado no orçamento", Toast.LENGTH_SHORT).show();
                extractProductsBudget(url_lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao cadastrar produto", Toast.LENGTH_SHORT).show();
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void extractProductsBudget(String url) {
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
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    showList(productsBudgets);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterProductsPerBudget(getApplicationContext(), budgets);
        adapter.setOnProductClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductClick(ProductsBudget product) {
        Toast.makeText(this, "item", Toast.LENGTH_SHORT).show();
    }
}