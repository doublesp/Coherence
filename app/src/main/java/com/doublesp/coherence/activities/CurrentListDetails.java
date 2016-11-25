package com.doublesp.coherence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doublesp.coherence.R;
import com.doublesp.coherence.adapters.ShoppingItemsAdapter;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.ShoppingListItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CurrentListDetails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String mListId;

    private List<ShoppingListItem> mShoppingListItems;

    private ShoppingItemsAdapter mShoppingItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list_details);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_details_recycler_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListId = getIntent().getStringExtra(ConstantsAndUtils.LIST_ID);
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS).child(mListId).child("ideas");


        mShoppingListItems = new ArrayList<>();
        mShoppingItemsAdapter = new ShoppingItemsAdapter(mShoppingListItems);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mShoppingItemsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShoppingListItems.clear();
        mShoppingItemsAdapter.notifyDataSetChanged();
        detachDatabaseReadListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu:
                Intent shareIntent = new Intent(this, ShareActivity.class);
                shareIntent.putExtra(ConstantsAndUtils.LIST_ID, mListId);
                startActivity(shareIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attachDatabaseReadListener() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Idea idea = dataSnapshot.getValue(Idea.class);
                for (Idea relatedIdea : idea.getRelatedIdeas()) {
                    String item = relatedIdea.getContent();
                    String imageUrl = relatedIdea.getMeta().getImageUrl();

                    ShoppingListItem shoppingListItem = new ShoppingListItem(item, imageUrl);

                    mShoppingListItems.add(0, shoppingListItem);
                    mShoppingItemsAdapter.notifyItemInserted(0);
                }
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
        };
        mListsDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mListsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}
