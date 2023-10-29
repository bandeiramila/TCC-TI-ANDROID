package com.example.projetofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClients extends RecyclerView.Adapter<AdapterClients.ViewHolder> {

    LayoutInflater inflater;
    List<Clients> clients;
    private OnClientClickListener clickListener;


    public AdapterClients(Context ctx, List<Clients> clients) {
        this.inflater = LayoutInflater.from(ctx);
        this.clients = clients;
    }

    //observador da interface
    public void setOnClientClickListener(OnClientClickListener listener){
        this.clickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameClient;
        CardView clientLineCardView;

        public ViewHolder(@NonNull View clientView) {
            super(clientView);

            nameClient = clientView.findViewById(R.id.nameClient);
            clientLineCardView = clientView.findViewById(R.id.clientLineCardView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_client_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder layout, int position) {
        Clients client = clients.get(position);
        String name = client.getNomeCliente();

        layout.nameClient.setText(name);

        layout.clientLineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { clickListener.onClientClick(client); }
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public interface OnClientClickListener {
        void onClientClick(Clients clients);
    }
}
