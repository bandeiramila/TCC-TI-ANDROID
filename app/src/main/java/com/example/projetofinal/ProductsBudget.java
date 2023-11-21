package com.example.projetofinal;

import java.io.Serializable;

public class ProductsBudget implements Serializable {
    int id;
    int id_orcamento;
    int id_produto;
    int quantidade;
    double valor_unitario;
    String nome_produto;

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_orcamento() {
        return id_orcamento;
    }

    public void setId_orcamento(int id_orcamento) {
        this.id_orcamento = id_orcamento;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor_unitario() {
        return valor_unitario;
    }

    public void setValor_unitario(double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }
}
