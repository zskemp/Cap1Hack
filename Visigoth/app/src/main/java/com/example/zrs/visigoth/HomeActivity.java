package com.example.zrs.visigoth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity {

    public static ArrayList<Transaction> transactions;
//    private ArrayList<Transaction> transactions;
    private DatabaseReference mDatabase;
    String transaction_name;

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
        transactions = new ArrayList<>();

        // ToDo: Change temp data with data from API call
        String url = "http://api.reimaginebanking.com/accounts/5938c8f1ceb8abe2425178e1/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75";

       // String url = "http://api.reimaginebanking.com/accounts/5938c8f1ceb8abe2425178e1/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75";
        String json = "{" +
                "  \"medium\": \"balance\"," +
                "  \"payee_id\": \"5938c93bceb8abe2425178e5\"," +
                "  \"amount\": 3," +
                "  \"transaction_date\": \"2017-06-08\"," +
                "  \"description\": \"string\"" +
                "}";

        new RetrieveFeedTask().execute(url);
        //FAKE DATA: USE API CALL
        transactions.add(new Transaction("John", "$50.49", R.drawable.money));
        transactions.add(new Transaction("Zach", "$44.32", R.drawable.receipt));
        transactions.add(new Transaction("Kyle", "$2.32", R.drawable.money));
        transactions.add(new Transaction("ben", "$145.65", R.drawable.receipt));
        transactions.add(new Transaction("Riyu", "$12.00", R.drawable.money));


        return transactions;
    }

    private static class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        String url = "http://api.reimaginebanking.com/accounts/" + "s5938c8f1ceb8abe2425178e1" + "/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75";
        String json = "{" +
                "  \"medium\": \"balance\"," +
                "  \"payee_id\": \"5938c93bceb8abe2425178e5\"," +
                "  \"amount\": 3," +
                "  \"transaction_date\": \"2017-06-08\"," +
                "  \"description\": \"string\"" +
                "}";

        protected Void doInBackground(String... urls) {
            APIClient_Get apiClient = new APIClient_Get();

            try {
                String response = apiClient.run(urls[0]);
                System.out.print(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    public String capitalone_id_to_name(String id) {
        final String capital_id = id;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        transaction_name = "default name";

        DatabaseReference users = mDatabase.child("users");

        //one time check of the mDatabase object
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    if (postSnapshot.child("c1id").equals(capital_id)){
                        transaction_name = postSnapshot.getValue().toString();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(), "Server failed to return", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return transaction_name;
    }
}
