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

    public void cadastrarProduto(View view) {
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivity(intent);
    }

    public void fazerOrcamento(View view) {
        Intent intent = new Intent(this, NewBudgetActivity.class);
        startActivity(intent);
    }

    public void listarProdutos(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public double verificaVirgula(String valor){
        
    }

}