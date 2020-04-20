package com.HT.OneJunk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        void onItemClick(int position);
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
        //final TextView description;
        final TextView price;
        //final TextView seller;
        //final TextView createdOn;

        ItemViewHolder(CardView v) {
            super(v);
            view = v;
            title = v.findViewById(R.id.title_in);
            //description = v.findViewById(R.id.description_in);
            price = v.findViewById(R.id.price_in);
            //seller = v.findViewById(R.id.seller);
            //createdOn = v.findViewById(R.id.created_on);
        }
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @NonNull int position, @NonNull final Item item){
        holder.title.setText(item.getTitle());
        //holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice());
        //holder.seller.setText(item.getSeller());
        //holder.createdOn.setText(holder.view.getContext().getString(R.string.created_on, format.format(item.getCreatedTime())));
        if (listener != null){
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAdapterPosition());
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
