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

}