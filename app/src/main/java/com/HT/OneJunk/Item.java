package com.HT.OneJunk;
import java.util.Date;

public class Item {
    private String title;
    private String description;
    private String price;
    private String seller;
    private Date createdTime;

    public Item() {}

    public Item(String title, String description, String price, String seller, Date createdTime){
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.createdTime = createdTime;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getPrice(){
        return price;
    }
    public String getSeller(){
        return seller;
    }
    public Date getCreatedTime(){
        return createdTime;
    }
}
