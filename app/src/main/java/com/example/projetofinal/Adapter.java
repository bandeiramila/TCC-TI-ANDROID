package com.example.projetofinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Products> products;

    public Adapter(Context ctx, List<Products> products) {
        this.inflater = LayoutInflater.from(ctx);
        this.products = products;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products produto = products.get(position);
        String name = produto.getProductName();
        String quant = produto.getQuant();
        String price = String.format("R$ %1$.2f", produto.getPrice());
        int id = products.get(position).getId();

        holder.nameProd.setText(name);
        holder.quantProd.setText(quant);
        holder.priceProd.setText(price);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView quantProd, nameProd, priceProd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quantProd = itemView.findViewById(R.id.quantProd);
            nameProd = itemView.findViewById(R.id.nameProd);
            priceProd = itemView.findViewById(R.id.priceProd);
        }
    }

    public void abrirProduto(View view){
        String text = "oi";
    }


}
