package com.example.zrs.visigoth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

import static com.example.zrs.visigoth.SplashScreen.PREFS_NAME;
import static java.security.AccessController.getContext;

public class PaymentRequestActivity extends AppCompatActivity {

    RecyclerView rvPeopleList;

    EditText mAmount;
    Button mSubmit;
    DatabaseReference mDatabase;
    ArrayList<People> post = new ArrayList<>();

    String id;
    String amount;

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
                        final String payee = adapter.getSelected();
                        amount = mAmount.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference users = mDatabase.child("users");

                        //one time check of the mDatabase object
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                id = snapshot.child(payee).child("c1id").getValue().toString();

                                Intent intent = new Intent(PaymentRequestActivity.this, AuthPaymentActivity.class);
                                intent.putExtra("PERSON_TO_PAY", payee);
                                intent.putExtra("AMOUNT", amount);
                                intent.putExtra("ID", id);
                                //makeTransation();
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Server failed to return", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
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
    /*public void makeTransation(){
        String medium = "medium";
        String date = "2017-06-08";
        String description = "description";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String accountID = settings.getString("accountID", "");

        //Log.i("zzz",id);
        //APIInterface apiService = APIClient.getClient().create(APIInterface.class);
        //Call<Example> call = apiService.transfer(medium, id, amount, date, description);
        String url = "http://api.reimaginebanking.com/accounts/" + id +"/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75";
        String json = "{" +
                "  \"medium\": \"balance\"," +
                "  \"payee_id\": \"" + accountID + "\"," +
                "  \"amount\": " + amount +"," +
                "  \"transaction_date\": \"2017-06-08\"," +
                "  \"description\": \"string\"" +
                "}";

        Log.i("zzz",json);

        new RetrieveFeedTask().execute(url, json);


    }

    class RetrieveFeedTask extends AsyncTask<String, String, Void> {



        private Exception exception;

        String url = "http://api.reimaginebanking.com/accounts/" + id +"/transfers?key=67d9a238a69baa7daee2a3a22bd1ee75";
        String json = "{" +
                "  \"medium\": \"balance\"," +
                "  \"payee_id\": \"5938c93bceb8abe2425178e5\"," +
                "  \"amount\": 3," +
                "  \"transaction_date\": \"2017-06-08\"," +
                "  \"description\": \"string\"" +
                "}";

        protected Void doInBackground(String... urls) {
            APIClient apiClient = new APIClient();

            try {
                apiClient.post(urls[0],urls[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }*/
}
