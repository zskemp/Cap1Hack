package com.example.zrs.visigoth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class PaymentRequestActivity extends AppCompatActivity {

    ArrayList<People> list;
    RecyclerView rvPeopleList;

    EditText mAmount;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAmount = (EditText) findViewById(R.id.moneyAmount);

        // Lookup the recyclerview in activity layout
        rvPeopleList = (RecyclerView) findViewById(R.id.recycler_view);
        //Create a layout manager for the recyclerview
        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        //Create and add dividers to the recyclerview
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvPeopleList.getContext(),
                rvLayoutManager.getOrientation());
        rvPeopleList.addItemDecoration(mDividerItemDecoration);
        // Initialize bucketlist
        list = preparePeopleList();
        // Create adapter passing in the sample user data
        final PeopleAdapter adapter = new PeopleAdapter(this.getApplicationContext(), list);
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

    //Here is where we prepare the Glossary Data
    public static ArrayList<People> preparePeopleList() {
        final ArrayList<People> glossaryList = new ArrayList<People>();

        // TODO: Get this from the datebase
        glossaryList.add(new People("Johnny"));
        glossaryList.add(new People("Johnny2"));
        glossaryList.add(new People("Johnny3"));
        glossaryList.add(new People("Johnny4"));
        glossaryList.add(new People("Johnny5"));
        glossaryList.add(new People("Johnny6"));

        return glossaryList;
    }

}
