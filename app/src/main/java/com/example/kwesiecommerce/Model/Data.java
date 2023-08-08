package com.example.kwesiecommerce.Model;
public class Data {
    private int id;
    private  double price;
    private String text;
    private String imageUrl;

    public Data(String text, String imageUrl, double price, int id){
        this.text = text;
        this.imageUrl = imageUrl;
        this.price = price;
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public double getPrice() {
        return price;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}

