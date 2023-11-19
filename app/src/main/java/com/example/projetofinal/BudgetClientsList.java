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

public class BudgetClientsList extends AppCompatActivity implements AdapterClients.OnClientClickListener{

    RecyclerView recyclerView;
    List<Clients>clients;
    private static final String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaclientescomorcamentos.php?";
    AdapterClients adapter;
    Button btn_search, btn_organize;
    EditText input_search;
    TextView close_search;
    FloatingActionButton fab_add_budget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_clients_list);
        recyclerView = findViewById(R.id.clientsBudgetList);
        extractClients(JSON_URL);
        btn_search = (Button) findViewById(R.id.button_search_budgets_list);
        btn_organize = (Button) findViewById(R.id.button_organize_budget_list);
        input_search = (EditText) findViewById(R.id.input_search_budgets_list);
        close_search = (TextView) findViewById(R.id.button_close_budgets_list);
        fab_add_budget = (FloatingActionButton) findViewById(R.id.fab_new_budget);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_search.setVisibility(View.VISIBLE);
                close_search.setVisibility(View.VISIBLE);
            }
        });

        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_search.setVisibility(View.GONE);
                close_search.setVisibility(View.GONE);
                input_search.setText("");

                imm.hideSoftInputFromWindow(input_search.getWindowToken(), 0);
            }
        });

        input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textoDigitado = editable.toString();

                String url_search = JSON_URL + "nome=" + textoDigitado;
                extractClients(url_search);
            }
        });

        btn_organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onCreateDialogOrderBy();}
        });

        fab_add_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBudgetActivity.class);
                startActivity(intent);
            }
        });
    }



    private void extractClients(String url) {
        clients = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject clientObject = response.getJSONObject(i);

                        Clients client = new Clients();
                        client.setId(clientObject.getInt("id"));
                        client.setNomeCliente(clientObject.getString("nome"));
                        clients.add(client);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                showList(clients);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void showList(List<Clients> clients) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterClients(getApplicationContext(), clients);
        adapter.setOnClientClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    ///////// ESCOLHE CLIENTE ///////////

    @Override
    public void onClientClick(Clients clients) {
        Intent intent = new Intent(this, BudgetsPerClientList.class);
        intent.putExtra("id_client", clients.getId());
        intent.putExtra("name_client", clients.getNomeCliente());
        startActivity(intent);
    }

    //////////////// ORDENAÇÃO //////////////
    private void onCreateDialogOrderBy(){
        String[] order_clients = getResources().getStringArray(R.array.order_clients);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ordenação");
        builder.setItems(order_clients, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String url = JSON_URL;
                switch (which) {
                    case 0:
                        url = JSON_URL;
                        Toast.makeText(BudgetClientsList.this, "Padrão", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        url = JSON_URL + "orderby=nome&sentido=asc";
                        Toast.makeText(BudgetClientsList.this, "De A a Z", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        url = JSON_URL + "orderby=nome&sentido=desc";
                        Toast.makeText(BudgetClientsList.this, "De Z a A", Toast.LENGTH_SHORT).show();
                        break;
                }
                extractClients(url);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}