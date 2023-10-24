package com.example.projetofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Boolean askRefresh = false;
    Boolean isSearch = false;
    Button btn_search, btn_organize;
    EditText input_search;
    TextView close_search;
    String url_request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list_activity);
        recyclerView = findViewById(R.id.productsList);
        extractProducts();
        btn_search = (Button) findViewById(R.id.button_search_products_list);
        btn_organize = (Button) findViewById(R.id.button_organize_products_list);
        input_search = (EditText) findViewById(R.id.input_search_products_list);
        close_search = (TextView) findViewById(R.id.button_close_products_list);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_search.setVisibility(View.VISIBLE);
                close_search.setVisibility(View.VISIBLE);
            }
        });

        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_search.setVisibility(View.GONE);
                close_search.setVisibility(View.GONE);
                input_search.setText("");
            }
        });

        input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método é chamado antes do texto ser alterado.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método é chamado quando o texto está sendo alterado.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textoDigitado = editable.toString();

                String url_search = JSON_URL + "=" + textoDigitado;
                //TextView txtTeste = (TextView) findViewById(R.id.title_products_list); //EXCLUIR ISSO PELO AMOR DE DEUS *************************
                //txtTeste.setText(url_search);
                //askRefresh = true;
                //extractProducts(url_search);

                products = new ArrayList<>();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_search, null, new Response.Listener<JSONArray>() {
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

                        if (!askRefresh){
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
        });
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

                if (!askRefresh){
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
        askRefresh = true;
        extractProducts();
    }
}