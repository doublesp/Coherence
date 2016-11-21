package com.doublesp.coherence.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.doublesp.coherence.R;
import com.doublesp.coherence.viewmodels.Plan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExternalLinkActivity extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_link);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListDatabaseReference = mFirebaseDatabase.getReference().child("lists");

        Intent intent = getIntent();
        final Uri data = intent.getData();
        int index = data.toString().indexOf("shared/") + "shared/".length();
        String listId = data.toString().substring(index);

        mListDatabaseReference.orderByKey().equalTo(listId).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // Use the below plan object to fetch the information and
                        // show it in the desired format.
                        Plan plan = dataSnapshot.getValue(Plan.class);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(listId);
    }

}
