package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class NewBudgetActivity extends AppCompatActivity {

    List<Clients> clients;
    ArrayList<String> clientNames;
    Clients client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_activity);
        fetchClientList();
    }

    public void fazerOrcamentoSalvar(View view){
        Intent intent = new Intent(this, NewBudgetActivityIncrease.class);
        startActivity(intent);
    }

    private void fetchClientList(){
        clients = new ArrayList<>();
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaclientes.php?orderby=nome&sentido=asc";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                clientNames = new ArrayList<>();
                clients = new ArrayList<>();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewBudgetActivity.this, android.R.layout.simple_spinner_dropdown_item, clientNames);
                Spinner spinner = findViewById(R.id.input_client_name_new_budget);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position > 0) {
                            String selectedClientName = clientNames.get(position);
                            fetchClientData(selectedClientName);
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

    private void fetchClientData(String selectedClientName){

    }
}