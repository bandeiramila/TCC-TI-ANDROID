package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Client_list extends AppCompatActivity implements AdapterClients.OnClientClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
    }

    @Override
    public void onClientClick(Clients clients) {

    }
}