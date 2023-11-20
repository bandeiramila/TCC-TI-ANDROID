package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Locale;

public class NewBudgetActivity extends AppCompatActivity {

    List<Clients> clients;
    ArrayList<String> clientNames;
    ClientsBudget budget;
    Clients client;
    EditText pfpj, empenho;
    Spinner spinner;
    Button btnSalvar;
    int idClient;
    String clientRegister, nameClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_activity);
        pfpj = (EditText) findViewById(R.id.input_digitar_cpf_cnpj_new_budget);
        btnSalvar = (Button) findViewById(R.id.botao_salvar_novo_orcamento);
        empenho = (EditText) findViewById(R.id.input_digitar_empenho);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fetchClientList();
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empenho_cliente;
                if (empenho.getText().toString().isEmpty()) {
                    empenho_cliente = "";
                } else {
                    empenho_cliente = empenho.getText().toString();
                }
                String novoempenho = empenho_cliente;
                int id = idClient;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_cliente", id);
                    jsonObject.put("empenho", novoempenho);
                    jsonObject.put("data_solicitacao","");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cadastrarOrcamento(getApplicationContext(),jsonObject);
            }
        });
    }

    private void fetchClientList(){
        clients = new ArrayList<>();
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaclientes.php?orderby=nome&sentido=asc";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                clientNames = new ArrayList<>();
                clientNames.add("Escolha o Cliente: ");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject clientObject = response.getJSONObject(i);
                        client = new Clients();
                        client.setId(clientObject.getInt("id"));
                        client.setNomeCliente(clientObject.getString("nome"));
                        client.setCpf_cnpj(clientObject.getString("cpf_cnpj"));
                        clients.add(client);

                        String clientName = clientObject.getString("nome");
                        clientNames.add(clientName);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                clientNames.add("Cadastrar novo cliente");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewBudgetActivity.this, android.R.layout.simple_spinner_dropdown_item, clientNames);
                spinner = findViewById(R.id.input_client_name_new_budget);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position > 0) {
                            if (position == clientNames.size()-1){
                                Intent intent = new Intent(getApplicationContext(),NewClientActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                onClientSelected(position);
                            }
                        } else {
                            pfpj.setText("");
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

    private void onClientSelected(int position){
        Clients cliente = clients.get(position-1);
        String pj = cliente.getCpf_cnpj();
        idClient = cliente.getId();
        clientRegister = pj;
        nameClient = cliente.getNomeCliente();
        pfpj.setText(pj);
    }

    private void cadastrarOrcamento(Context context, JSONObject jsonObject){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/novoorcamento.php?";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Orçamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                getIdBudget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao cadastrar orçamento!", Toast.LENGTH_SHORT).show();
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(postRequest);
    }

    private void getIdBudget(){
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaorcamentos.php?last_id=" + idClient;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    budget = new ClientsBudget();
                    budget.setId(jsonObject.getInt("id"));
                    budget.setId_cliente(jsonObject.getInt("id_cliente"));

                    onBudgetConfirmClick(budget.getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
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

    private void onBudgetConfirmClick(int id) {

        Intent intent = new Intent(this, NewBudgetActivityIncrease.class);
        intent.putExtra("id_budget", id);
        intent.putExtra("id_client", idClient);
        intent.putExtra("name_client", nameClient);
        intent.putExtra("cpf_cnpj", clientRegister);
        startActivity(intent);
    }

//    private void extractClient(int client_id) {
//        clients = new ArrayList<>();
//        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaclientes.php?id=" + client_id;
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    JSONObject jsonObject = response.getJSONObject(0);
//                    client = new Clients();
//                    client.setId(jsonObject.getInt("id"));
//                    client.setNomeCliente(jsonObject.getString("nome"));
//                    client.setCpf_cnpj(jsonObject.getString("cpf_cnpj"));
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        });
//    }


}