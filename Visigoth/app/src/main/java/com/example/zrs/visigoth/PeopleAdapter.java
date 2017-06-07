package com.example.zrs.visigoth;

import android.content.PeriodicSync;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zrs on 6/7/17.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<People> peopleList;
    //Int to find which person to send to :)
    int selectedPosition=-1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    // Store the context for easy access
    public Context mContext;


    // Pass in the contact array into the constructor
    public PeopleAdapter(Context context, List<People> list) {
        peopleList = list;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        final View contactView = inflater.inflate(R.layout.people_term_row, parent, false);
        //Holder instance to be returned
        final ViewHolder viewHolder = new ViewHolder(contactView);

//        contactView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                final int position = viewHolder.getAdapterPosition();
//                People term = peopleList.get(position);
//
////                if (selectedItems.get(position, false)) {
////                    selectedItems.delete(position);
////                    view.setSelected(false);
////                }
////                else {
////                    selectedItems.put(position, true);
////                    view.setSelected(true);
////                }
//            }
//        });

        // Return a new holder instance
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        People term = peopleList.get(position);
        holder.title.setText(term.getTitle());

        if(selectedPosition==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }
}
