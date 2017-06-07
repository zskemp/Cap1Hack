package com.example.zrs.visigoth;

/**
 * Created by zrs on 6/7/17.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        ViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            personName = (TextView)itemView.findViewById(R.id.textViewName);
            personAge = (TextView)itemView.findViewById(R.id.textViewVersion);
            personPhoto = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

    List<Transaction> transactions;

    TransactionAdapter(List<Transaction> persons){
        this.transactions = persons;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        final ViewHolder pvh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int i = pvh.getAdapterPosition();

            }
        });

        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder personViewHolder, final int i) {
        personViewHolder.personName.setText(transactions.get(i).name);
        personViewHolder.personAge.setText(transactions.get(i).age);
        personViewHolder.personPhoto.setImageResource(transactions.get(i).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
