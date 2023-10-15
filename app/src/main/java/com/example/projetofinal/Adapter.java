package com.example.projetofinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
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

        holder.productLineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(inflater.getContext(), String.valueOf(produto.getId()),Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void showAlertDialog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
//        builder.setTitle("Produto");
//        builder.setMessage("Nome do produto");   //produto.getProductName());
//        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(inflater.getContext(), "Editar",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNeutralButton("Excluir", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(inflater.getContext(), "Excluir",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(inflater.getContext(), "Cancelar",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }


    @Override
    public int getItemCount() {
        return products.size();
    }



}
