package com.example.projetofinal;

import java.io.Serializable;

public class Products implements Serializable {
    private int id;
    private String productName;
    private String barCode;
    private String quant;
    private float price;

    public Products(){}
    public Products(int id, String productName, String barCode, String quant, float price) {
        this.id = id;
        this.productName = productName;
        this.barCode = barCode;
        this.quant = quant;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
