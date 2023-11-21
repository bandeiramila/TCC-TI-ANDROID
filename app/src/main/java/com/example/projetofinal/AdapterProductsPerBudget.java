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

public class AdapterProductsPerBudget extends RecyclerView.Adapter<AdapterProductsPerBudget.ViewHolder> {
    LayoutInflater inflater;
    List<ProductsBudget> products;
    private OnProductClickListener clickListener;

    public AdapterProductsPerBudget(Context ctx, List<ProductsBudget> products) {
        this.inflater = LayoutInflater.from(ctx);
        this.products = products;
    }
    public void setOnProductClickListener(OnProductClickListener listener){
        this.clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantProd, nameProd, priceProd, totalProd;
        CardView prodLineCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantProd = itemView.findViewById(R.id.quant_prod_products_budget);
            nameProd = itemView.findViewById(R.id.name_prod_products_budget);
            priceProd = itemView.findViewById(R.id.price_prod_products_budget);
            totalProd = itemView.findViewById(R.id.total_price_prod_products_budget);
            prodLineCardView = itemView.findViewById(R.id.productLineCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_product_budget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductsBudget product = products.get(position);
        int quant = product.getQuantidade();
        String name = product.getNome_produto();
        double price = product.getValor_unitario();
        String productPrice = String.format("R$ %1$.2f", price);
        float totalPrice = (float) (price * quant);
        String totalProductPrice = String.format("R$ %1$.2f", totalPrice);

        holder.quantProd.setText(String.valueOf(quant));
        holder.nameProd.setText(name);
        holder.priceProd.setText(productPrice);
        holder.totalProd.setText(totalProductPrice);

        holder.prodLineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener.onProductClick(product);}
        });

    }

    @Override
    public int getItemCount() {return products.size();}

    public interface OnProductClickListener{
        void onProductClick(ProductsBudget product);
    }
}
