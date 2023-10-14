package com.example.projetofinal;

import android.content.Context;
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
        holder.nameProd.setText(products.get(position).getProductName());
        holder.quantProd.setText(products.get(position).getQuant());
        holder.priceProd.setText((int) products.get(position).getPrice());
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


}
