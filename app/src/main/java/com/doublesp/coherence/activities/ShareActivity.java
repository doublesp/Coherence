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
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity {
    private static DatabaseReference mShoppingListDatabaseReference;
    private static DatabaseReference mUsersListsDatabaseReference;
    private static String mListId;
    private static FirebaseRecyclerAdapter<User, FriendsViewHolder> mFirebaseRecyclerAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private RecyclerView mRecyclerView;
    private List<String> userFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle(R.string.share_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.friends_list_recycler_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_FRIENDS).child(ConstantsAndUtils.getOwner(this));
        mUsersListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS);

        mListId = getIntent().getStringExtra(ConstantsAndUtils.LIST_ID);

        userFriends = new ArrayList<>();
        userFriends.add(ConstantsAndUtils.getOwner(this));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(
                User.class, R.layout.single_add_friend, FriendsViewHolder.class,
                mListsDatabaseReference) {
            @Override
            protected void populateViewHolder(FriendsViewHolder holder, User user, int position) {
                String email = user.getEmail();
                String name = user.getName();
                userFriends.add(email);
                holder.mEmail.setText(name);
            }
        };

        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        intent.putStringArrayListExtra(ConstantsAndUtils.USER_FRIENDS,
                (ArrayList<String>) userFriends);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView mEmail;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mEmail = (TextView) itemView.findViewById(R.id.friend_email_address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String email = mFirebaseRecyclerAdapter.getItem(
                            getAdapterPosition()).getEmail();
                    mUsersListsDatabaseReference.child(
                            ConstantsAndUtils.getOwner(view.getContext()))
                            .orderByKey().equalTo(mListId).addChildEventListener(
                            new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    UserList list = dataSnapshot.getValue(UserList.class);
                                    mUsersListsDatabaseReference.child(email).child(mListId).
                                            setValue(list);
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
                    Intent intent = new Intent(view.getContext(), CurrentListDetails.class);
                    intent.putExtra(ConstantsAndUtils.LIST_ID, mListId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
