package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

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

public class NewBudgetActivityIncrease extends AppCompatActivity {
    int id_budget, id_client;
    String name_client, registerClient;
    List<Clients> client;
    Clients aClient;
    TextView insertClientName, insertNacionalRegisterClient;

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
    }

}