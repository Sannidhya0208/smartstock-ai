package com.smartstock.backend.dto;


public class ProductRequest {


    private String name;


    private String sku;


    private Double price;


    private Integer quantity;


    private Long categoryId;


    private Long supplierId;



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


    public Long getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public Long getSupplierId() {
        return supplierId;
    }


    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

}