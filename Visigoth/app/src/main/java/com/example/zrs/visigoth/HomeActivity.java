package com.example.zrs.visigoth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PaymentRequestActivity.class);
                startActivity(intent);
            }
        });

        transactions = initializeTransactions();

        RecyclerView rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        TransactionAdapter adapter = new TransactionAdapter(transactions);
        //Create a layout manager for the recyclerview
        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        //LinearLayoutManager llm = new GridLayoutManager(this.getContext(), 2);
        rv.setLayoutManager(rvLayoutManager);
        //rv.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(false);
    }


    //Create Cards for RecyclerView
    public static ArrayList<Transaction> initializeTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        // ToDo: Change temp data with data from API call
        //FAKE DATA: USE API CALL
        transactions.add(new Transaction("John", "$50.49", R.drawable.money));
        transactions.add(new Transaction("Zach", "$44.32", R.drawable.receipt));
        transactions.add(new Transaction("Kyle", "$2.32", R.drawable.money));
        transactions.add(new Transaction("Ben", "$145.65", R.drawable.receipt));
        transactions.add(new Transaction("Riyu", "$12.00", R.drawable.money));


        return transactions;
    }

}
