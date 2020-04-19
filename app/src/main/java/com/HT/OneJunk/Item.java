package com.HT.OneJunk;
import java.util.Date;

public class Item {
    private String title_in;
    private String description_in;
    private String price_in;
    private String seller;
    private Date created_on;

    public Item() {}

    public Item(String title, String description, String price, String seller, Date createdTime){
        this.title_in = title;
        this.description_in = description;
        this.price_in = price;
        this.seller = seller;
        this.created_on = createdTime;
    }

    public String getTitle(){
        return title_in;
    }
    public String getDescription(){
        return description_in;
    }
    public String getPrice(){
        return price_in;
    }
    public String getSeller(){
        return seller;
    }
    public Date getCreatedTime(){
        return created_on;
    }
}
