package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OpenBudget extends AppCompatActivity {
    int id_orcamento;
    String nome_cliente, cadastro_cliente, empenho_orcamento, data_orcamento;
    TextView input_nome, input_cadastro, input_empenho, input_data, input_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_budget);
        id_orcamento = getIntent().getIntExtra("id_orcamento",0);
        nome_cliente = getIntent().getStringExtra("nome");
        cadastro_cliente = getIntent().getStringExtra("cadastro");
        empenho_orcamento = getIntent().getStringExtra("empenho");
        data_orcamento = getIntent().getStringExtra("data");

        input_nome = (TextView) findViewById(R.id.text_name_client_open_budget);
        input_cadastro = (TextView) findViewById(R.id.text_cpf_cnpj_client_open_budget);
        input_empenho = (TextView) findViewById(R.id.text_empenho_client_open_budget);
        input_data = (TextView) findViewById(R.id.text_date_open_budget);
        input_total = (TextView) findViewById(R.id.text_total_cost_open_budget);

        input_nome.setText("Cliente: " + nome_cliente);
        input_cadastro.setText("CPF/CNPJ: " + cadastro_cliente);
        input_empenho.setText("Empenho: " + empenho_orcamento);
        input_data.setText("Data: " + data_orcamento);
    }
}