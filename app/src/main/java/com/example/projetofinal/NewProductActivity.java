package com.example.projetofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewProductActivity extends AppCompatActivity {

    private static String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/novoproduto.php";
    EditText inputNome, inputQuantidade, inputValor, inputCodigo;
    Button btnSalvar, btnLimpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_product_activity);
        inputNome = (EditText) findViewById(R.id.input_nome_novo_produto);
        inputQuantidade = (EditText) findViewById(R.id.input_quantidade_novo_produto);
        inputValor = (EditText) findViewById(R.id.input_preco_novo_produto);
        inputCodigo = (EditText) findViewById(R.id.input_digitar_codigo_novo_produto);
        btnSalvar = (Button) findViewById(R.id.botao_salvar_novo_produto);
        btnLimpa = (Button) findViewById(R.id.botao_limpar_novo_produto);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant;
                double valor;
                if(inputQuantidade.getText().toString().isEmpty()) {
                    quant = 0;
                } else {
                    quant = Integer.parseInt(inputQuantidade.getText().toString());
                }
                if(inputValor.getText().toString().isEmpty()) {
                    valor = 0;
                } else {
                    valor = Double.parseDouble(inputValor.getText().toString());
                }

                String novoNome = inputNome.getText().toString();
                String novoCodigo = inputCodigo.getText().toString();
                int novaQuantidade = quant;
                double novoValor = valor;

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("nome_produto", novoNome);
                    jsonData.put("codigo_de_barras", novoCodigo);
                    jsonData.put("quantidade", novaQuantidade);
                    jsonData.put("valor", novoValor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (novoNome.isEmpty()){
                    inputNome.setError("O nome do produto é obrigatório.");
                }else {
                    cadastrarProduto(getApplicationContext(), jsonData);
                }
            }
        });

        btnLimpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNome.setText("");
                inputQuantidade.setText("");
                inputValor.setText("");
                inputCodigo.setText("");
            }
        });
    }

    private void cadastrarProduto(Context context, JSONObject jsonData){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL, jsonData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Item cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao cadastrar ítem!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";}


        };
        queue.add(postRequest);
    }


}