package com.example.zrs.visigoth;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class PaymentRequestActivity extends AppCompatActivity {

    RecyclerView rvPeopleList;

    EditText mAmount;
    Button mSubmit;
    DatabaseReference mDatabase;
    ArrayList<People> post = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAmount = (EditText) findViewById(R.id.moneyAmount);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Lookup the recyclerview in activity layout
        rvPeopleList = (RecyclerView) findViewById(R.id.recycler_view);
        //Create a layout manager for the recyclerview
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        //Create and add dividers to the recyclerview
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvPeopleList.getContext(),
                rvLayoutManager.getOrientation());
        rvPeopleList.addItemDecoration(mDividerItemDecoration);
        // Initialize bucketlist
        DatabaseReference users = mDatabase.child("users");
        post.clear();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    post.add(new People(postSnapshot.getKey().toString()));

                }

                // Create adapter passing in the sample user data
                final PeopleAdapter adapter = new PeopleAdapter(getApplicationContext(), post);
                // Attach the adapter to the recyclerview to populate items
                rvPeopleList.setAdapter(adapter);
                // Set layout manager to position the items
                rvPeopleList.setLayoutManager(rvLayoutManager);
                mSubmit = (Button) findViewById(R.id.selectPayer);
                mSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        String payee = adapter.getSelected();
                        String amount = mAmount.getText().toString();

                        Intent intent = new Intent(PaymentRequestActivity.this, AuthPaymentActivity.class);
                        intent.putExtra("PERSON_TO_PAY", payee);
                        intent.putExtra("AMOUNT", amount);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                post.add(new People(""));

                Toast toast = Toast.makeText(getApplicationContext(), "Failed to connect to server", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

}
