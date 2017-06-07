package com.example.zrs.visigoth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AuthPaymentActivity extends AppCompatActivity {

    private String mPayee;
    private String mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mPayee= null;
                mAmount= null;
            } else {
                mPayee= extras.getString("PERSON_TO_PAY");
                mAmount= extras.getString("AMOUNT");
            }
        } else {
            mPayee = (String) savedInstanceState.getSerializable("PERSON_TO_PAY");
            mAmount = (String) savedInstanceState.getSerializable("AMOUNT");
        }

        Toast.makeText(getApplicationContext(), mPayee + "  " + mAmount, Toast.LENGTH_SHORT).show();
    }

}
