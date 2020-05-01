package com.HT.OneJunk;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Item {
    private String title_in;
    private String description_in;
    private String price_in;
    private String seller;
    private Date created_on;
    private String image;

    @DocumentId
    public String id;
    public Item() {}

    @PropertyName("title")
    public void setTitle_in(String title_in) {
        this.title_in = title_in;
    }

    @PropertyName("description")
    public void setDescription_in(String description_in) {
        this.description_in = description_in;
    }

    @PropertyName("price")
    public void setPrice_in(String price_in) {
        this.price_in = price_in;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }
    public void setImage(String image){
        this.image = image;
    }

    public Item(String title, String description, String price, String seller, Date created_on, String image){
        this.title_in = title;
        this.description_in = description;
        this.price_in = price;
        this.seller = seller;
        this.created_on = created_on;
        this.image = image;
    }


    @PropertyName("title")
    public String getTitle(){
        return title_in;
    }
    @PropertyName("description")
    public String getDescription(){
        return description_in;
    }
    @PropertyName("price")
    public String getPrice(){
        return price_in;
    }
    public String getSeller(){
        return seller;
    }
    public Date getCreatedTime(){
        return created_on;
    }
    public String getImage(){
        return image;
    }


}
