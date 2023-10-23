package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsListActivity extends AppCompatActivity implements Adapter.OnItemClickListener, PopUpItem.OnEditClickListener, PopUpEditItem.OnItemEditedListener {
    RecyclerView recyclerView;
    List<Products> products;
    private static String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaprodutos.php?nome";
    Adapter adapter;
    Boolean askEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list_activity);
        recyclerView = findViewById(R.id.productsList);
        extractProducts();
    }

    private void extractProducts() {
        products = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject productObject = response.getJSONObject(i);

                        Products product = new Products();
                        product.setId(productObject.getInt("id"));
                        product.setProductName(productObject.getString("nome_produto".toString()));
                        product.setBarCode(productObject.getString("codigo_de_barras".toString()));
                        product.setQuant(productObject.getString("quantidade".toString()));
                        product.setPrice((float) productObject.getDouble("valor"));
                        products.add(product);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!askEdit){
                    showList(products);
                }else {
                    refreshList(products);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void showList(List<Products> products) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(), products);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshList(List<Products> products){
        adapter.products = products;
        adapter.notifyDataSetChanged();
    }

    //////////POP UP GERAL //////////

    @Override
    public void onItemClick(Products produto) {
        showAlertDialog(produto);
    }

    public void showAlertDialog(Products produto) {
        PopUpItem dialogFragment = PopUpItem.newInstance(produto);
        getSupportFragmentManager().beginTransaction().commit();
        dialogFragment.show(getSupportFragmentManager(), "PopUpItem");
    }

    /////////POP UP DE EDIÇÃO//////////

    public void showAlertDialogEdit(Products produto) {
        PopUpEditItem dialogFragment = PopUpEditItem.newInstance(produto);
        getSupportFragmentManager().beginTransaction().commit();
        dialogFragment.show(getSupportFragmentManager(), "PopUpEditItem");
    }


    @Override
    public void onEditClick(Products produto) {
        showAlertDialogEdit(produto);
    }

    @Override
    public void onDeleteItem() {
        onEditSuccess();
    }

    @Override
    public void onEditSuccess() {
        askEdit = true;
        extractProducts();
    }
}