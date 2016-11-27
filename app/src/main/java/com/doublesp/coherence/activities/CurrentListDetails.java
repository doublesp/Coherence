package com.doublesp.coherence.activities;

import static com.doublesp.coherence.R.id.shopping_item_image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doublesp.coherence.R;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CurrentListDetails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private String mListId;

    private static FirebaseRecyclerAdapter<Idea, DetailsViewHolder> mFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list_details);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_details_recycler_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListId = getIntent().getStringExtra(ConstantsAndUtils.LIST_ID);
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS).child(mListId).child("ideas");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Idea, DetailsViewHolder>(
                Idea.class, R.layout.single_detail_list_item, DetailsViewHolder.class,
                mListsDatabaseReference) {
            @Override
            protected void populateViewHolder(DetailsViewHolder holder, Idea idea, int position) {
                String text = idea.getContent();
                holder.mShoppingItem.setText(text);
                // TODO: since there is no meta, not populating any image
                // if final wireframe needs it, then we can populate that as well.
            }
        };
        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
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

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView mShoppingItem;
        ImageView mShoppingItemImage;

        public DetailsViewHolder(View itemView) {
            super(itemView);

            mShoppingItem = (TextView) itemView.findViewById(R.id.shopping_item);
            mShoppingItemImage = (ImageView) itemView.findViewById(shopping_item_image);
        }
    }
}
