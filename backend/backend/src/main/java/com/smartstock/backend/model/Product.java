package com.smartstock.backend.model;


import jakarta.persistence.*;


@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String sku;


    private Double price;


    private Integer quantity;



    @ManyToOne
    private Category category;



    @ManyToOne
    private Supplier supplier;

    private Boolean addToInventory;

    private Integer stockLevel;

    private Integer minimumStock;



    public Product(){
    }


    public Long getId(){
        return id;
    }


    public void setId(Long id){
        this.id=id;
    }


    public String getName(){
        return name;
    }


    public void setName(String name){
        this.name=name;
    }


    public String getSku(){
        return sku;
    }


    public void setSku(String sku){
        this.sku=sku;
    }


    public Double getPrice(){
        return price;
    }


    public void setPrice(Double price){
        this.price=price;
    }


    public Integer getQuantity(){
        return quantity;
    }


    public void setQuantity(Integer quantity){
        this.quantity=quantity;
    }


    public Category getCategory(){
        return category;
    }


    public void setCategory(Category category){
        this.category=category;
    }


    public Supplier getSupplier(){
        return supplier;
    }


    public void setSupplier(Supplier supplier){
        this.supplier=supplier;
    }

    public Boolean getaddToInventory() {
        return addToInventory;
    }

    public void setaddToInventory(Boolean addToInventory) {
        this.addToInventory = addToInventory;
    }

    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

}