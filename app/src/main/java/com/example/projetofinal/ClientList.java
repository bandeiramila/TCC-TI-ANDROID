package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

public class ClientList extends AppCompatActivity implements AdapterClients.OnClientClickListener {
    RecyclerView recyclerView;
    List<Clients> clients;
    private static final String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaclientes.php?";
    AdapterClients adapter;
    Boolean askRefresh = false;
    Button btn_search, btn_organize;
    EditText input_search;
    TextView close_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        recyclerView = findViewById(R.id.clientsList);
        extractClients(JSON_URL, false);
        btn_search = (Button) findViewById(R.id.button_search_client_list);
        btn_organize = (Button) findViewById(R.id.button_organize_client_list);
        input_search = (EditText) findViewById(R.id.input_search_client_list);
        close_search = (TextView) findViewById(R.id.button_close_client_list);

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
                extractClients(url_search, false);
            }
        });

        btn_organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onCreateDialogOrderBy();
            }
        });
    }

    private void extractClients(String url, boolean askRefresh) {
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
                        client.setCpf_cnpj(clientObject.getString("cpf_cnpj"));
                        client.setTelefone(clientObject.getString("telefone"));
                        client.setEmail(clientObject.getString("email"));
                        client.setCep(clientObject.getString("cep"));
                        client.setCidade(clientObject.getString("cidade"));
                        client.setBairro(clientObject.getString("bairro"));
                        client.setLogradouro(clientObject.getString("logradouro"));
                        client.setNumero(clientObject.getString("numero"));
                        clients.add(client);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!askRefresh) {
                    showList(clients);
                } else {
                    refreshList(clients);
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

    private void showList(List<Clients> clients) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterClients(getApplicationContext(), clients);
        adapter.setOnClientClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshList(List<Clients> clients) {
        adapter.clients = clients;
        adapter.notifyDataSetChanged();
    }

    /////////POP UP GERAL ///////////
    @Override
    public void onClientClick(Clients clients) {
        showAlertDialog(clients);
    }

    public void showAlertDialog(Clients client) {
        PopUpClient dialogFragment = PopUpClient.newInstance(client);
        getSupportFragmentManager().beginTransaction().commit();
        dialogFragment.show(getSupportFragmentManager(), "PopUpClient");
    }

    /////////POP UP EDIÇÃO //////////


















}