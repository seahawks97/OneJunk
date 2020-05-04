package com.HT.OneJunk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class ItemRecyclerAdapter extends FirestoreRecyclerAdapter<Item, ItemRecyclerAdapter.ItemViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(Item item);
    }

    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private final OnItemClickListener listener;

    ItemRecyclerAdapter(FirestoreRecyclerOptions<Item> options, OnItemClickListener listener){
        super(options);
        this.listener = listener;

    }
    ItemRecyclerAdapter(FirestoreRecyclerOptions<Item> options){
        super(options);
        this.listener = null;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{
        final CardView view;
        final TextView title;
        final TextView price;
        final ImageView image;


        ItemViewHolder(CardView v) {
            super(v);
            view = v;
            title = v.findViewById(R.id.title_in);
            price = v.findViewById(R.id.price_in);
            image = v.findViewById(R.id.image);

        }
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @NonNull int position, @NonNull final Item item){
        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
//        holder.image.setText(item.getImage());

        if (listener != null){
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image, parent, false);
        return new ItemViewHolder(v);
    }

}
