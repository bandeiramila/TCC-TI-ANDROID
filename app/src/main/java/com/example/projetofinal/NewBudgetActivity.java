package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewBudgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budget_activity);
    }

    public void fazerOrcamentoSalvar(View view){
        Intent intent = new Intent(this, NewBudgetActivityIncrease.class);
        startActivity(intent);
    }
}