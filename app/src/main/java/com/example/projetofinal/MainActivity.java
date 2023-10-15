package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cadastrarProduto(View view){
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivity(intent);
    }

    public void fazerOrcamento(View view){
        Intent intent = new Intent(this, NewBudgetActivity.class);
        startActivity(intent);
    }

    public void listarProdutos(View view){
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

//    public void showAlertDialog(View view){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Produto");
//        builder.setMessage("Nome do produto");   //produto.getProductName());
//        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        });
//
//        builder.setNeutralButton("Excluir", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        });
//
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
}