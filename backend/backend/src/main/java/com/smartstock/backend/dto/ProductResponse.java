package com.smartstock.backend.dto;


public class ProductResponse {


    private Long id;


    private String name;


    private String sku;


    private Double price;


    private Integer quantity;


    private String category;


    private String supplier;



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSku() {
        return sku;
    }


    public void setSku(String sku) {
        this.sku = sku;
    }


    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getSupplier() {
        return supplier;
    }


    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

}