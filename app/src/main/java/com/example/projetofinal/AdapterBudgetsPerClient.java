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

public class AdapterBudgetsPerClient extends RecyclerView.Adapter<AdapterBudgetsPerClient.ViewHolder> {
    LayoutInflater inflater;
    List<ClientsBudget> budgets;
    private OnBudgetClickListener clickListener;


    public AdapterBudgetsPerClient(Context ctx, List<ClientsBudget> budgets) {
        this.inflater = LayoutInflater.from(ctx);
        this.budgets = budgets;
    }

    public void setOnBudgetClickListener(OnBudgetClickListener listener){
        this.clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateClient;
        CardView clientLineCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateClient = itemView.findViewById(R.id.nameClient);
            clientLineCardView = itemView.findViewById(R.id.clientLineCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_client_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientsBudget budget = budgets.get(position);
        String date = budget.getData_solicitacao();

        holder.dateClient.setText(date);

        holder.clientLineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener.onBudgetClick(budget);}
        });
    }

    @Override
    public int getItemCount() {return budgets.size();}

    public interface OnBudgetClickListener {
        void onBudgetClick(ClientsBudget budget);
    }
}
