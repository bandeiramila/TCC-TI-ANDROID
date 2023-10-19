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

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Products> products;
    private OnItemClickListener clickListener;


    public Adapter(Context ctx, List<Products> products) {
        this.inflater = LayoutInflater.from(ctx);
        this.products = products;
    }

    //observador da interface
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantProd, nameProd, priceProd;
        CardView productLineCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quantProd = itemView.findViewById(R.id.quantProd);
            nameProd = itemView.findViewById(R.id.nameProd);
            priceProd = itemView.findViewById(R.id.priceProd);
            productLineCardView = itemView.findViewById(R.id.productLineCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder layout, int position) {
        Products produto = products.get(position);
        String name = produto.getProductName();
        String quant = produto.getQuant();
        String price = String.format("R$ %1$.2f", produto.getPrice());

        layout.nameProd.setText(name);
        layout.quantProd.setText(quant);
        layout.priceProd.setText(price);

        layout.productLineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(produto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    //define as funções que a activity precisa ter
    public interface OnItemClickListener {
        void onItemClick(Products produto);
    }
}
