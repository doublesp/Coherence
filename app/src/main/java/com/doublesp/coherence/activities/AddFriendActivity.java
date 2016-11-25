package com.doublesp.coherence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.doublesp.coherence.R;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    private static DatabaseReference mListsDatabaseReference;
    private static FirebaseRecyclerAdapter<User, AddFriendViewHolder> mFirebaseRecyclerAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private RecyclerView mRecyclerView;
    private ArrayList<String> userFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setTitle(R.string.add_friend);

        mRecyclerView = (RecyclerView) findViewById(R.id.add_friends_recycler_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(ConstantsAndUtils.USERS);
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_FRIENDS);

        userFriends = getIntent().getStringArrayListExtra(ConstantsAndUtils.USER_FRIENDS);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, AddFriendViewHolder>(
                User.class, R.layout.single_add_friend, AddFriendViewHolder.class,
                mUsersDatabaseReference) {

            @Override
            protected void populateViewHolder(AddFriendViewHolder holder, User user, int position) {
                String email = user.getEmail();
                if (userFriends.contains(email)) return;
                holder.mEmail.setText(email.replace(",", "."));
            }
        };

        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
    }

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder {
        TextView mEmail;

        public AddFriendViewHolder(View itemView) {
            super(itemView);

            mEmail = (TextView) itemView.findViewById(R.id.friend_email_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = mFirebaseRecyclerAdapter.getItem(getAdapterPosition());
                    mListsDatabaseReference.child(ConstantsAndUtils.getOwner(view.getContext()))
                            .child(user.getEmail()).setValue(user);
                    Intent intent = new Intent(view.getContext(), ShareActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
