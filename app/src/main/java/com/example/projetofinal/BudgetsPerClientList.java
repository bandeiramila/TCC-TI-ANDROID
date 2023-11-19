package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class BudgetsPerClientList extends AppCompatActivity {
    private int id;
    private String nome;
    RecyclerView recyclerView;
    List<Clients> clients;
    private static final String URL_ORC = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaorcamentos.php?";
    AdapterClients adapter;
    Boolean askRefresh = false;
    TextView titulo;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets_per_client_list);
        id = getIntent().getIntExtra("id_client",0);
        nome = getIntent().getStringExtra("name_client");
        recyclerView = findViewById(R.id.budgetsPerClientList);
        titulo = (TextView) findViewById(R.id.title_budgets_per_client_list);
        texto = "Orçamentos de: " + nome;
        titulo.setText(texto);
    }




















}