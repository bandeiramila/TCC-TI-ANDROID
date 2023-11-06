package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewClientActivity extends AppCompatActivity {

    EditText inputNome, inputPFPJ, inputTelefone, inputEmail, inputCep, inputCidade, inputLogradouro, inputBairro, inputNumero;
    Button btnSalvar, btnLimpar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        inputNome = (EditText) findViewById(R.id.input_nome_cliente_add_client);
        inputPFPJ = (EditText) findViewById(R.id.input_digita_pfpj_add_client);
        inputTelefone = (EditText) findViewById(R.id.input_digita_telefone_add_client);
        inputEmail = (EditText) findViewById(R.id.input_digita_email_add_client);
        inputCep = (EditText) findViewById(R.id.input_digita_cep_add_client);
        inputCidade = (EditText) findViewById(R.id.input_digita_cidade_add_client);
        inputLogradouro = (EditText) findViewById(R.id.input_digita_logradouro_add_client);
        inputBairro = (EditText) findViewById(R.id.input_digita_bairro_add_client);
        inputNumero = (EditText) findViewById(R.id.input_digita_numero_add_client);
        btnSalvar = (Button) findViewById(R.id.botao_salvar_add_client);
        btnLimpar = (Button) findViewById(R.id.botao_limpar_add_client);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = inputNome.getText().toString();
                String cpf_cnpj = inputPFPJ.getText().toString();
                String telefone = inputTelefone.getText().toString();
                String email = inputEmail.getText().toString();
                String cep = inputCep.getText().toString();
                String cidade = inputCidade.getText().toString();
                String logradouro = inputLogradouro.getText().toString();
                String bairro = inputBairro.getText().toString();
                String numero = inputNumero.getText().toString();

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("nome", nome);
                    jsonData.put("cpf_cnpj", cpf_cnpj);
                    jsonData.put("telefone", telefone);
                    jsonData.put("email", email);
                    jsonData.put("cep", cep);
                    jsonData.put("cidade", cidade);
                    jsonData.put("logradouro", logradouro);
                    jsonData.put("bairro", bairro);
                    jsonData.put("numero", numero);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (nome.isEmpty()){
                    inputNome.setError("O campo NOME é obrigatório");
                } else if (cpf_cnpj.isEmpty()) {
                    inputPFPJ.setError("O campo CPF/CNPJ é obrigatório");
                } else if (cep.isEmpty()) {
                    inputCep.setError("O campo CEP é obrigatório");
                } else if (cidade.isEmpty()) {
                    inputCidade.setError("O campo CIDADE é obrigatório");
                } else if (logradouro.isEmpty()) {
                    inputLogradouro.setError("O campo LOGRADOURO é obrigatório");
                } else if (bairro.isEmpty()) {
                    inputBairro.setError("O campo BAIRRO é obrigatório");
                } else if (numero.isEmpty()) {
                    inputNumero.setError("O campo NÚMERO é obrigatório");
                } else {
                    cadastrarCliente(getApplicationContext(), jsonData);
                }
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNome.setText("");
                inputPFPJ.setText("");
                inputTelefone.setText("");
                inputEmail.setText("");
                inputCep.setText("");
                inputCidade.setText("");
                inputLogradouro.setText("");
                inputBairro.setText("");
                inputNumero.setText("");
            }
        });
    }

    private void cadastrarCliente(Context context, JSONObject jsonData){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/novocliente.php";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao cadastrar cliente!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(postRequest);
    }

}