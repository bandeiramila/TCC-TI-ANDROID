package com.example.projetofinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String JSON_URL = "http://" + Conexao.IP + "/mvc_sistema_livraria/view/listaprodutos.php?";
    Adapter adapter;
    Boolean askRefresh = false;
    Button btn_search, btn_organize;
    EditText input_search;
    TextView close_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list_activity);
        recyclerView = findViewById(R.id.productsList);
        extractProducts(JSON_URL, false);
        btn_search = (Button) findViewById(R.id.button_search_products_list);
        btn_organize = (Button) findViewById(R.id.button_organize_products_list);
        input_search = (EditText) findViewById(R.id.input_search_products_list);
        close_search = (TextView) findViewById(R.id.button_close_products_list);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // controle de teclado ativo ou inativo

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

                imm.hideSoftInputFromWindow(input_search.getWindowToken(), 0);  //teclado inativado
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

                String url_search = JSON_URL + "nome=" + textoDigitado;
                extractProducts(url_search, false);
            }
        });

        btn_organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDialogOrderBy();
            }
        });
    }

    private void extractProducts(String url, boolean askRefresh) {
        products = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject productObject = response.getJSONObject(i);

                        Products product = new Products();
                        product.setId(productObject.getInt("id"));
                        product.setProductName(productObject.getString("nome_produto"));
                        product.setBarCode(productObject.getString("codigo_de_barras"));
                        product.setQuant(productObject.getString("quantidade"));
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
        extractProducts(JSON_URL, true);
    }

    ///////////////////DIALOGO DE ORDENAÇÃO///////////////////
    private void onCreateDialogOrderBy(){
        String[] order_items = getResources().getStringArray(R.array.order_items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ordenação");
        builder.setItems(order_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //String selecionado = order_items[which];
                //Toast.makeText(ProductsListActivity.this, selecionado, Toast.LENGTH_SHORT).show();
                String url = JSON_URL;
                switch (which) {
                    case 0:
                        url = JSON_URL;
                        //Toast.makeText(ProductsListActivity.this, "Padrão", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        url = JSON_URL + "orderby=nome_produto&sentido=asc";
                        //Toast.makeText(ProductsListActivity.this, "De A a Z", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        url = JSON_URL + "orderby=nome_produto&sentido=desc";
                        //Toast.makeText(ProductsListActivity.this, "De Z a A", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        url = JSON_URL + "orderby=valor&sentido=desc";
                        //Toast.makeText(ProductsListActivity.this, "Maior preço", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        url = JSON_URL + "orderby=valor&sentido=asc";
                        //Toast.makeText(ProductsListActivity.this, "Menor preço", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        url = JSON_URL + "orderby=quantidade&sentido=desc";
                        //Toast.makeText(ProductsListActivity.this, "Maior quantidade", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        url = JSON_URL + "orderby=quantidade&sentido=asc";
                        //Toast.makeText(ProductsListActivity.this, "Menor quantidade", Toast.LENGTH_SHORT).show();
                        break;
                }
                extractProducts(url, false);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}