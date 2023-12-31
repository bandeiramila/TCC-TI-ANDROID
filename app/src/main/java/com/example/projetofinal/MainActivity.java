package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listarProdutos(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public void listarClientes(View view) {
        Intent intent = new Intent(this, ClientList.class);
        startActivity(intent);
    }

    public void listarOrcamentos(View view) {
        Intent intent = new Intent(this, BudgetClientsList.class);
        startActivity(intent);
    }

    public double verificaVirgula(String valor){
        String valortratado = valor.replace(",", "").replaceAll("\\s", "");

        try {
            return Double.parseDouble(valortratado);
        } catch (NumberFormatException e){
            return 0.0;
        }
    }

}